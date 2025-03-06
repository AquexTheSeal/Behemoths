package org.aqutheseal.behemoths.entity.ai.goal;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.aqutheseal.behemoths.entity.SkyCharydbis;

import java.util.EnumSet;

public class BMFlyAroundGoal extends Goal {
    private float tick;
    private final Mob mob;

    public BMFlyAroundGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        return mob.getTarget() == null;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void start() {
        this.tick = 0;
        this.move();
    }

    @Override
    public void stop() {
        this.tick = 0;
    }

    @Override
    public void tick() {
        super.tick();

        tick++;
        if (tick % 100 == 0 || mob.onGround() || mob.verticalCollision) {
            this.move();
        }
    }

    public void move() {
        RandomSource randomsource = this.mob.getRandom();
        Vec3 point = this.mob.position();
        int distance = 48;
        if (this.mob instanceof SkyCharydbis charydbis) {
            if (charydbis.homePosition != null) {
                point = charydbis.homePosition;
                distance /= 2;
            }
        }
        double d0 = point.x() + (-distance + randomsource.nextInt(distance * 2));
        double d1 = point.y() + (this.mob.onGround() ? randomsource.nextInt(distance) : (-distance + randomsource.nextInt(distance * 2)));
        double d2 = point.z() + (-distance + randomsource.nextInt(distance * 2));
        this.mob.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
    }
}
