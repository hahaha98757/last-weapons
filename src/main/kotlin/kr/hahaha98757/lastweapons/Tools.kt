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

fun addChat(text: ChatComponentText) = if (mc.thePlayer != null) mc.thePlayer.addChatMessage(text) else println(text.formattedText)

fun addChatLine(text: String) = if (mc.thePlayer != null) mc.thePlayer.addChatMessage(ChatComponentText("$LINE\n$text\n$LINE")) else println("With line: $text")

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

    return if (name == "II" || name == "Extra Health II" || name == "추가 체력 II" || name.contains("2") || name == "Extra Health 2") 2
    else if (name == "III" || name == "Extra Health III" || name == "추가 체력 III" || name.contains("3") || name == "Extra Health 3") 3
    else if (name == "IV" || name == "Extra Health IV" || name == "추가 체력 IV" || name.contains("4") || name == "Extra Health 4") 4
    else if (name == "V" || name == "Extra Health V" || name == "추가 체력 V" || name.contains("5") || name == "Extra Health 5") 5
    else if (name == "Extra Health VI" || name == "추가 체력 VI" || name == "Extra Health 6") 6
    else if (name == "Extra Health VII" || name == "추가 체력 VII" || name == "Extra Health 7") 7
    else if (name == "Extra Health VIII" || name == "추가 체력 VIII" || name == "Extra Health 8") 8
    else if (name == "Extra Health IX" || name == "추가 체력 IX" || name == "Extra Health 9") 9
    else if (name == "Extra Health X" || name == "추가 체력 X" || name == "Extra Health 10") 10
    else 0
}

fun isNotPlayZombies(): Boolean {
    if (mc.thePlayer == null || mc.theWorld == null) return true

    val scoreboard = mc.theWorld.scoreboard
    val objective = scoreboard.getObjectiveInDisplaySlot(1)

    if (objective == null || EnumChatFormatting.getTextWithoutFormattingCodes(objective.displayName) != "ZOMBIES") return true

    for (score in scoreboard.getSortedScores(objective))
        return !EnumChatFormatting.getTextWithoutFormattingCodes(ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.playerName), score.playerName)).replace(Regex("[^A-Za-z0-9_]"), "").contains(mc.thePlayer.name)
    return true
}

fun runBatchFileAndQuit(file: File, commands: String) {
    file.writeText(commands)
    Runtime.getRuntime().exec("cmd /c start ${file.absolutePath}")
    mc.shutdown()
}