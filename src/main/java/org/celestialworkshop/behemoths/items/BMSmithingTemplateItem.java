package org.celestialworkshop.behemoths.items;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.SmithingTemplateItem;
import org.celestialworkshop.behemoths.Behemoths;

import java.util.List;

public class BMSmithingTemplateItem {
    private static final ChatFormatting TITLE_FORMAT = ChatFormatting.GRAY;
    private static final ChatFormatting DESCRIPTION_FORMAT = ChatFormatting.BLUE;

    private static final ResourceLocation EMPTY_SLOT_HELMET = new ResourceLocation("item/empty_armor_slot_helmet");
    private static final ResourceLocation EMPTY_SLOT_CHESTPLATE = new ResourceLocation("item/empty_armor_slot_chestplate");
    private static final ResourceLocation EMPTY_SLOT_LEGGINGS = new ResourceLocation("item/empty_armor_slot_leggings");
    private static final ResourceLocation EMPTY_SLOT_BOOTS = new ResourceLocation("item/empty_armor_slot_boots");
    private static final ResourceLocation EMPTY_SLOT_HOE = new ResourceLocation("item/empty_slot_hoe");
    private static final ResourceLocation EMPTY_SLOT_AXE = new ResourceLocation("item/empty_slot_axe");
    private static final ResourceLocation EMPTY_SLOT_SWORD = new ResourceLocation("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_SLOT_SHOVEL = new ResourceLocation("item/empty_slot_shovel");
    private static final ResourceLocation EMPTY_SLOT_PICKAXE = new ResourceLocation("item/empty_slot_pickaxe");
    private static final ResourceLocation EMPTY_SLOT_INGOT = new ResourceLocation("item/empty_slot_ingot");

    public static SmithingTemplateItem createMortyxSmithingTemplate() {
        return createUpgradeSmithingTemplate("mortyx", toolSetIconList(), ingotMaterialList());
    }

    public static SmithingTemplateItem createUpgradeSmithingTemplate(String materialId, List<ResourceLocation> iconnList, List<ResourceLocation> materialList) {
        return new SmithingTemplateItem(
                Component.translatable(Util.makeDescriptionId("item", Behemoths.prefix("smithing_template." + materialId + "_upgrade.applies_to"))).withStyle(DESCRIPTION_FORMAT),
                Component.translatable(Util.makeDescriptionId("item", Behemoths.prefix("smithing_template." + materialId + "_upgrade.ingredients"))).withStyle(DESCRIPTION_FORMAT),
                Component.translatable(Util.makeDescriptionId("upgrade", Behemoths.prefix(materialId + "_upgrade"))).withStyle(TITLE_FORMAT),
                Component.translatable(Util.makeDescriptionId("item", Behemoths.prefix("smithing_template." + materialId + "_upgrade.base_slot_description"))),
                Component.translatable(Util.makeDescriptionId("item", Behemoths.prefix("smithing_template." + materialId + "_upgrade.additions_slot_description"))),
                iconnList, materialList
        );
    }

    public static List<ResourceLocation> toolSetIconList() {
        return List.of(EMPTY_SLOT_SWORD, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_AXE, EMPTY_SLOT_HOE, EMPTY_SLOT_SHOVEL);
    }

    public static List<ResourceLocation> ingotMaterialList() {
        return List.of(EMPTY_SLOT_INGOT);
    }
}
