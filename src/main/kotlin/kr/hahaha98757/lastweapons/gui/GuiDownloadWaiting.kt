package kr.hahaha98757.lastweapons.gui

import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard

class GuiDownloadWaiting: GuiScreen() {

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.drawDefaultBackground()
        if (!fail) this.drawCenteredString(this.fontRendererObj, "Downloading...", this.width / 2, this.height / 2 - 40, 0xffffff)
        else this.drawCenteredString(this.fontRendererObj, "Failed download the update.", this.width / 2, this.height / 2 - 40, 0xff5555)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode != Keyboard.KEY_ESCAPE) super.keyTyped(typedChar, keyCode)
    }

    companion object {
        var fail = false
    }
}