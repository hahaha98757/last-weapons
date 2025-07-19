@file:JvmName("Tools")

package kr.hahaha98757.lastweapons

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.ChatComponentText
import net.minecraft.util.ChatComponentTranslation
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.common.Loader
import java.io.File
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import java.util.*

const val LINE = "§e-----------------------------------------------------"
val mc by lazy { Minecraft.getMinecraft()!! }
val modFile: File by lazy {
    var file = File("")
    for (mod in Loader.instance().modList) if (mod.modId == MODID) file = mod.source
    file
}

fun getText(text: String): String = EnumChatFormatting.getTextWithoutFormattingCodes(text)

fun addLine() = mc.thePlayer?.addChatMessage(ChatComponentText(LINE)) ?: println(LINE)

fun addChat(text: String) = mc.thePlayer?.addChatMessage(ChatComponentText(text)) ?: println(text)
fun addChat(text: ChatComponentText) = mc.thePlayer?.addChatMessage(text) ?: println(text.formattedText)

fun addTranslationChat(key: String, vararg any: Any) = addChat(getTranslatedString(key, any = any))

@JvmOverloads
fun getTranslatedString(key: String, withColor: Boolean = true, vararg any: Any): String {
    val chatComponentTranslation = ChatComponentTranslation(getTranslateKey(key), *any)
    return if (withColor) chatComponentTranslation.formattedText else chatComponentTranslation.unformattedText
}

fun getX() = ScaledResolution(mc).scaledWidth.toFloat()

fun getY() = ScaledResolution(mc).scaledHeight.toFloat()

fun isNotPlayZombies(): Boolean {
    if (mc.thePlayer == null || mc.theWorld == null) return true

    val scoreboard = mc.theWorld.scoreboard ?: return true
    val objective = scoreboard.getObjectiveInDisplaySlot(1) ?: return true

    if (EnumChatFormatting.getTextWithoutFormattingCodes(objective.displayName) != "ZOMBIES") return true

    for (score in scoreboard.getSortedScores(objective))
        if (EnumChatFormatting.getTextWithoutFormattingCodes(ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.playerName), score.playerName)).replace(Regex("[^A-Za-z0-9_]"), "").contains(mc.thePlayer.name))
            return false
    return true
}

fun runBatchFileAndQuit(file: File, commands: String) {
    file.writeText(commands.replace("\n", "\r\n"))
    Runtime.getRuntime().exec("cmd /c start ${file.absolutePath}")
    mc.shutdown()
}

private fun getTranslateKey(key: String): String {
    val lang = LastWeapons.instance.config.language
    val langCode = when (lang) {
        "Auto" -> return key
        "English (US)" -> "en_US"
        "한국어 (한국)" ->  "ko_KR"
        else -> "en_US"
    }
    val langFile = Properties()
    val resourceLocation = ResourceLocation(MODID, "lang/$langCode.lang")
    try {
        InputStreamReader(mc.resourceManager.getResource(resourceLocation).inputStream, StandardCharsets.UTF_8).use { langFile.load(it) }
    } catch (_: Exception) {
        return key
    }
    return langFile.getProperty(key, key)
}