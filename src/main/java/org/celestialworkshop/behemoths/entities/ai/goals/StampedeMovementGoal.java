package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.BanishingStampede;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class StampedeMovementGoal extends Goal {

    private final BanishingStampede stampede;
    private @Nullable Archzombie target;
    private int recheckTimer = 0;

    public StampedeMovementGoal(BanishingStampede stampede) {
        this.stampede = stampede;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public void tick() {
        if (stampede.isVehicle()) {
            target = null;
            return;
        }

        if (target != null && (!target.isAlive() || target.isPassenger())) {
            target = null;
        }

        if (target == null) {
            if (recheckTimer-- <= 0) {
                target = findNearestArchzombie();
                recheckTimer = 20 + stampede.getRandom().nextInt(20);
            }
        }

        if (target != null) {
            moveToward(target);

            if (stampede.distanceTo(target) <= 2.0D) {
                target.startRiding(stampede);
                target = null;
                recheckTimer = 20;
            }
        } else {
            wander();
        }
    }

    private void moveToward(Archzombie zombie) {
        stampede.getNavigation().moveTo(zombie, 1.3);
        stampede.getLookControl().setLookAt(zombie, 30.0F, 30.0F);
    }

    private void wander() {
        Vec3 pos = DefaultRandomPos.getPos(stampede, 8, 4);
        if (pos != null) {
            stampede.getNavigation().moveTo(pos.x, pos.y, pos.z, 1.0);
        }
    }

    private @Nullable Archzombie findNearestArchzombie() {
        List<Archzombie> list = stampede.level().getEntitiesOfClass(Archzombie.class, stampede.getBoundingBox().inflate(32.0D), z -> z.isAlive() && !z.isPassenger());
        if (list.isEmpty()) return null;
        return list.stream().min(Comparator.comparingDouble(stampede::distanceToSqr)).orElse(null);
    }
}
