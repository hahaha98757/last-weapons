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