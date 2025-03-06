package org.aqutheseal.behemoths.util.compat;

import net.bettercombat.logic.PlayerAttackHelper;
import net.bettercombat.logic.PlayerAttackProperties;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;

public class BetterCombatUtil {

    public static ItemStack getSwingingStack(Player player) {
        if (ModList.get().isLoaded("bettercombat")) {
            var currentHand = PlayerAttackHelper.getCurrentAttack(player, ((PlayerAttackProperties) player).getComboCount());
            if (currentHand != null) {
                return currentHand.isOffHand() ? player.getOffhandItem() : player.getMainHandItem();
            } else {
                return player.getMainHandItem();
            }
        }
        return player.getMainHandItem();
    }
}
