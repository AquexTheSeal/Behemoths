package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.registries.BMBlocks;
import org.celestialworkshop.behemoths.registries.BMItems;

public class BMItemModelProvider extends ItemModelProvider {

    public BMItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Behemoths.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        handheld(BMItems.BEHEBUGGER.get());
        behemothSpawnEgg(BMItems.BANISHING_STAMPEDE_SPAWN_EGG.get());
        behemothSpawnEgg(BMItems.ARCHZOMBIE_SPAWN_EGG.get());
        behemothSpawnEgg(BMItems.HOLLOWBORNE_SPAWN_EGG.get());
        behemothSpawnEgg(BMItems.HOLLOWBORNE_TURRET_SPAWN_EGG.get());

        basicItem(BMItems.BEHEMOTH_HEART.get());
        basicItem(BMItems.BEHEMOTH_SADDLE.get());
        basicItem(BMItems.BEHEMOTH_HARNESS.get());
        basicItem(BMItems.SAVAGE_FLESH.get());
        basicItem(BMItems.COLOSSUS_BONE.get());

        basicItem(BMItems.MAGNALYTH_INGOT.get());
        basicItem(BMItems.MAGNALYTH_NUGGET.get());
        handheld(BMItems.MAGNALYTH_SWORD.get());
        handheld(BMItems.MAGNALYTH_AXE.get());
        handheld(BMItems.MAGNALYTH_PICKAXE.get());
        handheld(BMItems.MAGNALYTH_SHOVEL.get());
        handheld(BMItems.MAGNALYTH_HOE.get());
        block(BMBlocks.MAGNALYTH_BLOCK.get());

        basicItem(BMItems.MORTYX_UPGRADE_SMITHING_TEMPLATE.get());
        basicItem(BMItems.MORTYX_INGOT.get());
        handheld(BMItems.MORTYX_SWORD.get());
        handheld(BMItems.MORTYX_AXE.get());
        handheld(BMItems.MORTYX_PICKAXE.get());
        handheld(BMItems.MORTYX_SHOVEL.get());
        handheld(BMItems.MORTYX_HOE.get());
        block(BMBlocks.MORTYX_BLOCK.get());
    }

    public void handheld(Item item) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
        this.getBuilder(loc.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld")).texture("layer0", ResourceLocation.fromNamespaceAndPath(loc.getNamespace(), "item/" + loc.getPath()));
    }

    public void behemothSpawnEgg(Item item) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
        this.getBuilder(loc.toString()).parent(this.existingModel("item/template_spawn_egg"));
    }

    public void block(Block block) {
        ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(block);
        this.getBuilder(loc.toString()).parent(new ModelFile.UncheckedModelFile(Behemoths.prefix("block/" + loc.getPath())));
    }

//    public ItemModelBuilder ballistaItem(Item item) {
//        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
//        String itemPath = "item/" + loc.getPath();
//
//        ModelFile parent = this.getModelFromMod("item/ballista");
//
//        return this.getBuilder(loc.toString())
//                .parent(parent)
//                .texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
//                .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_standby"))
//                .override().predicate(new ResourceLocation("pulling"), 1).model(getBuilder(itemPath + "_pulling_0")
//                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
//                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_pulling_0"))).end()
//                .override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), 0.58f).model(getBuilder(itemPath + "_pulling_1")
//                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
//                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_pulling_1"))).end()
//                .override().predicate(new ResourceLocation("pulling"), 1).predicate(new ResourceLocation("pull"), 1.0f).model(getBuilder(itemPath + "_pulling_2")
//                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
//                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_pulling_2"))).end()
//                .override().predicate(new ResourceLocation("charged"), 1).model(getBuilder(itemPath + "_arrow")
//                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
//                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_arrow"))).end()
//                .override().predicate(new ResourceLocation("charged"), 1).predicate(new ResourceLocation("firework"), 1).model(getBuilder(itemPath + "_firework")
//                        .parent(parent).texture("0", new ResourceLocation(loc.getNamespace(), itemPath + "_base"))
//                        .texture("1", new ResourceLocation(loc.getNamespace(), itemPath + "_firework"))).end();
//    }

    public ModelFile.ExistingModelFile existingModel(String model) {
        return getExistingFile(mcLoc(model));
    }

    public ModelFile.ExistingModelFile existingModModel(String model) {
        return getExistingFile(modLoc(model));
    }
}

