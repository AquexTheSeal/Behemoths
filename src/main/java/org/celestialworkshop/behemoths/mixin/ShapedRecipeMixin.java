package org.celestialworkshop.behemoths.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {

    @ModifyReturnValue(
            method = "matches(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/level/Level;)Z",
            at = @At("RETURN")
    )
    public boolean behemoths$validateHeartEnergy(boolean original, @Local(argsOnly = true) CraftingContainer pContainer) {
        if (!original) return false;

        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack stack = pContainer.getItem(i);

            if (!stack.isEmpty() && stack.getItem() instanceof BehemothHeartItem) {
                if (!BehemothHeartItem.hasMaxHeartEnergy(stack)) {
                    return false;
                }
            }
        }
        return true;
    }
}
