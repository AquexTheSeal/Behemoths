package org.celestialworkshop.behemoths.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.celestialworkshop.behemoths.api.client.animation.EntityAnimationManager;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumVotingSystem;
import org.celestialworkshop.behemoths.client.animations.HollowborneTurretAnimations;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.action.HollowborneTurretShootBarrageAction;
import org.celestialworkshop.behemoths.entities.ai.goals.HollowborneTurretRecieveHurtTargetGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.SimpleChaseGoal;
import org.celestialworkshop.behemoths.entities.ai.goals.SimpleDistanceFromGoal;
import org.celestialworkshop.behemoths.misc.utils.EntityUtils;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HollowborneTurret extends PathfinderMob implements BMEntity, Enemy {

    public final EntityAnimationManager animationManager = new EntityAnimationManager(this);
    public final ActionManager<HollowborneTurret> attackManager = new ActionManager<>(this);

    public static final String SHOOT_BARRAGE_ANIMATION = "shoot_barrage";

    public int restTime = 0;

    public HollowborneTurret(EntityType<? extends PathfinderMob> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.navigation = new AmphibiousPathNavigation(this, pLevel);

        this.animationManager.registerAnimation(SHOOT_BARRAGE_ANIMATION, () -> HollowborneTurretAnimations.BARRAGE);

        this.attackManager.addAction(new HollowborneTurretShootBarrageAction(this));
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
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1D)
                .add(Attributes.ATTACK_DAMAGE, 0.1D)
                .add(Attributes.ARMOR, 8.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 1.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new SimpleDistanceFromGoal(this, 1.2, 6));
        this.goalSelector.addGoal(1, new SimpleChaseGoal(this, 1.0D, e -> e.getTarget() != null && e.getTarget().distanceTo(e) > 4));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, HollowborneTurret.class, Hollowborne.class, AbstractSkeleton.class).setAlertOthers());
        this.targetSelector.addGoal(1, new HollowborneTurretRecieveHurtTargetGoal(this, HollowborneTurret.class, Hollowborne.class, AbstractSkeleton.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Villager.class, true));
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (PandemoniumVotingSystem.hasPandemoniumCurse(this.level(), BMPandemoniumCurses.OVERSEER)) {
            return super.hurt(pSource, pAmount * 0.7F);
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public void tick() {
        super.tick();
        if (getVehicle() instanceof Hollowborne borne) {
            if (borne.isDeadOrDying()) {
                this.kill();
            }
        } else {
            this.kill();
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if (restTime > 0) {
            restTime--;
        }

        if (this.getTarget() != null) {
            double targetY = getTarget().getEyeY();
            this.getLookControl().setLookAt(getTarget().getX(), targetY, getTarget().getZ(), 1.2F, 90F);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        Hollowborne hollowborne = BMEntityTypes.HOLLOWBORNE.get().spawn(pLevel.getLevel(), this.blockPosition().above(), pReason);
        this.startRiding(hollowborne);
        if (PandemoniumVotingSystem.hasPandemoniumCurse(this.level(), BMPandemoniumCurses.OVERSEER)) {
            this.multiplyAttribute(Attributes.MAX_HEALTH, 1.3F);
            this.heal((float) this.getAttributeValue(Attributes.MAX_HEALTH) - this.getHealth());
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public void multiplyAttribute(Attribute attribute, float multiplier) {
        this.getAttribute(attribute).setBaseValue(this.getAttribute(attribute).getValue() * multiplier);
    }

    @Override
    protected void tickDeath() {
        this.level().explode(this, getX(), getY(), getZ(), 2.0F, Level.ExplosionInteraction.NONE);
        if (!this.level().isClientSide() && !this.isRemoved()) {
            this.remove(Entity.RemovalReason.KILLED);
        }
    }

    public static boolean checkHollowborneSpawnRules(EntityType<? extends HollowborneTurret> pType, ServerLevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return EntityUtils.doesAreaFitEntity(pLevel, pPos, pType, BMEntityTypes.HOLLOWBORNE.get()) && pLevel.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(pLevel, pPos, pRandom) && checkMobSpawnRules(pType, pLevel, pSpawnType, pPos, pRandom);
    }

    @Override
    public int getMaxHeadYRot() {
        return 180;
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pDimensions) {
        return 3.25F;
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

    public void setNoGravity(boolean ignored) {
        super.setNoGravity(true);
    }

    protected void dropCustomDeathLoot(DamageSource source, int looting, boolean recentlyHitIn) {
    }

}
