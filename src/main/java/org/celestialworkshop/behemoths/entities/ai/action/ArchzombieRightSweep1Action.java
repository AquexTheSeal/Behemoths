package org.celestialworkshop.behemoths.entities.ai.action;

import net.minecraft.server.level.ServerLevel;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

public class ArchzombieRightSweep1Action extends ManagedAction<Archzombie> {

    public ArchzombieRightSweep1Action(Archzombie entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.getTarget() != null && entity.getTarget().distanceTo(entity) <= 4 && !entity.shouldSit();
    }

    @Override
    public void onStart() {
        timer = 0;
        entity.getAnimationManager().startAnimation(Archzombie.SWEEP_1_ANIMATION);
    }

    @Override
    public boolean onTick() {
        timer++;

        if (entity.getTarget() != null) {
            if (timer == 15) {
                if (entity.getTarget().distanceTo(entity) <= 3.5) {
                    entity.multiplyAndAttackTarget(entity.getTarget(), 0.55F);
                    entity.getTarget().knockback(1.5, entity.getX() - entity.getTarget().getX(), entity.getZ() - entity.getTarget().getZ());
                }

                entity.playSound(BMSoundEvents.ARCHZOMBIE_SWING.get(), 1.0f, 0.8f + entity.getRandom().nextFloat() * 0.4f);
                VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("archzombie_slash_0"))
                        .type(VFXTypes.FLAT)
                        .lifetime(15)
                        .scale(2F)
                        .boundEntity(entity)
                        .yBoundOffset(1.5F)
                        .yRot(-entity.getYRot())
                        .zRot(180 + 22.5F);
                ((ServerLevel) entity.level()).sendParticles(data.build(), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0, 0, 0);
            }
        }
        return timer < 30;
    }

    @Override
    public void onStop() {
        timer = 0;
        entity.getAnimationManager().stopAnimation(Archzombie.SWEEP_1_ANIMATION);
    }
}