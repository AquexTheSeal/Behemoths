package org.aqutheseal.behemoths.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.client.armor.SkyBeastArmorModel;
import org.aqutheseal.behemoths.entity.variants.SkyCharydbisVariants;
import org.aqutheseal.behemoths.registry.BMTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class SkyBeastArmorItem extends ArmorItem {

    public final SkyCharydbisVariants variant;

    public SkyBeastArmorItem(ArmorMaterial material, ArmorItem.Type armorType, Properties properties, SkyCharydbisVariants variant) {
        super(material, armorType, properties);
        this.variant = variant;
    }

    public float getFinalReceivedDamage(LivingEntity wearer, DamageSource source, float originalAmount) {
        float mutableAmount = originalAmount;
        if (source.getEntity() != null && source.getEntity().getType().is(BMTags.Entities.BEHEMOTHS)) {
            mutableAmount *= 0.8F;
        }
        switch (variant) {
            case BARREN -> {
                if (source.is(DamageTypeTags.IS_FIRE)) mutableAmount *= 0.8F;
            }
            case LUSH -> {
                if (source.is(DamageTypeTags.IS_FALL)) mutableAmount *= 0.8F;
            }
            case NORTHERN -> {
                if (source.is(DamageTypeTags.IS_FREEZING)) mutableAmount *= 0.6F;
            }
            case NETHER -> {
                if (source.is(DamageTypeTags.IS_FIRE)) mutableAmount = 0F;
            }
            case SOUL -> {
                if (source.is(DamageTypeTags.IS_FIRE)) mutableAmount *= 0.5F;
            }
            case VOID -> {
                if (source.is(DamageTypeTags.IS_FALL)) {
                    if (wearer.level().dimension() == Level.END) {
                        mutableAmount = 0F;
                    } else {
                        mutableAmount *= 0.6F;
                    }
                }
            }
        }
        return mutableAmount;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("item.behemoths.sky_beast_gear.base_bonus").withStyle(ChatFormatting.GREEN));
        if (Screen.hasShiftDown() || Screen.hasAltDown()) {
            pTooltipComponents.add(Component.translatable("item.behemoths.sky_beast_armor.base_bonus_description").withStyle(ChatFormatting.GRAY));
        }
        pTooltipComponents.add(Component.translatable("item.behemoths.sky_beast_gear.effect_" + variant.id).withStyle(ChatFormatting.GREEN));
        if (Screen.hasShiftDown() || Screen.hasAltDown()) {
            pTooltipComponents.add(Component.translatable("item.behemoths.sky_beast_armor.effect_description_" + variant.id).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public boolean canWalkOnPowderedSnow(ItemStack stack, LivingEntity wearer) {
        return super.canWalkOnPowderedSnow(stack, wearer) || variant == SkyCharydbisVariants.NORTHERN;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            @OnlyIn(Dist.CLIENT)
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> defaultModel) {
                SkyBeastArmorModel model = new SkyBeastArmorModel(Minecraft.getInstance().getEntityModels().bakeLayer(SkyBeastArmorModel.LAYER_LOCATION));
                return slot == EquipmentSlot.LEGS ? defaultModel : model;
            }
        });
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slotType, String type) {
        String defaultTexture = Behemoths.MODID + ":textures/models/armor/sky_beast_layer_1.png";
        String legTexture = Behemoths.MODID + ":textures/models/armor/sky_beast_layer_2.png";

        return slotType == EquipmentSlot.LEGS ? legTexture : defaultTexture;
    }
}
