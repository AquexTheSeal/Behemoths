package org.celestialworkshop.behemoths.mixin.jei;

import mezz.jei.library.render.ItemStackRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ItemStackRenderer.class, remap = false)
public class ItemStackRendererMixin {

    @Inject(
            method = "render(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/item/ItemStack;II)V",
            at = @At(value = "HEAD"),
            remap = false
    )
    public void render(GuiGraphics guiGraphics, ItemStack ingredient, int posX, int posY, CallbackInfo ci) {
        if (ingredient != null) {
            if (ingredient.getItem() instanceof BehemothHeartItem) {
                BehemothHeartItem.fillHeartEnergy(ingredient);
            }
        }
    }
}
