package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;

import java.util.EnumSet;

public class SkyCharydbisStationaryGoal extends Goal {
    protected final SkyCharydbis entity;
    protected int pathRecalcTicks;

    public SkyCharydbisStationaryGoal(SkyCharydbis entity) {
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return entity.getTarget() == null;
    }

    @Override
    public void tick() {
        if (--pathRecalcTicks > 0) return;

        pathRecalcTicks = 40 + entity.getRandom().nextInt(5);

        if (entity.spawnPos != null) {
            double range = 24 - entity.getRandom().nextInt(6);
            float rotIntensity = 0.05F;
            double xR = Mth.sin(entity.tickCount * rotIntensity) * range;
            double yR = entity.getRandom().nextIntBetweenInclusive(14, 18);
            double zR = Mth.cos(entity.tickCount * rotIntensity) * range;
            Vec3 targetPos = new Vec3(entity.spawnPos.getX(), entity.spawnPos.getY(), entity.spawnPos.getZ()).add(xR, yR, zR);

            entity.getNavigation().moveTo(targetPos.x, targetPos.y, targetPos.z, 2.0F);
            return;
        }


       Vec3 targetPos = AirAndWaterRandomPos.getPos(this.entity, 14, 10, 0, entity.getX(), entity.getZ(), (float)Math.PI / 2F);
        if (targetPos == null) return;
        entity.getNavigation().moveTo(targetPos.x, targetPos.y + 4, targetPos.z, 1.0F);
    }
}
