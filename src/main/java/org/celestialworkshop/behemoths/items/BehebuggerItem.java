package org.celestialworkshop.behemoths.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.utils.WorldUtils;

public class BehebuggerItem extends Item {

    public BehebuggerItem(Properties pProperties) {
        super(pProperties);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            WorldUtils.openPandemoniumSelection(pLevel);
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pEntity instanceof Player pl) {
            if (pl.getOffhandItem() == pStack) {
                pEntity.setDeltaMovement(pEntity.getLookAngle().scale(2F));
            }
        }
    }
}
