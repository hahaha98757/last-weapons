package kr.hahaha98757.lastweapons

import kr.hahaha98757.lastweapons.config.LWConfig
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class EventListener {
    private val weapons = arrayOfNulls<ItemStack>(9)
    private val armors = arrayOfNulls<ItemStack>(4)
    companion object {
        @JvmStatic
        var win = false
    }

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

    private fun displayTexture(path: String, x: Int, y: Int) {
        mc.textureManager.bindTexture(ResourceLocation(MODID, path))
        GlStateManager.disableDepth()
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, 16, 16, 16f, 16f)
        GlStateManager.enableDepth()
    }
}