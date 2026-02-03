package org.celestialworkshop.behemoths.items;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

public class BehemothHeartItem extends Item {

    public static final String HEART_ENERGY = "HeartEnergy";

    public BehemothHeartItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pLevel.isClientSide) return;

        if (pEntity instanceof Player player) {
            int duplicate = player.getInventory().countItem(this) - 1;
            if (pEntity.tickCount % (20 + duplicate * 10) == 0) {
                increaseHeartEnergy(pStack);
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("tooltip.behemoths.heart_energy_desc", getHeartEnergy(pStack), getMaxHeartEnergy())
                .withStyle(style -> style.withColor(TextColor.fromRgb(0xffbb19))));

        int countColor = hasMaxHeartEnergy(pStack) ? Color.GREEN.getRGB() : 0x6e6e6e;
        pTooltipComponents.add(Component.translatable("tooltip.behemoths.heart_energy_count", getHeartEnergy(pStack), getMaxHeartEnergy())
                .withStyle(style -> style.withColor(TextColor.fromRgb(countColor))));
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        ItemStack copy = itemStack.copy();
        resetHeartEnergy(copy);
        return copy;
    }

    public int getMaxHeartEnergy() {
        return 3000;
    }

    // HELPERS

    public static int getHeartEnergy(ItemStack stack) {
        return stack.getOrCreateTag().getInt(HEART_ENERGY);
    }

    public static void setHeartEnergy(ItemStack stack, int energy) {
        stack.getOrCreateTag().putInt(HEART_ENERGY, energy);
    }

    public static void addHeartEnergy(ItemStack stack, int energy) {
        if (stack.getItem() instanceof BehemothHeartItem item) {
            int give = Math.min(getHeartEnergy(stack) + energy, item.getMaxHeartEnergy());
            setHeartEnergy(stack, give);
        }
    }

    public static boolean hasMaxHeartEnergy(ItemStack stack) {
        if (stack.getItem() instanceof BehemothHeartItem item) {
            return getHeartEnergy(stack) >= item.getMaxHeartEnergy();
        }
        return false;
    }

    public static void increaseHeartEnergy(ItemStack stack) {
        addHeartEnergy(stack, 1);
    }

    public static void fillHeartEnergy(ItemStack stack) {
        if (stack.getItem() instanceof BehemothHeartItem item) {
            setHeartEnergy(stack, item.getMaxHeartEnergy());
        }
    }

    public static void resetHeartEnergy(ItemStack stack) {
        setHeartEnergy(stack, 0);
    }
}
