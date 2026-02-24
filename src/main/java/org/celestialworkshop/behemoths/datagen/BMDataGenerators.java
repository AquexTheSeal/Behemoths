package org.celestialworkshop.behemoths.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.Behemoths;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BMDataGenerators {

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper efh = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new BMSoundDefinitionsProvider(packOutput, efh));
        generator.addProvider(event.includeClient(), new BMLanguageProvider(packOutput, "en_us"));
        generator.addProvider(event.includeClient(), new BMItemModelProvider(packOutput, efh));
        generator.addProvider(event.includeClient(), new BMBlockStateProvider(packOutput, efh));

        BMTagsProvider.BlocksProvider blockTagsProvider = new BMTagsProvider.BlocksProvider(packOutput, lookupProvider, efh);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new BMTagsProvider.ItemsProvider(packOutput, lookupProvider, blockTagsProvider, efh));
        generator.addProvider(event.includeServer(), new BMTagsProvider.EntityTypesProvider(packOutput, lookupProvider, efh));
        generator.addProvider(event.includeServer(), new BMTagsProvider.BiomesProvider(packOutput, lookupProvider, efh));
        generator.addProvider(event.includeServer(), new BMItemSpecialtyProvider(packOutput));
        generator.addProvider(event.includeServer(), new BMRecipeProvider(packOutput));
        generator.addProvider(event.includeServer(), new BMBehemothPropertiesProvider(packOutput));
        generator.addProvider(event.includeServer(), new BMLootTableProvider(packOutput));
        generator.addProvider(event.includeServer(), new BMAdvancementProvider(packOutput, lookupProvider, efh));
        generator.addProvider(event.includeServer(), new BMGlobalLootModifierProvider(packOutput));

        generator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(packOutput, lookupProvider, new RegistrySetBuilder()
                .add(ForgeRegistries.Keys.BIOME_MODIFIERS, BMMobSpawnsProvider::bootstrapBiome)
                .add(Registries.STRUCTURE, BMStructuresProvider::bootstrapStructure)
                .add(Registries.STRUCTURE_SET, BMStructuresProvider::bootstrapStructureSet),
                Set.of(Behemoths.MODID)
        ));
    }
}
