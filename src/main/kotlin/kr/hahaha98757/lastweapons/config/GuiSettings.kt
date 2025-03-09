package kr.hahaha98757.lastweapons.config

import kr.hahaha98757.lastweapons.LastWeapons
import kr.hahaha98757.lastweapons.MODID
import kr.hahaha98757.lastweapons.NAME
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.ConfigElement
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.client.IModGuiFactory
import net.minecraftforge.fml.client.config.GuiConfig

@Suppress("unused")
class LWGuiFactory: IModGuiFactory {
    override fun initialize(minecraftInstance: Minecraft?) {
    }

    override fun mainConfigGuiClass(): Class<out GuiScreen> {
        return LWGuiConfig::class.java
    }

    override fun runtimeGuiCategories(): MutableSet<IModGuiFactory.RuntimeOptionCategoryElement>? {
        return null
    }

    override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement?): IModGuiFactory.RuntimeOptionGuiHandler? {
        return null
    }
}

class LWGuiConfig(parentScreen: GuiScreen):
    GuiConfig(parentScreen, ConfigElement(LastWeapons.instance.config.config.getCategory(Configuration.CATEGORY_GENERAL)).childElements, MODID, false, false, "$NAME Configuration")