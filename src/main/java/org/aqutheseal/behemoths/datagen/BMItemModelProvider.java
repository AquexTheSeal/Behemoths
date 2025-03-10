package org.aqutheseal.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.registry.BMItems;
import org.aqutheseal.behemoths.util.gear.BasicGearSet;

public class BMItemModelProvider extends ItemModelProvider {

    public BMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Behemoths.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ballistaItem(BMItems.BEHEMOTH_BALLISTA.get());

        basicItem(BMItems.SKY_BEAST_BONE.get());
        basicItem(BMItems.BARREN_SKY_BEAST_SKIN.get());
        basicItem(BMItems.POLISHED_BARREN_SKY_BEAST_SKIN.get());

        spawnEggItem(BMItems.BARREN_SKY_CHARYDBIS_SPAWN_EGG.get());
        spawnEggItem(BMItems.LUSH_SKY_CHARYDBIS_SPAWN_EGG.get());
        spawnEggItem(BMItems.NORTHERN_SKY_CHARYDBIS_SPAWN_EGG.get());
        spawnEggItem(BMItems.NETHER_SKY_CHARYDBIS_SPAWN_EGG.get());
        spawnEggItem(BMItems.SOUL_SKY_CHARYDBIS_SPAWN_EGG.get());
        spawnEggItem(BMItems.VOID_SKY_CHARYDBIS_SPAWN_EGG.get());

        generateGearSetModels(BMItems.barrenSkyBeastSet);
    }

    public void handheldItem(Item item) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
        this.getBuilder(loc.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld")).texture("layer0", new ResourceLocation(loc.getNamespace(), "item/" + loc.getPath()));
    }

    public void spawnEggItem(Item item) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
        this.getBuilder(loc.toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }

    private void generateGearSetModels(BasicGearSet set) {
        handheldItem(set.sword.get());
        handheldItem(set.axe.get());
        handheldItem(set.pickaxe.get());
        handheldItem(set.shovel.get());
        handheldItem(set.hoe.get());
        basicItem(set.helmet.get());
        basicItem(set.chestplate.get());
        basicItem(set.leggings.get());
        basicItem(set.boots.get());
    }

    public ItemModelBuilder ballistaItem(Item item) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
        String itemPath = "item/" + loc.getPath();

        ModelFile parent = this.getModelFromMod("item/ballista");

        return this.getBuilder(loc.toString())
                .parent(parent)
                .texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
                .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_standby"))
                .override().predicate(new ResourceLocation("pulling"), 1).model(getBuilder(itemPath + "_pulling_0")
                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_pulling_0"))).end()
                .override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), 0.58f).model(getBuilder(itemPath + "_pulling_1")
                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_pulling_1"))).end()
                .override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), 1.0f).model(getBuilder(itemPath + "_pulling_2")
                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_pulling_2"))).end()
                .override().predicate(new ResourceLocation("charged"), 1).model(getBuilder(itemPath + "_arrow")
                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_arrow"))).end()
                .override().predicate(new ResourceLocation("charged"), 1).predicate(new ResourceLocation("firework"), 1).model(getBuilder(itemPath + "_firework")
                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_firework"))).end();
    }

    public ModelFile.ExistingModelFile getModelFromMC(String model) {
        return getExistingFile(mcLoc(model));
    }

    public ModelFile.ExistingModelFile getModelFromMod(String model) {
        return getExistingFile(modLoc(model));
    }
}
