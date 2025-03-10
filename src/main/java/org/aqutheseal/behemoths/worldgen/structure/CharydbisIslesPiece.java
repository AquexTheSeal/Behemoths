package org.aqutheseal.behemoths.worldgen.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.synth.PerlinNoise;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.aqutheseal.behemoths.entity.SkyCharydbis;
import org.aqutheseal.behemoths.registry.BMEntityTypes;
import org.aqutheseal.behemoths.registry.BMStructures;

import java.util.stream.IntStream;

public class CharydbisIslesPiece extends BlockPlacerPiece {
    public boolean spawnCharydbis;
    public int radius;
    public int length;

    public boolean hasSpawnedCharydbis = false;

    public CharydbisIslesPiece(BlockPos pos, boolean spawnCharydbis, int radius, int length) {
        super(BMStructures.CHARYDBIS_ISLES_PIECE.get(), pos.getX(), pos.getY(), pos.getZ(), radius * 2, length * 2, radius * 2, Direction.DOWN);
        this.spawnCharydbis = spawnCharydbis;
        this.radius = radius;
        this.length = length;
    }

    public CharydbisIslesPiece(CompoundTag pTag) {
        super(BMStructures.CHARYDBIS_ISLES_PIECE.get(), pTag);
        this.spawnCharydbis = pTag.getBoolean("SpawnCharydbis");
        this.radius = pTag.getInt("Type");
        this.length = pTag.getInt("Length");
    }

    @Override
    public void generatePiece(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {

        PerlinNoise floorNoise = PerlinNoise.create(random, IntStream.of(-2, 2));
        PerlinNoise surfaceNoise = PerlinNoise.create(random, IntStream.of(-4, 4));

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                if (x * x + z * z <= radius * radius) {
                    BlockPos surPos = pos.offset(x, 0, z);
                    double surfaceHeight = surfaceNoise.getValue(surPos.getX(), 0, surPos.getZ()) * 12;
                    BlockPos adjustedSurPos = surPos.above((int) surfaceHeight);
                    this.putBlock(level, Blocks.GRASS_BLOCK.defaultBlockState(), adjustedSurPos, box);

                    double floorValue = length + floorNoise.getValue(surPos.getX(), 0, surPos.getZ()) * length * 3;
                    for (int y = 1; y < floorValue; y++) {

                        int reducedRadius = (Mth.lerpInt((float) (y / floorValue), radius, 0));
                        if (x * x + z * z <= reducedRadius * reducedRadius) {
                            this.putBlock(level, Blocks.DIRT.defaultBlockState(), adjustedSurPos.below(y), box);
                        }
                    }
                }
            }
        }

        if (this.spawnCharydbis && !this.hasSpawnedCharydbis) {
            SkyCharydbis charydbis = BMEntityTypes.BARREN_SKY_CHARYDBIS.get().create(level.getLevel());
            charydbis.absMoveTo(pos.getX() + 0.5, pos.getY() + 10, pos.getZ() + 0.5, random.nextFloat() * 360, 0);
            charydbis.homePosition = new Vec3(pos.getX(), pos.getY() + 10, pos.getZ());
            charydbis.setCurrentGrowth(2);
            charydbis.refreshDimensions();
            level.addFreshEntity(charydbis);
            this.hasSpawnedCharydbis = true;
        }
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
        super.addAdditionalSaveData(pContext, pTag);
        pTag.putInt("Type", radius);
    }

    @Override
    public void putBlock(WorldGenLevel pLevel, BlockState pBlockstate, BlockPos blockpos, BoundingBox pBoundingbox) {
        if (pBoundingbox.isInside(blockpos) && (pLevel.isEmptyBlock(blockpos) || !pLevel.getBlockState(blockpos).getFluidState().isEmpty())) {
            pLevel.setBlock(blockpos, pBlockstate, 2);
            FluidState fluidstate = pLevel.getFluidState(blockpos);
            if (!fluidstate.isEmpty()) {
                pLevel.scheduleTick(blockpos, fluidstate.getType(), 0);
            }
        }
    }
}
