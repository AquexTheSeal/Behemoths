package org.celestialworkshop.behemoths.api;

import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.registries.BMItemSpecialties;

public class ItemSpecialty {

    public ResourceLocation id() {
        return BMItemSpecialties.REGISTRY.get().getKey(this);
    }
}
