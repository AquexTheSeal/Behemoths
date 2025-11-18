package org.celestialworkshop.behemoths.entities.ai.action;

import org.celestialworkshop.behemoths.api.ManagedAction;
import org.celestialworkshop.behemoths.entities.Archzombie;

public class ArchzombieRidingSweepAttackAction extends ManagedAction<Archzombie> {

    public ArchzombieRidingSweepAttackAction(Archzombie entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.getTarget() != null && entity.getTarget().distanceTo(entity) <= 3 && entity.shouldSit();
    }

    @Override
    public void onStart() {
        timer = 0;
        entity.startAnimation(Archzombie.RIDING_SWEEP_ANIMATION);
    }

    @Override
    public boolean onTick() {
        timer++;
        System.out.println("StampedeRamAction timer: " + timer);

        if (entity.getTarget() != null) {
            if (timer == 10 || timer == 15) {
                if (entity.getTarget().distanceTo(entity) <= 4.5) {
                    entity.getTarget().invulnerableTime = 0;
                    entity.multiplyAndAttackTarget(entity.getTarget(), 0.2F);
                }
            }
        }
        return timer <= 30;
    }

    @Override
    public void onStop() {
        timer = 0;
        entity.stopAnimation(Archzombie.RIDING_SWEEP_ANIMATION);
    }
}