package kr.hahaha98757.lastweapons.events

import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.fml.common.eventhandler.Event

class TitleEvent(private val title: String): Event() {
    fun getTitle(): String = EnumChatFormatting.getTextWithoutFormattingCodes(title)
}