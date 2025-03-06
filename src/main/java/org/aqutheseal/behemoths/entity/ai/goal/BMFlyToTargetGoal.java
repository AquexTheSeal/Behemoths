package org.aqutheseal.behemoths.entity.ai.goal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import org.aqutheseal.behemoths.entity.SkyCharydbis;

import java.util.EnumSet;

public class BMFlyToTargetGoal extends Goal {
    private final Mob mob;

    public BMFlyToTargetGoal(Mob mob) {
        this.mob = mob;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean canUse() {
        return mob.getTarget() != null && ((SkyCharydbis) mob).getAttackState() != 1;
    }

    @Override
    public void tick() {
        super.tick();
        Entity target = mob.getTarget();
        if (target != null) {
            this.mob.getMoveControl().setWantedPosition(target.getX(), target.getEyeY(), target.getZ(), 1.3D);
        }
    }
}
