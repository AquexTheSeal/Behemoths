package org.celestialworkshop.behemoths.entities;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
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
import net.minecraftforge.common.ForgeHooks;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.ActionManager;
import org.celestialworkshop.behemoths.entities.ai.BMCustomJumpableEntity;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.action.StampedeRamAction;
import org.celestialworkshop.behemoths.entities.ai.goals.StampedeArchzombieRamGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.StampedeMovementGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.StampedeRunAroundGoal;
import org.celestialworkshop.behemoths.entities.ai.movecontrols.BMMoveControl;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.shared.EntityActionSharedPacket;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class BanishingStampede extends Horse implements BMEntity, Enemy, BMCustomJumpableEntity {

    private static final EntityDataAccessor<Boolean> DATA_IS_RAMMING = SynchedEntityData.defineId(BanishingStampede.class, EntityDataSerializers.BOOLEAN);

    public static final String IDLE_ANIMATION = "idle";
    public static final String RAMMING_ANIMATION = "ram";
    public static final String THROW_RIDER_ANIMATION = "throw_rider";

    public final Map<String, AnimationState> animationStateMap = new Object2ObjectArrayMap<>();
    public final AnimationState idleAnimationState = this.createAnimationState(IDLE_ANIMATION);
    public final AnimationState rammingAnimationState = this.createAnimationState(RAMMING_ANIMATION);
    public final AnimationState throwRiderAnimationState = this.createAnimationState(THROW_RIDER_ANIMATION);

    public final ActionManager<BanishingStampede> attackManager = new ActionManager<>(this);

    public int jumpTime = 0;

    public BanishingStampede(EntityType<? extends Horse> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.setMaxUpStep(2.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 16.0F);
        this.moveControl = new BMMoveControl<>(this);

        this.attackManager.addAction(
                new StampedeRamAction(this)
        );
    }

    @Override
    public Map<String, AnimationState> getAnimationStateMap() {
        return animationStateMap;
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ARMOR, 4.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.JUMP_STRENGTH, 0.8D)
                .build();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide) {
            this.emitAura();
        } else {
            this.attackManager.tick();
        }

        this.manageIdleAnimations();
    }

    public void manageIdleAnimations() {
        this.idleAnimationState.startIfStopped(this.tickCount + random.nextInt(100));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new StampedeRunAroundGoal(this, 1.2D));
        this.goalSelector.addGoal(2, new StampedeArchzombieRamGoal(this));
//        this.goalSelector.addGoal(2, new RandomStandGoal(this));
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

    public boolean isWithinRamThreshold(float playerJumpPendingScale) {
        return playerJumpPendingScale > 0.6;
    }

    @Override
    public float calculateCustomJumpScale(boolean isJumping) {
        if (this.isRamming()) {
            this.jumpTime = 0;
            return 0.0F;
        }

        if (isJumping) {
            if (this.jumpTime < 100) {
                ++this.jumpTime;
            }
        } else {
            this.jumpTime = 0;
        }
        return Mth.lerp(this.jumpTime / 100.0F, 0.0F, 1.0F);
    }

    @Override
    public void onPlayerJump(int pJumpPower) {
        if (this.isSaddled()) {
            if (pJumpPower < 0) {
                pJumpPower = 0;
            } else {
                this.allowStandSliding = true;
                this.standIfPossible();
            }

            this.playerJumpPendingScale = (float) pJumpPower / 100.0F;
        }
    }

    public void executeRidersJump(float pPlayerJumpPendingScale, Vec3 pTravelVector) {
        if (this.isWithinRamThreshold(pPlayerJumpPendingScale)) {
            BMNetwork.sendToServer(new EntityActionSharedPacket(this.getId(), EntityActionSharedPacket.Action.START_STAMPEDE_RAMMING,
                    Pair.of("backswingTime", (pPlayerJumpPendingScale - 0.5F) * 2)
                    )
            );
        } else {
            pPlayerJumpPendingScale = (Math.min(pPlayerJumpPendingScale, 0.4F) + 0.2F) * 2;
            double calculatedJump = this.getCustomJump() * pPlayerJumpPendingScale * this.getBlockJumpFactor();
            double finalJump = calculatedJump + this.getJumpBoostPower();

            Vec3 movementFactor = this.getDeltaMovement();
            this.setDeltaMovement(movementFactor.x, finalJump, movementFactor.z);

            Vec3 horizontalBoost = this.getLookAngle().multiply(0.4F + pPlayerJumpPendingScale, 0, 0.4F + pPlayerJumpPendingScale);
            this.setDeltaMovement(this.getDeltaMovement().add(horizontalBoost));

            this.setIsJumping(true);
            this.hasImpulse = true;
            ForgeHooks.onLivingJump(this);
        }
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
    }

    public boolean isRamming() {
        return this.entityData.get(DATA_IS_RAMMING);
    }

    public void setRamming(boolean isRamming) {
        this.entityData.set(DATA_IS_RAMMING, isRamming);
    }

    @Override
    public float getRotationFreedom() {
        return 0.1F;
    }
}