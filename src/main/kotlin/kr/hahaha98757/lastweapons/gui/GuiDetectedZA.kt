package kr.hahaha98757.lastweapons.gui

import kr.hahaha98757.lastweapons.modFile
import kr.hahaha98757.lastweapons.runBatchFileAndQuit
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.fml.common.Loader
import org.lwjgl.input.Keyboard
import java.io.File

class GuiDetectedZA: GuiScreen() {

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.drawDefaultBackground()
        this.drawCenteredString(this.fontRendererObj, "Detected Zombies Addon.", this.width / 2, this.height / 2 - 40, 0xffffff)
        this.drawCenteredString(this.fontRendererObj, "Zombies Addon has Last Weapons.", this.width / 2, this.height / 2 - 31, 0xffffff)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun initGui() {
        super.initGui()
        this.buttonList.clear()
        this.buttonList.add(GuiButton(0, this.width / 2 - 100, this.height / 2, "Remove Last Weapons"))
        this.buttonList.add(GuiButton(1, this.width / 2 - 100, this.height / 2 + 30, "Remove Zombies Addon"))
        this.buttonList.add(GuiButton(2, this.width / 2 - 100, this.height / 2 + 60, "Quit the game"))
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> {
                println("Run remove Last Weapons.")
                runBatchFileAndQuit(File(File(mc.mcDataDir, "mods"), "deleter.bat"), """
                    @echo off
                    echo It should continue after Minecraft quits. If Minecraft quits quickly, you can skip the wait by pressing Ctrl + C, than N.
                    timeout /t 2 /nobreak
                    pause
                    del "${modFile.absolutePath}"
                    exit
                """.trimIndent())
            }
            1 -> {
                for (mod in Loader.instance().modList) {
                    if (mod.modId != "zombiesaddon") continue
                    val source = mod.source
                    if (source != null) {
                        println("Run remove Zombies Addon.")
                        runBatchFileAndQuit(File(File(mc.mcDataDir, "mods"), "deleter.bat"), """
                            @echo off
                            echo It should continue after Minecraft quits. If Minecraft quits quickly, you can skip the wait by pressing Ctrl + C, than N.
                            timeout /t 2 /nobreak
                            pause
                            del "${source.absolutePath}"
                            exit
                            """.trimIndent())
                    }
                }
            }
            2 -> mc.shutdown()
        }
    }

    override fun keyTyped(typedChar: Char, keyCode: Int) {
        if (keyCode != Keyboard.KEY_ESCAPE) super.keyTyped(typedChar, keyCode)
    }
}