package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import org.celestialworkshop.behemoths.api.entity.BehemothProperties;
import org.celestialworkshop.behemoths.datagen.parents.BehemothPropertiesProvider;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;

public class BMBehemothPropertiesProvider extends BehemothPropertiesProvider {
    public BMBehemothPropertiesProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void addEntries() {
        this.add(BMEntityTypes.ARCHZOMBIE.get(), new BehemothProperties(800));
        this.add(BMEntityTypes.BANISHING_STAMPEDE.get(), new BehemothProperties(500));
        this.add(BMEntityTypes.HOLLOWBORNE.get(), new BehemothProperties(800));
        this.add(BMEntityTypes.HOLLOWBORNE_TURRET.get(), new BehemothProperties(500));
    }
}
