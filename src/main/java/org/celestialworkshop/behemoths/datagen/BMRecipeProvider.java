package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 9)
                .requires(BMItems.COLOSSUS_BONE.get(), 4)
                .group("mortyx").unlockedBy(getHasName(BMItems.COLOSSUS_BONE.get()), has(BMItems.COLOSSUS_BONE.get()))
                .save(pWriter, Behemoths.prefix("bonemeal_from_colossus_bone"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BMItems.MAGNALYTH_INGOT.get(), 4)
                .requires(BMItems.BEHEMOTH_HEART.get()).requires(Items.IRON_INGOT, 3).requires(Items.DIAMOND, 1)
                .group("magnalyth").unlockedBy(getHasName(BMItems.BEHEMOTH_HEART.get()), has(BMItems.BEHEMOTH_HEART.get()))
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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BMItems.BEHEMOTH_SADDLE.get())
                .define('#', BMItems.BEHEMOTH_HEART.get())
                .define('I', ItemTags.WOOL)
                .define('S', Items.SADDLE)
                .define('G', Items.GOLD_INGOT)
                .pattern("ISI")
                .pattern("G#G")
                .group("behemoth_saddle")
                .unlockedBy(getHasName(BMItems.BEHEMOTH_HEART.get()), has(BMItems.BEHEMOTH_HEART.get()))
                .save(pWriter, Behemoths.prefix("behemoth_saddle"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BMItems.BEHEMOTH_HARNESS.get())
                .define('#', BMItems.BEHEMOTH_HEART.get())
                .define('I', Items.BLACKSTONE)
                .define('G', Items.GOLD_INGOT)
                .define('S', Items.SADDLE)
                .define('D', Items.DIAMOND)
                .pattern("ISI")
                .pattern("I#I")
                .pattern("GDG")
                .group("behemoth_saddle")
                .unlockedBy(getHasName(BMItems.BEHEMOTH_HEART.get()), has(BMItems.BEHEMOTH_HEART.get()))
                .save(pWriter, Behemoths.prefix("behemoth_harness"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, BMItems.MORTYX_INGOT.get())
                .requires(Items.GOLD_INGOT).requires(BMItems.COLOSSUS_BONE.get(), 4).requires(BMItems.SAVAGE_FLESH.get(), 4)
                .group("mortyx").unlockedBy(getHasName(Items.NETHERITE_INGOT), has(Items.NETHERITE_INGOT))
                .save(pWriter, Behemoths.prefix("mortyx_ingot"));

        storageCompression(pWriter, BMItems.MORTYX_INGOT.get(), BMBlocks.MORTYX_BLOCK.get());

        upgradeSmithing(pWriter, BMItems.MORTYX_UPGRADE_SMITHING_TEMPLATE.get(), BMItems.MAGNALYTH_SWORD.get(), BMItems.MORTYX_INGOT.get(),
                RecipeCategory.COMBAT, BMItems.MORTYX_SWORD.get()
        );
        upgradeSmithing(pWriter, BMItems.MORTYX_UPGRADE_SMITHING_TEMPLATE.get(), BMItems.MAGNALYTH_AXE.get(), BMItems.MORTYX_INGOT.get(),
                RecipeCategory.COMBAT, BMItems.MORTYX_AXE.get()
        );
        upgradeSmithing(pWriter, BMItems.MORTYX_UPGRADE_SMITHING_TEMPLATE.get(), BMItems.MAGNALYTH_PICKAXE.get(), BMItems.MORTYX_INGOT.get(),
                RecipeCategory.TOOLS, BMItems.MORTYX_PICKAXE.get()
        );
        upgradeSmithing(pWriter, BMItems.MORTYX_UPGRADE_SMITHING_TEMPLATE.get(), BMItems.MAGNALYTH_SHOVEL.get(), BMItems.MORTYX_INGOT.get(),
                RecipeCategory.TOOLS, BMItems.MORTYX_SHOVEL.get()
        );
        upgradeSmithing(pWriter, BMItems.MORTYX_UPGRADE_SMITHING_TEMPLATE.get(), BMItems.MAGNALYTH_HOE.get(), BMItems.MORTYX_INGOT.get(),
                RecipeCategory.TOOLS, BMItems.MORTYX_HOE.get()
        );

        copySmithingTemplate(pWriter, BMItems.MORTYX_UPGRADE_SMITHING_TEMPLATE.get(), Items.BONE_BLOCK, Items.GOLD_INGOT);
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

    protected static void upgradeSmithing(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item pTemplateItem, Item pIngredientItem, Item pMaterialItem, RecipeCategory pCategory, Item pResultItem) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(pTemplateItem), Ingredient.of(pIngredientItem), Ingredient.of(pMaterialItem), pCategory, pResultItem)
                .unlocks(getHasName(pMaterialItem), has(pMaterialItem)).save(pFinishedRecipeConsumer, Behemoths.prefix(getItemName(pResultItem) + "_smithing"));
    }

    protected static void copySmithingTemplate(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pResult, ItemLike pBaseItem, ItemLike pSurroundingItem) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult, 2)
                .define('#', pSurroundingItem).define('C', pBaseItem).define('S', pResult)
                .pattern("#S#")
                .pattern("#C#")
                .pattern("###")
                .unlockedBy(getHasName(pResult), has(pResult))
                .save(pFinishedRecipeConsumer)
        ;
    }

    private static String path(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem()).getPath();
    }
}
