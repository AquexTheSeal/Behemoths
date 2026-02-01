package org.celestialworkshop.behemoths.registries;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import org.celestialworkshop.behemoths.Behemoths;

import java.util.List;

public class BMItemTiers {

    public static final Tier MAGNALYTH = TierSortingRegistry.registerTier(
            new ForgeTier(3, 1800, 8.5F, 3.5F, 8, BMTags.Blocks.NEEDS_MAGNALYTH_TOOL, () -> Ingredient.of(BMItems.MAGNALYTH_INGOT.get())),
            Behemoths.prefix("magnalyth"), List.of(Tiers.DIAMOND), List.of(Tiers.NETHERITE)
    );
}
