package org.aqutheseal.behemoths.util.gear;

import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.entity.variants.SkyCharydbisVariants;
import org.aqutheseal.behemoths.item.ISkyBeastTool;
import org.aqutheseal.behemoths.item.SkyBeastArmorItem;

import java.util.function.BiFunction;
import java.util.function.Supplier;

public class BasicGearSet {

    public final DeferredRegister<Item> registry;
    public final String materialPrefix;

    public final Item.Properties itemProperties;
    public final Tier itemTier;
    public final ArmorMaterial armorMaterial;
    public final Supplier<Item> baseMaterial;
    public final Supplier<Item> stickMaterial;

    public final RegistryObject<Item> sword;
    public final RegistryObject<Item> axe;
    public final RegistryObject<Item> pickaxe;
    public final RegistryObject<Item> shovel;
    public final RegistryObject<Item> hoe;

    public final RegistryObject<Item> helmet;
    public final RegistryObject<Item> chestplate;
    public final RegistryObject<Item> leggings;
    public final RegistryObject<Item> boots;

    private BasicGearSet(Builder builder) {
        this.registry = builder.registry;
        this.materialPrefix = builder.materialPrefix;

        this.itemProperties = builder.itemProperties;
        this.itemTier = builder.itemTier;
        this.armorMaterial = builder.armorMaterial;
        this.baseMaterial = builder.baseMaterial;
        this.stickMaterial = builder.stickMaterial;

        this.sword = registry.register(materialPrefix + "_sword", () -> builder.swordSupplier.apply(itemProperties, itemTier));
        this.axe = registry.register(materialPrefix + "_axe", () -> builder.axeSupplier.apply(itemProperties, itemTier));
        this.pickaxe = registry.register(materialPrefix + "_pickaxe", () -> builder.pickaxeSupplier.apply(itemProperties, itemTier));
        this.shovel = registry.register(materialPrefix + "_shovel", () -> builder.shovelSupplier.apply(itemProperties, itemTier));
        this.hoe = registry.register(materialPrefix + "_hoe", () -> builder.hoeSupplier.apply(itemProperties, itemTier));

        this.helmet = registry.register(materialPrefix + "_helmet", () -> builder.helmetSupplier.apply(itemProperties, armorMaterial));
        this.chestplate = registry.register(materialPrefix + "_chestplate", () -> builder.chestplateSupplier.apply(itemProperties, armorMaterial));
        this.leggings = registry.register(materialPrefix + "_leggings", () -> builder.leggingsSupplier.apply(itemProperties, armorMaterial));
        this.boots = registry.register(materialPrefix + "_boots", () -> builder.bootsSupplier.apply(itemProperties, armorMaterial));
    }

    public static class Builder {
        private final DeferredRegister<Item> registry;
        private final String materialPrefix;

        private Item.Properties itemProperties = new Item.Properties();
        private Tier itemTier = Tiers.WOOD;
        private ArmorMaterial armorMaterial = ArmorMaterials.LEATHER;
        private Supplier<Item> baseMaterial = null;
        private Supplier<Item> stickMaterial = null;

        private BiFunction<Item.Properties, Tier, SwordItem> swordSupplier = (properties, tier) -> new SwordItem(tier, 3, -2.4F, itemProperties);
        private BiFunction<Item.Properties, Tier, AxeItem> axeSupplier = (properties, tier) -> new AxeItem(tier, 5, -3.0F, itemProperties);
        private BiFunction<Item.Properties, Tier, PickaxeItem> pickaxeSupplier = (properties, tier) -> new PickaxeItem(tier, 1, -2.8F, itemProperties);
        private BiFunction<Item.Properties, Tier, ShovelItem> shovelSupplier = (properties, tier) -> new ShovelItem(tier, 1.5F, -3.0F, itemProperties);
        private BiFunction<Item.Properties, Tier, HoeItem> hoeSupplier = (properties, tier) -> new HoeItem(tier, -4, 0.0F, itemProperties);

