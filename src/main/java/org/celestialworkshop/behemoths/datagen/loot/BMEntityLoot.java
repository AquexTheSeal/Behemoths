package org.celestialworkshop.behemoths.datagen.loot;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.datagen.BMLootTableProvider;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMItems;

import java.util.stream.Stream;

public class BMEntityLoot extends EntityLootSubProvider {
    public BMEntityLoot() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
         this.add(BMEntityTypes.ARCHZOMBIE.get(), LootTable.lootTable()
                 .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2)).add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
                         .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                         .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                 ))
                 .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(Items.IRON_INGOT)
                         .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                         .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                 ))
                 .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2)).add(LootItem.lootTableItem(Items.IRON_NUGGET)
                         .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                         .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                 ))
                 .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(BMItems.BEHEMOTH_HEART.get())
                         .when(LootItemKilledByPlayerCondition.killedByPlayer())
                         .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.17F, 0.02F))
                 ))
         );

        this.add(BMEntityTypes.BANISHING_STAMPEDE.get(), LootTable.lootTable()
                .withPool(LootPool.lootPool().setRolls(UniformGenerator.between(1, 2)).add(LootItem.lootTableItem(Items.ROTTEN_FLESH)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                ))
                .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(BMItems.BEHEMOTH_HEART.get())
                        .when(LootItemKilledByPlayerCondition.killedByPlayer())
                        .when(LootItemRandomChanceWithLootingCondition.randomChanceAndLootingBoost(0.12F, 0.02F))
                ))
        );
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        return BMLootTableProvider.knownSet(ForgeRegistries.ENTITY_TYPES).stream();
    }
}
