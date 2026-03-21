package org.celestialworkshop.behemoths.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.celestialworkshop.behemoths.client.animations.item.BreakerItemAnimationExtension;

import java.util.function.Consumer;

public class BehebuggerItem extends Item {

    public BehebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack item = pPlayer.getItemInHand(pUsedHand);
        if (!pPlayer.getCooldowns().isOnCooldown(this)) {
            pPlayer.startUsingItem(pUsedHand);
            return InteractionResultHolder.success(item);
        }
        return InteractionResultHolder.pass(item);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        if (pLivingEntity instanceof Player player) {
            if (player.isUsingItem() && player.getUseItem() == pStack) {
                for (LivingEntity target : pLevel.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(6, 2, 6).move(player.getLookAngle()))) {
                    if (target != player && player.getTicksUsingItem() >= 30) {
                        if (target.hurt(player.damageSources().playerAttack(player), player.fallDistance * 2)) {
                            player.getCooldowns().addCooldown(pStack.getItem(), 20);
                            player.stopUsingItem();
                        }
                    }
                }
            }
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CUSTOM;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new BreakerItemAnimationExtension());
    }

}
