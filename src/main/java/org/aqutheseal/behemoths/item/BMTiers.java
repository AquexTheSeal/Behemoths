package org.aqutheseal.behemoths.item;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.aqutheseal.behemoths.registry.BMItems;

import java.util.function.Supplier;

public enum BMTiers implements Tier {

    OVERWORLD_SKY_BEAST(4, 3500, 9.0F, 5.0F, 15, () -> Ingredient.of(BMItems.SKY_BEAST_BONE.get())),
    NETHER_SKY_BEAST(4, 5000, 12.0F, 7F, 20, () -> Ingredient.of(BMItems.SKY_BEAST_BONE.get())),
    END_SKY_BEAST(4, 6500, 15.0F, 9F, 25, () -> Ingredient.of(BMItems.SKY_BEAST_BONE.get()));

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    BMTiers(int pLevel, int pUses, float pSpeed, float pDamage, int pEnchantmentValue, Supplier<Ingredient> pRepairIngredient) {
        this.level = pLevel;
        this.uses = pUses;
        this.speed = pSpeed;
        this.damage = pDamage;
        this.enchantmentValue = pEnchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(pRepairIngredient);
    }

    public int getUses() {
        return this.uses;
    }

    public float getSpeed() {
        return this.speed;
    }

    public float getAttackDamageBonus() {
        return this.damage;
    }

    public int getLevel() {
        return this.level;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
}
