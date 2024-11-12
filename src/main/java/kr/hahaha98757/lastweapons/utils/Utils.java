package kr.hahaha98757.lastweapons.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
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

        if (sidebar == null || !EnumChatFormatting.getTextWithoutFormattingCodes(sidebar.getDisplayName()).equals("ZOMBIES")) return null;

        for (Score score : scoreboard.getSortedScores(sidebar))
            for (int i = 1; i <= 15; i++)
                if (score.getScorePoints() == i)
                    strings.add(EnumChatFormatting.getTextWithoutFormattingCodes(ScorePlayerTeam.formatPlayerName(scoreboard.getPlayersTeam(score.getPlayerName()), score.getPlayerName())).replaceAll("[^A-Za-z0-9가-힣:._,/\\s]", "").trim());
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

    public static void addChatWithURL(String text1, String urlText, String url, String showURLText, String text2) {
        ChatComponentText url1 = new ChatComponentText(urlText);

        url1.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        url1.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(showURLText)));

        ChatComponentText text = new ChatComponentText("");

        text.appendSibling(new ChatComponentText(text1));
        text.appendSibling(url1);
        text.appendSibling(new ChatComponentText(text2));

        mc.thePlayer.addChatComponentMessage(text);
    }

    public static float getX() {
        return new ScaledResolution(mc).getScaledWidth();
    }

    public static float getY() {
        return new ScaledResolution(mc).getScaledHeight();
    }

    public static int getLevel(String itemName) {
        if (itemName.contains("Ultimate")) try {
            itemName = itemName.split("Ultimate")[1].trim();
        } catch (Exception e) {
            return 0;
        }
        if (itemName.contains("레벨")) itemName = itemName.split("레벨")[0].trim();

        if (itemName.equals("II") || itemName.equals("Extra Health II") || itemName.equals("추가 체력 II") || itemName.contains("2") || itemName.equals("Extra Health 2"))
            return 2;
        else if (itemName.equals("III") || itemName.equals("Extra Health III") || itemName.equals("추가 체력 III") || itemName.contains("3") || itemName.equals("Extra Health 3"))
            return 3;
        else if (itemName.equals("IV") || itemName.equals("Extra Health IV") || itemName.equals("추가 체력 IV") || itemName.contains("4") || itemName.equals("Extra Health 4"))
            return 4;
        else if (itemName.equals("V") || itemName.equals("Extra Health V") || itemName.equals("추가 체력 V") || itemName.contains("5") || itemName.equals("Extra Health 5"))
            return 5;
        else if (itemName.equals("Extra Health VI") || itemName.equals("추가 체력 VI") || itemName.equals("Extra Health 6"))
            return 6;
        else if (itemName.equals("Extra Health VII") || itemName.equals("추가 체력 VII") || itemName.equals("Extra Health 7"))
            return 7;
        else if (itemName.equals("Extra Health VIII") || itemName.equals("추가 체력 VIII") || itemName.equals("Extra Health 8"))
            return 8;
        else if (itemName.equals("Extra Health IX") || itemName.equals("추가 체력 IX") || itemName.equals("Extra Health 9"))
            return 9;
        else if (itemName.equals("Extra Health X") || itemName.equals("추가 체력 X") || itemName.equals("Extra Health 10"))
            return 10;
        else return 0;
    }
}