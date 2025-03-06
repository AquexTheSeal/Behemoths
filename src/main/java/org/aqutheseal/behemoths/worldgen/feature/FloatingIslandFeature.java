package org.aqutheseal.behemoths.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.phys.Vec3;
import org.aqutheseal.behemoths.entity.SkyCharydbis;
import org.aqutheseal.behemoths.registry.BMEntityTypes;

import java.util.stream.IntStream;

//todo: convert this feature to a structure, perhaps copy the fortress thing from the Celestisynth mod?
public class FloatingIslandFeature extends Feature<NoneFeatureConfiguration> {
    public FloatingIslandFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();

        boolean hasGeneratedCharydbis = false;

        for (int i = 0; i < 5; i++) {
            int radius = 6 + random.nextInt(6);
            int depth = 48 + random.nextInt(8);
            int xzRangeScale = 16;
            int ranX = -xzRangeScale + random.nextInt(xzRangeScale * 2);
            int ranY = -16 + random.nextInt(32);
            int ranZ = -xzRangeScale + random.nextInt(xzRangeScale * 2);
            BlockPos placement = pos.offset(ranX, ranY, ranZ);
            this.markNeighboringChunksForGeneration(level, placement, radius);
            this.generateIsland(level, random, placement, radius, depth);

            if (!hasGeneratedCharydbis) {
                SkyCharydbis charydbis = BMEntityTypes.BARREN_SKY_CHARYDBIS.get().create(level.getLevel());
                charydbis.absMoveTo(
                        placement.getX() + 0.5,
                        placement.getY() + 5,
                        placement.getZ() + 0.5, context.random().nextFloat() * 360, 0
                );
                charydbis.homePosition = new Vec3(placement.getX(), placement.getY(), placement.getZ());
                level.addFreshEntity(charydbis);
                hasGeneratedCharydbis = true;
            }
        }

        return true;
    }

    private void generateIsland(WorldGenLevel level, RandomSource random, BlockPos pos, int radius, int depth) {
        PerlinNoise surNoise = PerlinNoise.create(random, IntStream.of(-4, 4));
        PerlinNoise floorNoise = PerlinNoise.create(random, IntStream.of(-4, 4));

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    BlockPos surPos = pos.offset(x, 0, z);
                    int surfaceElevation = (int) (surNoise.getValue(surPos.getX(), 0, surPos.getZ()) * 12);
                    BlockPos finalSurPos = surPos.above(surfaceElevation);
                    this.setBlock(level, finalSurPos, Blocks.GRASS_BLOCK.defaultBlockState());

                    double floorValue = depth + floorNoise.getValue(surPos.getX(), 0, surPos.getZ()) * depth * 3;
                    for (int y = 1; y < floorValue; y++) {

                        int reducedRadius = (Mth.lerpInt((float) (y / floorValue), radius, 0));
                        if (x * x + z * z <= reducedRadius * reducedRadius) {
                            this.setBlock(level, finalSurPos.below(y), Blocks.DIRT.defaultBlockState());
                        }
                    }
                }
            }
        }
    }

    private void markNeighboringChunksForGeneration(WorldGenLevel level, BlockPos center, int radius) {
        int chunkX = center.getX() >> 4;
        int chunkZ = center.getZ() >> 4;

        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                int neighborChunkX = chunkX + x;
                int neighborChunkZ = chunkZ + z;
                BlockPos chunkPos = new BlockPos(neighborChunkX << 4, 0, neighborChunkZ << 4);

                if (chunkPos.distSqr(center) <= (radius + 16) * (radius + 16)) {
                    level.getChunk(neighborChunkX, neighborChunkZ);
                }
            }
        }
    }
}