package org.celestialworkshop.behemoths.entities.ai.action;

import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

public class CharydbisSubmergeAction extends ManagedAction<SkyCharydbis> {

    public CharydbisSubmergeAction(SkyCharydbis entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.attackCooldown == 0 && entity.heldShards.isEmpty() && entity.isCurrentSleepFlag(SkyCharydbis.AWAKE_FLAG) && !entity.isSubmerged && entity.getTarget() != null;
    }

    @Override
    public boolean onTick() {
        timer++;
        return timer <= 60;
    }

    @Override
    public void onStart() {
        timer = 0;
        entity.attackCooldown = 500;
        entity.playSound(BMSoundEvents.CHARYDBIS_ROAR.get(), 3.0F, 0.5F);
    }

    @Override
    public void onStop() {
        timer = 0;
        entity.isSubmerged = true;
    }
}
