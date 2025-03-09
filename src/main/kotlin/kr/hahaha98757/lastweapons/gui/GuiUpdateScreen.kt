package kr.hahaha98757.lastweapons.gui

import kr.hahaha98757.lastweapons.update.UpdateChecker
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiConfirmOpenLink
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard
import java.awt.Desktop
import java.net.URI

class GuiUpdateScreen(private val i: Int): GuiScreen() {

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val str = if (i == 0) "§cThe mandatory update has been released." else "The new recommended version has been released."
        this.drawDefaultBackground()
        this.drawCenteredString(this.fontRendererObj, str, this.width / 2, this.height / 2 - 40, 0xffffff)
        if (i == 0) this.drawCenteredString(this.fontRendererObj, "You should update this mod or can't play the game.", this.width/2, this.height / 2 - 31, 0xff5555)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun initGui() {
        super.initGui()
        this.buttonList.clear()
        this.buttonList.add(GuiButton(0, this.width / 2 - 100, this.height / 2, "Auto update and quit the game"))
        this.buttonList.add(GuiButton(1, this.width / 2 - 100, this.height / 2 + 30, "Open download URL"))
        if (i != 0) this.buttonList.add(GuiButton(2, this.width / 2 - 100, this.height / 2 + 60, "Continue"))
        this.buttonList.add(GuiButton(3, this.width / 2 - 100, this.height / 2 + if (i != 0) 90 else 60, "Quit the game"))
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> {
                mc.displayGuiScreen(GuiDownloadWaiting())
                UpdateChecker.autoUpdate()
            }
            1 -> {
                mc.displayGuiScreen(GuiConfirmOpenLink(this, "https://github.com/hahaha98757/last-weapons/releases", 0, true))
            }
            2 -> mc.displayGuiScreen(null)
            3 -> mc.shutdown()
        }
    }

    override fun confirmClicked(result: Boolean, id: Int) {
        if (result) try {
            Desktop.getDesktop().browse(URI("https://github.com/hahaha98757/last-weapons/releases"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mc.displayGuiScreen(this)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode != Keyboard.KEY_ESCAPE) super.keyTyped(typedChar, keyCode)
    }
}