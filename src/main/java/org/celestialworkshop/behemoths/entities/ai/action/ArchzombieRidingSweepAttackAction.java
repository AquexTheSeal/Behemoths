package org.celestialworkshop.behemoths.entities.ai.action;

import net.minecraft.server.level.ServerLevel;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

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
        entity.getAnimationManager().startAnimation(Archzombie.RIDING_SWEEP_ANIMATION);
    }

    @Override
    public boolean onTick() {
        timer++;

        if (entity.getTarget() != null) {

            if (timer == 10 || timer == 15) {
                if (entity.getTarget().distanceTo(entity) <= 4.5) {
                    entity.getTarget().invulnerableTime = 0;
                    entity.multiplyAndAttackTarget(entity.getTarget(), 0.2F);
                }

                entity.playSound(BMSoundEvents.ARCHZOMBIE_SWING.get(), 1.0f, 0.8f + entity.getRandom().nextFloat() * 0.4f);

                if (timer == 10) {
                    VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("archzombie_slash_0"))
                            .type(VFXTypes.FLAT)
                            .lifetime(15)
                            .scale(2F)
                            .boundEntity(entity)
                            .yBoundOffset(1F)
                            .yRot(-entity.getYRot())
                            .zRot(-22.5F);
                    ((ServerLevel) entity.level()).sendParticles(data.build(), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0, 0, 0);
                } else {
                    VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("archzombie_slash_0"))
                            .type(VFXTypes.FLAT)
                            .lifetime(15)
                            .scale(2F)
                            .boundEntity(entity)
                            .yBoundOffset(1F)
                            .yRot(-entity.getYRot())
                            .zRot(180 + 22.5F);
                    ((ServerLevel) entity.level()).sendParticles(data.build(), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0, 0, 0);

                }
            }
        }
        return timer <= 30;
    }

    @Override
    public void onStop() {
        timer = 0;
        entity.getAnimationManager().stopAnimation(Archzombie.RIDING_SWEEP_ANIMATION);
    }
}