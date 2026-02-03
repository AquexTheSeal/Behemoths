package org.celestialworkshop.behemoths.datagen.parents;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;

import java.util.List;

public abstract class ItemSpecialtyProvider extends CodecBasedProvider<SpecialtyInstance.Wrapper> {

    protected ItemSpecialtyProvider(PackOutput output, String modid) {
        super(output, modid, "behemoths/item_specialties", PackOutput.Target.DATA_PACK, SpecialtyInstance.Wrapper.CODEC);
    }

    @Override
    public String getName() {
        return "Item Specialties: " + modid;
    }

    public void add(Item item, SpecialtyInstance... specialties) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        if (id == null)
            throw new IllegalStateException("Unregistered item: " + item);

        add(id, List.of(specialties));
    }

    public void add(ResourceLocation itemId, SpecialtyInstance... specialties) {
        add(itemId, List.of(specialties));
    }

    public void add(ResourceLocation itemId, List<SpecialtyInstance> specialties) {
        if (data.put(itemId, new SpecialtyInstance.Wrapper(specialties)) != null)
            throw new IllegalStateException("Duplicate specialty data for " + itemId);
    }
}
