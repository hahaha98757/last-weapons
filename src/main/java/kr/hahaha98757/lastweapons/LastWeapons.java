package kr.hahaha98757.lastweapons;

import kr.hahaha98757.lastweapons.events.SoundEvent;
import kr.hahaha98757.lastweapons.events.TitleEvent;
import kr.hahaha98757.lastweapons.utils.ClientCrash;
import kr.hahaha98757.lastweapons.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@Mod( modid = LastWeapons.MODID, name = LastWeapons.NAME, version = LastWeapons.VERSION)
public class LastWeapons {
    public static final String MODID = "lastweapons";
    public static final String NAME = "Last Weapons";
    public static final String VERSION = "1.0.1";
    private final Minecraft mc;
    private final KeyBinding toggleLastWeapons;
    private static boolean lastWeapons = true;

    public static boolean haveZombiesAddon;
    public static boolean join;

    public LastWeapons() {
        this.mc = Minecraft.getMinecraft();
        this.toggleLastWeapons = new KeyBinding("Toggle Last Weapons", Keyboard.CHAR_NONE, NAME);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientRegistry.registerKeyBinding(this.toggleLastWeapons);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new UpdateChecker());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (Loader.isModLoaded("zombiesaddon")) haveZombiesAddon = true;
    }

    @SubscribeEvent
    public void onPlayerJoinWorld(EntityJoinWorldEvent event) {
        if (event.entity != mc.thePlayer) return;

        if (join) return;

        if (haveZombiesAddon) {
            Utils.addChatLine("§cYou are using Zombies Addon.\n§cZombies Addon is having Last Weapons.\n§c§lThe game ends after 10 seconds.");
            ClientCrash.haveZombiesAddon();
            MinecraftForge.EVENT_BUS.register(new ClientCrash());
        }

        join = true;
    }


    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (!toggleLastWeapons.isPressed()) return;
        lastWeapons = !lastWeapons;
        Utils.addChat("§eToggled Last Weapons to " + (lastWeapons ? "§aon" : "§coff"));
    }


    private static final ItemStack[] weapons = new ItemStack[9];
    private static final ItemStack[] armors = new ItemStack[4];
    private static boolean win;

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT) return;
        if (!lastWeapons) return;

        if (Utils.isNotPlayZombies()) {
            win = false;
            return;
        }

        if (win) {
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
            int x = (int) (Utils.getX() / 2 - 88);
            int y = (int) (Utils.getY() - 19);

            GlStateManager.pushAttrib();

            for (int i = 0; i < 9; i++) {
                ItemStack weapon = weapons[i];

                if (weapon != null) {
                    int level = Utils.getLevel(EnumChatFormatting.getTextWithoutFormattingCodes(weapon.getDisplayName()));

                    renderItem.renderItemAndEffectIntoGUI(weapon, x + 20*i, y);

                    if (level != 0) {
                        mc.getTextureManager().bindTexture(new ResourceLocation("lastweapons", "textures/items/level" + level + ".png"));
                        GlStateManager.disableDepth();
                        Gui.drawModalRectWithCustomSizedTexture(x + 20*i, y, 0, 0, 16,16, 16, 16);
                        GlStateManager.enableDepth();
                    }
                    renderItem.renderItemOverlayIntoGUI(fr, weapons[i], x + 20*i, y, null);
                }
            }

            GlStateManager.popAttrib();

            x = (int) (Utils.getX() / 2 + 12);
            y = (int) (Utils.getY() - 60);
            for (int i = 0; i < 4; i++) {
                renderItem.renderItemAndEffectIntoGUI(armors[3-i], x + 20*i, y);
                renderItem.renderItemOverlayIntoGUI(fr, armors[3-i], x + 20*i, y, null);
            }
            return;
        }

        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (player.inventory.getStackInSlot(1) == null) return;

        for (int i = 0; i < 9; i++) weapons[i] = player.inventory.getStackInSlot(i);

        System.arraycopy(player.inventory.armorInventory, 0, armors, 0, 4);
    }

    @SubscribeEvent
    public void onTitle(TitleEvent event) {
        if (event.getTitle().equals("You Win!") || event.getTitle().equals("승리했습니다!")) win = true;
    }

    @SubscribeEvent
    public void onSound(SoundEvent event) {
        if (event.getSoundName().equals("mob.wither.spawn")) win = false;
    }
}