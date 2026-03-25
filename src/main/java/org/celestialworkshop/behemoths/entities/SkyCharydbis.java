package org.celestialworkshop.behemoths.entities;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
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
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.camera.CameraAngleManager;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.client.animations.SkyCharydbisAnimations;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.CustomSaddleable;
import org.celestialworkshop.behemoths.entities.ai.action.*;
import org.celestialworkshop.behemoths.entities.ai.controls.look.NoXRotResetLookControl;
import org.celestialworkshop.behemoths.entities.ai.controls.move.FlyingLookBasedMoveControl;
import org.celestialworkshop.behemoths.entities.ai.controls.pathnav.TrueFlightPathNavigation;
import org.celestialworkshop.behemoths.entities.ai.goals.SkyCharydbisStationaryGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.SkyCharydbisSurroundGoal;
import org.celestialworkshop.behemoths.entities.ai.mount.CustomJumpingMount;
import org.celestialworkshop.behemoths.entities.ai.mount.MountJumpManager;
import org.celestialworkshop.behemoths.entities.misc.AutomatedAttackEntity;
import org.celestialworkshop.behemoths.entities.misc.BehemothMultipart;
import org.celestialworkshop.behemoths.entities.misc.PhantashroomGlutton;
import org.celestialworkshop.behemoths.entities.projectile.CharydbisShard;
import org.celestialworkshop.behemoths.misc.utils.BlockUtils;
import org.celestialworkshop.behemoths.particles.TrailParticleData;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMItems;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class SkyCharydbis extends TamableAnimal implements BMEntity, Enemy, CustomSaddleable, CustomJumpingMount<SkyCharydbis> {

    private static final EntityDataAccessor<Byte> DATA_SLEEP_FLAG = SynchedEntityData.defineId(SkyCharydbis.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> DATA_IS_SADDLED = SynchedEntityData.defineId(SkyCharydbis.class, EntityDataSerializers.BOOLEAN);

    public final ServerBossEvent bossEvent = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.NOTCHED_6);

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<SkyCharydbis> attackManager = new ActionManager<>(this);
    public final MountJumpManager<SkyCharydbis> mountJumpManager = new MountJumpManager<>(this);

    public final BehemothMultipart<SkyCharydbis>[] subEntities;
    public final BehemothMultipart<SkyCharydbis> head, tail, tailMid, tailTip, leftWing, rightWing, leftWingTip, rightWingTip;

    public static final String AWAKEN_ANIMATION = "awaken";
    public static final String SHARD_SUMMON_ANIMATION = "shard_summon";
    public static final String SHARD_RELEASE_ANIMATION = "shard_release";

    public static final byte SLEEPING_FLAG = 2;
    public static final byte WAKING_FLAG = 1;
    public static final byte AWAKE_FLAG = 0;

    public @Nullable BlockPos spawnPos;
    public List<BlockPos> islandPositions = new ObjectArrayList<>();
    public Vec3 lastTrackedTargetFloor = Vec3.ZERO;
    public int awakeNoTargetTime = 0;
    public boolean isSubmerged = false;
    public int wakingTicks = 0;
    public int attackCooldown = 0;
    public List<CharydbisShard> heldShards = new ObjectArrayList<>();

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
        this.animationManager.registerAnimation(SHARD_SUMMON_ANIMATION, () -> SkyCharydbisAnimations.ATTACK_SHARDS_START);
        this.animationManager.registerAnimation(SHARD_RELEASE_ANIMATION, () -> SkyCharydbisAnimations.ATTACK_SHARDS_END);

        this.attackManager.addAction(new CharydbisShardSummonAction(this));
        this.attackManager.addAction(new CharydbisShardReleaseAction(this));
        this.attackManager.addAction(new CharydbisSubmergeAction(this));
        this.attackManager.addAction(new CharydbisResurfaceAction(this));
        this.attackManager.addAction(new CharydbisCrashAction(this));

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
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false, e -> e.getY() > this.spawnPos.getY() - 15));
    }

    @Override
    public void tick() {
        if (this.getSleepFlag() == AWAKE_FLAG) {
            if (this.tickCount % 50 == 0) {
                this.playSound(BMSoundEvents.CHARYDBIS_FLAP.get(), 3.0F, 1.0F);
            }

            if (this.tickCount % 30 == 0) {
                this.playSound(BMSoundEvents.CHARYDBIS_AMBIENT.get(), 3.0F, 1.0F);
                CameraAngleManager.shakeArea(level(), this.position(), 64, 0.5F, 100, 70);
            }

            if (this.level().isClientSide && this.tickCount % 20 == 0) {
                TrailParticleData data = new TrailParticleData(this.getId(), 40, 30, 3.0F, 0, 0.4F, 0, 0.5F);
                this.level().addParticle(data, this.getX(), this.getEyeY(), this.getZ(), 0, 0, 0);
            }
        }

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
                } else {
                    this.setDeltaMovement(0, -0.2, 0);
                }
            } else if (this.isCurrentSleepFlag(WAKING_FLAG)) {
                this.wakingTicks++;
                if (this.wakingTicks == 30) {
                    this.setDeltaMovement(0, 1.0F, 0);
                    this.hurtMarked = true;
                    this.populateIslandCenters();
                }

                if (this.wakingTicks > 25 && this.wakingTicks < 35) {
                    this.setXRot(this.getXRot() - 5);
                }

                if (this.wakingTicks >= 60) {
                    this.setSleepFlag(AWAKE_FLAG);
                    this.animationManager.stopAnimation(AWAKEN_ANIMATION);

                    for (ServerPlayer player : ((ServerLevel)this.level()).getPlayers(p -> this.getSensing().hasLineOfSight(p) || this.distanceTo(p) < 512)) {
                        this.addBossBarPlayer(bossEvent, player, 0);
                    }
                }
            }

            if (this.attackCooldown > 0) {
                this.attackCooldown--;
            }
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide && this.getTarget() != null) {
            if (!this.getTarget().position().equals(this.lastTrackedTargetFloor) && this.getTarget().onGround()) {
                this.lastTrackedTargetFloor = this.getTarget().position();
            }

            if (this.getTarget().getY() < this.spawnPos.getY() - 15) {
                this.setTarget(null);
            }

            if (this.isSubmerged) {
                for (int i = 0; i < 8 - (6 * Math.max(0, this.getAttacksIntervalScale())); i++) {
                    this.sendLightningToRandomIsland();
                }
            }
        }

        float healthPercent = this.getHealth() / this.getMaxHealth();
        if (Math.abs(this.bossEvent.getProgress() - healthPercent) > 0.01f) {
            this.bossEvent.setProgress(healthPercent);
        }
    }

    public void goBackToSleep() {
        this.setSleepFlag(SkyCharydbis.SLEEPING_FLAG);
        this.getNavigation().stop();
        for (ServerPlayer player : ((ServerLevel) this.level()).getPlayers(p -> this.getSensing().hasLineOfSight(p) || this.distanceTo(p) < 512)) {
            this.removeBossBarPlayer(bossEvent, player);
        }
        this.wakingTicks = 0;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer pServerPlayer) {
        super.startSeenByPlayer(pServerPlayer);
        if (this.getSleepFlag() == AWAKE_FLAG) {
            this.addBossBarPlayer(bossEvent, pServerPlayer, 0);
        }
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer pServerPlayer) {
        super.stopSeenByPlayer(pServerPlayer);
        this.removeBossBarPlayer(bossEvent, pServerPlayer);
    }

    public void sendLightningToRandomIsland() {
        if (this.level().isClientSide || this.islandPositions.isEmpty() || this.spawnPos == null) return;

        ServerLevel serverLevel = (ServerLevel) this.level();

        Predicate<BlockPos> spawnCondition = pos -> {
            BlockState stateAt = serverLevel.getBlockState(pos);
            boolean isSpaceEmpty = (stateAt.isAir() || !stateAt.canOcclude());
            boolean isFullBlock = BlockUtils.isBlockFull(level(), pos.below());
            return isSpaceEmpty && isFullBlock;
        };

        int xx = -32 + this.random.nextInt(64);
        int yy = -12 + this.random.nextInt(24);
        int zz = -32 + this.random.nextInt(64);
        BlockPos targetPos = this.spawnPos.offset(xx, yy, zz);
        BlockUtils.getRandomValidPosAround(serverLevel, targetPos, 12, 25, spawnCondition).ifPresent(foundPos -> {
            if (!this.level().isClientSide) {
                AutomatedAttackEntity aae = BMEntityTypes.AUTOMATED_ATTACK.get().create(this.level());
                aae.moveTo(foundPos, 0, 0);
                aae.setBehavior(this, 20, new AutomatedAttackEntity.AttackBehavior() {

                    @Override
                    public void onStart(AutomatedAttackEntity entity) {
                        VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("hollowborne_jump"))
                                .type(VFXTypes.FLAT).fadeIn()
                                .scale(10, 0, InterpolationTypes.EASE_OUT_QUAD)
                                .lifetime(20);
                        ((ServerLevel) entity.level()).sendParticles(data.build(), entity.getX(), entity.getY() + 0.2, entity.getZ(), 0, 0, 0, 0, 0);
                    }

                    @Override
                    public void onEnd(AutomatedAttackEntity entity) {
                        if (entity.getOwner() == null) return;

                        VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("charydbis_lightning"))
                                .type(VFXTypes.CHAIN_LOOK)
                                .lifetime(10)
                                .scale(1.5F);
                        ((ServerLevel) entity.level()).sendParticles(data.build(), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0, 0, 0);

                        for (LivingEntity target : entity.level().getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(1, 20, 1))) {
                            if (entity.getOwner().canAttack(target) && target != entity.getOwner()) {
                                target.hurt(entity.damageSources().indirectMagic(entity.getOwner(), entity), 5);
                            }
                        }
                    }
                });
                this.level().addFreshEntity(aae);
            }
        });
    }

    public float getAttacksIntervalScale() {
        float hpScale = 1.0F - (this.getHealth() / this.getMaxHealth());
        return Mth.lerp(hpScale, 1.0F, 0.4F);
    }

    public void populateIslandCenters() {
        if (this.level().isClientSide || this.islandPositions.isEmpty()) return;

        ServerLevel serverLevel = (ServerLevel) this.level();

        Predicate<BlockPos> spawnCondition = pos -> {
            BlockState stateAt = serverLevel.getBlockState(pos);
            BlockState stateAbove = serverLevel.getBlockState(pos.above());
            boolean isSpaceEmpty = (stateAt.isAir() || !stateAt.canOcclude()) && (stateAbove.isAir() || !stateAbove.canOcclude());
            return isSpaceEmpty && BlockUtils.isBlockFull(level(), pos.below());
        };

        Collections.shuffle(this.islandPositions);

        for (BlockPos center : this.islandPositions.subList(0, Mth.ceil(this.islandPositions.size() * 0.4))) {
            BlockUtils.getRandomValidPosAround(serverLevel, center, 10, 15, spawnCondition).ifPresent(foundPos -> {
                PhantashroomGlutton glutton = BMEntityTypes.PHANTASHROOM_GLUTTON.get().create(serverLevel);
                if (glutton != null) {
                    glutton.moveTo(foundPos.getX() + 0.5, foundPos.getY(), foundPos.getZ() + 0.5, 0.0F, 0.0F);
                    glutton.openTimer = -this.random.nextInt(80);
                    glutton.setOwner(this);
                    serverLevel.addFreshEntity(glutton);
                }
            });
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.level().isClientSide) {
            if (this.isCurrentSleepFlag(SLEEPING_FLAG) && this.level().isNight()) {
                this.wakeUp();
            }

            if (!this.isCurrentSleepFlag(AWAKE_FLAG)) {
                return false;
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (!this.onGround()) {
            this.push(0, -0.05, 0);
            this.setXRot(Math.min(90, Mth.rotLerp(0.2F, this.getXRot(), this.getXRot() + 7)));
        }
        if (((this.onGround() && this.deathTime > 20) || this.deathTime > 200) && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setSleepFlag(SLEEPING_FLAG);
        this.spawnPos = this.blockPosition();
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

//    @Override
//    public InteractionResult mobInteract(Player pPlayer, InteractionHand pHand) {
//        pPlayer.startRiding(this);
//        return InteractionResult.sidedSuccess(this.level().isClientSide);
//    }

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
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() < 7;
    }

    @Override
    public void handleEntityEvent(byte pId) {
        super.handleEntityEvent(pId);
        if (pId == 68) {
            for (int i = 0; i < 10; i++) {
                VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("charydbis_explode"))
                        .lifetime(random.nextInt(10) + 10).type(VFXTypes.FLAT_LOOK).scale((random.nextFloat() * 1.5F) + 1.5F);
                int rad = 6;
                Vec3 rPos = this.position().add(random.nextGaussian() * rad, random.nextGaussian() * rad / 2, random.nextGaussian() * rad);
                this.level().addParticle(data.build(), rPos.x(), rPos.y(), rPos.z(), 0, 0, 0);
            }
        }
    }

    @Override
    public MountJumpManager<SkyCharydbis> getMountJumpManager() {
        return this.mountJumpManager;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return BMSoundEvents.CHARYDBIS_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return BMSoundEvents.CHARYDBIS_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 3.0F;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        if (spawnPos != null) {
            tag.put("SpawnPos", NbtUtils.writeBlockPos(spawnPos));
        }

        if (!islandPositions.isEmpty()) {
            ListTag islandPos = new ListTag();
            for (BlockPos pos : islandPositions) {
                islandPos.add(NbtUtils.writeBlockPos(pos));
            }
            tag.put("IslandPositions", islandPos);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);

        if (tag.contains("SpawnPos")) {
            spawnPos = NbtUtils.readBlockPos(tag.getCompound("SpawnPos"));
        } else {
            spawnPos = null;
        }

        if (tag.contains("IslandPositions")) {
            ListTag islandPos = tag.getList("IslandPositions", 10);
            islandPositions.clear();
            for (int i = 0; i < islandPos.size(); i++) {
                islandPositions.add(NbtUtils.readBlockPos(islandPos.getCompound(i)));
            }
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
        return !this.isSleeping();
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
