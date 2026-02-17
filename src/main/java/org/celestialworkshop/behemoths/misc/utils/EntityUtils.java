package org.celestialworkshop.behemoths.misc.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class EntityUtils {

    public static boolean doesAreaFitEntity(ServerLevelAccessor level, BlockPos pos, EntityType<?> entity, @Nullable EntityType<?> vehicle) {
        int w = Mth.ceil(entity.getWidth());
        int h = Mth.ceil(entity.getHeight());
        boolean f = true;
        BlockPos.MutableBlockPos scanPos = pos.mutable();
        if (vehicle != null) {
            scanPos = scanPos.move(0, Mth.ceil(vehicle.getHeight()), 0);
        }
        for (int i = -w; i <= w; i++) {
            for (int j = -w; j <= w; j++) {
                for (int k = 0; k <= h; k++) {
                    scanPos = scanPos.move(i, j, k);
                    if (!(level.getBlockState(pos).isAir() || level.getBlockState(pos).canOcclude())) {
                        f = false;
                    }
                }
            }
        }
        if (vehicle != null && !doesAreaFitEntity(level, pos, vehicle, null)) {
            f = false;
        }
        return f;
    }

    public static void breakBlocks(Entity entity, boolean dropBlocks, Predicate<BlockState> toBreak) {

        if (!entity.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) return;

        int w = Mth.ceil(entity.getBbWidth() + 1);
        int h = Mth.ceil(entity.getBbHeight());
        Level level = entity.level();
        for (int i = -w; i <= w; i++) {
            for (int j = -w; j <= w; j++) {
                for (int k = -h; k <= h; k++) {
                    BlockPos pos = entity.blockPosition().offset(i, j, k);
                    BlockState state = level.getBlockState(pos);
                    if (toBreak.test(state)) {
                        level.destroyBlock(pos, dropBlocks, entity);
                    }
                }
            }
        }
    }
}
