package org.celestialworkshop.behemoths.api.item;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SaddleItem;
import net.minecraft.world.level.gameevent.GameEvent;
import org.celestialworkshop.behemoths.entities.ai.CustomSaddleable;

public class CustomSaddleItem extends SaddleItem {
    public CustomSaddleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pTarget, InteractionHand pHand) {
        if (pTarget instanceof CustomSaddleable saddleable && pTarget.isAlive()) {
            if (!saddleable.isSaddled() && saddleable.isSaddleable() && saddleable.canEquipSaddle(pStack)) {
                if (!pPlayer.level().isClientSide) {
                    saddleable.equipCustomSaddle(SoundSource.NEUTRAL);
                    pTarget.level().gameEvent(pTarget, GameEvent.EQUIP, pTarget.position());
                    pStack.shrink(1);
                }

                return InteractionResult.sidedSuccess(pPlayer.level().isClientSide);
            }
        }

        return InteractionResult.PASS;
    }
}
