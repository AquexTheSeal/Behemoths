package org.celestialworkshop.behemoths.world.structures;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public abstract class CenteredFeaturePiece extends ScatteredFeaturePiece {
    protected CenteredFeaturePiece(StructurePieceType pType, int pX, int pY, int pZ, int xSize, int ySize, int zSize, Direction pOrientation) {
        super(pType, pX, pY, pZ, xSize, ySize, zSize, pOrientation);
    }

    protected CenteredFeaturePiece(StructurePieceType pType, CompoundTag pTag) {
        super(pType, pTag);
    }

    public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
        this.generatePiece(level, structureManager, generator, random, box, chunkPos, this.getWSPos(0, 0, 0).immutable());
    }

    public abstract void generatePiece(WorldGenLevel level, StructureManager structureManager, ChunkGenerator generator, RandomSource random, BoundingBox box, ChunkPos chunkPos, BlockPos pos);

    protected BlockPos.MutableBlockPos getWSPos(int pX, int pY, int pZ) {
        return new BlockPos.MutableBlockPos(this.getWSX(pX), this.getWSY(pY), this.getWSZ(pZ));
    }

    protected int getWSX(int pX) {
        return this.boundingBox.getCenter().getX() + pX;
    }

    protected int getWSY(int pY) {
        return this.boundingBox.minY() + pY;
    }

    protected int getWSZ(int pZ) {
        return this.boundingBox.getCenter().getZ() + pZ;
    }

}
