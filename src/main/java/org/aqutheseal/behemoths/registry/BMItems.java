package org.aqutheseal.behemoths.registry;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.entity.variants.SkyCharydbisVariants;
import org.aqutheseal.behemoths.item.BMArmorMaterials;
import org.aqutheseal.behemoths.item.BMTiers;
import org.aqutheseal.behemoths.item.BallistaItem;
import org.aqutheseal.behemoths.util.gear.BasicGearSet;

import java.util.ArrayList;
import java.util.List;

public class BMItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Behemoths.MODID);
    public static final List<BasicGearSet> GEAR_SETS = new ArrayList<>();

    // Uncategorized Materials
    public static final RegistryObject<Item> SKY_BEAST_BONE = ITEMS.register("sky_beast_bone", () -> new Item(new Item.Properties()));

    // Sky Beast Skins
    public static final RegistryObject<Item> BARREN_SKY_BEAST_SKIN = ITEMS.register("barren_sky_beast_skin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LUSH_SKY_BEAST_SKIN = ITEMS.register("lush_sky_beast_skin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NORTHERN_SKY_BEAST_SKIN = ITEMS.register("northern_sky_beast_skin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> NETHER_SKY_BEAST_SKIN = ITEMS.register("nether_sky_beast_skin", () -> new Item(new Item.Properties()));
   public static final RegistryObject<Item> SOUL_SKY_BEAST_SKIN = ITEMS.register("soul_sky_beast_skin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> VOID_SKY_BEAST_SKIN = ITEMS.register("void_sky_beast_skin", () -> new Item(new Item.Properties()));

    // Polished Sky Beast Skins
    public static final RegistryObject<Item> POLISHED_BARREN_SKY_BEAST_SKIN = ITEMS.register("polished_barren_sky_beast_skin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POLISHED_LUSH_SKY_BEAST_SKIN = ITEMS.register("polished_lush_sky_beast_skin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POLISHED_NORTHERN_SKY_BEAST_SKIN = ITEMS.register("polished_northern_sky_beast_skin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POLISHED_NETHER_SKY_BEAST_SKIN = ITEMS.register("polished_nether_sky_beast_skin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POLISHED_SOUL_SKY_BEAST_SKIN = ITEMS.register("polished_soul_sky_beast_skin", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> POLISHED_VOID_SKY_BEAST_SKIN = ITEMS.register("polished_void_sky_beast_skin", () -> new Item(new Item.Properties()));

    // Uncategorized Weapons
    public static final RegistryObject<Item> BEHEMOTH_BALLISTA = ITEMS.register("behemoth_ballista", () -> new BallistaItem(
            new Item.Properties().rarity(Rarity.EPIC).stacksTo(1))
    );

    // Mob Spawn Eggs
    public static final RegistryObject<Item> BARREN_SKY_CHARYDBIS_SPAWN_EGG = ITEMS.register("barren_sky_charydbis_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.BARREN_SKY_CHARYDBIS, 7298625, 14661267, new Item.Properties()));
    public static final RegistryObject<Item> LUSH_SKY_CHARYDBIS_SPAWN_EGG = ITEMS.register("lush_sky_charydbis_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.LUSH_SKY_CHARYDBIS, 7317057, 10999991, new Item.Properties()));
    public static final RegistryObject<Item> NORTHERN_SKY_CHARYDBIS_SPAWN_EGG = ITEMS.register("northern_sky_charydbis_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.NORTHERN_SKY_CHARYDBIS, 6782627, 8374745, new Item.Properties()));
    public static final RegistryObject<Item> NETHER_SKY_CHARYDBIS_SPAWN_EGG = ITEMS.register("nether_sky_charydbis_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.NETHER_SKY_CHARYDBIS, 12919637, 15518116, new Item.Properties()));
    public static final RegistryObject<Item> SOUL_SKY_CHARYDBIS_SPAWN_EGG = ITEMS.register("soul_sky_charydbis_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.SOUL_SKY_CHARYDBIS, 4929843, 3138527, new Item.Properties()));
    public static final RegistryObject<Item> VOID_SKY_CHARYDBIS_SPAWN_EGG = ITEMS.register("void_sky_charydbis_spawn_egg", () -> new ForgeSpawnEggItem(BMEntityTypes.VOID_SKY_CHARYDBIS, 38, 13893852, new Item.Properties()));
    // Complete Armor Sets
    public static BasicGearSet barrenSkyBeastSet;
    public static BasicGearSet lushSkyBeastSet;
    public static BasicGearSet northernSkyBeastSet;
    public static BasicGearSet netherSkyBeastSet;
    public static BasicGearSet soulSkyBeastSet;
    public static BasicGearSet voidSkyBeastSet;

    public static void initGearSets() {
        barrenSkyBeastSet = registerGearSet(new BasicGearSet.Builder(ITEMS, "barren_sky_beast")
                .itemProperties(new Item.Properties().rarity(Rarity.RARE).fireResistant()).itemTier(BMTiers.OVERWORLD_SKY_BEAST).armorMaterial(BMArmorMaterials.OVERWORLD_SKY_BEAST)
                .baseMaterial(POLISHED_BARREN_SKY_BEAST_SKIN).stickMaterial(SKY_BEAST_BONE)
                .setSkyBeastSet(SkyCharydbisVariants.BARREN).build()
        );
        lushSkyBeastSet = registerGearSet(new BasicGearSet.Builder(ITEMS, "lush_sky_beast")
                .itemProperties(new Item.Properties().rarity(Rarity.RARE).fireResistant()).itemTier(BMTiers.OVERWORLD_SKY_BEAST).armorMaterial(BMArmorMaterials.OVERWORLD_SKY_BEAST)
                .baseMaterial(POLISHED_LUSH_SKY_BEAST_SKIN).stickMaterial(SKY_BEAST_BONE)
                .setSkyBeastSet(SkyCharydbisVariants.LUSH).build()
        );
        northernSkyBeastSet = registerGearSet(new BasicGearSet.Builder(ITEMS, "northern_sky_beast")
                .itemProperties(new Item.Properties().rarity(Rarity.RARE).fireResistant()).itemTier(BMTiers.OVERWORLD_SKY_BEAST).armorMaterial(BMArmorMaterials.OVERWORLD_SKY_BEAST)
                .baseMaterial(POLISHED_NORTHERN_SKY_BEAST_SKIN).stickMaterial(SKY_BEAST_BONE)
                .setSkyBeastSet(SkyCharydbisVariants.NORTHERN).build()
        );
        netherSkyBeastSet = registerGearSet(new BasicGearSet.Builder(ITEMS, "nether_sky_beast")
                .itemProperties(new Item.Properties().rarity(Rarity.RARE).fireResistant()).itemTier(BMTiers.NETHER_SKY_BEAST).armorMaterial(BMArmorMaterials.NETHER_SKY_BEAST)
                .baseMaterial(POLISHED_NETHER_SKY_BEAST_SKIN).stickMaterial(SKY_BEAST_BONE)
                .setSkyBeastSet(SkyCharydbisVariants.NETHER).build()
        );
        soulSkyBeastSet = registerGearSet(new BasicGearSet.Builder(ITEMS, "soul_sky_beast")
                .itemProperties(new Item.Properties().rarity(Rarity.RARE).fireResistant()).itemTier(BMTiers.END_SKY_BEAST).armorMaterial(BMArmorMaterials.NETHER_SKY_BEAST)
                .baseMaterial(POLISHED_SOUL_SKY_BEAST_SKIN).stickMaterial(SKY_BEAST_BONE)
                .setSkyBeastSet(SkyCharydbisVariants.SOUL).build()
        );
        voidSkyBeastSet = registerGearSet(new BasicGearSet.Builder(ITEMS, "void_sky_beast")
                .itemProperties(new Item.Properties().rarity(Rarity.RARE).fireResistant()).itemTier(BMTiers.END_SKY_BEAST).armorMaterial(BMArmorMaterials.END_SKY_BEAST)
                .baseMaterial(POLISHED_VOID_SKY_BEAST_SKIN).stickMaterial(SKY_BEAST_BONE)
                .setSkyBeastSet(SkyCharydbisVariants.VOID).build()
        );
    }

    public static BasicGearSet registerGearSet(BasicGearSet gearSet) {
        GEAR_SETS.add(gearSet);
        return gearSet;
    }
}