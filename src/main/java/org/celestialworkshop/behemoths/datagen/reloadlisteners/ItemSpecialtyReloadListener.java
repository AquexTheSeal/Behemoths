package org.celestialworkshop.behemoths.datagen.reloadlisteners;

import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSpecialtyReloadListener extends CodecBasedReloadListener<SpecialtyInstance.Wrapper> {

    public static final Map<ResourceLocation, SpecialtyInstance.Wrapper> SPECIALTY_DATA = new HashMap<>();

    public ItemSpecialtyReloadListener() {
        super("behemoths/item_specialties", SpecialtyInstance.Wrapper.CODEC, "Item Specialty");
    }

    @Override
    public Map<ResourceLocation, SpecialtyInstance.Wrapper> getMainData() {
        return SPECIALTY_DATA;
    }


    public static List<SpecialtyInstance> getSpecialtyData(ResourceLocation itemId) {
        return SPECIALTY_DATA.get(itemId).specialties();
    }

    public static boolean hasSpecialtyData(ResourceLocation itemId) {
        return SPECIALTY_DATA.containsKey(itemId);
    }
}
