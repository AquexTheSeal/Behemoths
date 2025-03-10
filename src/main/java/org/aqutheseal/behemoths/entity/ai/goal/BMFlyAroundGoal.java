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
        this.tick = mob.getRandom().nextInt(5);
        this.move();
    }

    @Override
    public void stop() {
        this.tick = mob.getRandom().nextInt(5);
    }

    @Override
    public void tick() {
        super.tick();

        if (++tick >= 200) {
            this.move();
            tick = 0;
        }
    }

    public void move() {
        RandomSource randomsource = this.mob.getRandom();
        Vec3 point = this.mob.position();
        int distance = 64;
        boolean allowVerticalRoaming = true;
        if (this.mob instanceof SkyCharydbis charydbis) {
            if (charydbis.homePosition != null) {
                point = charydbis.homePosition;
                allowVerticalRoaming = false;
            }
        }
        double d0 = point.x() + -distance + randomsource.nextInt(distance * 2);
        double d1 = point.y() + -distance + randomsource.nextInt(distance * 2);
        if (!allowVerticalRoaming) {
            d1 = point.y();
        }
        double d2 = point.z() + -distance + randomsource.nextInt(distance * 2);
        this.mob.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
    }
}
