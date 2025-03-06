package org.aqutheseal.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.aqutheseal.behemoths.Behemoths;

public class BMBlockstateProvider extends BlockStateProvider {

    public BMBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Behemoths.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
