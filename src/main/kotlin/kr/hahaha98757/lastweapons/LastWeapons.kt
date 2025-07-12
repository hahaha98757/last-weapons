package kr.hahaha98757.lastweapons

import kr.hahaha98757.lastweapons.config.LWConfig
import kr.hahaha98757.lastweapons.gui.GuiDetectedZA
import kr.hahaha98757.lastweapons.update.UpdateChecker
import kr.hahaha98757.lastweapons.update.UpdateCheckerListener
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.File

const val MODID = "lastweapons"
const val NAME = "Last Weapons"
const val VERSION = "1.2.4"

@Mod(modid = MODID, name = NAME, version = VERSION, guiFactory = "kr.hahaha98757.lastweapons.config.LWGuiFactory")
class LastWeapons {
    private var hasZombiesAddon = false

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        File(mc.mcDataDir, "mods/deleter.bat").delete()
        File(mc.mcDataDir, "mods/deleter_lastweapons.bat").delete()
        LWConfig.init(event)
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(EventListener())
        MinecraftForge.EVENT_BUS.register(UpdateCheckerListener())
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        hasZombiesAddon = Loader.isModLoaded("zombiesaddon")
        UpdateChecker.setVersion()
        println("$NAME v$VERSION is loaded.")
    }

    @SubscribeEvent
    fun startGame(event: GuiScreenEvent.DrawScreenEvent.Post) {
        MinecraftForge.EVENT_BUS.unregister(this)
        if (hasZombiesAddon) mc.displayGuiScreen(GuiDetectedZA())
        else UpdateChecker.checkUpdate()
    }
}