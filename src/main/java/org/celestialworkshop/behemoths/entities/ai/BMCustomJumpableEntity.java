package org.celestialworkshop.behemoths.entities.ai;

public interface BMCustomJumpableEntity {

    default boolean hasCustomJumpCalculation() {
        return true;
    }

    float calculateCustomJumpScale(boolean isJumping);
}