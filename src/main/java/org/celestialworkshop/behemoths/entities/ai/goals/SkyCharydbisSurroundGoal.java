package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;

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
        return entity.getTarget() != null;
    }

    @Override
    public void tick() {
        if (--pathRecalcTicks > 0) return;

        pathRecalcTicks = 10 + entity.getRandom().nextInt(4);

        LivingEntity target = entity.getTarget();

        if (target == null) return;

        double range = 14 - entity.getRandom().nextInt(6);
        float rotIntensity = 0.05F;
        double xR = Mth.sin(entity.tickCount * rotIntensity) * range;
        double yR = entity.getRandom().nextIntBetweenInclusive(12, 16);
        double zR = Mth.cos(entity.tickCount * rotIntensity) * range;
        Vec3 targetPos = target.position().add(xR, yR, zR);
//        targetPos = target.position();

        entity.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 2.0F);
    }
}
