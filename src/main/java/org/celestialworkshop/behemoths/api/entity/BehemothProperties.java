package org.celestialworkshop.behemoths.api.entity;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.api.entity.heartenergy.HeartEnergyReloadListener;

public record BehemothProperties(int heartEnergy) {

    public static boolean isBehemoth(Entity entity) {
        return HeartEnergyReloadListener.VALUES.containsKey(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
    }

    public static int getHeartEnergy(Entity entity) {
        return HeartEnergyReloadListener.VALUES.getOrDefault(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()), 0);
    }
}
