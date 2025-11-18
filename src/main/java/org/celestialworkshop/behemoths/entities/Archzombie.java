package org.celestialworkshop.behemoths.entities;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.celestialworkshop.behemoths.api.ActionManager;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.celestialworkshop.behemoths.entities.ai.goals.ArchzombieMovementGoal;
import org.celestialworkshop.behemoths.entities.ai.movecontrols.BMMoveControl;
import org.celestialworkshop.behemoths.entities.ai.action.ArchzombieRidingSweepAttackAction;
import org.celestialworkshop.behemoths.entities.ai.action.ArchzombieRightSweepAction;

import java.util.Map;

public class Archzombie extends Monster implements BMEntity {

    public static final String IDLE_ANIMATION = "idle";
    public static final String RIDING_ANIMATION = "riding";
    public static final String RIDING_SWEEP_ANIMATION = "ridingSweep";
    public static final String SWEEP_0_ANIMATION = "sweep_0";

    public final Map<String, AnimationState> animationStateMap = new Object2ObjectArrayMap<>();

    public final AnimationState idleAnimationState = this.createAnimationState(IDLE_ANIMATION);
    public final AnimationState ridingAnimationState = this.createAnimationState(RIDING_ANIMATION);
    public final AnimationState ridingSweepAnimationState = this.createAnimationState(RIDING_SWEEP_ANIMATION);
    public final AnimationState sweep0AnimationState = this.createAnimationState(SWEEP_0_ANIMATION);

    public final ActionManager<Archzombie> attackManager = new ActionManager<>(this);

    public Archzombie(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

        this.setMaxUpStep(1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, 16.0F);
        this.moveControl = new BMMoveControl<>(this);

        this.attackManager.addAction(
                new ArchzombieRightSweepAction(this),
                new ArchzombieRidingSweepAttackAction(this)
        );
    }

    @Override
    public Map<String, AnimationState> getAnimationStateMap() {
        return animationStateMap;
    }

    public static AttributeSupplier createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5D)
                .add(Attributes.FOLLOW_RANGE, 80.0D)
                .build();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ArchzombieMovementGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 16.0F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new HurtByTargetGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.manageIdleAnimations();

        if (!this.level().isClientSide) {
            this.attackManager.tick();
        }
    }

    public void manageIdleAnimations() {
        if (this.shouldSit()) {
            if (this.idleAnimationState.isStarted()) {
                this.idleAnimationState.stop();
            }
            this.ridingAnimationState.startIfStopped(this.tickCount);
        } else {
            if (this.ridingAnimationState.isStarted()) {
                this.ridingAnimationState.stop();
            }
            this.idleAnimationState.startIfStopped(this.tickCount);
        }
    }

    public boolean shouldSit() {
        return this.getVehicle() != null && this.getVehicle().shouldRiderSit();
    }

    @Override
    public float getRotationFreedom() {
        return shouldSit() ? 0.15F : 0.5F;
    }
}
