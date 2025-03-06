package org.aqutheseal.behemoths.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import org.aqutheseal.behemoths.Behemoths;

public class BMTags {

    public static final class Items {
    }

    public static final class Blocks {
    }

    public static final class Entities {
        public static final TagKey<EntityType<?>> BEHEMOTHS = BMTags.behemothsTag(Registries.ENTITY_TYPE, "behemoths");
    }

    private static <T> TagKey<T> behemothsTag(ResourceKey<Registry<T>> registry, String path) {
        return TagKey.create(registry, Behemoths.location(path));
    }

    private static <T> TagKey<T> minecraftTag(ResourceKey<Registry<T>> registry, String path) {
        return TagKey.create(registry, new ResourceLocation("minecraft", path));
    }

    private static <T> TagKey<T> forgeTag(ResourceKey<Registry<T>> registry, String path) {
        return TagKey.create(registry, new ResourceLocation("forge", path));
    }
}
