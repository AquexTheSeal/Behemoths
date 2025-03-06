package org.aqutheseal.behemoths.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;
import org.aqutheseal.behemoths.capability.FreezingCapability;
import org.aqutheseal.behemoths.entity.variants.SkyCharydbisVariants;
import org.aqutheseal.behemoths.registry.BMTags;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ISkyBeastTool {

    default float getFinalSentDamage(LivingEntity holder, LivingEntity target, float originalAmount) {
        float mutableAmount = originalAmount;
        if (target.getType().is(BMTags.Entities.BEHEMOTHS)) {
            mutableAmount *= 2F;
        }
        switch (this.getVariant()) {
            case BARREN -> {
                if (target.isOnFire()) mutableAmount *= 1.25F;
            }
            // LUSH handled in ModCommonEvents
            case NORTHERN -> target.getCapability(FreezingCapability.CAPABILITY).ifPresent(cap -> cap.setFreezeTime(200));
            case NETHER -> {
                target.setSecondsOnFire(5);
                if (target.isOnFire()) mutableAmount *= 1.1F;
            }
            case SOUL -> {
                float velocity = (float) holder.getDeltaMovement().length() * 0.15F;
                mutableAmount *= Math.min(1 + (velocity * 0.5F), 1.5F);
            }
            case VOID -> {
                float velocity = (float) holder.getDeltaMovement().length() * 0.3F;
                mutableAmount *= 1 + velocity;
                if (target.getType().is(Tags.EntityTypes.BOSSES)) {
                    mutableAmount *= 1.3F;
                }
            }
        }
        return mutableAmount;
    }

    default void hoverText(ItemStack pStack, List<Component> pTooltipComponents, SkyCharydbisVariants variant) {
        pTooltipComponents.add(Component.translatable("item.behemoths.sky_beast_gear.base_bonus").withStyle(ChatFormatting.GREEN));
        if (Screen.hasShiftDown() || Screen.hasAltDown()) {
            pTooltipComponents.add(Component.translatable("item.behemoths.sky_beast_weapon.base_bonus_description").withStyle(ChatFormatting.GRAY));
        }
        pTooltipComponents.add(Component.translatable("item.behemoths.sky_beast_gear.effect_" + variant.id).withStyle(ChatFormatting.GREEN));
        if (Screen.hasShiftDown() || Screen.hasAltDown()) {
            pTooltipComponents.add(Component.translatable("item.behemoths.sky_beast_weapon.effect_description_" + variant.id).withStyle(ChatFormatting.GRAY));
        }
    }

    SkyCharydbisVariants getVariant();

    class Sword extends SwordItem implements ISkyBeastTool {
        public final SkyCharydbisVariants variant;
        public Sword(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, SkyCharydbisVariants variant) {
            super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
            this.variant = variant;
        }

        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            hoverText(pStack, pTooltipComponents, variant);
        }

        @Override
        public SkyCharydbisVariants getVariant() {
            return this.variant;
        }
    }

    class Axe extends AxeItem implements ISkyBeastTool {
        public final SkyCharydbisVariants variant;
        public Axe(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, SkyCharydbisVariants variant) {
            super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
            this.variant = variant;
        }

        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            hoverText(pStack, pTooltipComponents, variant);
        }

        @Override
        public SkyCharydbisVariants getVariant() {
            return this.variant;
        }
    }

    class Pickaxe extends PickaxeItem implements ISkyBeastTool {
        public final SkyCharydbisVariants variant;
        public Pickaxe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, SkyCharydbisVariants variant) {
            super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
            this.variant = variant;
        }

        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            hoverText(pStack, pTooltipComponents, variant);
        }

        @Override
        public SkyCharydbisVariants getVariant() {
            return this.variant;
        }
    }

    class Shovel extends ShovelItem implements ISkyBeastTool {
        public final SkyCharydbisVariants variant;
        public Shovel(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, SkyCharydbisVariants variant) {
            super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
            this.variant = variant;
        }

        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            hoverText(pStack, pTooltipComponents, variant);
        }

        @Override
        public SkyCharydbisVariants getVariant() {
            return this.variant;
        }
    }

    class Hoe extends HoeItem implements ISkyBeastTool {
        public final SkyCharydbisVariants variant;
        public Hoe(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties, SkyCharydbisVariants variant) {
            super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
            this.variant = variant;
        }

        @Override
        public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
            super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
            hoverText(pStack, pTooltipComponents, variant);
        }

        @Override
        public SkyCharydbisVariants getVariant() {
            return this.variant;
        }
    }
}
