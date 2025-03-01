package kr.hahaha98757.lastweapons

import kr.hahaha98757.lastweapons.config.LWConfig
import kr.hahaha98757.lastweapons.events.SoundEvent
import kr.hahaha98757.lastweapons.events.TitleEvent
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

const val MODID = "lastweapons"
const val NAME = "Last Weapons"
const val VERSION = "1.1.0"

@Mod(modid = MODID, name = NAME, version = VERSION, guiFactory = "kr.hahaha98757.lastweapons.config.LWGuiFactory")
class LastWeapons {
    private var hasZombiesAddon = false
    private var join = false

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        LWConfig.config = Configuration(event.modConfigurationDirectory)
        LWConfig.loadConfig()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        hasZombiesAddon = Loader.isModLoaded("zombiesaddon")
    }

    @SubscribeEvent
    fun onPlayerJoinWorld(event: EntityJoinWorldEvent) {
        if (event.entity !== mc.thePlayer) return

        if (join) return

        UpdateChecker.checkUpdate()

        if (hasZombiesAddon) {
            addChatLine("§cYou are using Zombies Addon.\n§cZombies Addon is having Last Weapons.\n§c§lThe game ends after 10 seconds.")
            ClientCrash.hasZombiesAddon = true
            MinecraftForge.EVENT_BUS.register(ClientCrash())
        }
        join = true
    }

    val weapons = arrayOfNulls<ItemStack>(9)
    val armors = arrayOfNulls<ItemStack>(4)
    var win = false

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Text) {
        if (!LWConfig.toggle) return
        if (isNotPlayZombies()) {
            win = false
            return
        }

        if (win) {
            val renderItem = mc.renderItem
            val fr = mc.fontRendererObj
            var x = (getX() / 2 - 88).toInt()
            var y = (getY() - 19).toInt()

            GlStateManager.pushAttrib()

            for (i in 0..8) {
                val weapon = weapons[i]

                if (weapon != null) {
                    if (i == 4 && LWConfig.displayCooledDownSkill) {
                        if (weapon.item == Items.dye && weapon.itemDamage == 8) {
                            val name = weapon.displayName
                            if (name.contains("Heal Skill") || name.contains("회복 기술"))
                                displayTexture("textures/items/heal_cool.png", x + 20*i, y)
                            else if (name.contains("Lightning Rod Skill") || name.contains("번개 막대 기술"))
                                displayTexture("textures/items/lrod_cool.png", x + 20*i, y)
                            else if (name.contains("Deployable Turret Skill"))
                                displayTexture("textures/items/turret_cool.png", x + 20*i, y)
                            continue
                        }
                    }

                    val level = getLevel(EnumChatFormatting.getTextWithoutFormattingCodes(weapon.displayName))

                    renderItem.renderItemAndEffectIntoGUI(weapon, x + 20*i, y)

                    if (LWConfig.displayWeaponsLevel && level != 0)
                        displayTexture("textures/items/level$level.png", x + 20*i, y)
                    renderItem.renderItemOverlayIntoGUI(fr, weapon, x + 20*i, y, null)
                }
            }

            GlStateManager.popAttrib()

            if (LWConfig.displayArmors) {
                x = (getX() / 2 + 12).toInt()
                y = (getY() - 60).toInt()

                for (i in 0..3) {
                    renderItem.renderItemAndEffectIntoGUI(armors[3-i], x + 20*i, y)
                    renderItem.renderItemOverlayIntoGUI(fr, armors[3-i], x + 20*i, y, null)
                }
            }
        }

        val player = mc.thePlayer

        if (player.inventory.getStackInSlot(1) == null) return

        for (i in 0..8) weapons[i] = player.inventory.getStackInSlot(i)

        for (i in 0..3) armors[i] = player.inventory.armorInventory[i]

        System.arraycopy(player.inventory.armorInventory, 0, armors, 0, 4)
    }

    @SubscribeEvent
    fun onTitle(event: TitleEvent) {
        if (event.getTitle() == "You Win!" || event.getTitle() == "승리했습니다!") win = true
    }

    @SubscribeEvent
    fun onSound(event: SoundEvent) {
        if (event.getSoundName() == "mob.wither.spawn") win = false
    }

    private fun displayTexture(path: String, x: Int, y: Int) {
        mc.textureManager.bindTexture(ResourceLocation(MODID, path))
        GlStateManager.disableDepth()
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, 16, 16, 16f, 16f)
        GlStateManager.enableDepth()
    }
}