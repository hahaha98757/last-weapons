package kr.hahaha98757.lastweapons.config

import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent

object LWConfig {
    lateinit var config: Configuration

    fun init(event: FMLPreInitializationEvent) {
        config = Configuration(event.suggestedConfigurationFile)
        loadConfig()
    }

    var toggle = true
    var displayArmors = true
    var displayWeaponsLevel = true
    var displayCooledDownSkill = true

    private fun loadConfig() {
        config.load()

        toggle = config.get(Configuration.CATEGORY_GENERAL, "Toggle Last Weapons", true, "Displays your weapons when you win.").boolean
        displayArmors = config.get(Configuration.CATEGORY_GENERAL, "Display Armors", true, "Displays your armors.").boolean
        displayWeaponsLevel = config.get(Configuration.CATEGORY_GENERAL, "Display Weapons Level", true, "Displays level of weapons and perks.").boolean
        displayCooledDownSkill = config.get(Configuration.CATEGORY_GENERAL, "Display Cooled Down Skill", true, "Displays a black and white texture when a your skill is on cooldown.").boolean
    }

    fun save() {
        config.save()
        loadConfig()
    }
}