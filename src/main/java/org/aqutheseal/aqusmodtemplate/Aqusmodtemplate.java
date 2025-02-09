package org.aqutheseal.aqusmodtemplate;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Aqusmodtemplate.MODID)
public class Aqusmodtemplate {

    public static final String MODID = "aqusmodtemplate";
    public static final String MODNAME = "AqusModTemplate";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Aqusmodtemplate() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModCommonConfig.SPEC);
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }
}
