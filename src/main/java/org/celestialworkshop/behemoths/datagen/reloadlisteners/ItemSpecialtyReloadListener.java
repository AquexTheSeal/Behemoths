package org.celestialworkshop.behemoths.datagen.reloadlisteners;

import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstancesHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSpecialtyReloadListener extends CodecBasedReloadListener<SpecialtyInstancesHolder> {

    public static final Map<ResourceLocation, SpecialtyInstancesHolder> SPECIALTY_DATA = new HashMap<>();

    public ItemSpecialtyReloadListener() {
        super("item_specialties", SpecialtyInstancesHolder.CODEC, "Item Specialty");
    }

    @Override
    public Map<ResourceLocation, SpecialtyInstancesHolder> getMainData() {
        return SPECIALTY_DATA;
    }


    public static List<SpecialtyInstance> getSpecialtyData(ResourceLocation itemId) {
        return SPECIALTY_DATA.get(itemId).specialties();
    }

    public static boolean hasSpecialtyData(ResourceLocation itemId) {
        return SPECIALTY_DATA.containsKey(itemId);
    }
}
