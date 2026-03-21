package org.celestialworkshop.behemoths.entities.ai.action;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

import java.util.List;

public class CharydbisCrashAction extends ManagedAction<SkyCharydbis> {

    public Vec3 targetPos;
    public boolean collided;
    public int collidedTime;

    public CharydbisCrashAction(SkyCharydbis entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.attackCooldown == 0 && entity.heldShards.isEmpty() && entity.isCurrentSleepFlag(SkyCharydbis.AWAKE_FLAG) &&
                entity.getTarget() != null && entity.getY() > entity.getTarget().getY() + 2;
    }

    @Override
    public void onStart() {
        timer = 0;
        collidedTime = 0;
        collided = false;
        this.targetPos = entity.lastTrackedTargetFloor.add(0, -2, 0);
        entity.attackCooldown = 200;
        entity.playSound(BMSoundEvents.CHARYDBIS_CRASH_START.get(), 3.0F, 1.0F);
    }

    @Override
    public boolean onTick() {
        timer++;
        if (timer < 40) {
            entity.getNavigation().stop();
            entity.setDeltaMovement(0, 3, 0);
            entity.setXRot(entity.getXRot() - 2);
        } else {
            entity.getNavigation().stop();
            entity.getMoveControl().setWantedPosition(targetPos.x(), targetPos.y() - 2, targetPos.z(), 4.0F);
        }

        if (collided) {
            if (entity.tickCount % 4 == 0) {
                entity.playSound(BMSoundEvents.HOLLOWBORNE_SMASH.get(), 3.0F, 1.0F);
                int dist = 8;
                List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(dist, dist / 2F, dist));
                for (LivingEntity target : targets.stream().filter(t -> t != entity).toList()) {
                    entity.attackTargetMultiplication(target, 1 - (target.distanceTo(entity) / dist));
                    target.knockback(4.0F * (1 - (target.distanceTo(entity) / dist)), entity.getX() - target.getX(), entity.getZ() - target.getZ());
                }
                entity.level().broadcastEntityEvent(entity,  (byte)68);
            }

            if (collidedTime++ >= 40) {
                entity.getNavigation().stop();
                entity.getNavigation().moveTo(this.targetPos.x(), this.targetPos.y() + 10, this.targetPos.z(), 2.0F);
                return false;
            }

        } else if (entity.onGround() || entity.verticalCollision || entity.horizontalCollision) {
            collided = true;
            collidedTime = 0;
        }

        return timer < 120;
    }

    @Override
    public void onStop() {
        timer = 0;
    }
}
