package org.aqutheseal.behemoths;

import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.aqutheseal.behemoths.datagen.*;
import org.aqutheseal.behemoths.registry.*;
import org.slf4j.Logger;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod(Behemoths.MODID)
public class Behemoths {

    public static final String MODID = "behemoths";
    public static final String MODNAME = "Behemoths";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Behemoths() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        MinecraftForge.EVENT_BUS.register(this);

        BMItems.ITEMS.register(modEventBus);
        BMItems.initGearSets();
        BMCreativeTabs.CREATIVE_TABS.register(modEventBus);
        BMEntityTypes.ENTITIES.register(modEventBus);
        BMParticleTypes.PARTICLE_TYPES.register(modEventBus);
        BMSoundEvents.SOUND_EVENTS.register(modEventBus);
        BMStructures.STRUCTURE_TYPE.register(modEventBus);
        BMStructures.STRUCTURE_PIECE.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC);

        modEventBus.addListener(this::gatherData);
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new BMItemModelProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new BMLanguageProvider(packOutput, "en_us"));
        generator.addProvider(event.includeClient(), new BMSoundDefinitionsProvider(packOutput, existingFileHelper));

        BMTagsProvider.Blocks blockTagsProvider = new BMTagsProvider.Blocks(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new BMTagsProvider.Items(packOutput, lookupProvider, blockTagsProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new BMTagsProvider.EntityTypes(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new BMTagsProvider.Biomes(packOutput, lookupProvider, existingFileHelper));
        generator.addProvider(event.includeServer(), new BMRecipeProvider(packOutput));

        event.getGenerator().addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
                packOutput, lookupProvider, new RegistrySetBuilder()
                .add(Registries.STRUCTURE_SET, BMStructureProvider.StructureSets::bootstrap)
                .add(Registries.STRUCTURE, BMStructureProvider.Structures::bootstrap),
                Set.of(Behemoths.MODID)
        ));
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }
}
