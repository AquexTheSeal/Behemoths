package org.celestialworkshop.behemoths.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.entities.projectile.Hollowcorper;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;

public class BehebuggerItem extends Item {

    public BehebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide) {
            BMEntityTypes.HOLLOWBORNE_TURRET.get().spawn((ServerLevel) pContext.getLevel(), pContext.getClickedPos(), MobSpawnType.NATURAL);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {

            Hollowcorper proj = new Hollowcorper(BMEntityTypes.HOLLOWCORPER.get(), pPlayer.getX(), pPlayer.getY(0.3333), pPlayer.getZ(), pLevel);
            proj.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0, 0.05F, 0.0F);
            proj.setOwner(pPlayer);
            pLevel.addFreshEntity(proj);

        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }
}
