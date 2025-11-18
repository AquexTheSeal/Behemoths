package org.celestialworkshop.behemoths.items;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.shared.EntityActionSharedPacket;

public class RottenOathkeeperItem extends AxeItem {

    public RottenOathkeeperItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (!pAttacker.level().isClientSide() && pAttacker instanceof ServerPlayer player && pAttacker.getVehicle() instanceof BanishingStampede stampede) {
                BMNetwork.sendToPlayer(player, new EntityActionSharedPacket(stampede.getId(), EntityActionSharedPacket.Action.FORCE_JUMP_STAMPEDE,
                    Pair.of("strength", 0.2f)
            ));
        }

        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }
}
