package org.celestialworkshop.behemoths.datagen.parents;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.entity.BehemothProperties;

public abstract class BehemothPropertiesProvider extends CodecBasedProvider<BehemothProperties> {

    protected BehemothPropertiesProvider(PackOutput output) {
        super(output, Behemoths.MODID, "behemoths/behemoth_properties", PackOutput.Target.DATA_PACK, BehemothProperties.CODEC);
    }

    @Override
    public String getName() {
        return "Behemoth Properties: " + modid;
    }

    public void add(EntityType<?> entityType, BehemothProperties properties) {
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        if (id == null)
            throw new IllegalStateException("Unregistered entityType: " + entityType);

        add(id, properties);
    }

    public void add(ResourceLocation entityId, BehemothProperties properties) {
        if (data.put(entityId, properties) != null)
            throw new IllegalStateException("Duplicate BehemothProperties data for " + entityId);
    }
}
