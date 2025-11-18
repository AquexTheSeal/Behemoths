package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.BanishingStampede;

import java.util.EnumSet;

public class ArchzombieMovementGoal extends Goal {

    private final Archzombie archzombie;
    private int pathRecalcTicks;
    private int orbitTimer;
    private int pokeTimer;
    private boolean clockwise = true;

    public ArchzombieMovementGoal(Archzombie archzombie) {
        this.archzombie = archzombie;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.clockwise = archzombie.getRandom().nextBoolean();
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = archzombie.getTarget();

        if (--pathRecalcTicks > 0) return;

        if (target != null && archzombie.getVehicle() instanceof BanishingStampede stampede) {
            pathRecalcTicks = 1;
            moveAroundTarget(target);

        } else if (target != null) {
            pathRecalcTicks = 8 + archzombie.getRandom().nextInt(5);
            moveTowardTarget(target);

        } else {
            pathRecalcTicks = 20 + archzombie.getRandom().nextInt(10);
            wanderAimlessly();
        }
    }

    private void moveTowardTarget(LivingEntity target) {
        archzombie.getNavigation().moveTo(target, 1.2);
        archzombie.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }

    private void moveAroundTarget(LivingEntity target) {
        double radius = 3.0;
        double orbitSpeed = 0.4;
        double pokeInterval = 80;

        orbitTimer++;
        if (archzombie.distanceTo(target) < 8) {
            pokeTimer++;
        }

        if (orbitTimer % 100 == 0 && archzombie.getRandom().nextFloat() < 0.3F) {
            clockwise = !clockwise;
        }

        double angle = orbitTimer * orbitSpeed * (clockwise ? 1 : -1);
        Vec3 targetPos = target.position();
        double x = targetPos.x + Math.cos(angle) * radius;
        double z = targetPos.z + Math.sin(angle) * radius;
        double y = targetPos.y + 0.2;

        double offenseSpeed = target.getVehicle() != null ? 1.65F : 1.35F;
        double distanceScaling = archzombie.distanceTo(target) * 0.01F;
        double totalSpeed = offenseSpeed + (offenseSpeed * distanceScaling);
        if (pokeTimer >= pokeInterval) {
            archzombie.getNavigation().moveTo(target, totalSpeed);
            if (pokeTimer >= pokeInterval + 30) {
                pokeTimer = -archzombie.getRandom().nextInt(20);
            }
        } else {
            double distanceSq = archzombie.distanceToSqr(x, y, z);
            if (distanceSq > 4.0D) {
                archzombie.getNavigation().moveTo(x, y, z, totalSpeed);
            }
        }
    }

    private void wanderAimlessly() {
        Vec3 pos = DefaultRandomPos.getPos(archzombie, 8, 4);
        if (pos != null) {
            archzombie.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.0);
        }
    }
}
