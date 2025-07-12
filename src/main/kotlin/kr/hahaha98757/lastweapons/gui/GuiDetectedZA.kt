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
        this.buttonList.add(GuiButton(0, this.width / 2 - 100, this.height / 2, "Remove Last Weapons(Windows only)"))
        this.buttonList.add(GuiButton(1, this.width / 2 - 100, this.height / 2 + 30, "Remove Zombies Addon(Windows only)"))
        this.buttonList.add(GuiButton(2, this.width / 2 - 100, this.height / 2 + 60, "Quit the game"))
    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> {
                println("Run remove Last Weapons.")
                runBatchFileAndQuit(File(mc.mcDataDir, "mods/deleter_lastweapons.bat"), """
                    @echo off
                    chcp 65001
                    echo This is a mod deleter. It should continue after Minecraft quits.
                    echo 이것은 모드 제거 프로그램 입니다. 마인크래프트가 종료된 후 계속 진행되어야 합니다.
                    timeout /t 2 /nobreak
                    pause
                    echo Deleting Last Weapons...
                    echo Last Weapons를 제거하는 중...
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
                        runBatchFileAndQuit(File(mc.mcDataDir, "mods/deleter_lastweapons.bat"), """
                            @echo off
                            chcp 65001
                            echo This is a mod deleter. It should continue after Minecraft quits.
                            echo 이것은 모드 제거 프로그램 입니다. 마인크래프트가 종료된 후 계속 진행되어야 합니다.
                            timeout /t 2 /nobreak
                            pause
                            echo Deleting Zombies Addon...
                            echo Zombies Addon을 제거하는 중...
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