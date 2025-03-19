package kr.hahaha98757.lastweapons

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.scoreboard.ScorePlayerTeam
import net.minecraft.util.ChatComponentText
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.fml.common.Loader
import java.io.File

val mc: Minecraft by lazy { Minecraft.getMinecraft() }
val modFile: File by lazy {
    var file = File("")
    for (mod in Loader.instance().modList) if (mod.modId == MODID) file = mod.source
    file
}

const val LINE = "§e-----------------------------------------------------"

fun addChat(text: ChatComponentText) = mc.thePlayer?.addChatMessage(text) ?: println(text.formattedText)

fun addChatLine(text: String) = mc.thePlayer?.addChatMessage(ChatComponentText("$LINE\n$text\n$LINE")) ?: println("With line: $text")

fun getX() = ScaledResolution(mc).scaledWidth.toFloat()

fun getY() = ScaledResolution(mc).scaledHeight.toFloat()

fun getLevel(itemName: String): Int {
    var name = ""
    if (itemName.contains("Ultimate")) try {
        name = itemName.split("Ultimate")[1].trim()
    } catch (e: Exception) {
        return 0
    }
    if (name.contains("레벨")) name = itemName.split("레벨")[0].trim()

    return when (name) {
        "II", "Extra Health II", "추가 체력 II" -> 2
        "III", "Extra Health III", "추가 체력 III" -> 3
        "IV", "Extra Health IV", "추가 체력 IV" -> 4
        "V", "Extra Health V", "추가 체력 V" -> 5
        "Extra Health VI", "추가 체력 VI" -> 6
        "Extra Health VII", "추가 체력 VII" -> 7
        "Extra Health VIII", "추가 체력 VIII" -> 8
        "Extra Health IX", "추가 체력 IX" -> 9
        "Extra Health X", "추가 체력 X" -> 10
        else -> 0
    }
}

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
    file.writeText(commands)
    Runtime.getRuntime().exec("cmd /c start ${file.absolutePath}")
    mc.shutdown()
}