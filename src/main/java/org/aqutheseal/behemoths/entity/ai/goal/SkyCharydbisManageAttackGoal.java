package org.aqutheseal.behemoths.entity.ai.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import org.aqutheseal.behemoths.entity.SkyCharydbis;
import org.aqutheseal.behemoths.entity.misc.ShockwaveEntity;
import org.aqutheseal.behemoths.registry.BMEntityTypes;

import java.util.EnumSet;

public class SkyCharydbisManageAttackGoal extends Goal {
    private final SkyCharydbis mob;
    private int timer;


    public SkyCharydbisManageAttackGoal(SkyCharydbis mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() != null && mob.getAttackState() == 1;
    }

    @Override
    public void start() {
        super.start();
        this.timer = 0;
    }

    @Override
    public void stop() {
        super.stop();
        mob.setAttackState(0);
        this.timer = 0;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        Entity target = mob.getTarget();
        Level level = mob.level();

        if (target != null) {

            timer++;

            double xx = this.mob.getMoveControl().getWantedX();
            double yy = this.mob.getMoveControl().getWantedY();
            double zz = this.mob.getMoveControl().getWantedZ();
            double spD = this.mob.getMoveControl().getSpeedModifier();

            if (timer < 60) {
                this.mob.getMoveControl().setWantedPosition(xx, yy + 2, zz, spD);
            } else if (timer < 100) {
                this.mob.push(0, -2, 0);
                this.mob.getMoveControl().setWantedPosition(xx, yy - 15, zz, spD);
                if (mob.onGround()) {
                    ShockwaveEntity shockwave = BMEntityTypes.SHOCKWAVE.get().create(level);
                    shockwave.moveTo(mob.blockPosition(), 0, 0);
                    shockwave.owner = mob;
                    level.addFreshEntity(shockwave);
                    this.stop();
                }
            } else {
                this.stop();
            }
        }
    }
}
