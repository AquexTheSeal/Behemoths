package org.aqutheseal.behemoths.entity.ai.goal;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import org.aqutheseal.behemoths.entity.misc.ShockwaveEntity;
import org.aqutheseal.behemoths.entity.SkyCharydbis;
import org.aqutheseal.behemoths.registry.BMEntityTypes;

import java.util.EnumSet;

public class SkyCharydbisDiveDownGoal extends Goal {
    private final SkyCharydbis mob;
    private int timer;


    public SkyCharydbisDiveDownGoal(SkyCharydbis mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return mob.getTarget() != null && mob.getAttackState() == 1;
    }

    @Override
    public boolean canContinueToUse() {
        return true;
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

            if (timer < 60) {
                this.mob.push(0, Mth.lerp(timer / 60F, 0.1, 2), 0);
            } else if (timer < 100) {
                this.mob.push(0, -Mth.lerp((timer - 60) / 40F, 0.1, 7), 0);
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
