package org.celestialworkshop.behemoths.entities.projectile;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;
import org.celestialworkshop.behemoths.particles.TrailParticleData;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

import java.util.List;

public class CharydbisShard extends ThrowableProjectile {

    private static final EntityDataAccessor<Integer> STATUS_FLAG = SynchedEntityData.defineId(CharydbisShard.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_CONTROLLED = SynchedEntityData.defineId(CharydbisShard.class, EntityDataSerializers.BOOLEAN);

    public static final int HOSTILE_STATUS = -1, RETURNABLE_STATUS = 0, SUSPENDED_STATUS = 1, RETURNED_STATUS = 2;

    public List<LivingEntity> trackedPhantoms = new ObjectArrayList<>();
    public float allocatedDamage = 10.0F;
    public float suspendScale = 1.0F;
    public float suspendedTicks = 0;
    public int controlledRotationOffset = 0;
    public int controlledLayer = 0;

    public CharydbisShard(EntityType<? extends ThrowableProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void updateRotation() {
        if (isControlled()) return;
        super.updateRotation();
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount >= 1000) this.remove(RemovalReason.DISCARDED);

        if (this.level().isClientSide && this.tickCount % 10 == 0) {
            TrailParticleData data;
            float a = 1.0F;
            if (this.isControlled()) a = 0.2F;
            if (this.getStatusFlag() == HOSTILE_STATUS) {
                data = new TrailParticleData(this.getId(), 20, 15, 0.6F, 0.2F, 1, 0.2F, a);
            } else {
                data = new TrailParticleData(this.getId(), 20, 15, 0.6F, 1.0F, 0.1F, 0.6F, a);
            }
            this.level().addParticle(data, this.getX(), this.getY(), this.getZ(), 0, 0, 0);
        }

        if (getStatusFlag() == SUSPENDED_STATUS) {
            this.setDeltaMovement(0, 0.1 * suspendScale, 0);
            this.hurtMarked = true;
            if (this.suspendScale > 0.05F) {
                this.suspendScale *= 0.95F;
            }

            if (suspendedTicks++ >= 400) {
                this.setStatusFlag(HOSTILE_STATUS);
                this.playSound(BMSoundEvents.CHARYDBIS_SHARD_EXPIRE.get(), 1.0F, 1.0F);
                this.setNoGravity(false);
            }
        } else {
            suspendedTicks = 0;
        }

        if (this.isControlled() && this.getOwner() != null && !level().isClientSide) {
            Vec3 targetPos = this.getOwner().position().add(
                    Mth.sin(((this.getOwner().tickCount * 5) + controlledRotationOffset) * Mth.DEG_TO_RAD) * 6 * (1 + controlledLayer),
                    0,
                    Mth.cos(((this.getOwner().tickCount * 5) + controlledRotationOffset) * Mth.DEG_TO_RAD) * 8 * (1 + controlledLayer)
            );
            this.setDeltaMovement(targetPos.subtract(this.position()).scale(0.2F));
        }
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        boolean checkProjectile = pTarget instanceof Projectile proj && proj.getOwner() == this.getOwner();
        boolean checkMultipart = pTarget instanceof PartEntity<?> part && part.getParent() == this.getOwner();
        if (checkMultipart || checkProjectile || pTarget == this.getOwner()) return false;
        return super.canHitEntity(pTarget);
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.isInvulnerableTo(pSource) && this.getStatusFlag() == SUSPENDED_STATUS) {
            this.markHurt();
            this.setStatusFlag(RETURNED_STATUS);
//            this.setNoGravity(false);
            Entity entity = pSource.getEntity();
            if (entity != null) {
                if (!this.level().isClientSide && entity instanceof Player) {
                    Entity hitter = pSource.getEntity();
                    if (!this.shootTowardsNearestTarget(5.0F)) {
                        this.shootFromRotation(hitter, hitter.getXRot(), hitter.getYHeadRot(), 0, 3.0F, 0.0F);
                    }
                    this.setOwner(hitter);
                }
                return true;
            }
        }
        return false;
    }

