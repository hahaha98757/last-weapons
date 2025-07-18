package kr.hahaha98757.lastweapons.update

import com.google.gson.JsonParser
import kr.hahaha98757.lastweapons.*
import kr.hahaha98757.lastweapons.gui.GuiDownloadWaiting
import kr.hahaha98757.lastweapons.gui.GuiUpdateScreen
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatComponentTranslation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.apache.commons.io.IOUtils
import java.io.File
import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.KeyStore
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

object UpdateChecker {
    private const val URL = "https://raw.githubusercontent.com/hahaha98757/last-weapons/master/update.json"
    private var latest = Version()
    private var recommended = Version()

    private var gui = false

    private var ctx: SSLContext?
    init {
        try {
            val myKeyStore = KeyStore.getInstance("JKS")
            myKeyStore.load(UpdateChecker.javaClass.getResourceAsStream("/mykeystore.jks"), "changeit".toCharArray())
            val kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
            val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            kmf.init(myKeyStore, null)
            tmf.init(myKeyStore)
            ctx = SSLContext.getInstance("TLS")
            ctx!!.init(kmf.keyManagers, tmf.trustManagers, null)
        } catch (e: Exception) {
            e.printStackTrace()
            ctx = null
        }
    }

    fun setVersion() = Thread {
        try {
            val url = URL(URL)
            val connection = url.openConnection() as HttpsURLConnection?
            if (connection != null && ctx != null) connection.sslSocketFactory = ctx!!.socketFactory

            connection!!.requestMethod = "GET"
            connection.connectTimeout = 60000
            connection.readTimeout = 60000

            connection.inputStream.use {
                val jsonResponse = IOUtils.toString(it, StandardCharsets.UTF_8)

                val json = JsonParser().parse(jsonResponse).asJsonObject
                latest = Version.toVersion(json.get("latest").asString)
                recommended = Version.toVersion(json.get("recommended").asString)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            latest = Version()
            recommended = Version()
        }
    }.start()

    fun checkUpdate() {
        when (val i = compareVersion()) {
            0, 3, 4 -> if (!gui) mc.displayGuiScreen(GuiUpdateScreen(i == 0))
            2 -> {
                addLine()
                addTranslationChat("lastweapons.update.usingLatest")
                addTranslationChat("lastweapons.update.latestWarning")
                addLine()
            }
            5 -> {
                val url = ChatComponentTranslation("lastweapons.update.downloadText").apply {
                    chatStyle.chatClickEvent = ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/hahaha98757/last-weapons/releases")
                    chatStyle.chatHoverEvent = HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentTranslation("lastweapons.update.downloadHover"))
                }
                addLine()
                ChatComponentText("").run {
                    appendText(getTranslatedString("lastweapons.update.newLatest"))
                    appendSibling(url)
                    appendText("\n")
                    appendText(getTranslatedString("lastweapons.update.currentVer", true, VERSION))
                    appendText("\n")
                    appendText(getTranslatedString("lastweapons.update.latestVer", true, latest))
                    appendText("\n")
                    appendText(getTranslatedString("lastweapons.update.latestWarning"))
                    addChat(this)
                }
                addLine()
            }
        }
        gui = true
    }

    // 0: Required update, 1: Using recommended(= latest), 2: Using latest,
    // 3: Using old ver(latest != recommended), 4: New recommended, 5: New latest
    private fun compareVersion(): Int {
        val modVer = Version.toVersion(VERSION)
        if (modVer > latest) return 1

        if (latest == recommended) return when {
            modVer == recommended -> 1
            recommended.x > modVer.x -> 0
            else -> 4
        }

        return when {
            modVer == latest -> 2
            modVer < recommended -> 3
            else -> 5
        }
    }

    fun autoUpdate() = Thread {
        try {
            val connection = URL("https://github.com/hahaha98757/last-weapons/releases/download/$recommended/LastWeapons-$recommended.jar").openConnection() as HttpsURLConnection?
            if (connection != null && ctx != null) connection.sslSocketFactory = ctx!!.socketFactory

            connection!!.requestMethod = "GET"
            connection.connect()

            val newMod = File(modFile.parentFile, "LastWeapons-$recommended.jar")
            Files.copy(connection.inputStream, newMod.toPath(), StandardCopyOption.REPLACE_EXISTING)
            mc.addScheduledTask {
                println("Run auto update.")
                println("자동 업데이트를 실행합니다.")
                runBatchFileAndQuit(File(mc.mcDataDir, "mods/deleter_lastweapons.bat"), """
                    @echo off
                    chcp 65001
                    echo This is a mod deleter. It should continue after Minecraft quits.
                    echo 이것은 모드 제거 프로그램 입니다. 마인크래프트가 종료된 후 계속 진행되어야 합니다.
                    timeout /t 2 /nobreak
                    pause
                    echo Deleting old mod file...
                    echo 이전 모드 파일을 삭제하는 중...
                    del "${modFile.absolutePath}"
                    exit
                """.trimIndent())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            GuiDownloadWaiting.fail = true
        }
    }.start()
}

class UpdateCheckerListener {
    @SubscribeEvent
    fun onPlayerJoin(event: EntityJoinWorldEvent) {
        if (event.entity != mc.thePlayer) return
        UpdateChecker.checkUpdate()
        MinecraftForge.EVENT_BUS.unregister(this)
    }
}