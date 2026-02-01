package org.celestialworkshop.behemoths.utils;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientUtils {

    public static LivingEntity tryCreateDisplayEntity(Level level, Player player, EntityType<?> entityType) {
        LivingEntity entity;
        if (entityType != null && entityType.create(level) instanceof LivingEntity living) {
            entity = living;
        } else {
            entity = new RemotePlayer((ClientLevel) level, player.getGameProfile()) {
                @Override
                public Component getDisplayName() {
                    return Component.empty();
                }
            };
        }
        return entity;
    }
}
