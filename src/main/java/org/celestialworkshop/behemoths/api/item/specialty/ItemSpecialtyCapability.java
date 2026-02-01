package org.celestialworkshop.behemoths.api.item.specialty;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.registries.BMItemSpecialties;

import java.util.ArrayList;
import java.util.List;

public class ItemSpecialtyCapability implements INBTSerializable<CompoundTag> {

    private final List<SpecialtyInstance> specialties = new ArrayList<>();

    public List<SpecialtyInstance> getSpecialties() {
        return new ArrayList<>(specialties);
    }

    public void addSpecialtyInstance(SpecialtyInstance specialty) {
        if (specialty != null && !hasSpecialty(specialty.specialty())) {
            specialties.add(specialty);
        }
    }

    public boolean hasSpecialty(ItemSpecialty specialty) {
        return specialties.stream().anyMatch(s -> s.specialty().equals(specialty));
    }

    public int getSpecialtyLevel(ItemSpecialty specialty) {
        return specialties.stream()
                .filter(s -> s.specialty().equals(specialty))
                .findFirst()
                .map(SpecialtyInstance::level)
                .orElse(-1);
    }

    public void clearSpecialties() {
        specialties.clear();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        ListTag listTag = new ListTag();

        for (SpecialtyInstance specialty : specialties) {
            CompoundTag specialtyTag = new CompoundTag();
            specialtyTag.putString("specialty", specialty.specialty().id().toString());
            specialtyTag.putInt("level", specialty.level());
            listTag.add(specialtyTag);
        }

        tag.put("specialties", listTag);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag tag) {
        specialties.clear();

        if (tag.contains("specialties", Tag.TAG_LIST)) {
            ListTag listTag = tag.getList("specialties", Tag.TAG_COMPOUND);

            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag specialtyTag = listTag.getCompound(i);
                ResourceLocation specialtyId = ResourceLocation.parse(specialtyTag.getString("specialty"));
                int level = specialtyTag.getInt("level");

                ItemSpecialty specialty = BMItemSpecialties.REGISTRY.get().getValue(specialtyId);
                if (specialty != null) {
                    specialties.add(new SpecialtyInstance(specialty, level));
                }
            }
        }
    }
}
