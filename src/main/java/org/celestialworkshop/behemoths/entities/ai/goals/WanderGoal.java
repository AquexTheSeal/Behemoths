package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class WanderGoal extends Goal {
    protected final PathfinderMob entity;
    protected final double speed;
    protected int pathRecalcTicks;

    public WanderGoal(PathfinderMob entity, double speed) {
        this.entity = entity;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return entity.getTarget() == null;
    }

    @Override
    public void tick() {
        if (--pathRecalcTicks > 0) return;
        pathRecalcTicks = 20 + entity.getRandom().nextInt(10);

        Vec3 pos = DefaultRandomPos.getPos(entity, 8, 4);
        if (pos != null) {
            entity.getNavigation().moveTo(pos.x, pos.y, pos.z, speed);
        }
    }
}
