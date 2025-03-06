package org.aqutheseal.behemoths.worldgen.placement;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.aqutheseal.behemoths.registry.BMPlacementModifierTypes;

import java.util.stream.Stream;

public class SkyGroupedPlacement extends PlacementModifier {
    public static final SkyGroupedPlacement INSTANCE = new SkyGroupedPlacement();
    public static final Codec<SkyGroupedPlacement> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        int skyHeight = context.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());
        Stream.Builder<BlockPos> positions = Stream.builder();
        for (int i = 0; i < 5; i++) {
            int yPos = skyHeight + 100 + random.nextInt(50);
            positions.add(new BlockPos(pos.getX() - 48 + random.nextInt(96), yPos, pos.getZ() - 48 + random.nextInt(96)));
        }
        return positions.build();
    }

    @Override
    public PlacementModifierType<?> type() {
        return BMPlacementModifierTypes.SKY_GROUPED_PLACEMENT.get();
    }
}