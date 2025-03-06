package org.aqutheseal.behemoths.util.mixin;

public interface IScreenShaker {

    float behemoths$getShakeIntensity();

    void behemoths$setShakeIntensity(float value);

    default void tickShake() {
        float intensity = behemoths$getShakeIntensity();
        if (intensity > 0.03) {
            behemoths$setShakeIntensity(intensity * 0.9F);
        } else {
            behemoths$setShakeIntensity(0);
        }
    }
}
