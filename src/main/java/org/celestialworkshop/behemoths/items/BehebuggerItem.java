package org.celestialworkshop.behemoths.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.entities.projectile.CharydbisShard;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;

public class BehebuggerItem extends Item {

    public BehebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            CharydbisShard shard = new CharydbisShard(BMEntityTypes.CHARYDBIS_SHARD.get(), pLevel);
            shard.moveTo(pPlayer.getEyePosition());
            shard.setOwner(pPlayer);
            shard.setStatusFlag(CharydbisShard.RETURNABLE_STATUS);
            shard.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 0.2F, 1.0F);
            pLevel.addFreshEntity(shard);

        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
