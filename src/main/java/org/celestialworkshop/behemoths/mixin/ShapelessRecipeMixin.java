package org.celestialworkshop.behemoths.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeMixin {

    @ModifyReturnValue(
            method = "matches(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/level/Level;)Z",
            at = @At("RETURN")
    )
    public boolean matches(boolean original, @Local(argsOnly = true) CraftingContainer pContainer) {
        List<ItemStack> hearts = new ArrayList<>();
        List<ItemStack> fullHearts = new ArrayList<>();
        for (int i = 0; i < pContainer.getContainerSize(); i++) {
            ItemStack stack = pContainer.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof BehemothHeartItem) {
                hearts.add(stack);
                if (BehemothHeartItem.hasMaxHeartEnergy(stack)) {
                    fullHearts.add(stack);
                }
            }
        }
        return original && (!fullHearts.isEmpty() && hearts.size() == fullHearts.size());
    }
}
