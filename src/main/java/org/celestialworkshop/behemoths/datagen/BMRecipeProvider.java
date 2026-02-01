package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.registries.BMItems;

import java.util.function.Consumer;

public class BMRecipeProvider extends RecipeProvider {
    public BMRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BMItems.MAGNALYTH_INGOT.get(), 4)
                .requires(BMItems.BEHEMOTH_HEART.get()).requires(Items.IRON_INGOT, 3).requires(Items.DIAMOND, 1)
                .group("magnalyth")
                .unlockedBy(getHasName(BMItems.BEHEMOTH_HEART.get()), has(BMItems.BEHEMOTH_HEART.get()))
                .save(pWriter, Behemoths.prefix("magnalyth_ingot"));

    }
}
