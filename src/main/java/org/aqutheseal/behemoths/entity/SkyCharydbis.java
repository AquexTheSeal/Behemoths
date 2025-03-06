package org.aqutheseal.behemoths.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NonTameRandomTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.aqutheseal.behemoths.entity.ai.goal.BMFlyAroundGoal;
import org.aqutheseal.behemoths.entity.ai.goal.BMFlyToTargetGoal;
import org.aqutheseal.behemoths.entity.ai.goal.SkyCharydbisDiveDownGoal;
import org.aqutheseal.behemoths.entity.ai.lookcontrol.SmoothFlyingLookControl;
import org.aqutheseal.behemoths.entity.ai.movecontrol.BMFlyingControl;
import org.aqutheseal.behemoths.entity.misc.BHPartEntity;
import org.aqutheseal.behemoths.entity.misc.ISizableEntity;
import org.aqutheseal.behemoths.entity.variants.SkyCharydbisVariants;
import org.aqutheseal.behemoths.util.UtilLevel;
import org.jetbrains.annotations.Nullable;

public class SkyCharydbis extends TamableAnimal implements Enemy, ISizableEntity {
    private static final EntityDataAccessor<Float> SIZE = SynchedEntityData.defineId(SkyCharydbis.class, EntityDataSerializers.FLOAT);
    /**
     * Attack States:
     * @0 = None
     * @1 = Dive to ground
     * @2 = Laser beam
     */
    private static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(SkyCharydbis.class, EntityDataSerializers.INT);

    public final BHPartEntity<SkyCharydbis>[] subEntities;
    public final BHPartEntity<SkyCharydbis> head, tail, tailMid, tailTip, leftWing, rightWing, leftWingTip, rightWingTip;

    public SkyCharydbisVariants variant;
    public Vec3 homePosition;
    public double smashModeTimer;

    public float rollY;
    public float rollYO;
    public float rollX;
    public float rollXO;

    public SkyCharydbis(EntityType<? extends TamableAnimal> entityType, Level level, SkyCharydbisVariants variant) {
        super(entityType, level);
        this.variant = variant;
        this.navigation = this.createNavigation(level);
        this.moveControl = new BMFlyingControl(this);
        this.lookControl = new SmoothFlyingLookControl(this, 0);
        this.noCulling = true;

        head = new BHPartEntity<>(this, "head", 1.5F, 1.5F);
        tail = new BHPartEntity<>(this, "tail", 2, 2);
        tailMid = new BHPartEntity<>(this, "tail", 1.5F, 1.5F);
        tailTip = new BHPartEntity<>(this, "tail", 1.5F, 1.5F);
        leftWing = new BHPartEntity<>(this, "wing", 3.5F, 1.5F);
        rightWing = new BHPartEntity<>(this, "wing", 3.5F, 1.5F);
        leftWingTip = new BHPartEntity<>(this, "wing", 4, 1.5F);
        rightWingTip = new BHPartEntity<>(this, "wing", 4, 1.5F);
        subEntities = new BHPartEntity[]{head, tail, tailMid, tailTip, leftWing, rightWing, leftWingTip, rightWingTip};

        this.setSizeMultiplier(1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 200.0D)
                .add(Attributes.ARMOR, 20.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.FLYING_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 15.0D)
                .add(Attributes.FOLLOW_RANGE, 128.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new BMFlyToTargetGoal(this));
        this.goalSelector.addGoal(2, new BMFlyAroundGoal(this));
        this.goalSelector.addGoal(3, new SkyCharydbisDiveDownGoal(this));
        this.targetSelector.addGoal(1, new NonTameRandomTargetGoal<>(this, LivingEntity.class, false, e -> !(e instanceof SkyCharydbis)) {
            @Override
            protected AABB getTargetSearchArea(double pTargetDistance) {
                return this.mob.getBoundingBox().inflate(pTargetDistance, pTargetDistance, pTargetDistance);
            }
        });
    }

    @Override
    public void tick() {
        super.tick();

        this.rollYO = this.rollY;
        this.rollY = this.yBodyRotO - this.yBodyRot;

        this.rollXO = this.rollX;
        this.rollX = this.xRotO - this.getXRot();

        UtilLevel.shakeScreensOnSurroundings(level(), position(), 32, 0.5F, player -> this.getPassengers().contains(player));

        UtilLevel.loopThroughLivingArea(this, head.getBoundingBox(), target -> {
            if (TargetingConditions.forCombat().test(this, target)) {
                target.hurt(this.damageSources().mobAttack(this), 20F);
            }
        });

        if (this.getTarget() != null) {
            if (smashModeTimer > 200) {
                this.setAttackState(1);
                this.smashModeTimer = -random.nextInt(200);
            } else {
                this.smashModeTimer++;
            }
        } else {
            this.smashModeTimer = 0;
        }

        this.tickMultipartPositions();
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        this.xRotO = this.getXRot();
        this.setXRot(Mth.lerp((float) this.deathTime / 60, 0, 90));

        if (!this.level().isClientSide() && !this.isRemoved()) {
            if ((this.deathTime >= 200) || this.onGround()) {
                this.level().explode(null, this.getX(), this.getY(), this.getZ(), 5.0F, Level.ExplosionInteraction.NONE);
                this.level().broadcastEntityEvent(this, (byte) 60);
                this.remove(Entity.RemovalReason.KILLED);
            } else {
                Vec3 dir = this.getLookAngle().multiply(0.025, 0, 0.025).add(0, -0.0025 * this.deathTime, 0);
                this.hasImpulse = true;
                this.setDeltaMovement(this.getDeltaMovement().add(dir));
            }
        }
    }

