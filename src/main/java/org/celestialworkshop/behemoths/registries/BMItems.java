package org.celestialworkshop.behemoths.registries;

import net.minecraft.world.item.*;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.item.CustomSaddleItem;
import org.celestialworkshop.behemoths.items.BMSmithingTemplateItem;
import org.celestialworkshop.behemoths.items.BehebuggerItem;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;

public class BMItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Behemoths.MODID);

    // SPAWN EGGS
    public static final RegistryObject<Item> ARCHZOMBIE_SPAWN_EGG = ITEMS.register("archzombie_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.ARCHZOMBIE, 0x41b050, 0xcfffd5, new Item.Properties()));
    public static final RegistryObject<Item> BANISHING_STAMPEDE_SPAWN_EGG = ITEMS.register("banishing_stampede_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.BANISHING_STAMPEDE, 0x286344, 0x6db369, new Item.Properties()));
    public static final RegistryObject<Item> HOLLOWBORNE_SPAWN_EGG = ITEMS.register("hollowborne_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.HOLLOWBORNE, 0x4b5e5e, 0xffeeed, new Item.Properties()));
    public static final RegistryObject<Item> HOLLOWBORNE_TURRET_SPAWN_EGG = ITEMS.register("hollowborne_turret_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.HOLLOWBORNE_TURRET, 0x5e504b, 0xffeeed, new Item.Properties()));

    // MISC
    public static final RegistryObject<Item> BEHEBUGGER = ITEMS.register("behebugger", () -> new BehebuggerItem(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> BEHEMOTH_SADDLE = ITEMS.register("behemoth_saddle", () -> new CustomSaddleItem(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> BEHEMOTH_HARNESS = ITEMS.register("behemoth_harness", () -> new CustomSaddleItem(new Item.Properties().stacksTo(1)));

    // MATERIALS
    public static final RegistryObject<Item> BEHEMOTH_HEART = ITEMS.register("behemoth_heart", () -> new BehemothHeartItem(new Item.Properties().stacksTo(1).rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SAVAGE_FLESH = ITEMS.register("savage_flesh", () -> new Item(new Item.Properties().food(BMFoods.SAVAGE_FLESH)));
    public static final RegistryObject<Item> COLOSSUS_BONE = ITEMS.register("colossus_bone", () -> new Item(new Item.Properties()));

    // MAGNALYTH
    public static final RegistryObject<Item> MAGNALYTH_INGOT = ITEMS.register("magnalyth_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_NUGGET = ITEMS.register("magnalyth_nugget", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MAGNALYTH_SWORD = ITEMS.register("magnalyth_sword", () -> new SwordItem(BMItemTiers.MAGNALYTH, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_AXE = ITEMS.register("magnalyth_axe", () -> new AxeItem(BMItemTiers.MAGNALYTH, 6.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_PICKAXE = ITEMS.register("magnalyth_pickaxe", () -> new PickaxeItem(BMItemTiers.MAGNALYTH, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_SHOVEL = ITEMS.register("magnalyth_shovel", () -> new ShovelItem(BMItemTiers.MAGNALYTH, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> MAGNALYTH_HOE = ITEMS.register("magnalyth_hoe", () -> new HoeItem(BMItemTiers.MAGNALYTH, -3, 0.0F, new Item.Properties()));

    // MORTYX
    public static final RegistryObject<Item> MORTYX_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("mortyx_upgrade_smithing_template", BMSmithingTemplateItem::createMortyxSmithingTemplate);

    public static final RegistryObject<Item> MORTYX_INGOT = ITEMS.register("mortyx_ingot", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> MORTYX_SWORD = ITEMS.register("mortyx_sword", () -> new SwordItem(BMItemTiers.MORTYX, 3, -2.4F, new Item.Properties()));
    public static final RegistryObject<Item> MORTYX_AXE = ITEMS.register("mortyx_axe", () -> new AxeItem(BMItemTiers.MORTYX, 6.0F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> MORTYX_PICKAXE = ITEMS.register("mortyx_pickaxe", () -> new PickaxeItem(BMItemTiers.MORTYX, 1, -2.8F, new Item.Properties()));
    public static final RegistryObject<Item> MORTYX_SHOVEL = ITEMS.register("mortyx_shovel", () -> new ShovelItem(BMItemTiers.MORTYX, 1.5F, -3.0F, new Item.Properties()));
    public static final RegistryObject<Item> MORTYX_HOE = ITEMS.register("mortyx_hoe", () -> new HoeItem(BMItemTiers.MORTYX, -3, 0.0F, new Item.Properties()));

}
