package org.celestialworkshop.behemoths.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.registries.BMBlocks;
import org.celestialworkshop.behemoths.registries.BMItems;
import org.celestialworkshop.behemoths.registries.BMTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BMTagsProvider {

    public static class ItemsProvider extends ItemTagsProvider {
        public ItemsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTags.contentsGetter(), Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(ItemTags.SWORDS).add(this.getItemsOfClass(SwordItem.class));
            this.tag(ItemTags.AXES).add(this.getItemsOfClass(AxeItem.class));
            this.tag(ItemTags.PICKAXES).add(this.getItemsOfClass(PickaxeItem.class));
            this.tag(ItemTags.SHOVELS).add(this.getItemsOfClass(ShovelItem.class));
            this.tag(ItemTags.HOES).add(this.getItemsOfClass(HoeItem.class));

            this.tag(BMTags.Items.HOLLOWBORNE_FOOD).add(
                    Items.SOUL_SAND,  Items.SOUL_SOIL,  Items.SOUL_TORCH, Items.SOUL_LANTERN, Items.SOUL_CAMPFIRE
            );
            this.tag(BMTags.Items.HOLLOWBORNE_FOOD).addTag(
                    ItemTags.SOUL_FIRE_BASE_BLOCKS
            );
        }

        public Item[] getItemsOfClass(Class<? extends Item> className) {
            return BMItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(className::isInstance).toArray(Item[]::new);
        }
    }

    public static class BlocksProvider extends BlockTagsProvider {
        public BlocksProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                    BMBlocks.MAGNALYTH_BLOCK.get(),
                    BMBlocks.MORTYX_BLOCK.get()
            );

            this.tag(BlockTags.NEEDS_IRON_TOOL).add(
                    BMBlocks.MAGNALYTH_BLOCK.get()
            );

            this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                    BMBlocks.MORTYX_BLOCK.get()
            );
        }
    }

    public static class EntityTypesProvider extends EntityTypeTagsProvider {
        public EntityTypesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
        }
    }

    public static class BiomesProvider extends BiomeTagsProvider {

        public BiomesProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @org.jetbrains.annotations.Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pProvider, Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(BMTags.Biomes.SHOULD_SPAWN_ARCHZOMBIES)
                    .addTag(Tags.Biomes.IS_PLAINS)
                    .addTag(BiomeTags.IS_SAVANNA)
                    .addTag(BiomeTags.IS_JUNGLE)
                    .addTag(BiomeTags.IS_FOREST)
                    .addTag(Tags.Biomes.IS_PLATEAU)
                    .addTag(BiomeTags.IS_MOUNTAIN)
                    .addTag(Tags.Biomes.IS_SWAMP)
            ;

            this.tag(BMTags.Biomes.CONTAINS_CHARYDBIS_ZONE)
                    .addTag(Tags.Biomes.IS_PLAINS)
                    .addTag(Tags.Biomes.IS_PLATEAU)
                    .addTag(BiomeTags.IS_MOUNTAIN)
            ;
        }
    }
}