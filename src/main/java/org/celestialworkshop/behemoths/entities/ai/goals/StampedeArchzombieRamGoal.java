package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.utils.WorldUtils;

public class StampedeArchzombieRamGoal extends Goal {
    private final BanishingStampede stampede;
    public int archzombieRamTimer;

    public StampedeArchzombieRamGoal(BanishingStampede stampede) {
        this.stampede = stampede;
        this.archzombieRamTimer = -stampede.getRandom().nextInt(100);
    }

    @Override
    public boolean canUse() {
        if (stampede.getControllingPassenger() instanceof Archzombie arc && arc.getTarget() != null) {
            if (WorldUtils.hasPandemoniumCurse(stampede.level(), BMPandemoniumCurses.GRAVEBREAKER_MOMENTUM.get())) {
                return true;
            } else {
                return arc.isLeader();
            }
        }
        return false;
    }

    @Override
    public void tick() {
        this.archzombieRamTimer++;
        if (this.archzombieRamTimer >= 400) {
            stampede.setRamming(true);
            this.archzombieRamTimer = -stampede.getRandom().nextInt(200);
        }
    }

    @Override
    public void stop() {
        this.archzombieRamTimer = -stampede.getRandom().nextInt(200);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
