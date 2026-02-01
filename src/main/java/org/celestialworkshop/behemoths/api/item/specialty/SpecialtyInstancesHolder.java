package org.celestialworkshop.behemoths.api.item.specialty;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record SpecialtyInstancesHolder(List<SpecialtyInstance> specialties) {

    public static final Codec<SpecialtyInstancesHolder> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SpecialtyInstance.CODEC.listOf().fieldOf("specialties").forGetter(SpecialtyInstancesHolder::specialties)
    ).apply(instance, SpecialtyInstancesHolder::new));
}
