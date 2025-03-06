package org.aqutheseal.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.registry.BMItems;

public class BMLanguageProvider extends LanguageProvider {

    public BMLanguageProvider(PackOutput output, String locale) {
        super(output, Behemoths.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        for (RegistryObject<Item> item : BMItems.ITEMS.getEntries()) {
            this.addItem(item, WordUtils.capitalize(item.getId().getPath().replace("_", " ")));
        }

        this.add("item.behemoths.sky_beast_gear.base_bonus", "Behemoth Hunter");

        this.add("item.behemoths.sky_beast_armor.base_bonus_description", "Damage taken from Behemoths is reduced to 95% per gear.");
        this.add("item.behemoths.sky_beast_weapon.base_bonus_description", "Damage dealt to behemoths is increased by +100%.");

        this.add("item.behemoths.sky_beast_gear.effect_barren", "Dune Sky Beast's Blessing");
        this.add("item.behemoths.sky_beast_gear.effect_lush", "Free Sky Beast's Blessing");
        this.add("item.behemoths.sky_beast_gear.effect_northern", "Arctic Sky Beast's Blessing");
        this.add("item.behemoths.sky_beast_gear.effect_nether", "Scorching Sky Beast's Blessing");
        this.add("item.behemoths.sky_beast_gear.effect_soul", "Spirit Sky Beast's Blessing");
        this.add("item.behemoths.sky_beast_gear.effect_void", "Dark Sky Beast's Blessing");

        this.add("item.behemoths.sky_beast_armor.effect_description_barren", "Damage taken from burning is reduced to 80% per gear. Increases movement speed when in lava.");
        this.add("item.behemoths.sky_beast_armor.effect_description_lush", "Damage taken from falling is reduced to 80% per gear.");
        this.add("item.behemoths.sky_beast_armor.effect_description_northern", "Damage taken from freezing is reduced to 60% per gear. Nullifies sinking when on powdered snow.");
        this.add("item.behemoths.sky_beast_armor.effect_description_nether", "Damage taken from burning is nullified. Piglins will be friendly towards the wearer.");
        this.add("item.behemoths.sky_beast_armor.effect_description_soul", "Damage taken from burning is reduced to 50% per gear. Nullifies movement speed reduction from soul sand.");
        this.add("item.behemoths.sky_beast_armor.effect_description_void", "Damage taken from falling is nullified in The End dimension, otherwise it's reduced to 60% per gear. Getting hurt by void damage instantly teleports you to the Y block limit.");

        this.add("item.behemoths.sky_beast_weapon.effect_description_barren", "Damage dealt to burning targets is increased by +25%.");
        this.add("item.behemoths.sky_beast_weapon.effect_description_lush", "Critical hit damage is increased by +15%.");
        this.add("item.behemoths.sky_beast_weapon.effect_description_northern", "Freezes targets for 5 seconds.");
        this.add("item.behemoths.sky_beast_weapon.effect_description_nether", "Burns targets for 5 seconds. Damage dealt to burning targets is increased by +10%.");
        this.add("item.behemoths.sky_beast_weapon.effect_description_soul", "Damage increases based on movement velocity up to +50%");
        this.add("item.behemoths.sky_beast_weapon.effect_description_void", "Damage increases based on movement velocity with no limit. Damage dealt to bosses is increased by +30%.");

    }
}