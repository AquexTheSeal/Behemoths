package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.utils.WorldUtils;

import java.util.EnumSet;

public class ArchzombieMovementGoal extends Goal {

    private final Archzombie archzombie;

    private int pathRecalcTicks;
    private int orbitTimer;
    private int pokeTimer;
    private boolean clockwise;

    public ArchzombieMovementGoal(Archzombie archzombie) {
        this.archzombie = archzombie;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.clockwise = archzombie.getRandom().nextBoolean();
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = archzombie.getTarget();

        if (target != null && target.distanceTo(archzombie) > 6 && archzombie.getControlledVehicle() instanceof BanishingStampede stampede && stampede.onGround()) {
            this.scanAndJumpOverGaps((BanishingStampede) archzombie.getControlledVehicle());
        }

        if (--pathRecalcTicks > 0) return;

        if (target != null && archzombie.getVehicle() instanceof BanishingStampede) {
            pathRecalcTicks = 1;
            moveAroundTarget(target);
        }
        else if (target != null) {
            pathRecalcTicks = 8 + archzombie.getRandom().nextInt(5);
            moveTowardTarget(target);
        }
        else {
            pathRecalcTicks = 20 + archzombie.getRandom().nextInt(10);
            wanderAimlessly();
        }
    }

    private void moveTowardTarget(LivingEntity target) {
        archzombie.getNavigation().moveTo(target, 1.2);
        archzombie.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }

    private void moveAroundTarget(LivingEntity target) {
        double pandemonium = WorldUtils.hasPandemoniumCurse(archzombie.level(), BMPandemoniumCurses.ARCHZOMBIE_SPEED.get()) ? 1.2 : 1.0;
        orbitTimer++;
        double distance = archzombie.distanceTo(target);

        if (distance < 8) pokeTimer++;

        if (orbitTimer % 100 == 0 && archzombie.getRandom().nextFloat() < 0.3F) {
            clockwise = !clockwise;
        }

        double orbitSpeed = 0.4 * pandemonium;
        double angle = orbitTimer * orbitSpeed * (clockwise ? 1 : -1);
        double radius = 3.0;

        Vec3 targetPos = target.position();
        double x = targetPos.x + Math.cos(angle) * radius;
        double z = targetPos.z + Math.sin(angle) * radius;
        double y = targetPos.y + 0.2;

        double offenseSpeed = (target.getVehicle() != null ? 1.45F : 1.25F) * pandemonium;
        double totalSpeed = offenseSpeed * (1 + distance * 0.015F);

        if (pokeTimer >= 80) {
            archzombie.getNavigation().moveTo(target, totalSpeed);

            if (pokeTimer >= 80 + (int)(30 * pandemonium)) {
                pokeTimer = -archzombie.getRandom().nextInt(20);
            }
            return;
        }

        if (archzombie.distanceToSqr(x, y, z) > 4.0D) {
            archzombie.getNavigation().moveTo(x, y, z, totalSpeed);
        }
    }

    private void wanderAimlessly() {
        Vec3 pos = DefaultRandomPos.getPos(archzombie, 8, 4);
        if (pos != null) {
            archzombie.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.0);
        }
    }

    private void scanAndJumpOverGaps(BanishingStampede stampede) {
        LivingEntity target = archzombie.getTarget();
        if (target == null) return;

        Level level = stampede.level();
        Vec3 start = stampede.position();
        Vec3 end = target.position();

        double dist = start.distanceTo(end);
        if (dist < 2) return;

        Vec3 dir = end.subtract(start).normalize();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (double f = 1.0; f <= Math.min(dist, 6); f++) {
            Vec3 step = start.add(dir.scale(f));

            pos.set(step.x, step.y, step.z);

            boolean groundMissing = true;
            boolean hasWater = false;

            for (int d = 1; d <= 4; d++) {
                pos.setY(pos.getY() - 1);

                BlockState state = level.getBlockState(pos);
                FluidState fluid = state.getFluidState();

                if (fluid.is(FluidTags.WATER)) {
                    hasWater = true;
                    break;
                }

                if (!state.isAir()) {
                    groundMissing = false;
                    break;
                }
            }

            if (groundMissing || hasWater) {
                Vec3 jump = new Vec3(dir.x * 3, 1.0, dir.z * 3);
                stampede.setDeltaMovement(jump);
                return;
            }
        }
    }
}
