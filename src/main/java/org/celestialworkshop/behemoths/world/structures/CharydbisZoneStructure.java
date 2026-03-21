package org.celestialworkshop.behemoths.world.structures;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.phys.AABB;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMStructures;

import java.util.List;
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
                int dist = context.random().nextIntBetweenInclusive(10, 14) + radiusMult;
                int roundX = (int) (Math.cos(i * Mth.DEG_TO_RAD) * dist) + context.random().nextIntBetweenInclusive(-6, 6);
                int randY = (int)(radiusMult / 2.5) + context.random().nextIntBetweenInclusive(-6, 6);
                int roundZ = (int) (Math.sin(i * Mth.DEG_TO_RAD) * dist) + context.random().nextIntBetweenInclusive(-6, 6);
                int randRadius = context.random().nextIntBetweenInclusive(3, 6);

                builder.addPiece(new CharydbisZoneIslandPiece(placePos.offset(roundX, randY, roundZ), false, randRadius, 4, 1.0F));
            }
        }
    }

    @Override
    public void afterPlace(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator, RandomSource pRandom, BoundingBox pBoundingBox, ChunkPos pChunkPos, PiecesContainer pPieces) {
        if (!(pLevel instanceof ServerLevel serverLevel)) return;

        BlockPos bossSpawnPos = null;
        List<BlockPos> islandPosList = new ObjectArrayList<>();

        for (StructurePiece piece : pPieces.pieces()) {
            if (piece instanceof CharydbisZoneIslandPiece islandPiece) {
                BlockPos pos = islandPiece.getLocatorPosition().below(15);
                if (islandPiece.isCenter) {
                    bossSpawnPos = pos;
                } else {
                    islandPosList.add(pos);
                }
            }
        }

        if (bossSpawnPos != null) {
            if (serverLevel.getEntitiesOfClass(SkyCharydbis.class, new AABB(bossSpawnPos).inflate(64)).isEmpty()) {
                SkyCharydbis boss = new SkyCharydbis(BMEntityTypes.SKY_CHARYDBIS.get(), serverLevel.getLevel());
                boss.moveTo(bossSpawnPos.getX() + 0.5, bossSpawnPos.getY(), bossSpawnPos.getZ() + 0.5);
                boss.islandPositions.addAll(islandPosList);
                boss.spawnPos = bossSpawnPos;

                boss.finalizeSpawn(serverLevel, pLevel.getCurrentDifficultyAt(bossSpawnPos), MobSpawnType.STRUCTURE, null, null);
                serverLevel.addFreshEntity(boss);
            }
        }
    }

    @Override
    public StructureType<?> type() {
        return BMStructures.CHARYDBIS_ZONE_TYPE.get();
    }
}
