package kr.hahaha98757.lastweapons.config

import net.minecraftforge.common.config.Configuration

object LWConfig {
    lateinit var config: Configuration

    var toggle = true
    var displayArmors = true
    var displayWeaponsLevel = true
    var displayCooledDownSkill = true

    fun loadConfig() {
        config.load()

        try {
            toggle = config.getBoolean(
                "Toggle Last Weapons",
                Configuration.CATEGORY_GENERAL,
                true,
                "Displays your weapons when you win."
            )
            displayArmors = config.getBoolean(
                "Display Armors",
                Configuration.CATEGORY_GENERAL,
                true,
                "Displays your armors."
            )
            displayWeaponsLevel = config.getBoolean(
                "Display Weapons Level",
                Configuration.CATEGORY_GENERAL,
                true,
                "Displays level of weapons and perks."
            )
            displayCooledDownSkill = config.getBoolean(
                "Display Cooled Down Skill",
                Configuration.CATEGORY_GENERAL,
                true,
                "Displays a black and white texture when a your skill is on cooldown."
            )
        } finally {
            if (config.hasChanged()) config.save()
        }
    }
}