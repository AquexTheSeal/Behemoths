package org.celestialworkshop.behemoths.mixin.emi;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.emi.emi.api.stack.ItemEmiStack;
import net.minecraft.world.item.ItemStack;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = ItemEmiStack.class, remap = false)
public class ItemEmiStackMixin {

    @ModifyReturnValue(
            method = "getItemStack",
            at = @At("RETURN")
    )
    public ItemStack getItemStack(ItemStack original) {
        if (original.getItem() instanceof BehemothHeartItem) {
            BehemothHeartItem.fillHeartEnergy(original);
        }
        return original;
    }
}
