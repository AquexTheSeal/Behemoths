package org.celestialworkshop.behemoths.entities.ai.action;

import org.celestialworkshop.behemoths.api.entity.ManagedAction;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumVotingSystem;
import org.celestialworkshop.behemoths.entities.HollowborneTurret;
import org.celestialworkshop.behemoths.entities.projectile.Hollowcorper;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

public class HollowborneTurretShootBarrageAction extends ManagedAction<HollowborneTurret> {

    public HollowborneTurretShootBarrageAction(HollowborneTurret entity) {
        super(entity);
    }

    @Override
    public boolean canStart() {
        return entity.getTarget() != null && entity.getTarget().distanceTo(entity) > 6 && entity.restTime == 0;
    }

    @Override
    public void onStart() {
        entity.getAnimationManager().startAnimation(HollowborneTurret.SHOOT_BARRAGE_ANIMATION);
        entity.restTime = 100 + entity.getRandom().nextInt(60);
        timer = 0;
    }

    @Override
    public boolean onTick() {
        timer++;

        if (timer >= 40 && timer <= 60 && timer % 2 == 0) {
            entity.playSound(BMSoundEvents.HOLLOWBORNE_TURRET_SHOOT.get(), 5.0F, 1.0F);
            Hollowcorper proj = new Hollowcorper(BMEntityTypes.HOLLOWCORPER.get(), entity.getX(), entity.getEyeY(), entity.getZ(), entity.level());
            float multiplier = PandemoniumVotingSystem.hasPandemoniumCurse(entity.level(), BMPandemoniumCurses.DEADLY_HOLLOWCORPER) ? 1.5F : 1.0F;
            proj.shootFromRotation(entity, entity.getXRot(), entity.getYHeadRot(), 0, 2.5F * multiplier, 4F);
            proj.damage *= multiplier;
            proj.setOwner(entity);
            entity.level().addFreshEntity(proj);
        }

        return timer <= 80;
    }

    @Override
    public void onStop() {
        entity.getAnimationManager().stopAnimation(HollowborneTurret.SHOOT_BARRAGE_ANIMATION);
        timer = 0;
    }
}
