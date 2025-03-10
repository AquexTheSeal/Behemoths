package org.aqutheseal.behemoths.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.registry.BMEntityTypes;
import org.aqutheseal.behemoths.registry.BMTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class BMTagsProvider {

    public static class Items extends ItemTagsProvider {
        public Items(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, BlockTagsProvider blockTags, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, blockTags.contentsGetter(), Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
        }
    }

    public static class Blocks extends BlockTagsProvider {
        public Blocks(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
        }
    }

    public static class EntityTypes extends EntityTypeTagsProvider {
        public EntityTypes(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            this.tag(BMTags.Entities.BEHEMOTHS)
                    .add(BMEntityTypes.BARREN_SKY_CHARYDBIS.get())
                    .add(BMEntityTypes.LUSH_SKY_CHARYDBIS.get())
                    .add(BMEntityTypes.NORTHERN_SKY_CHARYDBIS.get())
                    .add(BMEntityTypes.NETHER_SKY_CHARYDBIS.get())
                    .add(BMEntityTypes.SOUL_SKY_CHARYDBIS.get())
                    .add(BMEntityTypes.VOID_SKY_CHARYDBIS.get())
            ;
        }
    }

    public static class Biomes extends BiomeTagsProvider {

        public Biomes(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @org.jetbrains.annotations.Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pProvider, Behemoths.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(BMTags.Biomes.HAS_CHARYDBIS_ISLES)
                    .addTag(BiomeTags.IS_OVERWORLD)
                    .addTag(BiomeTags.IS_NETHER)
                    .addTag(BiomeTags.IS_END)
            ;
        }
    }
}