package org.celestialworkshop.behemoths.entities.ai.action;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumVotingSystem;

public class StampedeRamAction extends ManagedAction<BanishingStampede> {
    public Vec3 lockedDirection = null;

    public StampedeRamAction(BanishingStampede entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.isRamming();
    }

    @Override
    public void onStart() {
        timer = 0;
        entity.getAnimationManager().startAnimation(BanishingStampede.RAMMING_ANIMATION);
        entity.playSound(BMSoundEvents.STAMPEDE_CHARGE_ROAR.get());
    }

    @Override
    public void onStop() {
        timer = 0;
        entity.setRamming(false);
        entity.getAnimationManager().stopAnimation(BanishingStampede.RAMMING_ANIMATION);
    }

    @Override
    public boolean onTick() {
        timer++;

        if (timer < 40) {
            if (entity.getControllingPassenger() instanceof Mob mob && mob.getTarget() != null) {
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, mob.getTarget().position());
                entity.getLookControl().setLookAt(mob.getTarget().position());
            }
        }

        if (timer == 40) {
            lockedDirection = entity.getLookAngle();
        }

        if (timer >= 40) {

            float interval = (timer - 40) * 0.075F;
            if (timer % (4 + (int) interval) == 0) {
                entity.playSound(BMSoundEvents.STAMPEDE_CHARGE_STEP.get(), 0.7F, (1.0F - (interval * 0.05F)));
            }

            double ratio = Mth.lerp((timer - 40) / 60F, 1.5F, 0.0F);
            double speed = entity.getControllingPassenger() instanceof Mob ? 1.0F : 2.0F;

            Vec3 horizontalMovement = lockedDirection.scale(ratio).multiply(speed, 0, speed);
            entity.setDeltaMovement(horizontalMovement.x, entity.getDeltaMovement().y - 0.2F, horizontalMovement.z);

            this.emitSmoke();
            this.emitSmoke();

            if (timer % 3 == 0) {
                VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("stampede_charging"))
                        .type(VFXTypes.FLAT)
                        .fadeOut()
                        .lifetime(10)
                        .scale(0.0F, 3.5F, InterpolationTypes.EASE_OUT_QUAD)
                        .xRot(entity.getRandom().nextFloat() * 360)
                        .xRot(90)
                        .zRot(entity.getYRot());
                Vec3 pos = entity.position().add(entity.getDeltaMovement().scale(3));
                ((ServerLevel) entity.level()).sendParticles(data.build(), pos.x(), pos.y() + 2, pos.z(), 0, 0, 0, 0, 0);
            }

            if (entity.getControllingPassenger() instanceof Mob) {
                Vec3 targetPosition = entity.position().add(horizontalMovement);
                entity.lookAt(EntityAnchorArgument.Anchor.EYES, targetPosition);
                entity.getLookControl().setLookAt(targetPosition);
            }

            for (LivingEntity target : entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(2.5D))) {
                if ((!entity.getPassengers().isEmpty() && !entity.getPassengers().contains(target)) && target != entity) {
                    entity.attackTargetMultiplication(target, PandemoniumVotingSystem.hasPandemoniumCurse(entity.level(), BMPandemoniumCurses.GRAVEBREAKER_MOMENTUM.get()) ? 1.0F : 0.5F);
                    target.push(0, 0.2F, 0);
                    target.hurtMarked = true;
                }
            }

        }

        return timer <= 100;
    }

    public void emitSmoke() {
        Vec3 delta = entity.getDeltaMovement().scale(1.5F);
        double xx = delta.x() + entity.getX() + entity.getRandom().nextDouble() * 4 - 2;
        double yy = entity.getY() + entity.getRandom().nextDouble() * 0.75;
        double zz = delta.z() + entity.getZ() + entity.getRandom().nextDouble() * 4 - 2;

        VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("stampede_charging_smoke"))
                .type(VFXTypes.FLAT_LOOK)
                .alpha(0.5F, 0.0F)
                .lifetime(30 + entity.getRandom().nextInt(5))
                .scale(0.75F + entity.getRandom().nextFloat() * 0.25F)
                .zRot(entity.getRandom().nextInt(180));
        ((ServerLevel) entity.level()).sendParticles(data.build(), xx, yy, zz, 0, 0, 0, 0, 0.1F);
    }
}
