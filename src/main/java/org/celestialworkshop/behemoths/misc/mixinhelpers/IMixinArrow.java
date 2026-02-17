package org.celestialworkshop.behemoths.misc.mixinhelpers;

import net.minecraft.world.entity.Entity;

public interface IMixinArrow {

    boolean behemoths$isBoostedArrow();

    void behemoths$setBoostedArrow(boolean val);

    static void boost(Entity entity) {
        if (entity instanceof IMixinArrow arrow) {
            arrow.behemoths$setBoostedArrow(true);
        }
    }

    static boolean isBoosted(Entity entity) {
        if (entity instanceof IMixinArrow arrow) {
            return arrow.behemoths$isBoostedArrow();
        }
        return false;
    }
}
