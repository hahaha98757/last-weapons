package kr.hahaha98757.lastweapons.gui

import kr.hahaha98757.lastweapons.addTranslationChat
import kr.hahaha98757.lastweapons.getTranslatedString
import kr.hahaha98757.lastweapons.update.UpdateChecker
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiConfirmOpenLink
import net.minecraft.client.gui.GuiScreen
import org.lwjgl.input.Keyboard
import java.awt.Desktop
import java.net.URI

class GuiUpdateScreen(private val mandatory: Boolean): GuiScreen() {

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        val str = getTranslatedString(if (mandatory) "lastweapons.gui.GuiUpdateScreen.title.mandatory" else "lastweapons.gui.GuiUpdateScreen.title.normal")
        drawDefaultBackground()
        drawCenteredString(fontRendererObj, str, width / 2, height / 2 - 40, 0xffffff)
        if (mandatory) drawCenteredString(fontRendererObj, getTranslatedString("lastweapons.gui.GuiUpdateScreen.message.mandatory"), width / 2, height / 2 - 31, 0xff5555)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun initGui() {
        super.initGui()
        buttonList.clear()
        buttonList.add(GuiButton(0, width / 2 - 100, height / 2, getTranslatedString("lastweapons.gui.GuiUpdateScreen.button.autoupdate")))
        buttonList.add(GuiButton(1, width / 2 - 100, height / 2 + 30, getTranslatedString("lastweapons.gui.GuiUpdateScreen.button.url")))
        if (!mandatory) buttonList.add(GuiButton(2, width / 2 - 100, height / 2 + 60, getTranslatedString("lastweapons.gui.GuiUpdateScreen.button.continue")))
        buttonList.add(GuiButton(3, width / 2 - 100, height / 2 + if (mandatory) 90 else 60, getTranslatedString("menu.quit")))
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> {
                if ("win" !in System.getProperty("os.name").lowercase()) {
                    addTranslationChat("lastweapons.gui.windowsOnly")
                    return
                }
                mc.displayGuiScreen(GuiDownloadWaiting())
                UpdateChecker.autoUpdate()
            }
            1 -> mc.displayGuiScreen(GuiConfirmOpenLink(this, "https://github.com/hahaha98757/last-weapons/releases", 0, true))
            2 -> mc.displayGuiScreen(null)
            3 -> mc.shutdown()
        }
    }

    override fun confirmClicked(result: Boolean, id: Int) {
        if (result) try {
            Desktop.getDesktop().browse(URI("https://github.com/hahaha98757/last-weapons/releases"))
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
        }
        mc.displayGuiScreen(this)
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode != Keyboard.KEY_ESCAPE) super.keyTyped(typedChar, keyCode)
    }
}