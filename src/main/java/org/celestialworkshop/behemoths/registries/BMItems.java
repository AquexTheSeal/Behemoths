package org.celestialworkshop.behemoths.registries;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.items.BehebuggerItem;
import org.celestialworkshop.behemoths.items.RottenOathkeeperItem;

public class BMItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Behemoths.MODID);

    // WEAPONS
    public static final RegistryObject<Item> ROTTEN_OATHKEEPER = ITEMS.register("rotten_oathkeeper", () -> new RottenOathkeeperItem(Tiers.DIAMOND, 9.0F, 1.0F - 4.0F, new Item.Properties()));

    // SPAWN EGGS
    public static final RegistryObject<Item> ARCHZOMBIE_SPAWN_EGG = ITEMS.register("archzombie_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.ARCHZOMBIE, 0x41b050, 0xcfffd5, new Item.Properties()));
    public static final RegistryObject<Item> BANISHING_STAMPEDE_SPAWN_EGG = ITEMS.register("banishing_stampede_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.BANISHING_STAMPEDE, 0x286344, 0x6db369, new Item.Properties()));

    // MISC
    public static final RegistryObject<Item> BEHEBUGGER = ITEMS.register("behebugger", () -> new BehebuggerItem(new Item.Properties().stacksTo(1)));

}
