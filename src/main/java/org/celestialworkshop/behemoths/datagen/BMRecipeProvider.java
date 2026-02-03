package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.registries.BMBlocks;
import org.celestialworkshop.behemoths.registries.BMItems;

import java.util.function.Consumer;

public class BMRecipeProvider extends RecipeProvider {
    public BMRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BMItems.BEHEBUGGER.get())
                .define('#', Items.BEDROCK)
                .define('O', BMItems.BEHEMOTH_HEART.get())
                .pattern("###")
                .pattern("#O#")
                .pattern("###")
                .group("magnalyth_sword")
                .unlockedBy(getHasName(Items.BEDROCK), has(Items.BEDROCK))
                .save(pWriter, Behemoths.prefix("shaped_recipe_heart_test"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, BMItems.MAGNALYTH_INGOT.get(), 4)
                .requires(BMItems.BEHEMOTH_HEART.get()).requires(Items.IRON_INGOT, 3).requires(Items.DIAMOND, 1)
                .group("magnalyth")
                .unlockedBy(getHasName(BMItems.BEHEMOTH_HEART.get()), has(BMItems.BEHEMOTH_HEART.get()))
                .save(pWriter, Behemoths.prefix("magnalyth_ingot_from_heart"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BMItems.MAGNALYTH_SWORD.get())
                .define('#', BMItems.MAGNALYTH_INGOT.get())
                .define('I', Items.STICK)
                .pattern(" # ")
                .pattern(" # ")
                .pattern(" I ")
                .group("magnalyth_sword")
                .unlockedBy(getHasName(BMItems.MAGNALYTH_INGOT.get()), has(BMItems.MAGNALYTH_INGOT.get()))
                .save(pWriter, Behemoths.prefix("magnalyth_sword"));

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, BMItems.MAGNALYTH_AXE.get())
                .define('#', BMItems.MAGNALYTH_INGOT.get())
                .define('I', Items.STICK)
                .pattern("## ")
                .pattern("#I ")
                .pattern(" I ")
                .group("magnalyth_axe")
                .unlockedBy(getHasName(BMItems.MAGNALYTH_INGOT.get()), has(BMItems.MAGNALYTH_INGOT.get()))
                .save(pWriter, Behemoths.prefix("magnalyth_axe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BMItems.MAGNALYTH_PICKAXE.get())
                .define('#', BMItems.MAGNALYTH_INGOT.get())
                .define('I', Items.STICK)
                .pattern("###")
                .pattern(" I ")
                .pattern(" I ")
                .group("magnalyth_pickaxe")
                .unlockedBy(getHasName(BMItems.MAGNALYTH_INGOT.get()), has(BMItems.MAGNALYTH_INGOT.get()))
                .save(pWriter, Behemoths.prefix("magnalyth_pickaxe"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BMItems.MAGNALYTH_SHOVEL.get())
                .define('#', BMItems.MAGNALYTH_INGOT.get())
                .define('I', Items.STICK)
                .pattern(" # ")
                .pattern(" I ")
                .pattern(" I ")
                .group("magnalyth_shovel")
                .unlockedBy(getHasName(BMItems.MAGNALYTH_INGOT.get()), has(BMItems.MAGNALYTH_INGOT.get()))
                .save(pWriter, Behemoths.prefix("magnalyth_shovel"));

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, BMItems.MAGNALYTH_HOE.get())
                .define('#', BMItems.MAGNALYTH_INGOT.get())
                .define('I', Items.STICK)
                .pattern("## ")
                .pattern(" I ")
                .pattern(" I ")
                .group("magnalyth_hoe")
                .unlockedBy(getHasName(BMItems.MAGNALYTH_INGOT.get()), has(BMItems.MAGNALYTH_INGOT.get()))
                .save(pWriter, Behemoths.prefix("magnalyth_hoe"));

        storageCompression(pWriter, BMItems.MAGNALYTH_NUGGET.get(), BMItems.MAGNALYTH_INGOT.get());

        storageCompression(pWriter, BMItems.MAGNALYTH_INGOT.get(), BMBlocks.MAGNALYTH_BLOCK.get());
    }

    protected static void storageCompression(Consumer<FinishedRecipe> pWriter, ItemLike small, ItemLike large) {
        String smallName = path(small);
        String largeName = path(large);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, large)
                .define('#', small)
                .pattern("###").pattern("###").pattern("###")
                .unlockedBy(getHasName(small), has(small))
                .save(pWriter, Behemoths.prefix(largeName + "_from_" + smallName));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, small, 9)
                .requires(large)
                .unlockedBy(getHasName(large), has(large))
                .save(pWriter, Behemoths.prefix(smallName + "_from_" + largeName));
    }

    private static String path(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
    }
}
