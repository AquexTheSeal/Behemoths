package org.celestialworkshop.behemoths.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.celestialworkshop.behemoths.Behemoths;

public final class BMConfigManager {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final BMCommonConfig COMMON;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final BMClientConfig CLIENT;

    static {
        final Pair<BMCommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(BMCommonConfig::new);
        final Pair<BMClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(BMClientConfig::new);

        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();

        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static void registerConfigs() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, Behemoths.MODID + "/behemoths-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_SPEC, Behemoths.MODID + "/behemoths-client.toml");
    }
}
