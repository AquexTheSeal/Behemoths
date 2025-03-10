package org.aqutheseal.behemoths.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.aqutheseal.behemoths.item.ISkyBeastTool;
import org.aqutheseal.behemoths.util.compat.BetterCombatUtil;
import org.aqutheseal.behemoths.util.mixin.IScreenShaker;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin implements IScreenShaker {

    @Unique public float behemoths$shakeIntensity;

    @ModifyVariable(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
                    shift = At.Shift.BEFORE
            ),
            ordinal = 0
    )
    private float modifyDamage(float damage, Entity target) {
        if (target instanceof LivingEntity livingTarget) {
            Player player = (Player) (Object) this;
            ItemStack stack = BetterCombatUtil.getSwingingStack(player);
            Item item = stack.getItem();
            if (item instanceof ISkyBeastTool sbTool) {
                Log.debug("Stack: " + item.getName(stack));
                return sbTool.getFinalSentDamage(player, livingTarget, damage);
            }
        }
        return damage;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci) {
        this.tickShake();
    }

    @Override
    public float behemoths$getShakeIntensity() {
        return behemoths$shakeIntensity;
    }

    @Override
    public void behemoths$setShakeIntensity(float value) {
        this.behemoths$shakeIntensity = value;
    }
}
