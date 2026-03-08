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
        return entity.attackCooldown == 0 && entity.heldShards.isEmpty() && entity.isCurrentSleepFlag(SkyCharydbis.AWAKE_FLAG)
                && entity.getTarget() != null && entity.getTarget().onGround();
    }

    @Override
    public boolean onTick() {
        timer++;
        if (timer < 20) {
            entity.getNavigation().stop();
            entity.setDeltaMovement(0, 2, 0);
            entity.setXRot(entity.getXRot() - 3);
        } else {
            entity.getNavigation().stop();
            entity.getMoveControl().setWantedPosition(targetPos.x(), targetPos.y() - 2, targetPos.z(), 3.0F);
        }

        if (collided) {
            if (entity.tickCount % 4 == 0) {
                entity.playSound(BMSoundEvents.HOLLOWBORNE_SMASH.get(), 3.0F, 1.0F);
                List<LivingEntity> targets = entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(6));
                for (LivingEntity target : targets.stream().filter(t -> t != entity).toList()) {
                    target.knockback(1.0F, entity.getX() - target.getX(), entity.getZ() - target.getZ());
                    target.hurtMarked = true;
                }
            }

            if (collidedTime++ >= 20) {
                entity.getNavigation().stop();
                entity.getMoveControl().setWantedPosition(entity.getX(), entity.getY() + 10, entity.getZ(), 3.0F);
                return false;
            }

        } else if (entity.onGround() || entity.verticalCollision || entity.horizontalCollision) {
            collided = true;
            collidedTime = 0;
        }

        return timer < 120;
    }

    @Override
    public void onStart() {
        timer = 0;
        collidedTime = 0;
        collided = false;
        this.targetPos = entity.getTarget().position().add(0, -1, 0);
        entity.attackCooldown = 200;
        entity.playSound(BMSoundEvents.CHARYDBIS_CRASH_START.get(), 3.0F, 1.0F);
    }

    @Override
    public void onStop() {
        timer = 0;
    }

    @Override
    public int getWeight() {
        return 200;
    }
}
