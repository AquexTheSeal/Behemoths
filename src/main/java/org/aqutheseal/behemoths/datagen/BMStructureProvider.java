package org.aqutheseal.behemoths.datagen;

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
import org.aqutheseal.behemoths.registry.BMStructures;
import org.aqutheseal.behemoths.registry.BMTags;
import org.aqutheseal.behemoths.worldgen.structure.CharydbisIslesStructure;

import java.util.Map;

public class BMStructureProvider {

    public static class Structures {
        public static void bootstrap(BootstapContext<Structure> ctx) {
            HolderGetter<Biome> biomesReg = ctx.lookup(Registries.BIOME);

            ctx.register(BMStructures.CHARYDBIS_ISLES, new CharydbisIslesStructure(new Structure.StructureSettings(
                    biomesReg.getOrThrow(BMTags.Biomes.HAS_CHARYDBIS_ISLES), Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE
            )));
        }
    }

    public static class StructureSets {
        public static void bootstrap(BootstapContext<StructureSet> ctx) {
            HolderGetter<Structure> structuresReg = ctx.lookup(Registries.STRUCTURE);

            ctx.register(BMStructures.CHARYDBIS_ISLES_SET, new StructureSet(
                    structuresReg.getOrThrow(BMStructures.CHARYDBIS_ISLES),
                    new RandomSpreadStructurePlacement(32, 32, RandomSpreadType.LINEAR, 1014436850))
            );
        }
    }
}
