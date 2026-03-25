package org.celestialworkshop.behemoths.misc.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Predicate;

public class BlockUtils {

    public static boolean isBlockFull(Level level, BlockPos pos) {
        return level.getBlockState(pos).isSolid();
    }

    public static Optional<BlockPos> getRandomValidPosAround(Level level, BlockPos center, int radius, int attempts, Predicate<BlockPos> posPredicate) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        RandomSource random = level.getRandom();

        for (int i = 0; i < attempts; i++) {
            double offX = (random.nextDouble() - 0.5) * radius;
            double offY = (random.nextDouble() - 0.5) * radius;
            double offZ = (random.nextDouble() - 0.5) * radius;

            mutablePos.set(center.getX() + offX, center.getY() + offY, center.getZ() + offZ);

            if (posPredicate.test(mutablePos)) {
                return Optional.of(mutablePos.immutable());
            }
        }
        return Optional.empty();
    }
}
