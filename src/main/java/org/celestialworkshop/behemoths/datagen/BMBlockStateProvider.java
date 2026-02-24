package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
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

        phantashroomBlock(BMBlocks.PHANTASHROOM.get());
        simpleOrientableBlock(BMBlocks.PHANTASHROOM_BLOCK.get());

    }

    public void phantashroomBlock(Block block) {
        ModelFile model = models().getExistingFile(Behemoths.prefix("block/phantashroom"));
        this.simpleBlock(block, model);
    }

    public void simpleOrientableBlock(Block block) {
        ModelFile model = models().cube(name(block),
                blockTexture(block).withSuffix("_bottom"),
                blockTexture(block).withSuffix("_top"),
                blockTexture(block).withSuffix("_side"),
                blockTexture(block).withSuffix("_side"),
                blockTexture(block).withSuffix("_side"),
                blockTexture(block).withSuffix("_side")
        ).texture("particle", blockTexture(block).withSuffix("_side"));
        this.simpleBlock(block, model);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

}
