package org.celestialworkshop.behemoths.datagen.reloadlisteners;

import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.api.entity.BehemothProperties;

import java.util.HashMap;
import java.util.Map;

public class BehemothPropertiesReloadListener extends CodecBasedReloadListener<BehemothProperties> {

    public static final Map<ResourceLocation, BehemothProperties> PROPERTY_DATA = new HashMap<>();

    public BehemothPropertiesReloadListener() {
        super("behemoths/behemoth_properties", BehemothProperties.CODEC, "Behemoth Properties");
    }

    @Override
    public Map<ResourceLocation, BehemothProperties> getMainData() {
        return PROPERTY_DATA;
    }

    public static BehemothProperties getProperties(ResourceLocation entityId) {
        return PROPERTY_DATA.get(entityId);
    }

    public static boolean hasProperties(ResourceLocation entityId) {
        return PROPERTY_DATA.containsKey(entityId);
    }
}
