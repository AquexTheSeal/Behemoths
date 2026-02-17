package org.celestialworkshop.behemoths.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.client.animations.BanishingStampedeAnimations;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.CustomSaddleable;
import org.celestialworkshop.behemoths.entities.ai.action.StampedeRamAction;
import org.celestialworkshop.behemoths.entities.ai.goals.StampedeArchzombieRamGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.StampedeMovementGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.StampedeRunAroundGoal;
import org.celestialworkshop.behemoths.entities.ai.mount.CustomJumpingMount;
import org.celestialworkshop.behemoths.entities.ai.mount.MountJumpManager;
import org.celestialworkshop.behemoths.entities.ai.movecontrols.BMMoveControl;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMItems;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BanishingStampede extends Horse implements BMEntity, Enemy, CustomSaddleable, CustomJumpingMount<BanishingStampede> {

    private static final EntityDataAccessor<Boolean> DATA_IS_SADDLED = SynchedEntityData.defineId(BanishingStampede.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_IS_RAMMING = SynchedEntityData.defineId(BanishingStampede.class, EntityDataSerializers.BOOLEAN);

    public static final String IDLE_ANIMATION = "idle";
    public static final String RAMMING_ANIMATION = "ram";
    public static final String THROW_RIDER_ANIMATION = "throw_rider";

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<BanishingStampede> attackManager = new ActionManager<>(this);
    public final MountJumpManager<BanishingStampede> mountJumpManager = new MountJumpManager<>(this);

    public BanishingStampede(EntityType<? extends Horse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.setMaxUpStep(2.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 16.0F);
        this.moveControl = new BMMoveControl<>(this);

        this.animationManager.registerAnimation(IDLE_ANIMATION, () -> BanishingStampedeAnimations.IDLE);
        this.animationManager.registerAnimation(RAMMING_ANIMATION, () -> BanishingStampedeAnimations.RAM);
        this.animationManager.registerAnimation(THROW_RIDER_ANIMATION, () -> BanishingStampedeAnimations.THROW_RIDER);

        this.attackManager.addAction(new StampedeRamAction(this));
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return this.animationManager;
    }

    @Override
    public List<ActionManager> getActionManagers() {
        return List.of(attackManager);
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.JUMP_STRENGTH, 0.8D)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 1.5D)
                .build();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) {
            this.manageIdleAnimations();
            this.emitAura();
        }
    }

    public void manageIdleAnimations() {
        this.getAnimationManager().getAnimationState(IDLE_ANIMATION).startIfStopped(this.tickCount + random.nextInt(100));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new StampedeRunAroundGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new StampedeArchzombieRamGoal(this));
        this.goalSelector.addGoal(3, new StampedeMovementGoal(this));
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return BMSoundEvents.STAMPEDE_DEATH.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return BMSoundEvents.STAMPEDE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return BMSoundEvents.STAMPEDE_AMBIENT.get();
    }

    public boolean isWithinRamThreshold(int power) {
        return power >= 60;
    }

    @Override
    public void performJump(int power) {

        float jumpScale = (Math.min(power, 40) / 100.0F + 0.2F) * 2;

        if (isWithinRamThreshold(power)) {
            if (!level().isClientSide) {
                this.setRamming(true);
            }
        } else {
            if (level().isClientSide) {
                double verticalJump = this.getCustomJump() * jumpScale * this.getBlockJumpFactor();
                double finalJump = verticalJump + this.getJumpBoostPower();

                Vec3 movement = this.getDeltaMovement();
                this.setDeltaMovement(movement.x, finalJump, movement.z);

                Vec3 horizontalBoost = this.getLookAngle().multiply(0.4F + jumpScale, 0, 0.4F + jumpScale);
                this.setDeltaMovement(this.getDeltaMovement().add(horizontalBoost));
            }
        }
    }

    @Override
    public boolean getJumpCondition() {
        return this.onGround() && this.isSaddled();
    }

    @Override
    public int getJumpCooldown(int power) {
        return isWithinRamThreshold(power) ? 200 : 40;
    }

    public void emitAura() {
        Vec3 delta = this.getDeltaMovement().scale(3);
        double width = this.getBbWidth() * 2;
        double xx = delta.x() + this.getX() + this.random.nextDouble() * width - width * 0.5D;
        double yy = this.getY() + this.random.nextDouble() * this.getBbHeight();
        double zz = delta.z() + this.getZ() + this.random.nextDouble() * width - width * 0.5D;

        double xr = this.random.nextGaussian() * 0.01F;
        double yr = this.random.nextGaussian() * 0.01F;
        double zr = this.random.nextGaussian() * 0.01F;

        VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("stampede_aura"))
                .type(VFXTypes.FLAT_LOOK)
                .fadeOut()
                .lifetime(20 + random.nextInt(5))
                .scale(0.3F + random.nextFloat() * 0.3F)
                .zRot(random.nextInt(180));
        this.level().addParticle(data.build(), xx, yy, zz, xr, yr, zr);
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
        if (this.hasPassenger(pPassenger)) {
            float f = Mth.sin(this.yBodyRot * ((float)Math.PI / 180F));
            float f1 = -Mth.cos(this.yBodyRot * ((float)Math.PI / 180F));
            double d0 = this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset();
            pCallback.accept(pPassenger, this.getX() + f * 0.5F, d0, this.getZ() + f1 * 0.5F);
        }
    }

    @Override
    protected void randomizeAttributes(RandomSource pRandom) {
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.getItem() == Items.BONE || pStack.getItem() == Items.BONE_BLOCK || pStack.getItem() == Items.BONE_MEAL;
    }

    @Override
    public boolean canMate(Animal pOtherAnimal) {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public boolean canWearArmor() {
        return false;
    }

    @Override
    protected int calculateFallDamage(float pDistance, float pDamageMultiplier) {
        return Mth.ceil((pDistance * 0.35F - 7.0F) * pDamageMultiplier);
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
        super.checkFallDamage(pY, pOnGround, pState, pPos);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_RAMMING, false);
        this.entityData.define(DATA_IS_SADDLED, false);
    }

    public boolean isRamming() {
        return this.entityData.get(DATA_IS_RAMMING);
    }

    public void setRamming(boolean isRamming) {
        this.entityData.set(DATA_IS_RAMMING, isRamming);
    }

    @Override
    public float getRotationFreedom() {
        return 0.075F;
    }

    @Override
    public MountJumpManager<BanishingStampede> getMountJumpManager() {
        return mountJumpManager;
    }

    @Override
    public void onPlayerJump(int pJumpPower) {
    }

    @Override
    public boolean canJump() {
        return false;
    }

    @Override
    public void handleStartJump(int pJumpPower) {
    }

    @Override
    public void handleStopJump() {
    }

    @Override
    public boolean canEquipSaddle(ItemStack stack) {
        return stack.getItem() == BMItems.BEHEMOTH_SADDLE.get();
    }

    @Override
    public void equipCustomSaddle(@Nullable SoundSource pSource) {
        this.inventory.setItem(0, new ItemStack(BMItems.BEHEMOTH_SADDLE.get()));
    }

    public void equipSaddle(@Nullable SoundSource pSource) {
    }
}