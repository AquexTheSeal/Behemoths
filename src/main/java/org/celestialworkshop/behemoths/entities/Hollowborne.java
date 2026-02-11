package org.celestialworkshop.behemoths.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.client.animations.HollowborneAnimations;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.goals.HollowborneTargetSyncGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.SimpleChaseGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.WanderGoal;
import org.celestialworkshop.behemoths.entities.ai.movecontrols.BMMoveControl;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;
import org.jetbrains.annotations.Nullable;

public class Hollowborne extends Monster implements BMEntity {

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<Hollowborne> attackManager = new ActionManager<>(this);

    public float[][] legOffsets = {{3, 3}, {-3, 3}, {3, -3}, {-3, -3}};
    public float[] targetDepthsO = new float[]{-3f, -3f, -3f, -3f};
    public float[] targetDepths = new float[]{-3f, -3f, -3f, -3f};

    public static final String DEATH_ANIMATION = "death_barrage";

    public Hollowborne(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.setMaxUpStep(3.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 16.0F);
        this.moveControl = new BMMoveControl<>(this);

        this.animationManager.registerAnimation(DEATH_ANIMATION, () -> HollowborneAnimations.DEATH);
    }

    @Override
    public EntityAnimationManager getAnimationManager() {
        return animationManager;
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
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SimpleChaseGoal(this, 1.0D, mob -> mob.getFirstPassenger() instanceof HollowborneTurret));
        this.goalSelector.addGoal(2, new SimpleChaseGoal(this, 1.3D, mob -> !(mob.getFirstPassenger() instanceof HollowborneTurret)));
        this.goalSelector.addGoal(3, new WanderGoal(this, 0.8D));
        this.targetSelector.addGoal(1, new HollowborneTargetSyncGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && !(mob.getFirstPassenger() instanceof HollowborneTurret);
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && !(mob.getFirstPassenger() instanceof HollowborneTurret);
            }
        });
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) {
            updateLegDepths();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.attackManager.tick();
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
    protected InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        if (this.getPassengers().size() < 2) {
            pPlayer.startRiding(this);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight();
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
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

    @Override
    public @Nullable LivingEntity getControllingPassenger() {
        if (this.getFirstPassenger() instanceof Player le) {
            return le;
        } else {
            return null;
        }
    }

    @Override
    public float getRotationFreedom() {
        if (this.getFirstPassenger() instanceof HollowborneTurret) {
            return 0.015F;
        }
        return 0.03F;
    }

    private void updateLegDepths() {

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
        if (passengerIndex >= 0) {
            boolean isFirstPassenger = passengerIndex == 0;
            float forwardOffset = 0.0F;

            if (this.getPassengers().size() > 1) {
                float gap = 0.5F;

                if (isFirstPassenger) {
                    forwardOffset = gap;
                } else {
                    forwardOffset = -gap;
                }
            }

            Vec3 offset = new Vec3(0.0D, 0.0D, forwardOffset).yRot(-this.yBodyRot * Mth.DEG_TO_RAD);
            double passengerY = this.getY() + this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset();

            pCallback.accept(pPassenger, this.getX() + offset.x, passengerY, this.getZ() + offset.z);
        }
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < 2;
    }

    @Override
    public void travel(Vec3 pTravelVec) {
        if (this.isAlive()) {
            LivingEntity rider = this.getControllingPassenger();
            if (this.isVehicle() && rider != null) {
                this.setYRot(rider.getYRot());
                this.yRotO = this.getYRot();
                this.setXRot(rider.getXRot() * 0.5F);
                this.setRot(this.getYRot(), this.getXRot());
                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;

                float forward = rider.zza;
                float strafe = rider.xxa;

                this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.4F);
                super.travel(new Vec3(strafe, pTravelVec.y, forward));

                return;
            }
        }
        super.travel(pTravelVec);
    }

    public boolean isPushedByFluid() {
        return false;
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public boolean isPushable() {
        return false;
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource damageSource) {
        return false;
    }

    protected void checkFallDamage(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }
}
