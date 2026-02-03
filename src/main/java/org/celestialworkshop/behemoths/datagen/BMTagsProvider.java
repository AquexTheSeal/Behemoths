package org.celestialworkshop.behemoths.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.registries.BMBlocks;
import org.celestialworkshop.behemoths.registries.BMItems;
import org.celestialworkshop.behemoths.registries.BMTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BMTagsProvider {

    public static class Items extends ItemTagsProvider {
        public Items(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTags.contentsGetter(), Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(ItemTags.SWORDS).add(
                    BMItems.MAGNALYTH_SWORD.get()
            );

            this.tag(ItemTags.AXES).add(
                    BMItems.MAGNALYTH_AXE.get()
            );

            this.tag(ItemTags.PICKAXES).add(
                    BMItems.MAGNALYTH_PICKAXE.get()
            );

            this.tag(ItemTags.SHOVELS).add(
                    BMItems.MAGNALYTH_SHOVEL.get()
            );

            this.tag(ItemTags.HOES).add(
                    BMItems.MAGNALYTH_HOE.get()
            );
        }
    }

    public static class Blocks extends BlockTagsProvider {
        public Blocks(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                    BMBlocks.MAGNALYTH_BLOCK.get()
            );

            this.tag(BlockTags.NEEDS_IRON_TOOL).add(
                    BMBlocks.MAGNALYTH_BLOCK.get()
            );
        }
    }

    public static class EntityTypes extends EntityTypeTagsProvider {
        public EntityTypes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
        }
    }

    public static class Biomes extends BiomeTagsProvider {

        public Biomes(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @org.jetbrains.annotations.Nullable ExistingFileHelper existingFileHelper) {
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
        }
    }
}