package org.celestialworkshop.behemoths.entities.projectile;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

public class Hollowcorper extends AbstractArrow {
    public float damage = 5.0F;
    public boolean hasHit;

    public Hollowcorper(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
    }

    public Hollowcorper(EntityType<? extends AbstractArrow> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
        this.setNoGravity(true);
    }

    public Hollowcorper(EntityType<? extends AbstractArrow> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
        this.setNoGravity(true);
    }

    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        this.hasHit = true;
        this.playSound(BMSoundEvents.HOLLOWCORPER_IMPACT.get(), 2.0f, 1.0f);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        Entity target = pResult.getEntity();

        Entity owner = this.getOwner();
        DamageSource damagesource;
        if (owner == null) {
            damagesource = this.damageSources().arrow(this, this);
        } else {
            damagesource = this.damageSources().arrow(this, owner);
            if (owner instanceof LivingEntity) {
                ((LivingEntity) owner).setLastHurtMob(target);
            }
        }

        if (target.hurt(damagesource, damage)) {
            if (target instanceof LivingEntity le) {
                this.doPostHurtEffects(le);
            }
        }

        this.discard();

    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide && !hasHit) {
            double rX = this.position().x();
            double rY = this.position().y();
            double rZ = this.position().z();
            VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("hollowcorper_trail"))
                    .type(VFXTypes.FLAT)
                    .fadeOut()
                    .lifetime(10 + random.nextInt(2))
                    .scale(0.0F, 1.5F, InterpolationTypes.EASE_OUT_CUBIC)
                    .zRot(this.getXRot() + 90)
                    .yRot(this.getYRot() - 90)
                    ;
            this.level().addParticle(data.build(), rX, rY, rZ, 0, 0, 0);
        }

        if (tickCount >= 60) {
            this.discard();
        }
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    public boolean isPickable() {
        return false;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}
