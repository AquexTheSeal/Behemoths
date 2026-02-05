package org.celestialworkshop.behemoths.entities.ai.goals;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.ShieldItem;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.misc.utils.WorldUtils;

import java.util.EnumSet;

public class ArchzombieShieldGoal extends Goal {
    private final Archzombie archzombie;
    public int shieldTimer = 0;

    public ArchzombieShieldGoal(Archzombie archzombie) {
        this.archzombie = archzombie;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return archzombie.getOffhandItem().getItem() instanceof ShieldItem && WorldUtils.hasPandemoniumCurse(archzombie.level(), BMPandemoniumCurses.ARCHZOMBIE_DOMINION);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        this.shieldTimer++;
        if (this.shieldTimer > 100) {
            this.shieldTimer = 0;
            archzombie.setBlocking(true);
            archzombie.getNavigation().stop();
        }
    }

    @Override
    public void start() {
        this.shieldTimer = 0;
    }

    @Override
    public void stop() {
        this.shieldTimer = 0;
    }
}
