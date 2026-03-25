package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;
import org.celestialworkshop.behemoths.entities.ai.action.CharydbisCrashAction;

import java.util.EnumSet;

public class SkyCharydbisSurroundGoal extends Goal {
    protected final SkyCharydbis entity;
    protected int pathRecalcTicks;

    public SkyCharydbisSurroundGoal(SkyCharydbis entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return entity.getTarget() != null && !(entity.getActionManagers().get(0).getCurrentAction() instanceof CharydbisCrashAction);
    }

    @Override
    public void tick() {
        if (--pathRecalcTicks > 0) return;

        pathRecalcTicks = 10 + entity.getRandom().nextInt(10);

        LivingEntity target = entity.getTarget();

        if (target == null) return;

//        if (target.distanceTo(entity) > 20) {
//            entity.getNavigation().moveTo(target.getX(), target.getY(), target.getZ(), 2.0F);
//            return;
//        }

        double range = 14 - entity.getRandom().nextInt(6);
        float rotIntensity = 0.03F;
        double xR = Mth.sin(entity.tickCount * rotIntensity) * range;
        double yR = entity.isSubmerged ? -entity.getRandom().nextIntBetweenInclusive(15, 20) : entity.getRandom().nextIntBetweenInclusive(12, 16);
        double zR = Mth.cos(entity.tickCount * rotIntensity) * range;
        Vec3 targetPos = target.position().add(xR, yR, zR);

        entity.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 2.0F);
    }
}
