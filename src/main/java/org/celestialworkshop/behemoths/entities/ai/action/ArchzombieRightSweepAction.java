package org.celestialworkshop.behemoths.entities.ai.action;

import org.celestialworkshop.behemoths.api.ManagedAction;
import org.celestialworkshop.behemoths.entities.Archzombie;

public class ArchzombieRightSweepAction extends ManagedAction<Archzombie> {

    public ArchzombieRightSweepAction(Archzombie entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.getTarget() != null && entity.getTarget().distanceTo(entity) <= 4 && !entity.shouldSit();
    }

    @Override
    public void onStart() {
        timer = 0;
        entity.startAnimation(Archzombie.SWEEP_0_ANIMATION);
    }

    @Override
    public boolean onTick() {
        timer++;
        if (entity.getTarget() != null) {
            if (timer == 15) {
                if (entity.getTarget().distanceTo(entity) <= 4) {
                    entity.multiplyAndAttackTarget(entity.getTarget(), 0.5F);
                    entity.getTarget().knockback(1.5, entity.getX() - entity.getTarget().getX(), entity.getZ() - entity.getTarget().getZ());
                }
            }
        }
        return timer <= 30;
    }

    @Override
    public void onStop() {
        timer = 0;
        entity.stopAnimation(Archzombie.SWEEP_0_ANIMATION);
    }
}