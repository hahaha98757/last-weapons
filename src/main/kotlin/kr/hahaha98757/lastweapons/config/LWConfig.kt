package kr.hahaha98757.lastweapons.config

import net.minecraftforge.common.config.Configuration
import net.minecraftforge.common.config.Property

class LWConfig(val config: Configuration) {
    var enableMod = true
    var language = "Auto"
    var displayArmors = true
    var displayWeaponsLevel = true
    var displayCooledDownSkill = true

    init {
        loadConfig()
    }

    private fun loadConfig() {
        config.load()

        enableMod = createOption("lastweapons.config.enableMod", config.get(Configuration.CATEGORY_GENERAL, "enableMod", true, "lastweapons.config.enableMod.description")).boolean
        language = createOption("lastweapons.config.language", config.get(Configuration.CATEGORY_GENERAL, "language", "Auto", "lastweapons.config.language.description", arrayOf("Auto", "English (US)", "한국어 (한국)"))).string
        displayArmors = createOption("lastweapons.config.displayArmors", config.get(Configuration.CATEGORY_GENERAL, "displayArmors", true, "lastweapons.config.displayArmors.description")).boolean
        displayWeaponsLevel = createOption("lastweapons.config.displayWeaponsLevel", config.get(Configuration.CATEGORY_GENERAL, "displayWeaponsLevel", true, "lastweapons.config.displayWeaponsLevel.description")).boolean
        displayCooledDownSkill = createOption("lastweapons.config.displayCooledDownSkill", config.get(Configuration.CATEGORY_GENERAL, "displayCooledDownSkill", true, "lastweapons.config.displayCooledDownSkill.description")).boolean
    }

    fun save() {
        config.save()
        loadConfig()
    }

    private fun createOption(langKey: String, prop: Property): Property {
        prop.languageKey = langKey
        return prop
    }
}