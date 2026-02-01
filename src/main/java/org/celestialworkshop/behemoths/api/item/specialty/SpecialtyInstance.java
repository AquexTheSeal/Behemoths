package org.celestialworkshop.behemoths.api.item.specialty;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.registries.BMItemSpecialties;

public record SpecialtyInstance(ItemSpecialty specialty, int level) {

    public static final Codec<SpecialtyInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("id").xmap(
                    g -> BMItemSpecialties.REGISTRY.get().getValue(g), ItemSpecialty::id).forGetter(SpecialtyInstance::specialty),
            Codec.INT.fieldOf("level").forGetter(SpecialtyInstance::level)
    ).apply(instance, SpecialtyInstance::new));

   public ResourceLocation getTextureLocation() {
        return ResourceLocation.fromNamespaceAndPath(specialty.id().getNamespace(), "textures/item_specialties/" + specialty.id().getPath() + ".png");
    }

    public ResourceLocation getDisplayTextureLocation() {
        return specialty.getTextureLocation();
    }

    public Component getDisplayName() {
        return specialty.getDisplayName(level);
    }

    public Component getDisplayDescription() {
        return specialty.getDisplayDescription(level);
    }
}
