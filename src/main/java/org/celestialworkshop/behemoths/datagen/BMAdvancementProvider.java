package org.celestialworkshop.behemoths.datagen;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.KilledTrigger;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.advancmeents.KillBehemothTrigger;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class BMAdvancementProvider extends ForgeAdvancementProvider {

    public BMAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new Advancements()));
    }

    public static class Advancements implements ForgeAdvancementProvider.AdvancementGenerator {

        public Advancement root;

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<Advancement> saver, ExistingFileHelper existingFileHelper) {

            root = Advancement.Builder.advancement()
                    .display(BMItems.BEHEMOTH_HEART.get(), asTitle("kill_behemoth"), asDescription("kill_behemoth"),
                            Behemoths.prefix("textures/block/magnalyth_block.png"),
                            FrameType.GOAL, true, true, false)
                    .addCriterion("killed_behemoth", KillBehemothTrigger.TriggerInstance.any())
                    .save(saver, "behemoths:killed_behemoth");

            Advancement obtainHeart = obtainItemBasic(BMItems.BEHEMOTH_HEART, root, FrameType.GOAL, saver);

            Advancement obtainMagnalyth = obtainItemBasic(BMItems.MAGNALYTH_INGOT, obtainHeart, FrameType.TASK, saver);

            Advancement tameHollowborne = Advancement.Builder.advancement()
                    .display(Items.SOUL_SAND, asTitle("tame_hollowborne"), asDescription("tame_hollowborne"),
                            null, FrameType.TASK, true, true, false)
                    .addCriterion("tamed_hollowborne", TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity().of(BMEntityTypes.HOLLOWBORNE.get()).build()))
                    .save(saver, "behemoths:tamed_hollowborne");
        }
    }

    public static Advancement obtainItemBasic(RegistryObject<Item> item, Advancement parent, FrameType frameType, Consumer<Advancement> saver) {
        String name = item.getId().getPath();
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(
                        item.get(),
                        asTitle("obtain_" + name), asDescription("obtain_" + name),
                        null, frameType, true, true, false
                )
                .addCriterion("obtained_" + name, InventoryChangeTrigger.TriggerInstance.hasItems(item.get()))
                .save(saver, "behemoths:obtain_" + name);
    }

    public static <T extends Entity> Advancement killEntity(RegistryObject<EntityType<T>> entity, RegistryObject<Item> displayItem, Advancement parent, FrameType frameType, Consumer<Advancement> saver) {
        String name = entity.getId().getPath();
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(
                        displayItem.get(),
                        asTitle("kill_" + name), asDescription("kill_" + name),
                        null, frameType, true, false, false
                )
                .addCriterion("killed_" + name, KilledTrigger.TriggerInstance.playerKilledEntity(EntityPredicate.Builder.entity().of(entity.get())))
                .save(saver, "behemoths:kill_" + name);
    }

    public static Component asTitle(String name) {
        return Component.translatable("advancement.behemoths." + name + ".title");
    }

    public static Component asDescription(String name) {
        return Component.translatable("advancement.behemoths." + name + ".description");
    }
}
