package org.celestialworkshop.behemoths.entities.ai.action;

import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;

public class CharydbisResurfaceAction extends ManagedAction<SkyCharydbis> {

    public CharydbisResurfaceAction(SkyCharydbis entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.attackCooldown == 0 && entity.isCurrentSleepFlag(SkyCharydbis.AWAKE_FLAG) && entity.isSubmerged;
    }

    @Override
    public boolean onTick() {
        return false;
    }

    @Override
    public void onStart() {
        entity.isSubmerged = false;
        entity.attackCooldown = Mth.ceil(300 * entity.getAttacksIntervalScale());
    }
}
