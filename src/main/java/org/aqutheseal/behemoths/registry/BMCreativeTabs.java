package org.aqutheseal.behemoths.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;

public class BMCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Behemoths.MODID);

    public static final RegistryObject<CreativeModeTab> BEHEMOTHS = CREATIVE_TABS.register("behemoths", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group." + Behemoths.MODID + ".behemoths"))
            .icon(() -> new ItemStack(BMItems.BARREN_SKY_BEAST_SKIN.get()))
            .displayItems((params, output) -> {
                BMItems.ITEMS.getEntries().stream().map(RegistryObject::get).forEach(output::accept);
            })
            .build()
    );
}
