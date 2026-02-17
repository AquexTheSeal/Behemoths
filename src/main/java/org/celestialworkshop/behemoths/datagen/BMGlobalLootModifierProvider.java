package org.celestialworkshop.behemoths.datagen;

import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.loot.CurseScalableLootModifier;
import org.celestialworkshop.behemoths.registries.BMItems;

public class BMGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public BMGlobalLootModifierProvider(PackOutput output) {
        super(output, Behemoths.MODID);
    }

    @Override
    protected void start() {
        this.add("mortyx_template_underground", new CurseScalableLootModifier(
                new LootItemCondition[]{
                        AnyOfCondition.anyOf(mostlyUndergroundStructures()).build()
                },
                BMItems.MORTYX_UPGRADE_SMITHING_TEMPLATE.get(), 1, 0.0f, 0.05f, 0.6f
        ));
    }

    public LootItemCondition.Builder[] mostlyUndergroundStructures() {
        return new LootItemCondition.Builder[]{
                LootTableIdCondition.builder(ResourceLocation.parse("chests/simple_dungeon")),
                LootTableIdCondition.builder(ResourceLocation.parse("chests/abandoned_mineshaft")),
                LootTableIdCondition.builder(ResourceLocation.parse("chests/ancient_city")),
                LootTableIdCondition.builder(ResourceLocation.parse("chests/stronghold_corridor")),
                LootTableIdCondition.builder(ResourceLocation.parse("chests/stronghold_crossing")),
                LootTableIdCondition.builder(ResourceLocation.parse("chests/stronghold_library")),
                LootTableIdCondition.builder(ResourceLocation.parse("chests/desert_pyramid")),
                LootTableIdCondition.builder(ResourceLocation.parse("chests/jungle_temple")),
                LocationCheck.checkLocation(LocationPredicate.Builder.location().setY(MinMaxBounds.Doubles.atMost(50.0)))
        };
    }
}