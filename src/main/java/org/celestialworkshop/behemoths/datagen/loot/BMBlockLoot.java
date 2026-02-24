package org.celestialworkshop.behemoths.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.datagen.BMLootTableProvider;
import org.celestialworkshop.behemoths.registries.BMBlocks;

import java.util.Set;

public class BMBlockLoot extends BlockLootSubProvider {

    public BMBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
         this.dropSelf(BMBlocks.MAGNALYTH_BLOCK.get());
        this.dropSelf(BMBlocks.MORTYX_BLOCK.get());

        this.dropSelf(BMBlocks.PHANTASHROOM.get());
        this.dropWhenSilkTouch(BMBlocks.PHANTASHROOM_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BMLootTableProvider.knownSet(ForgeRegistries.BLOCKS);
    }
}
