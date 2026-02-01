package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TieredItem;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;
import org.celestialworkshop.behemoths.datagen.parents.ItemSpecialtyProvider;
import org.celestialworkshop.behemoths.registries.BMItemSpecialties;
import org.celestialworkshop.behemoths.registries.BMItemTiers;
import org.celestialworkshop.behemoths.registries.BMItems;

public class BMItemSpecialtyProvider extends ItemSpecialtyProvider {

    public BMItemSpecialtyProvider(PackOutput output) {
        super(output, Behemoths.MODID);
    }

    @Override
    protected void addEntries() {
        for (RegistryObject<Item> obj : BMItems.ITEMS.getEntries()) {
            Item item = obj.get();
            if (item instanceof TieredItem tieredItem) {
                if (tieredItem.getTier() == BMItemTiers.MAGNALYTH) {
                    this.add(item, new SpecialtyInstance(BMItemSpecialties.BEHEMOTH_DAMAGE_BONUS.get(), 0));
                }
            }
        }
    }
}