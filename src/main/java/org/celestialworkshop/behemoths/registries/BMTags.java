package org.celestialworkshop.behemoths.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import org.celestialworkshop.behemoths.Behemoths;

public class BMTags {

    public static final class Items {
        public static final TagKey<Item> HOLLOWBORNE_FOOD = BMTags.behemothsTag(Registries.ITEM, "hollowborne_food");
    }

    public static final class Blocks {
        public static final TagKey<Block> NEEDS_MAGNALYTH_TOOL = BMTags.behemothsTag(Registries.BLOCK, "needs_magnalyth_tool");
        public static final TagKey<Block> NEEDS_MORTYX_TOOL = BMTags.behemothsTag(Registries.BLOCK, "needs_mortyx_tool");
    }

    public static final class Entities {
    }

    public static final class Biomes {
        public static final TagKey<Biome> SHOULD_SPAWN_ARCHZOMBIES = behemothsTag(Registries.BIOME, "should_spawn_archzombies");
    }

    private static <T> TagKey<T> behemothsTag(ResourceKey<Registry<T>> registry, String path) {
        return TagKey.create(registry, Behemoths.prefix(path));
    }

    private static <T> TagKey<T> minecraftTag(ResourceKey<Registry<T>> registry, String path) {
        return TagKey.create(registry, ResourceLocation.fromNamespaceAndPath("minecraft", path));
    }

    private static <T> TagKey<T> forgeTag(ResourceKey<Registry<T>> registry, String path) {
        return TagKey.create(registry, ResourceLocation.fromNamespaceAndPath("forge", path));
    }
}
