package org.celestialworkshop.behemoths.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.ModList;
import org.celestialworkshop.behemoths.api.item.specialty.ItemSpecialtyCapability;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;
import org.celestialworkshop.behemoths.compat.BMBetterCombat;
import org.celestialworkshop.behemoths.registries.BMCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    @Unique
    public boolean bm$critProcess = false;

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @ModifyVariable(method = "attack", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z",
            shift = At.Shift.BEFORE
    ), ordinal = 0)
    private float modifyMeleeDamage(float damage, Entity target) {
        float result = damage;
        if (target instanceof LivingEntity livingTarget) {
            ItemStack stack = this.bm$getAttackingItem();
            LazyOptional<ItemSpecialtyCapability> cap = stack.getCapability(BMCapabilities.ITEM_SPECIALTY);
            if (cap.isPresent()) {
                for (SpecialtyInstance entry : cap.resolve().get().getSpecialties()) {
                    result = entry.specialty().onDamageMelee(this, livingTarget, stack, result, this.bm$critProcess, entry.level());
                }
            }
        }
        return result;
    }

    @Redirect(method = "attack", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
    ))
    private boolean modifySweepingDamage(LivingEntity sweepTarget, DamageSource source, float sweepDamage, @Local(ordinal = 0) float originalDamage) {
        float result = sweepDamage;
        ItemStack stack = this.bm$getAttackingItem();
        LazyOptional<ItemSpecialtyCapability> cap = stack.getCapability(BMCapabilities.ITEM_SPECIALTY);
        if (cap.isPresent()) {
            for (SpecialtyInstance entry : cap.resolve().get().getSpecialties()) {
                result = entry.specialty().onDamageSweep(this, sweepTarget, stack, result, originalDamage, entry.level());
            }
        }
        return sweepTarget.hurt(source, result);
    }

    @Inject(method = "attack", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;doPostHurtEffects(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/Entity;)V"
    ))
    private void triggerPostAttack(Entity pTarget, CallbackInfo ci) {
        if (pTarget instanceof LivingEntity livingTarget) {
            ItemStack stack = this.bm$getAttackingItem();
            LazyOptional<ItemSpecialtyCapability> cap = stack.getCapability(BMCapabilities.ITEM_SPECIALTY);
            if (cap.isPresent()) {
                for (SpecialtyInstance entry : cap.resolve().get().getSpecialties()) {
                    entry.specialty().onPostMelee(this, livingTarget, stack, this.bm$critProcess, entry.level());
                }
            }
            this.bm$critProcess = false;
        }
    }

    @Inject(method = "attack", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraftforge/event/entity/player/CriticalHitEvent;getDamageModifier()F"
    ))
    private void enableCritProcess(Entity pTarget, CallbackInfo ci) {
        this.bm$critProcess = true;
    }

    @Unique
    private ItemStack bm$getAttackingItem() {
        if (ModList.get().isLoaded("bettercombat")) {
            return BMBetterCombat.getSwingingStack((Player) (Object) this);
        } else {
            return this.getMainHandItem();
        }
    }
}
