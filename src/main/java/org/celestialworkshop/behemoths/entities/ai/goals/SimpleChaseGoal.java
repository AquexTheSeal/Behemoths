package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.function.Predicate;

public class SimpleChaseGoal extends Goal {
    protected final Mob entity;
    protected final double speed;
    protected int pathRecalcTicks;
    protected Predicate<Mob> condition;

    public SimpleChaseGoal(Mob entity, double speed, Predicate<Mob> condition) {
        this.entity = entity;
        this.speed = speed;
        this.condition = condition;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public SimpleChaseGoal(Mob entity, double speed) {
        this(entity, speed, (mob) -> true);
    }

    @Override
    public boolean canUse() {
        return entity.getTarget() != null && condition.test(entity);
    }

    @Override
    public void tick() {
        if (--pathRecalcTicks > 0) return;
        pathRecalcTicks = 8 + entity.getRandom().nextInt(5);

        LivingEntity target = entity.getTarget();

        if (target == null) return;

        entity.getNavigation().moveTo(target, speed);
        entity.getLookControl().setLookAt(target, 30.0F, 30.0F);
    }
}
