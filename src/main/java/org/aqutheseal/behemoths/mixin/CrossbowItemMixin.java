package org.aqutheseal.behemoths.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.aqutheseal.behemoths.item.BallistaItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CrossbowItem.class)
public abstract class CrossbowItemMixin {

    @Inject(method = "getChargeDuration", at = @At(value = "RETURN"), cancellable = true)
    private static void modifyChargeDuration(ItemStack pCrossbowStack, CallbackInfoReturnable<Integer> cir) {
        if (pCrossbowStack.getItem() instanceof BallistaItem ballista) {
            int modified = (int) (cir.getReturnValue() * ballista.getChargeTimeMultiplier(pCrossbowStack));
            cir.setReturnValue(Math.max(1, modified));
        }
    }

    @Inject(method = "shootProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z", shift = At.Shift.BEFORE), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void modifyArrowProperties(Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pCrossbowStack, ItemStack pAmmoStack, float pSoundPitch, boolean pIsCreativeMode, float pVelocity, float pInaccuracy, float pProjectileAngle, CallbackInfo ci, boolean flag, Projectile projectile) {
        if (pCrossbowStack.getItem() instanceof BallistaItem ballistaItem) {
            if (projectile instanceof AbstractArrow arrow) {
                ballistaItem.modifyArrowProperties(arrow);
            }
        }
    }

    @Inject(method = "releaseUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), cancellable = true)
    private void changePullingSound(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci) {
        if ((CrossbowItem) (Object) this instanceof BallistaItem ballista) {
            ci.cancel();
            pLevel.playSound(null, pEntityLiving.getX(), pEntityLiving.getY(), pEntityLiving.getZ(), ballista.getLoadedSound(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.5F + 1.0F) + 0.2F);
        }
    }

    @Inject(method = "shootProjectile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"), cancellable = true)
    private static void changeShootSound(Level pLevel, LivingEntity pShooter, InteractionHand pHand, ItemStack pCrossbowStack, ItemStack pAmmoStack, float pSoundPitch, boolean pIsCreativeMode, float pVelocity, float pInaccuracy, float pProjectileAngle, CallbackInfo ci) {
        if (pCrossbowStack.getItem() instanceof BallistaItem ballista) {
            ci.cancel();
            pLevel.playSound(null, pShooter.getX(), pShooter.getY(), pShooter.getZ(), ballista.getShootSound(), SoundSource.PLAYERS, 1.0F, pSoundPitch);
        }
    }

    @Inject(method = "getStartSound", at = @At("HEAD"), cancellable = true)
    private void getStartSound(int pEnchantmentLevel, CallbackInfoReturnable<SoundEvent> cir) {
        if ((CrossbowItem) (Object) this instanceof BallistaItem ballista) {
            cir.setReturnValue(ballista.getStartSound());
        }
    }
}
