package org.aqutheseal.behemoths.entity.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.aqutheseal.behemoths.registry.BMEntityTypes;
import org.aqutheseal.behemoths.registry.BMParticleTypes;
import org.aqutheseal.behemoths.util.UtilLevel;
import org.aqutheseal.behemoths.util.UtilParticle;
import org.aqutheseal.behemoths.util.UtilSounds;

import java.util.ArrayList;
import java.util.List;

public class ShockwaveEntity extends Entity {

    public int lifespan = 5;
    public LivingEntity owner = null;
    public Vec3 direction = Vec3.ZERO;
    public int maxChain = 5;
    public int chainIndex = 0;

    public ShockwaveEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public List<Vec3> generateCircleVectors(double radius, int numberOfVectors) {
        List<Vec3> vectors = new ArrayList<>();
        for (int i = 0; i < numberOfVectors; i++) {
            double angle = 2 * Math.PI * i / numberOfVectors;
            double x = radius * Math.cos(angle);
            double z = radius * Math.sin(angle);
            Vec3 vector = new Vec3(x, 0, z);
            vectors.add(vector);
        }
        return vectors;
    }

    @Override
    public void tick() {
        if (this.firstTick) {
            UtilLevel.shakeScreensOnSurroundings(level(), position(), 16, 0.75F, p -> false);
            UtilParticle.addParticleServer(level(), BMParticleTypes.SHOCKWAVE_RING.get(), getX(), getY(), getZ());
            UtilParticle.addParticleServer(level(), BMParticleTypes.SHOCKWAVE_STREAK.get(), getX(), getY() + 1, getZ());
            UtilSounds.playSoundFromServer(level(), this, SoundEvents.GENERIC_EXPLODE, 1, 0.75F);
            if (owner != null) {
                UtilLevel.loopThroughLivingArea(owner, getBoundingBox().inflate(2, 0, 2), en -> {
                    if (TargetingConditions.forCombat().test(owner, en)) {
                        en.hurt(damageSources().mobAttack(owner), 15);
                        en.hasImpulse = true;
                        en.setDeltaMovement(0, 0.5, 0);
                    }
                });
            }
        }
        
        super.tick();

        if (tickCount >= lifespan) {
            if (!this.level().isClientSide()) {
                if (this.chainIndex < this.maxChain) {
                    if (this.chainIndex == 0) {
                        for (Vec3 dir : this.generateCircleVectors(6, 16)) {
                            this.spawnShockwave(dir);
                        }
                    } else {
                        this.spawnShockwave(this.direction);
                    }
                }
            }
            this.remove(RemovalReason.DISCARDED);
        }
    }

    private void spawnShockwave(Vec3 direction) {
        ShockwaveEntity shockwave = BMEntityTypes.SHOCKWAVE.get().create(level());
        shockwave.moveTo(blockPosition().offset((int) direction.x(), (int) direction.y(), (int) direction.z()), 0, 0);
        shockwave.direction = direction.scale(1.2);
        shockwave.owner = this.owner;
        shockwave.chainIndex = this.chainIndex + 1;
        level().addFreshEntity(shockwave);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {

    }
}
