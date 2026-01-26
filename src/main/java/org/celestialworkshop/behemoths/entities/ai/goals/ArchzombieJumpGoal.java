package org.celestialworkshop.behemoths.entities.ai.goals;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.shared.EntityActionSharedPacket;

import java.util.EnumSet;

public class ArchzombieJumpGoal extends Goal {

    private static final int MAX_GAP_SIZE = 8;
    private static final int MIN_GAP_SIZE = 3;
    private static final double SCAN_DISTANCE = 20.0;
    private static final int MAX_DEPTH_CHECK = 5;

    private final Archzombie archzombie;

    public ArchzombieJumpGoal(Archzombie archzombie) {
        this.archzombie = archzombie;
        this.setFlags(EnumSet.of(Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = archzombie.getTarget();
        if (target == null) return false;

        return archzombie.getControlledVehicle() instanceof BanishingStampede stampede &&
                stampede.onGround() &&
                target.distanceTo(archzombie) > 5 &&
                !isDeepUnderwater(stampede.level(), stampede.blockPosition());
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (!(archzombie.getControlledVehicle() instanceof BanishingStampede stampede)) return;

        LivingEntity target = archzombie.getTarget();

        if (target == null) return;

        scanAndJumpOverGaps(stampede, target);
    }

    private void scanAndJumpOverGaps(BanishingStampede stampede, LivingEntity target) {
        Level level = stampede.level();
        Vec3 start = stampede.position();
        Vec3 end = target.position();

        double dist = start.distanceTo(end);
        if (dist < 2) return;

        Vec3 dir = end.subtract(start).normalize();
        BlockPos.MutableBlockPos scanPos = new BlockPos.MutableBlockPos();

        double scanDistance = Math.min(dist, SCAN_DISTANCE);
        for (double f = 1.0; f <= scanDistance; f += 0.5) {
            Vec3 step = start.add(dir.scale(f));
            scanPos.set(step.x, step.y, step.z);

            GapInfo gapInfo = detectGap(level, scanPos, dir);

            if (gapInfo != null) {
                if (gapInfo.size >= MIN_GAP_SIZE && gapInfo.size <= MAX_GAP_SIZE) {
                    double gapStartDist = start.distanceTo(new Vec3(scanPos.getX(), scanPos.getY(), scanPos.getZ()));
                    double gapEndDist = gapInfo.landingPos != null ?
                        end.distanceTo(new Vec3(gapInfo.landingPos.getX(), gapInfo.landingPos.getY(), gapInfo.landingPos.getZ())) : dist;

                    if (gapStartDist < dist && gapEndDist < dist) {
                        performJump(stampede, dir, gapInfo, gapStartDist);
                        return;
                    }
                }
            }
        }
    }

    private GapInfo detectGap(Level level, BlockPos.MutableBlockPos startPos, Vec3 direction) {
        if (!isGapStart(level, startPos)) {
            return null;
        }

        int gapSize = 0;
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

        for (int i = 0; i <= MAX_GAP_SIZE; i++) {
            Vec3 offset = direction.scale(i);
            checkPos.set(startPos.getX() + offset.x, startPos.getY(), startPos.getZ() + offset.z);

            if (isGapStart(level, checkPos)) {
                gapSize++;
            } else {
                if (gapSize > 0) {
                    return new GapInfo(gapSize, checkPos.immutable());
                }
                break;
            }
        }

        if (gapSize > MAX_GAP_SIZE) {
            return null;
        }

        return gapSize > 0 ? new GapInfo(gapSize, null) : null;
    }

    private boolean isGapStart(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos checkPos = new BlockPos.MutableBlockPos();

        for (int d = 1; d <= MAX_DEPTH_CHECK; d++) {
            checkPos.set(pos.getX(), pos.getY() - d, pos.getZ());
            BlockState state = level.getBlockState(checkPos);
            FluidState fluid = state.getFluidState();

            if (fluid.is(FluidTags.WATER)) {
                return true;
            }
            if (!state.isAir()) {
                return false;
            }
        }

        return true;
    }

    private void performJump(BanishingStampede stampede, Vec3 direction, GapInfo gapInfo, double distanceToGap) {
        double jumpPower = 0.5 + (gapInfo.size * 0.02);
        double horizontalSpeed = 0.8 + (gapInfo.size * 0.2);

        if (distanceToGap < 3.0) {
            horizontalSpeed *= 0.7;
        }

        Vec3 jump = new Vec3(direction.x * horizontalSpeed, jumpPower, direction.z * horizontalSpeed);
        stampede.setDeltaMovement(jump);

        float yRot = (float) Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0f;
        archzombie.setYRot(yRot);
        archzombie.yRotO = yRot;
        stampede.setYRot(yRot);
        stampede.yRotO = yRot;

        BMNetwork.sendToAll(new EntityActionSharedPacket(
            stampede.getId(),
            EntityActionSharedPacket.Action.SET_Y_ROTATION,
            Pair.of("val", yRot)
        ));
    }

    private boolean isDeepUnderwater(Level level, BlockPos pos) {
        return level.getFluidState(pos).is(FluidTags.WATER) &&
               level.getFluidState(pos.above()).is(FluidTags.WATER);
    }

    private record GapInfo(int size, BlockPos landingPos) {
    }
}