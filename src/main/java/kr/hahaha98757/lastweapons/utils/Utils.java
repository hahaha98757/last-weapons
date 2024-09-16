package kr.hahaha98757.lastweapons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String LINE = "§e-----------------------------------------------------";
    private static final Minecraft mc = Minecraft.getMinecraft();

    private static List<String> getScoreboard() {
        List<String> strings = new ArrayList<>();
        if (mc.theWorld == null || mc.thePlayer == null) return null;

        Scoreboard scoreboard = mc.theWorld.getScoreboard();
        ScoreObjective sidebar = scoreboard.getObjectiveInDisplaySlot(1);

        if (sidebar == null || !LanguageSupport.ZOMBIES_TITLE.contains(EnumChatFormatting.getTextWithoutFormattingCodes(sidebar.getDisplayName()))) return null;

        for (Score score : scoreboard.getSortedScores(sidebar))
            for (int i = 1; i <= 15; i++)
                if (score.getScorePoints() == i)
                    strings.add(EnumChatFormatting.getTextWithoutFormattingCodes(score.getPlayerName()));
        return strings;
    }

    public static boolean isNotPlayZombies() {
        if (getScoreboard() == null) return true;
        for (String str : getScoreboard()) if (str.contains(mc.thePlayer.getName())) return false;
        return true;
    }

    public static void addChat(String text) {
        mc.thePlayer.addChatComponentMessage(new ChatComponentText(text));
    }

    public static void addChatLine(String text) {
        mc.thePlayer.addChatComponentMessage(new ChatComponentText(LINE + "\n" + text + "\n" + LINE));
    }

    public static float getX() {
        return new ScaledResolution(mc).getScaledWidth();
    }

    public static float getY() {
        return new ScaledResolution(mc).getScaledHeight();
    }
}