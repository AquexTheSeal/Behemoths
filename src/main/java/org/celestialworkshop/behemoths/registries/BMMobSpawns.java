package org.celestialworkshop.behemoths.registries;

import net.minecraft.resources.ResourceKey;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.Behemoths;

public class BMMobSpawns {

    // Biome Spawn Modifiers
    public static final ResourceKey<BiomeModifier> OVERWORLD_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Behemoths.prefix("overworld_spawns"));

    public static final ResourceKey<BiomeModifier> MOUNTAIN_SPAWNS = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Behemoths.prefix("mountain_spawns"));

}