    public boolean shootTowardsNearestTarget(float vel) {
        LivingEntity target = null;

        if (trackedPhantoms.size() < 5) {
            TargetingConditions phantomCond = TargetingConditions.forCombat().selector(e -> e instanceof Phantom && !trackedPhantoms.contains(e) && e.hasLineOfSight(this));
            target = this.level().getNearestEntity(Phantom.class, phantomCond, null, getX(), getY(), getZ(), this.getBoundingBox().inflate(16));
        }

        if (target == null || trackedPhantoms.size() >= 10) {
            target = this.level().getNearestEntity(SkyCharydbis.class, TargetingConditions.forCombat(), null, getX(), getY(), getZ(), this.getBoundingBox().inflate(64));
        }

        if (target != null) {
            Vec3 estimatedTargetPos = target.getEyePosition().add(target.getDeltaMovement());
            Vec3 dir = estimatedTargetPos.subtract(this.position()).normalize();
            this.shoot(dir.x, dir.y, dir.z, vel, 0.0F);
            return true;
        }

        return false;
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        if (this.level().isClientSide) return;

        if (this.getOwner() != null) {
            if (this.getStatusFlag() == RETURNED_STATUS && pResult.getEntity() instanceof Phantom ph) {
                this.explode(false);
                trackedPhantoms.add(ph);
                this.shootTowardsNearestTarget(Math.min((float) this.getDeltaMovement().length() * 1.25F, 5.0F));
                this.allocatedDamage *= 1.3F;
            } else {
                this.explode(true);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        int status = getStatusFlag();
        boolean controlled = isControlled();
        if (!controlled) {
            if (status == RETURNABLE_STATUS) {
                this.playSound(BMSoundEvents.CHARYDBIS_SHARD_SUSPEND.get(), 1.0F, 1.0F);
                setStatusFlag(SUSPENDED_STATUS);
                suspendScale = 1.0F;
                this.setNoGravity(true);
            } else if (status == RETURNED_STATUS || status == HOSTILE_STATUS) {
                this.explode(true);
            }
        }
    }

    public void explode(boolean discard) {
        if (level().isClientSide) return;

        List<Entity> targets = this.level().getEntitiesOfClass(Entity.class, this.getBoundingBox().inflate(3));
        for (Entity target : targets) {
            if (this.getOwner() instanceof LivingEntity lo && target != lo && (target instanceof LivingEntity || target instanceof PartEntity<?>)) {
                target.hurt(damageSources().mobProjectile(this, lo), this.allocatedDamage);
            }
        }

        this.playSound(BMSoundEvents.CHARYDBIS_SHARD_EXPLODE.get(), 1.0F, 1.0F);
        VFXParticleData.Builder particle = new VFXParticleData.Builder().textureName(Behemoths.prefix("charydbis_shard_explode"))
                .type(VFXTypes.FLAT_LOOK).scale(1.5F + (trackedPhantoms.size() * 0.3F)).zRot(random.nextInt(180))
                .lifetime(7 + random.nextInt(5));
        ((ServerLevel) this.level()).sendParticles(particle.build(), this.getX(), this.getY(), this.getZ(), 0, 0, 0, 0, 0);

        if (discard) remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(STATUS_FLAG, HOSTILE_STATUS);
        this.entityData.define(IS_CONTROLLED, false);
    }

    public int getStatusFlag() {
        return this.entityData.get(STATUS_FLAG);
    }

    public void setStatusFlag(int val) {
        this.entityData.set(STATUS_FLAG, val);
    }

    public boolean isControlled() {
        return this.entityData.get(IS_CONTROLLED);
    }

    public void setControlled(boolean val) {
        this.entityData.set(IS_CONTROLLED, val);
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setStatusFlag(pCompound.getInt("StatusFlag"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("StatusFlag", this.getStatusFlag());
    }
}