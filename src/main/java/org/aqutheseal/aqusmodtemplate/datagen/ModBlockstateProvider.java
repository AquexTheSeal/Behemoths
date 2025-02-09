package org.aqutheseal.aqusmodtemplate.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.aqutheseal.aqusmodtemplate.Aqusmodtemplate;

public class ModBlockstateProvider extends BlockStateProvider {

    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Aqusmodtemplate.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
    }
}
