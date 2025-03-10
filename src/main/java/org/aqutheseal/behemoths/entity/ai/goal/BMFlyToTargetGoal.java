package org.aqutheseal.behemoths.entity.ai.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

import java.util.EnumSet;

public class BMFlyToTargetGoal extends Goal {
    private final Mob mob;
    private int ticksUntilNextWantedRecalc;

    public BMFlyToTargetGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        return mob.getTarget() != null;
    }

    public boolean canContinueToUse() {
        LivingEntity target = this.mob.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative();
        }
    }

    @Override
    public void tick() {
        super.tick();
        Entity target = mob.getTarget();

//        if (--this.ticksUntilNextPathRecalculation <= 0) {
//            this.path = this.mob.getNavigation().createPath(target, 0);
//            this.mob.getNavigation().moveTo(path, 1.3F);
//            this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
//        }

        if (target != null) {
            if (--this.ticksUntilNextWantedRecalc <= 0) {
                this.mob.getMoveControl().setWantedPosition(target.getX(), target.getEyeY() + mob.getRandom().nextInt(5), target.getZ(), 1.3D);
                this.ticksUntilNextWantedRecalc = 20 + mob.getRandom().nextInt(20);
            }
        }
    }
}
