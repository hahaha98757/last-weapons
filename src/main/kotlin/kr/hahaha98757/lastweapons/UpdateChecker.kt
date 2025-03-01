package kr.hahaha98757.lastweapons

import com.google.gson.JsonParser
import kr.hahaha98757.lastweapons.VersionType.*
import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.ChatComponentText
import net.minecraftforge.common.MinecraftForge
import org.apache.commons.io.IOUtils
import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.KeyStore
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

object UpdateChecker {
    private const val URL = "https://raw.githubusercontent.com/hahaha98757/last-weapons/master/update.json"
    private var latest: Version = Version()
    private var recommended: Version = Version()

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

    fun checkUpdate() = Thread {
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
            mc.addScheduledTask { displayText(compareVersion()) }
        } catch (e: Exception) {
            e.printStackTrace()
            latest = Version()
            recommended = Version()
        }
    }.start()

    //0: Required update, 1: Using recommended(= latest), 2: Using latest, 3: Using old ver(latest != recommended), 4: New recommended, 5: New latest
    private fun compareVersion(): Int {
        val modVer = Version.toVersion(VERSION)

        if (latest == recommended) {
            if (modVer == recommended) return 1
            if (recommended.x > modVer.x) return 0
            return 4
        }
        if (modVer == latest) return 2
        if (modVer == recommended || modVer.x == latest.x && modVer.y == latest.y && modVer.z == latest.z) return 5
        if (recommended.x > modVer.x) return -1
        return 3
    }

    private fun displayText(i: Int) {
        when (i) {
            0 -> {
                addDownloadURL("§cRequired updates released.", "§cUPDATE NOW.\n§c§lThe game ends after 10 seconds.")
                ClientCrash.update = true
                MinecraftForge.EVENT_BUS.register(ClientCrash())
            }
            2 -> addChatLine("You are using latest version.\n§cLatest version is not perfect. There may be bugs.")
            3 -> addDownloadURL("You are using old version.", "\nCurrent version: $VERSION\nRecommended version: $recommended\nLatest version: $latest")
            4 -> addDownloadURL("A new recommended version is available.", "Current version: $VERSION\nRecommended version: $latest")
            5 -> addDownloadURL("A new latest version is available.", "Current version: $VERSION\nLatest version: $latest\n§cLatest version is not perfect. There may be bugs.")
        }
    }

    private fun addDownloadURL(beforeText: String, afterText: String) {
        val url = ChatComponentText("§9Click here to download.").also {
            it.chatStyle.setChatClickEvent(ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/hahaha98757/last-weapons/releases"))
            it.chatStyle.setChatHoverEvent(HoverEvent(HoverEvent.Action.SHOW_TEXT, ChatComponentText("Open download URL.")))
        }

        val text = ChatComponentText("")

        text.appendText("$LINE\n$beforeText ")
        text.appendSibling(url)
        text.appendText("\n§r$afterText\n$LINE")

        mc.thePlayer.addChatMessage(text)
    }
}

enum class VersionType(val str: String) {
    ALPHA("alpha"), BETA("beta"), PRE_RELEASE("pre"), RELEASE_CANDIDATE("rc"), RELEASE("")
}

data class Version(val x: Int, val y: Int, val z: Int, val versionType: VersionType, val w: Int) {

    constructor(x: Int, y: Int, z: Int): this(x, y, z, RELEASE, 9999)

    constructor(): this(0, 0, 0)

    companion object {
        fun toVersion(str: String): Version {
            try {
                val versionType = if (str.contains("alpha")) ALPHA
                else if (str.contains("beta")) BETA
                else if (str.contains("pre")) PRE_RELEASE
                else if (str.contains("rc")) RELEASE_CANDIDATE
                else RELEASE

                if (!str.contains("-")) {
                    val strArray = str.split(".")
                    return Version(strArray[0].toInt(), strArray[1].toInt(), strArray[2].toInt())
                }

                val w = str.split("-")[1].replace(Regex("[^0-9]"), "").toInt()

                val strArray = str.split("-")[0].split(".")
                return Version(strArray[0].toInt(), strArray[1].toInt(), strArray[2].toInt(), versionType, w)
            } catch (e: Exception) {
                return Version()
            }
        }
    }

    override fun toString(): String {
        if (versionType == RELEASE)
            return "$x.$y.$z"
        return "$x.$y.$z-${versionType.str}$w"
    }
}