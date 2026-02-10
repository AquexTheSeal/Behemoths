package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import org.celestialworkshop.behemoths.entities.Hollowborne;
import org.celestialworkshop.behemoths.entities.HollowborneTurret;

public class HollowborneTargetSyncGoal extends TargetGoal {
    protected final Hollowborne borne;

    public HollowborneTargetSyncGoal(Hollowborne borne) {
        super(borne, false);
        this.borne = borne;
    }

    @Override
    public boolean canUse() {
        if (borne.getFirstPassenger() instanceof HollowborneTurret turret) {
            LivingEntity turretTarget = turret.getTarget();

            if (turretTarget != null && turretTarget.isAlive()) {
                this.targetMob = turretTarget;
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        this.borne.setTarget(this.targetMob);
        super.start();
    }

    @Override
    public boolean canContinueToUse() {
        if (borne.getFirstPassenger() instanceof HollowborneTurret turret) {
            LivingEntity turretTarget = turret.getTarget();
            return turretTarget != null && turretTarget.isAlive() && turretTarget == this.targetMob;
        }
        return false;
    }
}