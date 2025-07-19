package kr.hahaha98757.lastweapons.gui

import kr.hahaha98757.lastweapons.getTranslatedString
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard

class GuiDownloadWaiting: GuiScreen() {

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        if (!fail) drawCenteredString(fontRendererObj, getTranslatedString("lastweapons.gui.downloadWaiting.title"), width / 2, height / 2 - 40, 0xffffff)
        else drawCenteredString(fontRendererObj, getTranslatedString("lastweapons.gui.downloadWaiting.failed"), width / 2, height / 2 - 40, 0xff5555)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode != Keyboard.KEY_ESCAPE) super.keyTyped(typedChar, keyCode)
    }

    companion object {
        var fail = false
    }
}