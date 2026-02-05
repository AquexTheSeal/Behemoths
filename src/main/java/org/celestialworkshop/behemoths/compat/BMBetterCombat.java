package org.celestialworkshop.behemoths.compat;

import net.bettercombat.api.AttackHand;
import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.PlayerAttackProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BMBetterCombat {

    public static ItemStack getSwingingStack(Player player) {
        AttackHand currentHand = PlayerAttackHelper.getCurrentAttack(player, ((PlayerAttackProperties) player).getComboCount());
        if (currentHand != null) {
            return currentHand.isOffHand() ? player.getOffhandItem() : player.getMainHandItem();
        } else {
            return player.getMainHandItem();
        }
    }
}