        private BiFunction<Item.Properties, ArmorMaterial, ArmorItem> helmetSupplier = (properties, armorMaterial) -> new ArmorItem(armorMaterial, ArmorItem.Type.HELMET, itemProperties);
        private BiFunction<Item.Properties, ArmorMaterial, ArmorItem> chestplateSupplier = (properties, armorMaterial) -> new ArmorItem(armorMaterial, ArmorItem.Type.CHESTPLATE, itemProperties);
        private BiFunction<Item.Properties, ArmorMaterial, ArmorItem> leggingsSupplier = (properties, armorMaterial) -> new ArmorItem(armorMaterial, ArmorItem.Type.LEGGINGS, itemProperties);
        private BiFunction<Item.Properties, ArmorMaterial, ArmorItem> bootsSupplier = (properties, armorMaterial) -> new ArmorItem(armorMaterial, ArmorItem.Type.BOOTS, itemProperties);

        public Builder(DeferredRegister<Item> registry, String materialPrefix) {
            this.registry = registry;
            this.materialPrefix = materialPrefix;
        }

        public Builder itemProperties(Item.Properties properties) {
            this.itemProperties = properties;
            return this;
        }

        public Builder itemTier(Tier itemTier) {
            this.itemTier = itemTier;
            return this;
        }

        public Builder armorMaterial(ArmorMaterial armorMaterial) {
            this.armorMaterial = armorMaterial;
            return this;
        }

        public Builder baseMaterial(Supplier<Item> baseMaterial) {
            this.baseMaterial = baseMaterial;
            return this;
        }

        public Builder stickMaterial(Supplier<Item> stickMaterial) {
            this.stickMaterial = stickMaterial;
            return this;
        }

        // CUSTOM ITEM SUPPLIERS
        public Builder swordSupplier(BiFunction<Item.Properties, Tier, SwordItem> supplier) {
            this.swordSupplier = supplier;
            return this;
        }

        public Builder axeSupplier(BiFunction<Item.Properties, Tier, AxeItem> supplier) {
            this.axeSupplier = supplier;
            return this;
        }

        public Builder pickaxeSupplier(BiFunction<Item.Properties, Tier, PickaxeItem> supplier) {
            this.pickaxeSupplier = supplier;
            return this;
        }

        public Builder shovelSupplier(BiFunction<Item.Properties, Tier, ShovelItem> supplier) {
            this.shovelSupplier = supplier;
            return this;
        }

        public Builder hoeSupplier(BiFunction<Item.Properties, Tier, HoeItem> supplier) {
            this.hoeSupplier = supplier;
            return this;
        }

        public Builder helmetSupplier(BiFunction<Item.Properties, ArmorMaterial, ArmorItem> supplier) {
            this.helmetSupplier = supplier;
            return this;
        }

        public Builder chestplateSupplier(BiFunction<Item.Properties, ArmorMaterial, ArmorItem> supplier) {
            this.chestplateSupplier = supplier;
            return this;
        }

        public Builder leggingsSupplier(BiFunction<Item.Properties, ArmorMaterial, ArmorItem> supplier) {
            this.leggingsSupplier = supplier;
            return this;
        }

        public Builder bootsSupplier(BiFunction<Item.Properties, ArmorMaterial, ArmorItem> supplier) {
            this.bootsSupplier = supplier;
            return this;
        }

        // SPECIALIZED BUILDERS
        public Builder setSkyBeastSet(SkyCharydbisVariants variant) {
            this.swordSupplier((properties, tier) -> new ISkyBeastTool.Sword(tier, 3, -2.4F, itemProperties, variant));
            this.axeSupplier((properties, tier) -> new ISkyBeastTool.Axe(tier, 5, -3.0F, itemProperties, variant));
            this.pickaxeSupplier((properties, tier) -> new ISkyBeastTool.Pickaxe(tier, 1, -2.8F, itemProperties, variant));
            this.shovelSupplier((properties, tier) -> new ISkyBeastTool.Shovel(tier, 1.5F, -3.0F, itemProperties, variant));
            this.hoeSupplier((properties, tier) -> new ISkyBeastTool.Hoe(tier, -4, 0.0F, itemProperties, variant));

            this.helmetSupplier((properties, material) -> new SkyBeastArmorItem(material, ArmorItem.Type.HELMET, properties, variant));
            this.chestplateSupplier((properties, material) -> new SkyBeastArmorItem(material, ArmorItem.Type.CHESTPLATE, properties, variant));
            this.leggingsSupplier((properties, material) -> new SkyBeastArmorItem(material, ArmorItem.Type.LEGGINGS, properties, variant));
            this.bootsSupplier((properties, material) -> new SkyBeastArmorItem(material, ArmorItem.Type.BOOTS, properties, variant));
            return this;
        }

        public BasicGearSet build() {
            return new BasicGearSet(this);
        }
    }
}
