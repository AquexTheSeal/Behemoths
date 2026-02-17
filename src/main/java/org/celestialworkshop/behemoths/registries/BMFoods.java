package org.celestialworkshop.behemoths.registries;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class BMFoods {

    public static final FoodProperties SAVAGE_FLESH = new FoodProperties.Builder()
            .nutrition(8)
            .saturationMod(0.6F)
            .effect(() -> new MobEffectInstance(MobEffects.HUNGER, 800, 1), 1.0F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 0), 1.0F)
            .meat()
            .build();
}
