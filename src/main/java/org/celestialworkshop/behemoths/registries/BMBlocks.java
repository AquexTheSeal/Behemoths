package org.celestialworkshop.behemoths.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;

import java.util.function.Supplier;

public class BMBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Behemoths.MODID);

    public static final RegistryObject<Block> MAGNALYTH_BLOCK = registerBlock("magnalyth_block", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).strength(6.0F, 7.5F))
    );

    public static final RegistryObject<Block> MORTYX_BLOCK = registerBlock("mortyx_block", () ->
            new Block(BlockBehaviour.Properties.copy(Blocks.NETHERITE_BLOCK).strength(12.0F, 10.0F))
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, new Item.Properties());
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, Item.Properties properties) {
        RegistryObject<T> registeredBlock = BLOCKS.register(name, block);
        BMItems.ITEMS.register(name, () -> new BlockItem(registeredBlock.get(), properties));
        return registeredBlock;
    }
}
