package kr.hahaha98757.lastweapons.mixins;

import kr.hahaha98757.lastweapons.LastWeapons;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.util.EnumChatFormatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "displayTitle", at = @At("HEAD"))
    private void displayTitle(String title, String subTitle, int timeFadeIn, int displayTime, int timeFadeOut, CallbackInfo ci) {
        if (title != null) {
            String text = EnumChatFormatting.getTextWithoutFormattingCodes(title);
            if (text.equals("You Win!") || text.equals("승리했습니다!")) LastWeapons.getInstance().getEventListener().setWin(true);
        }
    }
}