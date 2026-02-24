package org.celestialworkshop.behemoths.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.PartEntity;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.client.animations.SkyCharydbisAnimations;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.CustomSaddleable;
import org.celestialworkshop.behemoths.entities.ai.controls.look.NoXRotResetLookControl;
import org.celestialworkshop.behemoths.entities.ai.controls.move.FlyingLookBasedMoveControl;
import org.celestialworkshop.behemoths.entities.ai.controls.pathnav.TrueFlightPathNavigation;
import org.celestialworkshop.behemoths.entities.ai.goals.SkyCharydbisStationaryGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.SkyCharydbisSurroundGoal;
import org.celestialworkshop.behemoths.entities.ai.mount.CustomJumpingMount;
import org.celestialworkshop.behemoths.entities.ai.mount.MountJumpManager;
import org.celestialworkshop.behemoths.entities.misc.BehemothMultipart;
import org.celestialworkshop.behemoths.registries.BMItems;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SkyCharydbis extends TamableAnimal implements BMEntity, Enemy, CustomSaddleable, CustomJumpingMount<SkyCharydbis> {

    private static final EntityDataAccessor<Byte> DATA_SLEEP_FLAG = SynchedEntityData.defineId(SkyCharydbis.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_IS_SADDLED = SynchedEntityData.defineId(SkyCharydbis.class, EntityDataSerializers.BOOLEAN);

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<SkyCharydbis> attackManager = new ActionManager<>(this);
    public final MountJumpManager<SkyCharydbis> mountJumpManager = new MountJumpManager<>(this);

    public final BehemothMultipart<SkyCharydbis>[] subEntities;
    public final BehemothMultipart<SkyCharydbis> head, tail, tailMid, tailTip, leftWing, rightWing, leftWingTip, rightWingTip;

    public static final String AWAKEN_ANIMATION = "awaken";
    public static final byte SLEEPING_FLAG = 2;
    public static final byte WAKING_FLAG = 1;
    public static final byte AWAKE_FLAG = 0;

    public @Nullable BlockPos spawnPos;
    public int wakingTicks = 0;

    public float tailYRot = 0;
    public float tailYRotO = 0;
    public float tailXRot = 0;
    public float tailXRotO = 0;

    public SkyCharydbis(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setMaxUpStep(2.0F);
        this.setNoGravity(true);
        this.setPathfindingMalus(BlockPathTypes.WATER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 16.0F);
        this.moveControl = new FlyingLookBasedMoveControl(this);
        this.navigation = new TrueFlightPathNavigation(this, pLevel);
        this.lookControl = new NoXRotResetLookControl(this);
        this.noCulling = true;

        this.animationManager.registerAnimation(AWAKEN_ANIMATION, () -> SkyCharydbisAnimations.AWAKEN);

        head = new BehemothMultipart<>(this, "head", 1.5F, 1.5F);
        tail = new BehemothMultipart<>(this, "tail", 2, 2);
        tailMid = new BehemothMultipart<>(this, "tail", 1.5F, 1.5F);
        tailTip = new BehemothMultipart<>(this, "tail", 1.5F, 1.5F);
        leftWing = new BehemothMultipart<>(this, "wing", 3.5F, 1.5F);
        rightWing = new BehemothMultipart<>(this, "wing", 3.5F, 1.5F);
        leftWingTip = new BehemothMultipart<>(this, "wing", 4, 1.5F);
        rightWingTip = new BehemothMultipart<>(this, "wing", 4, 1.5F);
        subEntities = new BehemothMultipart[]{head, tail, tailMid, tailTip, leftWing, rightWing, leftWingTip, rightWingTip};
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
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.FLYING_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 20.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 1.5D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 80.0D)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SkyCharydbisSurroundGoal(this));
        this.goalSelector.addGoal(2, new SkyCharydbisStationaryGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void tick() {
        this.tailYRotO = this.tailYRot;
        this.tailYRot = Mth.rotLerp(0.15F, this.tailYRot, this.getYRot());

        this.tailXRotO = this.tailXRot;
        this.tailXRot = Mth.rotLerp(0.2F, this.tailXRot, this.getXRot());

        super.tick();
        this.tickMultipartPositions();

        if (!this.level().isClientSide) {
            if (this.isCurrentSleepFlag(SLEEPING_FLAG)) {
                if (this.level().isNight() && this.getTarget() != null) {
                    this.wakeUp();
                }
            } else if (this.isCurrentSleepFlag(WAKING_FLAG)) {
                this.wakingTicks++;
                if (this.wakingTicks == 30) {
                    this.setDeltaMovement(0, 1.0F, 0);
                    this.hurtMarked = true;
                }

                if (this.wakingTicks >= 60) {
                    this.setSleepFlag(AWAKE_FLAG);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (this.isCurrentSleepFlag(SLEEPING_FLAG)) {
            this.wakeUp();
        } else if (this.isCurrentSleepFlag(WAKING_FLAG)) {
            return false;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setSleepFlag(SLEEPING_FLAG);
        this.spawnPos = this.blockPosition();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
        pPlayer.startRiding(this);
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return super.getBoundingBoxForCulling().inflate(4, 0, 4);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }

    @Override
    public boolean isSaddleable() {
        return this.isTame();
    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(DATA_IS_SADDLED);
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_SADDLED, false);
        this.entityData.define(DATA_SLEEP_FLAG, AWAKE_FLAG);
    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Player player) {
            return player;
        }
        return null;
    }

    private void tickMultipartPositions() {
        float pitchOffset = -Mth.sin(this.getXRot() * Mth.DEG_TO_RAD);

        float headExpansion = 2F;
        this.movePart(head, getLookAngle().x() * headExpansion, pitchOffset * headExpansion * 2, getLookAngle().z() * headExpansion);
        this.movePart(tail, -getLookAngle().x() * headExpansion, pitchOffset * -headExpansion, -getLookAngle().z() * headExpansion);
        this.movePart(tailMid, -getLookAngle().x() * headExpansion * 2, pitchOffset * -headExpansion * 2, -getLookAngle().z() * headExpansion * 2);
        this.movePart(tailTip, -getLookAngle().x() * headExpansion * 4, pitchOffset * -headExpansion * 4, -getLookAngle().z() * headExpansion * 3);

        float yHelper = this.getYRot() * ((float)Math.PI / 180F);
        float zR = -Mth.sin(yHelper);
        float xR = -Mth.cos(yHelper);
        float wingExpansion = 3F;
        this.movePart(leftWing, xR * wingExpansion, 0, zR * wingExpansion);
        this.movePart(leftWingTip, xR * wingExpansion * 2, 0, zR * wingExpansion * 2);
        this.movePart(rightWing, -xR * wingExpansion, 0, -zR * wingExpansion);
        this.movePart(rightWingTip, -xR * wingExpansion * 2, 0, -zR * wingExpansion * 2);
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

    private void movePart(BehemothMultipart<SkyCharydbis> pPart, double offsetX, double offsetY, double offsetZ) {
        pPart.moveTo(getX() + offsetX, getY() + offsetY, getZ() + offsetZ);
    }

    @Override
    public void travel(Vec3 pTravelVec) {
        if (this.isAlive()) {
            LivingEntity rider = this.getControllingPassenger();
            if (this.isVehicle() && rider instanceof Player) {
                float forward = rider.zza;
                float speed = (float) this.getAttributeValue(Attributes.FLYING_SPEED) * 1.3F;
                if (this.onGround()) {
                    speed = Math.min(speed, 0.15F);
                }
                this.setSpeed(speed);

                float up = (float) this.getLookAngle().y() * getSpeed();
                if (forward == 0) {
                    up = 0;
                }

                super.travel(new Vec3(0, up, forward + 0.05));

                return;
            }
        }

        super.travel(pTravelVec);
    }

    @Override
    protected void tickRidden(Player pPlayer, Vec3 pTravelVector) {
        super.tickRidden(pPlayer, pTravelVector);
        this.setYRot(Mth.rotLerp(0.2F, this.getYRot(), pPlayer.getYRot()));
        this.setXRot(Mth.clamp(Mth.rotLerp(0.2F, this.getXRot(), pPlayer.getXRot()), -45, 45));
        this.setRot(this.getYRot(), this.getXRot());
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction pCallback) {
        if (this.hasPassenger(pPassenger)) {
            double yOff = this.getPassengersRidingOffset() + pPassenger.getMyRidingOffset() + 0.5;
            int passengerIndex = this.getPassengers().indexOf(pPassenger);

            double xR = 0, zR = 0;
            if (passengerIndex == 0) {
                zR = 1;
            } else {
                int gridIndex = passengerIndex - 1;
                int col = gridIndex % 2;
                int row = gridIndex / 2;
                xR = (col == 0 ? -0.5 : 0.5);
                zR = -row;
            }

            Vec3 offsetPos = new Vec3(xR, yOff, zR)
                    .xRot(-this.getXRot() * Mth.DEG_TO_RAD)
                    .yRot(-this.getYRot() * Mth.DEG_TO_RAD);
            pCallback.accept(pPassenger, this.getX() + offsetPos.x(), this.getY() + offsetPos.y(), this.getZ() + offsetPos.z());
        }
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isSleeping() || !this.isCurrentSleepFlag(AWAKE_FLAG);
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < 7;
    }

    @Override
    public MountJumpManager<SkyCharydbis> getMountJumpManager() {
        return this.mountJumpManager;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("HasSpawnPos", this.spawnPos != null);
        if (this.spawnPos != null) {
            pCompound.put("SpawnPos", NbtUtils.writeBlockPos(this.spawnPos));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.getBoolean("HasSpawnPos")) {
            this.spawnPos = NbtUtils.readBlockPos(pCompound.getCompound("SpawnPos"));
        }
    }

    @Override
    public void performJump(int power) {
    }

    @Override
    public boolean getJumpCondition() {
        return false;
    }

    @Override
    public int getJumpCooldown(int power) {
        return 200;
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    public boolean fireImmune() {
        return true;
    }

    public void wakeUp() {
        if (this.isCurrentSleepFlag(SLEEPING_FLAG)) {
            this.animationManager.startAnimation(AWAKEN_ANIMATION);
            this.wakingTicks = 0;
            this.setSleepFlag(WAKING_FLAG);
        }
    }

    @Override
    public boolean isSleeping() {
        return this.isCurrentSleepFlag(SLEEPING_FLAG);
    }

    public boolean isCurrentSleepFlag(byte flag) {
        return this.getSleepFlag() == flag;
    }

    public byte getSleepFlag() {
        return this.entityData.get(DATA_SLEEP_FLAG);
    }

    public void setSleepFlag(byte flag) {
        this.entityData.set(DATA_SLEEP_FLAG, flag);
    }
}
