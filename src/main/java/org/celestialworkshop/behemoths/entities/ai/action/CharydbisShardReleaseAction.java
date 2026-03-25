package org.celestialworkshop.behemoths.entities.ai.action;

import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;
import org.celestialworkshop.behemoths.entities.projectile.CharydbisShard;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

public class CharydbisShardReleaseAction extends ManagedAction<SkyCharydbis> {

    public CharydbisShardReleaseAction(SkyCharydbis entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.attackCooldown == 0 && !entity.heldShards.isEmpty() && entity.isCurrentSleepFlag(SkyCharydbis.AWAKE_FLAG);
    }

    @Override
    public boolean onTick() {
        ++timer;
        if (timer < 20) {
            Vec3 rise = entity.position().add(0, 10, 0);
            entity.getNavigation().moveTo(rise.x(), rise.y(), rise.z(), 3.0);
            entity.setXRot(entity.getXRot() - 10);
        }
        if (timer == 20) {
            for (CharydbisShard shard : entity.heldShards) {
                double dx = shard.getX() - entity.getX();
                double dz = shard.getZ() - entity.getZ();
                double dist = Math.sqrt(dx * dx + dz * dz);

                shard.setControlled(false);
                shard.setNoGravity(false);
                shard.shoot(dx / dist, -1, dz / dist, 0.75F, 1.5F);
            }
            entity.heldShards.clear();
        }
        return timer < 50;
    }

    @Override
    public void onStart() {
        timer = 0;
        entity.attackCooldown = Mth.ceil(300 * entity.getAttacksIntervalScale());
        entity.getAnimationManager().startAnimation(SkyCharydbis.SHARD_RELEASE_ANIMATION);
        entity.playSound(BMSoundEvents.CHARYDBIS_SHARD_RELEASE.get(), 3.0F, 1.0F);
    }

    @Override
    public void onStop() {
        timer = 0;
        entity.getAnimationManager().stopAnimation(SkyCharydbis.SHARD_RELEASE_ANIMATION);
    }
}
