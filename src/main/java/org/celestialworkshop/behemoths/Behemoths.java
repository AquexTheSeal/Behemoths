package org.celestialworkshop.behemoths;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.celestialworkshop.behemoths.config.BMConfigManager;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.registries.*;
import org.slf4j.Logger;

@Mod(Behemoths.MODID)
public class Behemoths {
    public static final String MODID = "behemoths";
    public static final String MODNAME = "Behemoths";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Behemoths() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        MinecraftForge.EVENT_BUS.register(this);

        BMNetwork.register();

        BMConfigManager.registerConfigs();

        BMEntityTypes.ENTITY_TYPES.register(modBus);
        BMCreativeTabs.CREATIVE_TABS.register(modBus);
        BMItems.ITEMS.register(modBus);
        BMBlocks.BLOCKS.register(modBus);
        BMParticleTypes.PARTICLE_TYPES.register(modBus);
        BMSoundEvents.SOUND_EVENTS.register(modBus);
        BMPandemoniumCurses.PANDEMONIUM_CURSES.register(modBus);
        BMItemSpecialties.ITEM_SPECIALTIES.register(modBus);
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
