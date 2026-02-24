package org.celestialworkshop.behemoths.datagen;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import org.celestialworkshop.behemoths.registries.BMStructures;
import org.celestialworkshop.behemoths.registries.BMTags;
import org.celestialworkshop.behemoths.world.structures.CharydbisZoneStructure;

import java.util.Map;

public class BMStructuresProvider {

    public static void bootstrapStructure(BootstapContext<Structure> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(
                BMStructures.CHARYDBIS_ZONE,
                new CharydbisZoneStructure(
                        new Structure.StructureSettings(
                                biomes.getOrThrow(BMTags.Biomes.CONTAINS_CHARYDBIS_ZONE),
                                Map.of(),
                                GenerationStep.Decoration.SURFACE_STRUCTURES,
                                TerrainAdjustment.NONE
                        )
                )
        );
    }

    public static void bootstrapStructureSet(BootstapContext<StructureSet> context) {
        HolderGetter<Structure> structures = context.lookup(Registries.STRUCTURE);

        context.register(
                BMStructures.CHARYDBIS_ZONE_SET,
                new StructureSet(
                        structures.getOrThrow(BMStructures.CHARYDBIS_ZONE),
                        new RandomSpreadStructurePlacement(32, 12, RandomSpreadType.LINEAR, 74602431)
                )
        );
    }
}