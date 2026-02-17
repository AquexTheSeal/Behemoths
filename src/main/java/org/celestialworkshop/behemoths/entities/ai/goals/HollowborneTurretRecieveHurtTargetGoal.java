package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.GameRules;
import org.celestialworkshop.behemoths.entities.Hollowborne;
import org.celestialworkshop.behemoths.entities.HollowborneTurret;

import java.util.EnumSet;

public class HollowborneTurretRecieveHurtTargetGoal extends TargetGoal {

    private static final TargetingConditions HURT_BY_TARGETING = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();
    private final HollowborneTurret turret;
    private final Class<?>[] toIgnoreDamage;
    private int timestamp;

    public HollowborneTurretRecieveHurtTargetGoal(HollowborneTurret turret, Class<?>... pToIgnoreDamage) {
        super(turret, false);
        this.turret = turret;
        this.toIgnoreDamage = pToIgnoreDamage;
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
    }

    public boolean canUse() {
        if (!(this.turret.getVehicle() instanceof Hollowborne hollowborne)) return false;

        int i = hollowborne.getLastHurtByMobTimestamp();
        LivingEntity livingentity = hollowborne.getLastHurtByMob();
        if (i != this.timestamp && livingentity != null) {
            if (livingentity.getType() == EntityType.PLAYER && hollowborne.level().getGameRules().getBoolean(GameRules.RULE_UNIVERSAL_ANGER)) {
                return false;
            } else {
                for(Class<?> oclass : this.toIgnoreDamage) {
                    if (oclass.isAssignableFrom(livingentity.getClass())) {
                        return false;
                    }
                }

                return this.canAttack(livingentity, HURT_BY_TARGETING);
            }
        } else {
            return false;
        }
    }

    public void start() {
        if (!(this.turret.getVehicle() instanceof Hollowborne hollowborne)) return;

        this.mob.setTarget(hollowborne.getLastHurtByMob());
        this.targetMob = this.mob.getTarget();
        this.timestamp = hollowborne.getLastHurtByMobTimestamp();
        this.unseenMemoryTicks = 300;

        super.start();
    }
}
