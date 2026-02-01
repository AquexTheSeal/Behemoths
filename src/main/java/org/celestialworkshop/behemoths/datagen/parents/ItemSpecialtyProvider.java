package org.celestialworkshop.behemoths.datagen.parents;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstancesHolder;

import java.util.List;

public abstract class ItemSpecialtyProvider extends CodecBasedProvider<SpecialtyInstancesHolder> {


    protected ItemSpecialtyProvider(PackOutput output, String modid) {
        super(output, modid, "item_specialties", PackOutput.Target.DATA_PACK, SpecialtyInstancesHolder.CODEC);
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
        if (data.put(itemId, new SpecialtyInstancesHolder(specialties)) != null)
            throw new IllegalStateException("Duplicate specialty data for " + itemId);
    }
}
