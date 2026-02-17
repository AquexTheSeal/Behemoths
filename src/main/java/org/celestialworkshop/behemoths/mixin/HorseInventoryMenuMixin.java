package org.celestialworkshop.behemoths.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.celestialworkshop.behemoths.entities.ai.CustomSaddleable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HorseInventoryMenu.class)
public abstract class HorseInventoryMenuMixin extends AbstractContainerMenu {

    protected HorseInventoryMenuMixin(@Nullable MenuType<?> pMenuType, int pContainerId) {
        super(pMenuType, pContainerId);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInitInject(int pContainerId, Inventory pPlayerInventory, Container pContainer, AbstractHorse pHorse, CallbackInfo ci) {

        Slot oldSaddleSlot = this.slots.get(0);

        if (pHorse instanceof CustomSaddleable custom) {
            this.slots.set(0, new Slot(pContainer, 0, oldSaddleSlot.x, oldSaddleSlot.y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return custom.canEquipSaddle(stack) && !this.hasItem() && pHorse.isSaddleable();
                }

                @Override
                public boolean isActive() {
                    return pHorse.isSaddleable();
                }
            });
        }
    }
}
