package org.celestialworkshop.behemoths.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumVotingSystem;
import org.celestialworkshop.behemoths.misc.mixinhelpers.IMixinArrow;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMixin extends Monster {

    protected AbstractSkeletonMixin(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @ModifyReturnValue(
            method = "getArrow",
            at = @At("RETURN")
    )
    protected AbstractArrow getArrow(AbstractArrow original) {
        if (PandemoniumVotingSystem.hasPandemoniumCurse(level(), BMPandemoniumCurses.HEAVY_ARROW)) {
            if (random.nextFloat() < 0.15F) {
                IMixinArrow.boost(original);
            }
        }
        return original;
    }

    @WrapOperation(
            method = "performRangedAttack",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shoot(DDDFF)V")
    )
    private void modifyVelocityOnly(AbstractArrow instance, double x, double y, double z, float velocity, float inaccuracy, Operation<Void> original) {
        float r = velocity;
        if (IMixinArrow.isBoosted(instance)) {
            r *= 2.0F;
        }
        original.call(instance, x, y, z, r, inaccuracy);
    }
}
