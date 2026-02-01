package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.datagen.parents.HeartEnergyProvider;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;

public class BMHeartEnergyProvider extends HeartEnergyProvider {

    public BMHeartEnergyProvider(PackOutput output) {
        super(output, Behemoths.MODID);
    }

    @Override
    protected void addHeartEnergyValues() {
        this.addEntry(BMEntityTypes.ARCHZOMBIE.get(), 500);
        this.addEntry(BMEntityTypes.BANISHING_STAMPEDE.get(), 300);
    }
}
