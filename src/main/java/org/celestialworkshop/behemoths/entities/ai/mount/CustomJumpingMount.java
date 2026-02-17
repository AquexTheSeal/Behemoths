package org.celestialworkshop.behemoths.entities.ai.mount;

import net.minecraft.world.entity.LivingEntity;

/**
 * Simple interface with more freedom than PlayerRideableJumping interface.
 */
public interface CustomJumpingMount<T extends LivingEntity & CustomJumpingMount> {

    MountJumpManager<T> getMountJumpManager();

    void performJump(int power);

    boolean getJumpCondition();

    int getJumpCooldown(int power);

    default int getMaximumJumpPower() {
        return 100;
    }
}
