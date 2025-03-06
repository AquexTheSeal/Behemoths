package org.aqutheseal.behemoths.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.worldgen.feature.FloatingIslandFeature;

public class BMFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(Registries.FEATURE, Behemoths.MODID);

    /**
     * Features
     */
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> FLOATING_ISLAND = FEATURES.register("floating_island", () -> new FloatingIslandFeature(NoneFeatureConfiguration.CODEC));

    /**
     * Configured Features:
     * {@link org.aqutheseal.behemoths.datagen.BMFeatureProvider.ConfiguredFeatures}
     */
    public static final ResourceKey<ConfiguredFeature<?, ?>> BARREN_ISLAND_CONFIGURED = ResourceKey.create(Registries.CONFIGURED_FEATURE, Behemoths.location("barren_island"));

    /**
     * Placed Features:
     * {@link org.aqutheseal.behemoths.datagen.BMFeatureProvider.PlacedFeatures}
     */
    public static final ResourceKey<PlacedFeature> BARREN_ISLAND_PLACED = ResourceKey.create(Registries.PLACED_FEATURE, Behemoths.location("barren_island_placed"));

    /**
     * Biome Modifiers:
     * {@link org.aqutheseal.behemoths.datagen.BMFeatureProvider.BiomeModifiers}
     */
    public static final ResourceKey<BiomeModifier> BARREN_ISLAND_MODIFIER = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, Behemoths.location("barren_island"));

}