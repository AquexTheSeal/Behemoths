package org.celestialworkshop.behemoths.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.FluidType;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.camera.CameraAngleManager;
import org.celestialworkshop.behemoths.api.camera.ScreenShakeHandler;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.client.animations.HollowborneAnimations;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.CustomSaddleable;
import org.celestialworkshop.behemoths.entities.ai.action.HollowborneSmashAction;
import org.celestialworkshop.behemoths.entities.ai.goals.WanderGoal;
import org.celestialworkshop.behemoths.entities.ai.mount.CustomJumpingMount;
import org.celestialworkshop.behemoths.entities.ai.mount.MountJumpManager;
import org.celestialworkshop.behemoths.entities.ai.controls.move.BMMoveControl;
import org.celestialworkshop.behemoths.misc.utils.EntityUtils;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMItems;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;
import org.celestialworkshop.behemoths.registries.BMTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Hollowborne extends TamableAnimal implements BMEntity, Enemy, CustomSaddleable, CustomJumpingMount<Hollowborne> {

    private static final EntityDataAccessor<Boolean> DATA_IS_SADDLED = SynchedEntityData.defineId(Hollowborne.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_SHOULD_SHOW_TAMED_PARTICLES = SynchedEntityData.defineId(Hollowborne.class, EntityDataSerializers.BOOLEAN);
    public static final byte SMASH_PARTICLES_ENTITY_EVENT = 69;
    public static final byte TAME_HEARTS_EVENT = 7;

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<Hollowborne> attackManager = new ActionManager<>(this);
    public final MountJumpManager<Hollowborne> mountJumpManager = new MountJumpManager<>(this);

    public boolean shouldUpdateLegs = true;
    public int savedJumpPower = 0;
    public float[][] legOffsets = {{3, 3}, {-3, 3}, {3, -3}, {-3, -3}};
    public float[] targetDepthsO = new float[]{-3f, -3f, -3f, -3f};
    public float[] targetDepths = new float[]{-3f, -3f, -3f, -3f};

    public static final String SMASH_ANIMATION = "smash";
    public static final String DEATH_ANIMATION = "death";

    public int smashCooldown = 0;
    public boolean forceSmash = false;
    public boolean switchStep = false;

    public Hollowborne(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.setMaxUpStep(6.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 16.0F);
        this.moveControl = new BMMoveControl<>(this);

        this.animationManager.registerAnimation(DEATH_ANIMATION, () -> HollowborneAnimations.DEATH);
        this.animationManager.registerAnimation(SMASH_ANIMATION, () -> HollowborneAnimations.SMASH);

        this.attackManager.addAction(new HollowborneSmashAction(this));
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return animationManager;
    }

    @Override
    public List<ActionManager> getActionManagers() {
        return List.of(attackManager);
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.22D)
                .add(Attributes.ATTACK_DAMAGE, 20.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(ForgeMod.SWIM_SPEED.get(), 1.0D)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WanderGoal(this, 0.8D));
    }

    @Override
    public boolean canSwimInFluidType(FluidType type) {
        return type == Fluids.WATER.getFluidType();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            this.updateLegDepths();
            if (this.shouldShowTamedParticles()) {
                Vec3 pos = this.getEyePosition().offsetRandom(this.random, 8);
                level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, pos.x(), pos.y(), pos.z(), 0, 0, 0);
            }
        } else {
            if (smashCooldown > 0) {
                smashCooldown--;
            }

            if (this.onGround() && this.savedJumpPower > 0) {
                AABB area = getBoundingBox().inflate(12, 1, 12).expandTowards(0, -(getBoundingBox().getYsize() / 2), 0);
                List<LivingEntity> targetList = level().getEntitiesOfClass(LivingEntity.class, area).stream().filter(e -> e != this && !getPassengers().contains(e)).toList();

                for (LivingEntity target : targetList) {
                    float dist = Math.min(distanceTo(target), 12.0F);
                    float dmgScale = Mth.lerp(savedJumpPower / 200F, 0.2F, 1.8F);
                    float dmg = Mth.lerp(dist / 12, dmgScale, 0.1F);
                    if (attackTargetMultiplication(target, dmg)) {
                        target.knockback(3.0F, getX() - target.getX(), getZ() - target.getZ());
                        hasImpulse = true;
                    }
                }

                this.playSound(BMSoundEvents.HOLLOWBORNE_SMASH.get(), 2.0F, 1.0F);
                float intensity = Mth.lerp(savedJumpPower / 200F, 0.1F, 2.0F);
                CameraAngleManager.shakeArea(this.level(), this.position(), 24, intensity, 40, 30);
                this.level().broadcastEntityEvent(this, Hollowborne.SMASH_PARTICLES_ENTITY_EVENT);
                this.savedJumpPower = 0;
            }
        }

        if (!this.isTame()) {
            EntityUtils.breakBlocks(this, true, state -> state.is(BlockTags.LEAVES));
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
        if (pId == SMASH_PARTICLES_ENTITY_EVENT) {
            for (int i = 0; i < 50; i++) {
                VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("hollowborne_smash_blast"))
                        .type(VFXTypes.FLAT_LOOK).scale(3 + random.nextFloat() * 2).lifetime(10 + random.nextInt(10));
                double xx = this.getX() + this.random.nextGaussian() * 12F;
                double yy = this.getY() + this.random.nextGaussian() * 2F;
                double zz = this.getZ() + this.random.nextGaussian() * 12F;
                level().addParticle(data.build(), xx, yy, zz, 0, 0, 0);
            }
        } else if (pId == TAME_HEARTS_EVENT) {
            Minecraft.getInstance().level.playLocalSound(getX(), getY(), getZ(), SoundEvents.ZOMBIE_VILLAGER_CURE, SoundSource.HOSTILE, 0.3F, 1.2F, false);
            for (int i = 0; i < 100; i++) {
                double x = random.nextDouble() * 2 - 1;
                double y = random.nextDouble() * 2 - 1;
                double z = random.nextDouble() * 2 - 1;
                double length = Math.sqrt(x * x + y * y + z * z);
                if (length == 0) continue;
                x /= length;
                y /= length;
                z /= length;
                level().addParticle(ParticleTypes.SOUL_FIRE_FLAME, getX(), getEyeY(), getZ(), x * 0.3, y * 0.3, z * 0.3);
            }
        } else {
            super.handleEntityEvent(pId);
        }
    }

    @Override
    public MountJumpManager<Hollowborne> getMountJumpManager() {
        return mountJumpManager;
    }

    @Override
    public void performJump(int power) {
        if (this.shouldSmashFromPower(power)) {
            if (!level().isClientSide) {
                this.forceSmash = true;
            }
        } else {
            if (level().isClientSide) {
                Vec3 direction = getLookAngle().multiply(1, 0, 1).normalize();
                Vec3 jumpVelocity = direction.scale(power * 0.025).add(0, power * 0.0125, 0);
                setDeltaMovement(jumpVelocity);
            } else {
                if (power < 100) {
                    this.playSound(BMSoundEvents.HOLLOWBORNE_JUMP.get(), 0.5F, 0.7F);
                } else {
                    this.playSound(BMSoundEvents.HOLLOWBORNE_JUMP_STRONG.get(), 2.0F, 1.0F);
                }
                float delta = power / 200F;
                float intensity = Mth.lerp(delta, 0.1F, 2.0F);
                CameraAngleManager.shakeArea(this.level(), this.position(), 24, intensity, 40, 30);

                VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("hollowborne_jump"))
                        .type(VFXTypes.FLAT).scale(0,  10 + delta * 5, InterpolationTypes.EASE_OUT_QUAD)
                        .fadeOut().lifetime(Mth.ceil(10 + delta * 5));
                ((ServerLevel) this.level()).sendParticles(data.build(), this.getX(), this.getY() + 0.5, this.getZ(), 0, 0, 0, 0, 0);

                this.savedJumpPower = power;
                this.setOnGround(false);
            }
        }
    }

    @Override
    public boolean getJumpCondition() {
        return this.onGround() || this.isInWater();
    }

    @Override
    public int getJumpCooldown(int power) {
        if (power < 5) return 0;

        if (this.shouldSmashFromPower(power)) {
            return 200;
        }
        return 20 + Mth.ceil(power * 1.5);
    }

    @Override
    public int getMaximumJumpPower() {
        return 200;
    }

    @Override
    protected void tickDeath() {
        if (this.deathTime == 0) {
            this.animationManager.startAnimation(DEATH_ANIMATION);
        }
        ++this.deathTime;
        if (this.deathTime >= 40 && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (this.isFood(itemstack) && !(this.getControllingPassenger() instanceof HollowborneTurret)) {
            if (!this.isTame()) {
                if (!this.isImmobile()) {
                    if (this.getRandom().nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, pPlayer)) {
                        this.tame(pPlayer);
                        this.level().broadcastEntityEvent(this, TAME_HEARTS_EVENT);
                    } else {
                        this.forceSmash = true;
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                    this.usePlayerItem(pPlayer, pHand, itemstack);
                }
            } else {
                if (this.getHealth() < this.getMaxHealth() - 1E-5) {
                    this.heal(2.0F);
                    this.level().broadcastEntityEvent(this, TAME_HEARTS_EVENT);
                    this.usePlayerItem(pPlayer, pHand, itemstack);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);

        } else if (this.canAddPassenger(pPlayer) && this.isTame() && this.isSaddled()) {
            pPlayer.startRiding(this);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return InteractionResult.PASS;
    }

    @Override
    public void tame(Player pPlayer) {
        super.tame(pPlayer);
        this.enableTamedParticles();
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(BMTags.Items.HOLLOWBORNE_FOOD);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_SADDLED, false);
        this.entityData.define(DATA_SHOULD_SHOW_TAMED_PARTICLES, false);
    }

    public boolean shouldSmashFromPower(int power) {
        return power >= 5 && power <= 23;
    }

    @Override
    public boolean canBeLeashed(Player pPlayer) {
        return false;
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight();
    }

    @Override
    protected float getSoundVolume() {
        return 3.0F;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return BMSoundEvents.HOLLOWBORNE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return BMSoundEvents.HOLLOWBORNE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BMSoundEvents.HOLLOWBORNE_DEATH.get();
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Mob mob) {
            return mob;
        } else if (entity instanceof Player player) {
            return player;
        }
        return null;
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(4, 0, 4);
    }

    @Override
    public float getRotationFreedom() {
        if (this.getControllingPassenger() instanceof HollowborneTurret) {
            return 0.015F;
        }
        return 0.03F;
    }

    private void updateLegDepths() {

        if (!shouldUpdateLegs) {
            for (int i = 0; i < 4; i++) {
                targetDepthsO[i] = 0;
                targetDepths[i] = 0;
            }
            return;
        }

        for (int i = 0; i < 4; i++) {
            targetDepthsO[i] = targetDepths[i];

            Vec3 relPos = new Vec3(legOffsets[i][0], 0, legOffsets[i][1]).yRot(-this.getYRot() * Mth.DEG_TO_RAD);
            double x = this.getX() + relPos.x();
            double y = this.getY();
            double z = this.getZ() + relPos.z();

            int scanRange = 5;
            float foundY = (float) y - 5F;

            for (int j = scanRange; j >= -scanRange; j--) {
                BlockPos checkPos = BlockPos.containing(x, y + j, z);
                BlockState state = this.level().getBlockState(checkPos);
                VoxelShape shape = state.getCollisionShape(this.level(), checkPos);
                if (!shape.isEmpty()) {
                    foundY = (float) (checkPos.getY() + shape.max(Direction.Axis.Y));
                    break;
                }
            }

            float scannedDepth = foundY - (float) y;
            targetDepths[i] = Mth.lerp(0.2F, targetDepths[i], scannedDepth);
        }
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
        int passengerIndex = this.getPassengers().indexOf(pPassenger);
        double passengerY = this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset();
        Vec3 offset;

        int[][] quadrant = {{0, 0}, {1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        if (this.isSaddled()) {
            if (passengerIndex == 0) {
                pCallback.accept(pPassenger, this.getX(), passengerY + 1.6F, this.getZ());
            } else {
                offset = new Vec3(1.3D * quadrant[passengerIndex][0], 0.0D, 1.3D * quadrant[passengerIndex][1]).yRot(-this.yBodyRot * Mth.DEG_TO_RAD);
                pCallback.accept(pPassenger, this.getX() + offset.x(), passengerY + 0.9F, this.getZ() + offset.z());
            }
        } else {
            pCallback.accept(pPassenger, this.getX(), passengerY, this.getZ());
        }
    }

    @Override
    public boolean isSaddleable() {
        return this.isTame();
    }

    @Override
    public boolean canEquipSaddle(ItemStack stack) {
        return stack.getItem() == BMItems.BEHEMOTH_HARNESS.get();
    }

    @Override
    public void equipCustomSaddle(@Nullable SoundSource pSource) {
        this.entityData.set(DATA_IS_SADDLED, true);
        this.playSound(SoundEvents.ARMOR_EQUIP_NETHERITE);
    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(DATA_IS_SADDLED);
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < 5;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.attackManager.getCurrentAction() instanceof HollowborneSmashAction;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
        super.playStepSound(pPos, pState);
        if (switchStep) {
            if (this.level().isClientSide) {
                ScreenShakeHandler.shakeLocal(0.5F, 20, 7.5F);
            }
            this.playSound(BMSoundEvents.HOLLOWBORNE_STEP.get(), 3.0F, 1.1F);
            switchStep = false;
        } else {
            switchStep = true;
        }
    }

    @Override
    public void travel(Vec3 pTravelVec) {
        if (this.isAlive()) {
            if (this.horizontalCollision && this.isInWater()) {
                this.setDeltaMovement(0, 0.3, 0);
            }

            LivingEntity rider = this.getControllingPassenger();
            if (this.isVehicle() && rider instanceof Player) {
                float forward = rider.zza;
                this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.4F);

                if (this.level().isClientSide && this.animationManager.getAnimationState(SMASH_ANIMATION).isStarted()) {
                    forward *= 0.2F;
                }

                super.travel(new Vec3(0, pTravelVec.y, forward));

                return;
            }
        }
        super.travel(pTravelVec);
    }

    @Override
    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);

        float forward = pPlayer.zza;

        float turnSpeed = forward > 0 ? 3.0F : 2.0F;
        float rotationInput = -pPlayer.xxa;

        float newYRot = this.getYRot() + (rotationInput * turnSpeed);
        this.setYRot(newYRot);
        this.yRotO = this.getYRot();
        this.yHeadRot = this.getYRot();
        this.yBodyRot = this.getYRot();
        this.setRot(this.getYRot(), this.getXRot());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Saddled", this.isSaddled());
        pCompound.putBoolean("ShouldShowTamed", this.entityData.get(DATA_SHOULD_SHOW_TAMED_PARTICLES));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.entityData.set(DATA_IS_SADDLED, pCompound.getBoolean("Saddled"));
        this.entityData.set(DATA_SHOULD_SHOW_TAMED_PARTICLES, pCompound.getBoolean("ShouldShowTamed"));
    }

    public boolean shouldShowTamedParticles() {
        return this.entityData.get(DATA_SHOULD_SHOW_TAMED_PARTICLES);
    }

    public void enableTamedParticles() {
        this.entityData.set(DATA_SHOULD_SHOW_TAMED_PARTICLES, true);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(BMItems.BEHEMOTH_HARNESS.get());
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public Vec3 getFluidFallingAdjustedMovement(double gravity, boolean isFalling, Vec3 delta) {
        double waterHeight = this.getFluidHeight(FluidTags.WATER);
        double threshold = this.getBbHeight() * 0.75D;
        if (this.isInWater() && waterHeight > threshold) {
            return new Vec3(delta.x, 0.01D, delta.z);
        }
        return super.getFluidFallingAdjustedMovement(gravity, isFalling, delta);
    }
}
