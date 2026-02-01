package org.celestialworkshop.behemoths.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMMobSpawns;

import java.util.List;

public class BMMobSpawnsProvider {

    public static void bootstrapBiome(BootstapContext<BiomeModifier> ctx) {
        HolderGetter<Biome> biomeRegistry = ctx.lookup(Registries.BIOME);
        ctx.register(BMMobSpawns.ARCHZOMBIE_SPAWNS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomeRegistry.getOrThrow(BiomeTags.IS_OVERWORLD),
                List.of(
                new MobSpawnSettings.SpawnerData(BMEntityTypes.ARCHZOMBIE.get(), 30, 1, 1)
                ))
        );
    }
}
