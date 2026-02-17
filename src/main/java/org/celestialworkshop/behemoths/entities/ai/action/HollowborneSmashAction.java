package org.celestialworkshop.behemoths.entities.ai.action;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.celestialworkshop.behemoths.api.camera.CameraAngleManager;
import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.Hollowborne;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

import java.util.List;

public class HollowborneSmashAction extends ManagedAction<Hollowborne> {
    public HollowborneSmashAction(Hollowborne entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.forceSmash ||
                (!entity.isInWater() &&
                entity.getControllingPassenger() instanceof Mob mob &&
                mob.getTarget() != null && entity.smashCooldown == 0 &&
                mob.getTarget().getBoundingBox().intersects(entity.getBoundingBox().inflate(4, 0, 4)));
    }

    @Override
    public boolean onTick() {
        ++timer;
        if (timer == 35) {
            AABB area = entity.getBoundingBox().inflate(12, 1, 12).expandTowards(0, -(entity.getBoundingBox().getYsize() / 2), 0);
            List<LivingEntity> targetList = entity.level().getEntitiesOfClass(LivingEntity.class, area)
                    .stream().filter(e -> e != entity && !entity.getPassengers().contains(e)).toList();

            float mx = 0.7F;
            if (entity.getControllingPassenger() instanceof Player) {
                mx = 1.2F;
            }

            for (LivingEntity target : targetList) {
                float dist = Math.min(entity.distanceTo(target), 12.0F);
                float dmg = Mth.lerp(dist / 12, mx, 0.1F);
                if (entity.attackTargetMultiplication(target, dmg)) {
                    target.knockback(3.0F, entity.getX() - target.getX(), entity.getZ() - target.getZ());
                    entity.hasImpulse = true;
                }
            }
            entity.playSound(BMSoundEvents.HOLLOWBORNE_SMASH.get(), 2.0F, 0.5F);
            entity.level().broadcastEntityEvent(entity, Hollowborne.SMASH_PARTICLES_ENTITY_EVENT);
            CameraAngleManager.shakeArea(entity.level(), entity.position(), 24, 2.5F, 40, 30);

        }
        return timer < 70;
    }

    @Override
    public void onStart() {
        entity.animationManager.startAnimation(Hollowborne.SMASH_ANIMATION);
        entity.playSound(BMSoundEvents.HOLLOWBORNE_SMASH_START.get(), 2.0F, 1.0F);
        timer = 0;
        entity.smashCooldown = entity.getRandom().nextIntBetweenInclusive(200, 240);
        entity.forceSmash = false;
    }

    @Override
    public void onStop() {
        entity.animationManager.stopAnimation(Hollowborne.SMASH_ANIMATION);
        timer = 0;
        entity.forceSmash = false;
    }
}
