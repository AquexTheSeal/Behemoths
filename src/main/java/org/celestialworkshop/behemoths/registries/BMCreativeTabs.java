package org.celestialworkshop.behemoths.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;

import java.util.List;

public class BMCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Behemoths.MODID);

    public static final List<RegistryObject<?>> BLACKLIST = List.of(
            BMItems.HOLLOWBORNE_SPAWN_EGG,
            BMItems.HOLLOWBORNE_TURRET_SPAWN_EGG
    );

    public static final RegistryObject<CreativeModeTab> BEHEMOTHS = CREATIVE_TABS.register("behemoths", () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group." + Behemoths.MODID + ".behemoths"))
            .icon(() -> new ItemStack(BMItems.MAGNALYTH_SWORD.get()))
            .displayItems((params, output) -> {
                BMItems.ITEMS.getEntries().stream().filter(entry -> !BLACKLIST.contains(entry)).map(RegistryObject::get).forEach(output::accept);
            })
            .build()
    );
}
