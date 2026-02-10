package org.celestialworkshop.behemoths.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.misc.utils.WorldUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

// Fix: 1200 priority for Pufferfish Attributes
@Mixin(value = FoodData.class, priority = 1200)
public abstract class FoodDataMixin {

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 4.0F, ordinal = 0), require = 0)
    private float foodLvlRedThresh(float value, Player player) {
        if (WorldUtils.hasPandemoniumCurse(player.level(), BMPandemoniumCurses.FRAGILITY)) {
            return value * 0.5F;
        }
        return value;
    }

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 4.0F, ordinal = 1), require = 0)
    private float foodLvlRedThresh2(float value, Player player) {
        if (WorldUtils.hasPandemoniumCurse(player.level(), BMPandemoniumCurses.FRAGILITY)) {
            return value * 0.5F;
        }
        return value;
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 10), require = 0)
    private int fastHealTimer(int value, Player player) {
        if (WorldUtils.hasPandemoniumCurse(player.level(), BMPandemoniumCurses.FRAGILITY)) {
            return value * 2;
        }
        return value;
    }

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 1.0F, ordinal = 0), require = 0)
    private float healNerf(float value, Player player) {
        if (WorldUtils.hasPandemoniumCurse(player.level(), BMPandemoniumCurses.FRAGILITY)) {
            return value * 0.5F;
        }
        return value;
    }
}