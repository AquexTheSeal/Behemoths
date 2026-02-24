package org.celestialworkshop.behemoths.world.structures;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import org.celestialworkshop.behemoths.registries.BMStructures;

import java.util.Optional;

public class CharydbisZoneStructure extends Structure {
    public static final MapCodec<CharydbisZoneStructure> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    settingsCodec(instance)
            ).apply(instance, CharydbisZoneStructure::new)
    );

    public CharydbisZoneStructure(StructureSettings pSettings) {
        super(pSettings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext pContext) {

        ChunkPos chunkpos = pContext.chunkPos();
        int i = chunkpos.getMiddleBlockX();
        int j = chunkpos.getMiddleBlockZ();
        int offsetFromFloor = 30 + pContext.random().nextInt(10);
        int k = pContext.chunkGenerator().getFirstOccupiedHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, pContext.heightAccessor(), pContext.randomState());
        BlockPos pPosition = new BlockPos(i, k + offsetFromFloor, j);
        return Optional.of(new Structure.GenerationStub(pPosition, builder -> generatePieces(builder, pContext, pPosition)));
    }

    private void generatePieces(StructurePiecesBuilder builder, Structure.GenerationContext context, BlockPos placePos) {
        builder.addPiece(new CharydbisZoneIslandPiece(placePos.offset(-7, 0, -7), true, 14, 6, 1.0F));

        for (int r = 1; r <= 3; r++) {
            int radiusMult = r * 12;
            for (int i = 0; i < 360; i += 30) {
                int dist = context.random().nextIntBetweenInclusive(12, 16) + radiusMult;
                int roundX = (int) (Math.cos(i * Mth.DEG_TO_RAD) * dist) + context.random().nextIntBetweenInclusive(-6, 6);
                int randY = (radiusMult / 2) + context.random().nextIntBetweenInclusive(-8, 8);
                int roundZ = (int) (Math.sin(i * Mth.DEG_TO_RAD) * dist) + context.random().nextIntBetweenInclusive(-6, 6);
                int randRadius = context.random().nextIntBetweenInclusive(3, 6);

                builder.addPiece(new CharydbisZoneIslandPiece(placePos.offset(roundX, randY, roundZ), false, randRadius, 4, 1.0F));
            }
        }
    }

    @Override
    public StructureType<?> type() {
        return BMStructures.CHARYDBIS_ZONE_TYPE.get();
    }
}
