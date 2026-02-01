package org.celestialworkshop.behemoths.registries;

import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.items.BehebuggerItem;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;

public class BMItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Behemoths.MODID);

    // SPAWN EGGS
    public static final RegistryObject<Item> ARCHZOMBIE_SPAWN_EGG = ITEMS.register("archzombie_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.ARCHZOMBIE, 0x41b050, 0xcfffd5, new Item.Properties()));
    public static final RegistryObject<Item> BANISHING_STAMPEDE_SPAWN_EGG = ITEMS.register("banishing_stampede_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.BANISHING_STAMPEDE, 0x286344, 0x6db369, new Item.Properties()));

    // MISC
    public static final RegistryObject<Item> BEHEBUGGER = ITEMS.register("behebugger", () -> new BehebuggerItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));

    // MATERIALS
    public static final RegistryObject<Item> BEHEMOTH_HEART = ITEMS.register("behemoth_heart", () -> new BehemothHeartItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));

    // MAGNALYTH
    public static final RegistryObject<Item> MAGNALYTH_INGOT = ITEMS.register("magnalyth_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_NUGGET = ITEMS.register("magnalyth_nugget", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MAGNALYTH_SWORD = ITEMS.register("magnalyth_sword", () -> new SwordItem(BMItemTiers.MAGNALYTH, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_AXE = ITEMS.register("magnalyth_axe", () -> new AxeItem(BMItemTiers.MAGNALYTH, 6.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_PICKAXE = ITEMS.register("magnalyth_pickaxe", () -> new PickaxeItem(BMItemTiers.MAGNALYTH, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_SHOVEL = ITEMS.register("magnalyth_shovel", () -> new ShovelItem(BMItemTiers.MAGNALYTH, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_HOE = ITEMS.register("magnalyth_hoe", () -> new HoeItem(BMItemTiers.MAGNALYTH, -3, 0.0F, new Item.Properties()));

}
