package org.aqutheseal.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.registry.BMItems;
import org.aqutheseal.behemoths.util.gear.BasicGearSet;

import java.util.Map;
import java.util.function.Consumer;

public class BMRecipeProvider extends RecipeProvider {
    private static final Map<String, String[]> TOOL_PATTERNS = Map.of(
            "sword", new String[]{"O  ", "O  ", "I  "},
            "axe", new String[]{"OO ", "OI ", " I "},
            "pickaxe", new String[]{"OOO", " I ", " I "},
            "shovel", new String[]{" O ", " I ",  " I "},
            "hoe", new String[]{"OO ", " I ", " I "}
    );

    private static final Map<String, String[]> ARMOR_PATTERNS = Map.of(
            "helmet", new String[]{"OOO", "O O", "   "},
            "chestplate", new String[]{"O O", "OOO", "OOO"},
            "leggings", new String[]{"OOO", "O O", "O O"},
            "boots", new String[]{"O O", "O O", "   "}
    );

    public BMRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        for (BasicGearSet gearSet : BMItems.GEAR_SETS) {
            this.materialSet(pWriter, gearSet);
        }
    }

    private void materialSet(Consumer<FinishedRecipe> pWriter, BasicGearSet materialSet) {
        TOOL_PATTERNS.forEach((toolType, pattern) -> createToolRecipe(pWriter, materialSet, toolType, pattern));
        ARMOR_PATTERNS.forEach((armorType, pattern) -> createArmorRecipe(pWriter, materialSet, armorType, pattern));
    }

    private void createToolRecipe(Consumer<FinishedRecipe> pWriter, BasicGearSet set, String toolType, String[] pattern) {
        RegistryObject<Item> tool = switch (toolType) {
            case "sword" -> set.sword;
            case "axe" -> set.axe;
            case "pickaxe" -> set.pickaxe;
            case "shovel" -> set.shovel;
            case "hoe" -> set.hoe;
            default -> throw new IllegalArgumentException("Unknown gear set item: " + toolType);
        };
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, tool.get())
                .define('O', set.baseMaterial.get()).define('I', set.stickMaterial.get()).pattern(pattern[0]).pattern(pattern[1]).pattern(pattern[2])
                .unlockedBy("has_material", has(set.baseMaterial.get())).save(pWriter);
    }

    private void createArmorRecipe(Consumer<FinishedRecipe> pWriter, BasicGearSet set, String armorType, String[] pattern) {
        RegistryObject<Item> armor = switch (armorType) {
            case "helmet" -> set.helmet;
            case "chestplate" -> set.chestplate;
            case "leggings" -> set.leggings;
            case "boots" -> set.boots;
            default -> throw new IllegalArgumentException("Unknown gear set item: " + armorType);
        };
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, armor.get())
                .define('O', set.baseMaterial.get()).pattern(pattern[0]).pattern(pattern[1]).pattern(pattern.length > 2 ? pattern[2] : "")
                .unlockedBy("has_material", has(set.baseMaterial.get())).save(pWriter);
    }

}
