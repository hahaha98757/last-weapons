package kr.hahaha98757.lastweapons.update

import com.google.gson.JsonParser
import kr.hahaha98757.lastweapons.*
import kr.hahaha98757.lastweapons.gui.GuiDownloadWaiting
import kr.hahaha98757.lastweapons.gui.GuiUpdateScreen
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
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
    private var latest: Version = Version()
    private var recommended: Version = Version()

    private var gui = false

    private var ctx: SSLContext?
    init {
        try {
            val myKeyStore = KeyStore.getInstance("JKS")
            @Suppress("SpellCheckingInspection")
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

            val inputStream = connection.inputStream
            val jsonResponse = IOUtils.toString(inputStream, StandardCharsets.UTF_8)
            inputStream.close()

            val json = JsonParser().parse(jsonResponse).asJsonObject
            latest = Version.toVersion(json.get("latest").asString)
            recommended = Version.toVersion(json.get("recommended").asString)
        } catch (e: Exception) {
            e.printStackTrace()
            latest = Version()
            recommended = Version()
        }
    }.start()

    fun checkUpdate() {
        when (val i = compareVersion()) {
            0, 3, 4 -> if (!gui) mc.displayGuiScreen(GuiUpdateScreen(i))
            2 -> addChatLine("You are using the latest version.\n§cThe latest version is not perfect. There may be bugs.")
            5 -> {
                val url = ChatComponentText("§9Click here to download.").also {
                    it.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/hahaha98757/last-weapons/releases"))
                    it.chatStyle.setChatHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText("Open download URL.")))
                }
                val text = ChatComponentText("")
                text.appendText("$LINE\nThe new latest version has been released. ")
                text.appendSibling(url)
                text.appendText("\n§rCurrent version: $VERSION\nLatest version: $latest\n§cThe latest version is not perfect. There may be bugs.\n$LINE")
                addChat(text)
            }
        }
    }

    //0: Required update, 1: Using recommended(= latest), 2: Using latest, 3: Using old ver(latest != recommended), 4: New recommended, 5: New latest
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
                runBatchFileAndQuit(File(File(mc.mcDataDir, "mods"), "deleter.bat"), """
                        @echo off
                        echo It should continue after Minecraft quits.
                        timeout /t 2 /nobreak
                        pause
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
    private var join = false

    @SubscribeEvent
    fun onPlayerJoin(event: EntityJoinWorldEvent) {
        if (event.entity != mc.thePlayer) return
        if (join) return
        UpdateChecker.checkUpdate()
        join = true
    }
}