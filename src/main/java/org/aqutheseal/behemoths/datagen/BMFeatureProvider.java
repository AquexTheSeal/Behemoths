package org.aqutheseal.behemoths.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import org.aqutheseal.behemoths.registry.BMFeatures;
import org.aqutheseal.behemoths.worldgen.placement.SkyGroupedPlacement;

import java.util.List;

public class BMFeatureProvider {

    public static class ConfiguredFeatures {
        public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
            context.register(BMFeatures.BARREN_ISLAND_CONFIGURED, new ConfiguredFeature<>(BMFeatures.FLOATING_ISLAND.get(), NoneFeatureConfiguration.INSTANCE));
        }
    }

    public static class PlacedFeatures {
        public static void bootstrap(BootstapContext<PlacedFeature> context) {
            HolderGetter<ConfiguredFeature<?, ?>> configuredFeatureRegistry = context.lookup(Registries.CONFIGURED_FEATURE);

            context.register(BMFeatures.BARREN_ISLAND_PLACED, new PlacedFeature(configuredFeatureRegistry.getOrThrow(BMFeatures.BARREN_ISLAND_CONFIGURED),
                    List.of(RarityFilter.onAverageOnceEvery(50), InSquarePlacement.spread(), BiomeFilter.biome(), SkyGroupedPlacement.INSTANCE)
            ));
        }
    }

    public static class BiomeModifiers {
        public static void bootstrap(BootstapContext<BiomeModifier> ctx) {
            final HolderGetter<PlacedFeature> featureRegistry = ctx.lookup(Registries.PLACED_FEATURE);
            final HolderGetter<Biome> biomeRegistry = ctx.lookup(Registries.BIOME);

            ctx.register(BMFeatures.BARREN_ISLAND_MODIFIER, new ForgeBiomeModifiers.AddFeaturesBiomeModifier(
                            biomeRegistry.getOrThrow(Tags.Biomes.IS_HOT_OVERWORLD),
                            HolderSet.direct(featureRegistry.getOrThrow(BMFeatures.BARREN_ISLAND_PLACED)),
                            GenerationStep.Decoration.SURFACE_STRUCTURES
                    )
            );
        }
    }
}