    private void tickMultipartPositions() {
        float pitchOffset = 0;
        float mobSize = this.getSizeMultiplier();

        float headExpansion = mobSize * 1.5F;
        this.movePart(head, getLookAngle().x() * headExpansion, mobSize + pitchOffset * headExpansion, getLookAngle().z() * headExpansion);
        this.movePart(tail, -getLookAngle().x() * headExpansion, mobSize + pitchOffset * -headExpansion, -getLookAngle().z() * headExpansion);
        this.movePart(tailMid, -getLookAngle().x() * headExpansion *2, mobSize + pitchOffset * -headExpansion *2, -getLookAngle().z() * headExpansion *2);
        this.movePart(tailTip, -getLookAngle().x() * headExpansion *4, mobSize + pitchOffset * -headExpansion *4, -getLookAngle().z() * headExpansion *3);

        float yHelper = this.getYRot() * ((float)Math.PI / 180F);
        float zR = -Mth.sin(yHelper);
        float xR = -Mth.cos(yHelper);
        float wingExpansion = mobSize * 3F;
        this.movePart(leftWing, xR * wingExpansion, mobSize, zR * wingExpansion);
        this.movePart(leftWingTip, xR * wingExpansion *2, mobSize, zR * wingExpansion *2);
        this.movePart(rightWing, -xR * wingExpansion, mobSize, -zR * wingExpansion);
        this.movePart(rightWingTip, -xR * wingExpansion *2, mobSize, -zR * wingExpansion *2);

        Vec3[] subVec = new Vec3[subEntities.length];
        for (int i = 0; i < subEntities.length; ++i) {
            subVec[i] = new Vec3(subEntities[i].getX(), subEntities[i].getY(), subEntities[i].getZ());
        }
        for (int i = 0; i < subEntities.length; ++i) {
            subEntities[i].xo = subVec[i].x;
            subEntities[i].yo = subVec[i].y;
            subEntities[i].zo = subVec[i].z;
            subEntities[i].xOld = subVec[i].x;
            subEntities[i].yOld = subVec[i].y;
            subEntities[i].zOld = subVec[i].z;
        }
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation nav = new FlyingPathNavigation(this, pLevel);
        nav.setCanPassDoors(true);
        nav.setCanFloat(true);
        return nav;
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        return EntityDimensions.scalable(3.0F, 2.5F).scale(this.getSizeMultiplier());
    }

    @Override
    public InteractionResult interactAt(Player pPlayer, Vec3 pVec, InteractionHand pHand) {
        if (pPlayer.getMainHandItem().getItem() == Items.DRAGON_EGG) {
            this.tame(pPlayer);
            pPlayer.startRiding(this);
        }
        return super.interactAt(pPlayer, pVec, pHand);
    }

    @Override
    public boolean shouldRender(double pX, double pY, double pZ) {
        return true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double pDistance) {
        return true;
    }

    private void movePart(BHPartEntity<SkyCharydbis> pPart, double offsetX, double offsetY, double offsetZ) {
        pPart.setPos(getX() + offsetX, getY() + offsetY, getZ() + offsetZ);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
        double allowance = this.getBbHeight() - (this.getBbHeight() * 0.2);

        double d0 = this.getX();
        double d1 = this.getY() + allowance;
        double d2 = this.getZ();

        float verticalRotation = this.getXRot();
        double forwardOffset = -Math.sin(Math.toRadians(verticalRotation)) * Mth.PI * this.getSizeMultiplier();
        double verticalOffset = -Math.toRadians(Math.abs(verticalRotation)) * Mth.HALF_PI * this.getSizeMultiplier();

        d0 += Math.sin(Math.toRadians(this.getYRot())) * forwardOffset;
        d1 += verticalOffset;
        d2 += -Math.cos(Math.toRadians(this.getYRot())) * forwardOffset;

        pCallback.accept(pPassenger, d0, d1, d2);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 1.0F);
        this.entityData.define(ATTACK_STATE, 0);
    }

    @Nullable
    @Override
    public PartEntity<?>[] getParts() {
        return subEntities;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
    }

    @Override
    public float getSizeMultiplier() {
        return this.entityData.get(SIZE);
    }

    @Override
    public void setSizeMultiplier(float value) {
        this.entityData.set(SIZE, Math.max(0, value));
        this.refreshDimensions();

//        //todo: Properly set health-scalable growth.
//        this.getAtt(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(Math.round(this.minimumHealth + (healthStep * age)));

        for (BHPartEntity<SkyCharydbis> subEntity : subEntities) {
            subEntity.refreshDimensions();
        }
    }

    public int getAttackState() {
        return this.entityData.get(ATTACK_STATE);
    }

    public void setAttackState(int value) {
        this.entityData.set(ATTACK_STATE, value);
    }

    @Override
    protected boolean isImmobile() {
        return false;
    }

    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            this.discard();
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    @Override
    public boolean canFallInLove() {
        return super.canFallInLove();
    }
}