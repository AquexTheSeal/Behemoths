package org.aqutheseal.behemoths.worldgen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.aqutheseal.behemoths.registry.BMStructures;

import java.util.Optional;

public class CharydbisIslesStructure extends Structure {
    public static final Codec<CharydbisIslesStructure> CODEC = simpleCodec(CharydbisIslesStructure::new);

    public CharydbisIslesStructure(StructureSettings pSettings) {
        super(pSettings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {
        ChunkPos chunkpos = pContext.chunkPos();
        int i = chunkpos.getMiddleBlockX();
        int j = chunkpos.getMiddleBlockZ();
        int k = pContext.chunkGenerator().getFirstOccupiedHeight(i, j, Heightmap.Types.WORLD_SURFACE, pContext.heightAccessor(), pContext.randomState());
        BlockPos pPosition = new BlockPos(i, k + 100 + pContext.random().nextInt(50), j);
        return Optional.of(new Structure.GenerationStub(pPosition, builder -> generatePieces(builder, pContext, pPosition)));
    }


    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context, BlockPos placePos) {
        this.generatePortion(builder, context, placePos);
        this.generatePortion(builder, context, placePos.offset(64, 0, 0));
    }

    private void generatePortion(StructurePiecesBuilder builder, GenerationContext context, BlockPos placePos) {
        WorldgenRandom random = context.random();
        for (int i = 0; i < 20 + random.nextInt(10); i++) {
            int radius = 8 + random.nextInt(4);
            int depth = 24 + random.nextInt(8);
            int xzRangeScale = 64;
            int ranX = -xzRangeScale + random.nextInt(xzRangeScale * 2);
            int ranY = -32 + random.nextInt(32);
            int ranZ = -xzRangeScale + random.nextInt(xzRangeScale * 2);
            builder.addPiece(new CharydbisIslesPiece(placePos.offset(ranX, ranY, ranZ), false, radius, depth));
        }

        for (int i = 0; i < 40 + random.nextInt(20); i++) {
            int radius = 1 + random.nextInt(3);
            int depth = 28 + random.nextInt(12);
            int xzRangeScale = 64;
            int ranX2 = (-xzRangeScale + random.nextInt(xzRangeScale * 2));
            int ranY2 = -32 + random.nextInt(48);
            int ranZ2 = (-xzRangeScale + random.nextInt(xzRangeScale * 2));
            builder.addPiece(new CharydbisIslesPiece(placePos.offset(ranX2, ranY2, ranZ2), false, radius, depth));
        }

        int radius = 18 + random.nextInt(18);
        int depth = 128;
        builder.addPiece(new CharydbisIslesPiece(placePos, true, radius, depth));
    }

    @Override
    public StructureType<?> type() {
        return BMStructures.CHARYDBIS_ISLES_TYPE.get();
    }
}
