package org.celestialworkshop.behemoths.world.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.material.FluidState;
import org.celestialworkshop.behemoths.block.PhantashroomBlock;
import org.celestialworkshop.behemoths.registries.BMBlocks;
import org.celestialworkshop.behemoths.registries.BMStructures;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class CharydbisZoneIslandPiece extends CenteredFeaturePiece {

    public final boolean isCenter;
    public final int baseRadius;
    public final int allowance;
    public final double noiseScale;

    public PerlinNoise heightNoise;
    public PerlinNoise shapeNoise;
    public PerlinNoise bottomNoise;

    CharydbisZoneIslandPiece(BlockPos pos, boolean isCenter, int baseRadius, int allowance, double noiseScale) {
        super(BMStructures.CHARYDBIS_ZONE_CENTER_PIECE.get(), pos.getX(), pos.getY(), pos.getZ(), baseRadius * 2, 32, baseRadius * 2, Direction.DOWN);
        this.isCenter = isCenter;
        this.baseRadius = baseRadius;
        this.allowance = allowance;
        this.noiseScale = noiseScale;
    }

    public CharydbisZoneIslandPiece(CompoundTag pTag) {
        super(BMStructures.CHARYDBIS_ZONE_CENTER_PIECE.get(), pTag);
        this.isCenter = pTag.getBoolean("isCenter");
        this.baseRadius = pTag.getInt("baseRadius");
        this.allowance = pTag.getInt("allowance");
        this.noiseScale = pTag.getDouble("noiseScale");
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
        pTag.putBoolean("isCenter", this.isCenter);
        pTag.putInt("baseRadius", this.baseRadius);
        pTag.putInt("allowance", this.allowance);
        pTag.putDouble("noiseScale", this.noiseScale);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        super.postProcess(level, structureManager, generator, random, box, chunkPos, pos);
    }

    @Override
    public void generatePiece(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {

        if (heightNoise == null) {
            heightNoise = PerlinNoise.create(random, IntStream.of(0, 1));
        }
        if (shapeNoise == null) {
            shapeNoise = PerlinNoise.create(random, IntStream.of(0));
        }
        if (bottomNoise == null) {
            bottomNoise = PerlinNoise.create(random, IntStream.of(0, 1));
        }

        double shapeScale = 0.12 * this.noiseScale;
        double surfaceHeightScale = 0.05;
        int surfaceHeightVariation = 4;

        double tipScale = 0.04;
        int baseTipLength = 24;
        int tipVariation = 48 + baseRadius * 2;

        int centerY = pos.getY();

        double maxRadiusSq = (double)(baseRadius + allowance) * (baseRadius + allowance);

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        for (int xx = -baseRadius - allowance; xx <= baseRadius + allowance; xx++) {
            for (int zz = -baseRadius - allowance; zz <= baseRadius + allowance; zz++) {

                double distanceSq = (double)(xx * xx) + (double)(zz * zz);

                if (distanceSq > maxRadiusSq) continue;

                double distance = Math.sqrt(distanceSq);
                double wobble = shapeNoise.getValue((pos.getX() + xx) * shapeScale, 0, (pos.getZ() + zz) * shapeScale);
                double radius = baseRadius + (wobble * allowance);

                if (distance <= radius) {
                    int worldX = pos.getX() + xx;
                    int worldZ = pos.getZ() + zz;

                    double yNoise = heightNoise.getValue(worldX * surfaceHeightScale, 0, worldZ * surfaceHeightScale);
                    int yOffset = (int)(yNoise * surfaceHeightVariation);
                    int surfaceY = centerY + yOffset;

                    mutablePos.set(worldX, surfaceY, worldZ);

                    if (box.isInside(mutablePos)) {
                        level.setBlock(mutablePos, this.getBlockFromNoise(level, mutablePos, true), 2);
                        FluidState fluidstate = level.getFluidState(mutablePos);
                        if (!fluidstate.isEmpty()) {
                            level.scheduleTick(mutablePos, fluidstate.getType(), 0);
                        }
                        if (!this.isCenter || distance >= 5) {
                            this.generateSurfacePlants(level, box, random, mutablePos.above());
                        }

                        for (int yt = surfaceY - 1; yt > centerY - 3; yt--) {
                            mutablePos.set(worldX, yt, worldZ);
                            if (box.isInside(mutablePos)) {
                                level.setBlock(mutablePos, this.getBlockFromNoise(level, mutablePos, false), 2);
                            }
                        }

                        double normalizedDist = distance / baseRadius;
                        double coneSlope = Math.pow(normalizedDist, 1.5) * baseTipLength * 0.75;
                        double noiseInfluence = 1.0 - (normalizedDist * 0.7);
                        int tipLength = (int)(bottomNoise.getValue(worldX * tipScale, 0, worldZ * tipScale) * tipVariation * noiseInfluence);
                        int tipY = (centerY - baseTipLength - tipLength) + (int) coneSlope;

                        for (int yt = centerY - 3; yt > tipY; yt--) {
                            mutablePos.set(worldX, yt, worldZ);
                            if (box.isInside(mutablePos) && level.getBlockState(mutablePos).isAir()) {
                                level.setBlock(mutablePos, this.getBlockFromNoise(level, mutablePos, false), 2);
                            }
                        }
                    }
                }
            }
        }

        int featureRolls = isCenter ? 2 : 1;
        for (int pl = 0; pl < featureRolls; pl++) {
            List<ResourceKey<ConfiguredFeature<?, ?>>> keys = List.of(
                    VegetationFeatures.FLOWER_MEADOW,
                    VegetationFeatures.PATCH_TALL_GRASS,
                    VegetationFeatures.PATCH_GRASS
            );
            for (ResourceKey<ConfiguredFeature<?, ?>> key : keys) {
                Optional<Holder.Reference<ConfiguredFeature<?, ?>>> feature = level.registryAccess()
                        .registryOrThrow(Registries.CONFIGURED_FEATURE)
                        .getHolder(key);
                feature.ifPresent((f) -> {
                    int rX = pos.getX() + random.nextInt(16) - 8;
                    int rZ = pos.getZ() + random.nextInt(16) - 8;
                    f.value().place(level, generator, random, mutablePos.set(rX, pos.getY(), rZ));
                });
            }
        }
    }

    public BlockState getBlockFromNoise(WorldGenLevel level, BlockPos pos, boolean isSurface) {
        double noise = heightNoise.getValue(pos.getX() * 0.1, pos.getY() * 0.1, pos.getZ() * 0.1);
        if (noise < -0.43) {
            if (isSurface) {
                return Blocks.WATER.defaultBlockState();
            }
        }
        if (noise < -0.2) {
            if (isSurface) {
                return Blocks.GRASS_BLOCK.defaultBlockState();
            }
            return Blocks.DIRT.defaultBlockState();
        }
        if (noise < -0.1) {
            return Blocks.STONE.defaultBlockState();
        }
        return Blocks.MUD.defaultBlockState();
    }

    public void generateSurfacePlants(WorldGenLevel level, BoundingBox box, RandomSource random, BlockPos pos) {
        if (level.getBlockState(pos.below()).is(Blocks.MUD)) {
            if (random.nextFloat() < 0.1F) {
                level.setBlock(pos, BMBlocks.PHANTASHROOM.get().defaultBlockState(), 2);
            }
            float treeChance = isCenter ? 0.005F : 0.015F;
            if (random.nextFloat() < treeChance) {
                PhantashroomBlock.generateHugeMushroom(level, random, pos);
            }
        }
    }
}
