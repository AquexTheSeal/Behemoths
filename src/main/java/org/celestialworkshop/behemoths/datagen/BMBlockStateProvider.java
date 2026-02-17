package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.registries.BMBlocks;

public class BMBlockStateProvider extends BlockStateProvider {
    public BMBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Behemoths.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(BMBlocks.MAGNALYTH_BLOCK.get());
        simpleBlock(BMBlocks.MORTYX_BLOCK.get());
    }
}
