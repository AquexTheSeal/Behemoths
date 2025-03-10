package org.aqutheseal.behemoths.entity.pathnavigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;

public class FreeFlyingPathNavigator extends GroundPathNavigation {

    private final Mob mob;

    public FreeFlyingPathNavigator(Mob mob, Level world) {
        super(mob, world);
        this.mob = mob;
    }

    public void tick() {
        ++this.tick;
    }
}