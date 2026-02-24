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
        ctx.register(BMMobSpawns.OVERWORLD_SPAWNS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomeRegistry.getOrThrow(BiomeTags.IS_OVERWORLD),
                List.of(
                        new MobSpawnSettings.SpawnerData(BMEntityTypes.ARCHZOMBIE.get(), 15, 1, 2),
                        new MobSpawnSettings.SpawnerData(BMEntityTypes.HOLLOWBORNE_TURRET.get(), 5, 1, 1)
                ))
        );

        ctx.register(BMMobSpawns.MOUNTAIN_SPAWNS, new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomeRegistry.getOrThrow(BiomeTags.IS_MOUNTAIN),
                List.of(
                        new MobSpawnSettings.SpawnerData(BMEntityTypes.HOLLOWBORNE_TURRET.get(), 7, 1, 1)
                ))
        );
    }
}
