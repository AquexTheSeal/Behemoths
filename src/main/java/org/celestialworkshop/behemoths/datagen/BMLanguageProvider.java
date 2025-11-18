package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMItems;

public class BMLanguageProvider extends LanguageProvider {

    public BMLanguageProvider(PackOutput output, String locale) {
        super(output, Behemoths.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        for (RegistryObject<Item> item : BMItems.ITEMS.getEntries()) {
            this.addItem(item, WordUtils.capitalize(item.getId().getPath().replace("_", " ")));
        }

        for (RegistryObject<EntityType<?>> en : BMEntityTypes.ENTITY_TYPES.getEntries()) {
            this.addEntityType(en, WordUtils.capitalize(en.getId().getPath().replace("_", " ")));
        }

    }
}