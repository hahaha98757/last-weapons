package kr.hahaha98757.lastweapons

import kr.hahaha98757.lastweapons.config.LWConfig
import kr.hahaha98757.lastweapons.gui.GuiDetectedZA
import kr.hahaha98757.lastweapons.update.UpdateChecker
import kr.hahaha98757.lastweapons.update.UpdateCheckerListener
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.io.File

const val MODID = "lastweapons"
const val NAME = "Last Weapons"
const val VERSION = "1.2.0"

@Mod(modid = MODID, name = NAME, version = VERSION, guiFactory = "kr.hahaha98757.lastweapons.config.LWGuiFactory")
class LastWeapons {
    init {
        instance = this
    }
    companion object {
        @JvmStatic
        lateinit var instance: LastWeapons
            private set
    }
    lateinit var config: LWConfig
        private set
    val eventListener = EventListener()
    private var hasZombiesAddon = false
    private var start = false

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        File(mc.mcDataDir, "mods/deleter.bat").delete()
        config = LWConfig(Configuration(event.suggestedConfigurationFile))
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(this)
        MinecraftForge.EVENT_BUS.register(eventListener)
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
        if (start) return
        if (hasZombiesAddon) {
            mc.displayGuiScreen(GuiDetectedZA())
            start = true
            return
        }

        UpdateChecker.checkUpdate()
        start = true
    }

    @SubscribeEvent
    fun onConfigChange(event: ConfigChangedEvent.OnConfigChangedEvent) {
        if (event.modID == MODID) config.save()
    }
}