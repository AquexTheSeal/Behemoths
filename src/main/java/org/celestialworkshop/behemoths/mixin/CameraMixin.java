package org.celestialworkshop.behemoths.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.celestialworkshop.behemoths.entities.misc.Cragpiercer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    protected abstract void move(double pDistanceOffset, double pVerticalOffset, double pHorizontalOffset);

    @Shadow
    protected abstract double getMaxZoom(double pStartingDistance);

    @Inject(
            method = "setup",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/Camera;move(DDD)V",
                    shift = At.Shift.BEFORE),
            cancellable = true
    )
    private void setup(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (entity instanceof Cragpiercer && detached) {
            double customZoomDistance = 0.75;
            double finalZoom = this.getMaxZoom(customZoomDistance);
            this.move(-finalZoom, 1.0, 0.5);
            ci.cancel();
        }
    }
}
