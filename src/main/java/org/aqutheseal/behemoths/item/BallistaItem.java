package org.aqutheseal.behemoths.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.aqutheseal.behemoths.registry.BMSoundEvents;
import org.aqutheseal.behemoths.util.UtilLevel;
import org.aqutheseal.behemoths.util.mixin.IBallistaArrow;

public class BallistaItem extends CrossbowItem {
    private boolean startSoundPlayed = false;
    private boolean midLoadSoundPlayed = false;

    public BallistaItem(Properties pProperties) {
        super(pProperties);
    }

    public float getChargeTimeMultiplier(ItemStack crossbowStack) {
        return 5F;
    }

    public void modifyArrowProperties(AbstractArrow arrow) {
        if (arrow instanceof IBallistaArrow ballistaArrow) {
            ballistaArrow.behemoths$setBehemothDamageMultiplier(1.5F);
            ballistaArrow.behemoths$setOnCollide(result -> {
                arrow.level().explode(null, arrow.getX(), arrow.getY(), arrow.getZ(), 3.0F, Level.ExplosionInteraction.NONE);
                arrow.remove(Entity.RemovalReason.DISCARDED);
                if (result instanceof EntityHitResult entityHitResult && entityHitResult.getEntity() instanceof LivingEntity le) {
                    le.invulnerableTime = 0;
                }
            });
        }
        UtilLevel.shakeScreensOnSurroundings(arrow.level(), arrow.position(), 16, 1F, p -> false);
        arrow.setBaseDamage(arrow.getBaseDamage() * 2.5F);
        arrow.setDeltaMovement(arrow.getDeltaMovement().scale(1.2));
        arrow.setNoGravity(true);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pCount) {
        if (!pLevel.isClientSide) {
            int i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, pStack);
            SoundEvent soundevent = this.getStartSound();
            SoundEvent soundevent1 = i == 0 ? SoundEvents.CROSSBOW_LOADING_MIDDLE : null;
            float f = (float) (pStack.getUseDuration() - pCount) / (float) getChargeDuration(pStack);
            if (f < 0.03F) {
                this.startSoundPlayed = false;
                this.midLoadSoundPlayed = false;
            }

            if (f >= 0.03F && !this.startSoundPlayed) {
                this.startSoundPlayed = true;
                pLevel.playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), soundevent, SoundSource.PLAYERS, 0.25F, 1.0F);
            }

            if (f >= 0.5F && soundevent1 != null && !this.midLoadSoundPlayed) {
                this.midLoadSoundPlayed = true;
                pLevel.playSound(null, pLivingEntity.getX(), pLivingEntity.getY(), pLivingEntity.getZ(), soundevent1, SoundSource.PLAYERS, 0.5F, 1.0F);
            }
        }
    }

    public SoundEvent getStartSound() {
        return BMSoundEvents.BALLISTA_START.get();
    }

    public SoundEvent getLoadedSound() {
        return BMSoundEvents.BALLISTA_LOADED.get();
    }

    public SoundEvent getShootSound() {
        return BMSoundEvents.BALLISTA_SHOOT.get();
    }
}
