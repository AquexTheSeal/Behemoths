package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SimpleDistanceFromGoal extends Goal {
    protected final PathfinderMob entity;
    protected final double minDistance;
    protected final double speed;
    protected int pathRecalcTicks;

    public SimpleDistanceFromGoal(PathfinderMob entity, double speed, double minDistance) {
        this.entity = entity;
        this.speed = speed;
        this.minDistance = minDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        return target != null && entity.distanceToSqr(target) < (minDistance * minDistance);
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        if (target == null) return;

        if (--pathRecalcTicks > 0 && !entity.getNavigation().isDone()) return;
        pathRecalcTicks = 10 + entity.getRandom().nextInt(10);

        Vec3 fleePos = DefaultRandomPos.getPosAway(this.entity, 16, 7, target.position());
        if (fleePos != null) {
            this.entity.getNavigation().moveTo(fleePos.x, fleePos.y, fleePos.z, this.speed);
        }
    }
}