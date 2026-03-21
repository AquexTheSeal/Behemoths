package org.celestialworkshop.behemoths.entities.ai.action;

import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.misc.PhantashroomGlutton;

public class PhantashroomGluttonOpenAction extends ManagedAction<PhantashroomGlutton> {
    public PhantashroomGluttonOpenAction(PhantashroomGlutton entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.openTimer >= 100 && entity.canOpen();
    }

    @Override
    public void onStart() {
        entity.animationManager.stopAnimation(PhantashroomGlutton.CLOSE_ANIMATION);
        entity.animationManager.startAnimation(PhantashroomGlutton.OPEN_ANIMATION);
        timer = 0;
        entity.openTimer = -500 - entity.getRandom().nextInt(500);
    }

    @Override
    public boolean onTick() {
        timer++;
        if (timer == 5) {
            entity.onMouthOpened();
        }
        if (timer == 50) {
            entity.animationManager.stopAnimation(PhantashroomGlutton.OPEN_ANIMATION);
            entity.animationManager.startAnimation(PhantashroomGlutton.CLOSE_ANIMATION);
        }
        return timer < 75;
    }

    @Override
    public void onStop() {
        timer = 0;
    }
}
