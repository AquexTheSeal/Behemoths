package org.celestialworkshop.behemoths.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.misc.utils.WorldUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(FoodData.class)
public abstract class FoodDataMixin {


    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 4.0F, ordinal = 0))
    private float foodLvlRedThresh(float original, Player player) {
        if (WorldUtils.hasPandemoniumCurse(player.level(), BMPandemoniumCurses.FRAGILITY)) {
            return 2.0F;
        }
        return original;
    }

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 4.0F, ordinal = 1))
    private float foodLvlRedThresh2(float original, Player player) {
        if (WorldUtils.hasPandemoniumCurse(player.level(), BMPandemoniumCurses.FRAGILITY)) {
            return 2.0F;
        }
        return original;
    }

    @ModifyConstant(method = "tick", constant = @Constant(intValue = 10))
    private int fastHealTimer(int original, Player player) {
        if (WorldUtils.hasPandemoniumCurse(player.level(), BMPandemoniumCurses.FRAGILITY)) {
            return 20;
        }
        return original;
    }

    @ModifyConstant(method = "tick", constant = @Constant(floatValue = 1.0F, ordinal = 0))
    private float healNerf(float original, Player player) {
        if (WorldUtils.hasPandemoniumCurse(player.level(), BMPandemoniumCurses.FRAGILITY)) {
            return 0.5F;
        }
        return original;
    }
}
