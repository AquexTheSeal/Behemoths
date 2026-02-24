package org.celestialworkshop.behemoths.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumVotingSystem;
import org.celestialworkshop.behemoths.client.animations.ArchzombieAnimations;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.action.ArchzombieRidingSweepAttackAction;
import org.celestialworkshop.behemoths.entities.ai.action.ArchzombieRightSweep0Action;
import org.celestialworkshop.behemoths.entities.ai.action.ArchzombieRightSweep1Action;
import org.celestialworkshop.behemoths.entities.ai.goals.ArchzombieJumpGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.ArchzombieOrbitGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.SimpleChaseGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.WanderGoal;
import org.celestialworkshop.behemoths.entities.ai.controls.move.BMMoveControl;
import org.celestialworkshop.behemoths.misc.utils.EntityUtils;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMItems;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Archzombie extends Monster implements BMEntity {

    public static final EntityDataAccessor<Boolean> IS_LEADER = SynchedEntityData.defineId(Archzombie.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> IS_BLOCKING = SynchedEntityData.defineId(Archzombie.class, EntityDataSerializers.BOOLEAN);

    public static final String IDLE_ANIMATION = "idle";
    public static final String RIDING_SWEEP_ANIMATION = "riding_sweep";
    public static final String SWEEP_0_ANIMATION = "sweep_right_0";
    public static final String SWEEP_1_ANIMATION = "sweep_right_1";

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<Archzombie> attackManager = new ActionManager<>(this);

    public Archzombie(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.setMaxUpStep(1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 16.0F);
        this.moveControl = new BMMoveControl<>(this);

        this.animationManager.registerAnimation(IDLE_ANIMATION, () -> ArchzombieAnimations.IDLE);
        this.animationManager.registerAnimation(RIDING_SWEEP_ANIMATION, () -> ArchzombieAnimations.RIDING_SWEEP);
        this.animationManager.registerAnimation(SWEEP_0_ANIMATION, () -> ArchzombieAnimations.SWEEP_RIGHT_0);
        this.animationManager.registerAnimation(SWEEP_1_ANIMATION, () -> ArchzombieAnimations.SWEEP_RIGHT_1);

        this.attackManager.addAction(new ArchzombieRightSweep0Action(this));
        this.attackManager.addAction(new ArchzombieRightSweep1Action(this));
        this.attackManager.addAction(new ArchzombieRidingSweepAttackAction(this));
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
                .add(Attributes.MAX_HEALTH, 70.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.ARMOR, 7.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ArchzombieJumpGoal(this));
        this.goalSelector.addGoal(1, new ArchzombieOrbitGoal(this));
        this.goalSelector.addGoal(2, new SimpleChaseGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new WanderGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers(Archzombie.class, BanishingStampede.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (!this.level().isClientSide) {
            if (this.getVehicle() != null) {
                this.setYRot(this.getVehicle().getYRot());
                this.yBodyRot = this.getVehicle().getYRot();
            }
        } else {
            this.manageIdleAnimations();
        }
    }

    public void manageIdleAnimations() {
        AnimationState idle = this.getAnimationManager().getAnimationState(IDLE_ANIMATION);
        if (this.shouldSit()) {
            if (idle.isStarted()) idle.stop();
        } else {
            idle.startIfStopped(this.tickCount);
        }
    }

    public boolean shouldSit() {
        return this.getVehicle() != null && this.getVehicle().shouldRiderSit();
    }

    @Override
    public float getRotationFreedom() {
        return shouldSit() ? 0.15F : 0.5F;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_LEADER, false);
        this.entityData.define(IS_BLOCKING, false);

    }

    @SuppressWarnings("deprecation")
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {

        boolean leaderEnabled = PandemoniumVotingSystem.hasPandemoniumCurse(level(), BMPandemoniumCurses.ARCHZOMBIE_DOMINION);
        float leaderChance = 0.12F;

        if (reason != MobSpawnType.REINFORCEMENT && random.nextFloat() < leaderChance && leaderEnabled) {
            setupAsLeader(level, difficulty, reason);
        } else {
            ItemStack weapon = random.nextFloat() < 0.7F ? new ItemStack(Items.IRON_SWORD) : new ItemStack(Items.IRON_AXE);
            this.setItemSlot(EquipmentSlot.MAINHAND, weapon);

            float mountChance = PandemoniumVotingSystem.hasPandemoniumCurse(level(), BMPandemoniumCurses.PHANTOM_STEED) ? 0.25F : 0.05F;
            if (random.nextFloat() < mountChance) {
                spawnStampedeAndRideOnIt(level, difficulty);
            }
        }

        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    public void setupAsLeader(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason) {
        this.setLeader(true);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BMItems.MAGNALYTH_AXE.get()));
        this.multiplyAttribute(Attributes.MAX_HEALTH, 2.0F);
        this.heal((float) this.getAttributeValue(Attributes.MAX_HEALTH) - this.getHealth());
        this.multiplyAttribute(Attributes.ATTACK_DAMAGE, 1.2F);
        this.multiplyAttribute(Attributes.ARMOR, 1.4F);

        this.spawnStampedeAndRideOnIt(pLevel, pDifficulty);
        this.spawnReinforcementArchzombiesAround(pLevel, pDifficulty);
    }

    public void multiplyAttribute(Attribute attribute, float multiplier) {
        this.getAttribute(attribute).setBaseValue(this.getAttribute(attribute).getValue() * multiplier);
    }

    private void spawnReinforcementArchzombiesAround(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty) {
        if (!this.level().isClientSide) {
            for (int i = 0; i < (2 + random.nextInt(3)); i++) {
                Archzombie archzombie = BMEntityTypes.ARCHZOMBIE.get().create(this.level());
                archzombie.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                archzombie.finalizeSpawn(pLevel, pDifficulty, MobSpawnType.REINFORCEMENT, null, null);
                this.level().addFreshEntity(archzombie);
            }
        }
    }

    private void spawnStampedeAndRideOnIt(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty) {
        if (!this.level().isClientSide) {
            BanishingStampede stampede = BMEntityTypes.BANISHING_STAMPEDE.get().create(this.level());

            if (stampede != null) {
                stampede.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                stampede.finalizeSpawn(pLevel, pDifficulty, MobSpawnType.JOCKEY, null, null);
                this.level().addFreshEntity(stampede);
                this.startRiding(stampede);
            }
        }
    }

    public static boolean checkArchzombieSpawnRules(EntityType<? extends Monster> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return EntityUtils.doesAreaFitEntity(pLevel, pPos, pType, BMEntityTypes.BANISHING_STAMPEDE.get()) && Monster.checkMonsterSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return BMSoundEvents.ARCHZOMBIE_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource pDamageSource) {
        return BMSoundEvents.ARCHZOMBIE_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return BMSoundEvents.ARCHZOMBIE_DEATH.get();
    }

    @Override
    public boolean isLeftHanded() {
        return false;
    }

    @Override
    public void setLeftHanded(boolean pLeftHanded) {
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("IsLeader", this.isLeader());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setLeader(pCompound.getBoolean("IsLeader"));
    }

    public boolean isBlocking() {
        return this.entityData.get(IS_BLOCKING);
    }

    public void setBlocking(boolean value) {
        this.entityData.set(IS_BLOCKING, value);
    }

    public boolean isLeader() {
        return this.entityData.get(IS_LEADER);
    }

    public void setLeader(boolean value) {
        this.entityData.set(IS_LEADER, value);
    }
}
