package org.celestialworkshop.behemoths.mixin;

import net.minecraft.world.entity.LivingEntity;
import org.celestialworkshop.behemoths.api.entity.ActionManager;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    /**
     * ActionManager ticking not run on LivingEvent#LivingTickEvent that event runs at the very
     * beginning of the tick which prevents movement updates from running.
     */
    @Inject(method = "aiStep", at = @At("TAIL"))
    public void aiStep(CallbackInfo ci) {
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance instanceof BMEntity bm && !instance.level().isClientSide) {
            bm.getActionManagers().forEach(ActionManager::tick);
        }
    }
}
