package org.celestialworkshop.behemoths.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Skeleton;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumVotingSystem;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RangedBowAttackGoal.class, priority = 1400)
public abstract class RangedBowAttackGoalMixin<T extends Mob & RangedAttackMob> {

    @Shadow private T mob;
    @Shadow private int attackIntervalMin;

    @Shadow
    private int seeTime;

    @Inject(method = "start", at = @At("HEAD"))
    private void modifyAttackSpeed(CallbackInfo ci) {
        if (mob instanceof Skeleton skeleton) {
            if (PandemoniumVotingSystem.hasPandemoniumCurse(skeleton.level(), BMPandemoniumCurses.QUICKDRAW.get())) {
                this.attackIntervalMin /= 2;
            }
        }
    }

    @ModifyVariable(
            method = "tick",
            at = @At(value = "STORE"),
            ordinal = 0
    )
    private int modifyChargeTime(int value) {
        if (mob instanceof Skeleton skeleton && PandemoniumVotingSystem.hasPandemoniumCurse(skeleton.level(), BMPandemoniumCurses.QUICKDRAW.get())) {
            return value * 2;
        }
        return value;
    }

    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/ai/goal/RangedBowAttackGoal;seeTime:I",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            )
    )
    private void speedUpSeeTime(CallbackInfo ci) {
        if (mob instanceof Skeleton skeleton && PandemoniumVotingSystem.hasPandemoniumCurse(skeleton.level(), BMPandemoniumCurses.QUICKDRAW.get())) {
            this.seeTime += 1;
        }
    }
}
