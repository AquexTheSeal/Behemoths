package org.celestialworkshop.behemoths.api.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.datagen.reloadlisteners.BehemothPropertiesReloadListener;

public record BehemothProperties(int heartEnergy) {

    public static final Codec<BehemothProperties> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("heart_energy").forGetter(BehemothProperties::heartEnergy)
    ).apply(instance, BehemothProperties::new));

    public static BehemothProperties getFrom(Entity entity) {
        ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
        if (entityId == null) return null;
        return BehemothPropertiesReloadListener.getProperties(entityId);
    }

    public static boolean isBehemoth(Entity entity) {
        return BehemothPropertiesReloadListener.hasProperties(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()));
    }

    public static int getHeartEnergy(Entity entity) {
        return getFrom(entity).heartEnergy();
    }
}
