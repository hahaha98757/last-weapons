package kr.hahaha98757.lastweapons

import kr.hahaha98757.lastweapons.config.LWConfig
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.client.event.ConfigChangedEvent
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

                weapon?.let {
                    if (i == 4 && LWConfig.displayCooledDownSkill && it.item == Items.dye && it.itemDamage == 8) {
                        val name = it.displayName
                        if ("Heal Skill" in name || "회복 기술" in name) {
                            displayTexture("textures/items/heal_cool.png", x + 20 * i, y)
                            return@let
                        } else if ("Lightning Rod Skill" in name || "번개 막대 기술" in name) {
                            displayTexture("textures/items/lrod_cool.png", x + 20 * i, y)
                            return@let
                        } else if ("Deployable Turret Skill" in name) {
                            displayTexture("textures/items/turret_cool.png", x + 20 * i, y)
                            return@let
                        }
                    }

                    val level = getLevel(EnumChatFormatting.getTextWithoutFormattingCodes(it.displayName))

                    renderItem.renderItemAndEffectIntoGUI(it, x + 20 * i, y)

                    if (LWConfig.displayWeaponsLevel && level != 0)
                        displayTexture("textures/items/level$level.png", x + 20 * i, y)
                    renderItem.renderItemOverlayIntoGUI(fr, it, x + 20 * i, y, null)
                }
            }

            GlStateManager.popAttrib()

            if (LWConfig.displayArmors) {
                x = (getX() / 2 + 12).toInt()
                y = (getY() - 60).toInt()

                for (i in 3 downTo 0) {
                    renderItem.renderItemAndEffectIntoGUI(armors[i], x + 20 * i, y)
                    renderItem.renderItemOverlayIntoGUI(fr, armors[i], x + 20 * i, y, null)
                }
            }
        }

        val player = mc.thePlayer

        if (player.inventory.getStackInSlot(1) == null) return

        for (i in 0..8) weapons[i] = player.inventory.getStackInSlot(i)
        player.inventory.armorInventory.copyInto(armors)
    }

    private fun displayTexture(path: String, x: Int, y: Int) {
        mc.textureManager.bindTexture(ResourceLocation(MODID, path))
        GlStateManager.disableDepth()
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0f, 0f, 16, 16, 16f, 16f)
        GlStateManager.enableDepth()
    }

    private fun getLevel(itemName: String): Int {
        var name = itemName
        if ("Ultimate" in itemName) try {
            name = itemName.split("Ultimate")[1].trim()
        } catch (e: Exception) {
            return 0
        }
        if ("레벨" in itemName) name = itemName.split("레벨")[0].trim()

        return when (name) {
            "II", "Extra Health II", "추가 체력 II" -> 2
            "III", "Extra Health III", "추가 체력 III" -> 3
            "IV", "Extra Health IV", "추가 체력 IV" -> 4
            "V", "Extra Health V", "추가 체력 V" -> 5
            "Extra Health VI", "추가 체력 VI" -> 6
            "Extra Health VII", "추가 체력 VII" -> 7
            "Extra Health VIII", "추가 체력 VIII" -> 8
            "Extra Health IX", "추가 체력 IX" -> 9
            "Extra Health X", "추가 체력 X" -> 10
            else -> 0
        }
    }

    @SubscribeEvent
    fun onConfigChange(event: ConfigChangedEvent.OnConfigChangedEvent) {
        if (event.modID == MODID) LWConfig.save()
    }
}