package org.aqutheseal.aqusmodtemplate.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.aqutheseal.aqusmodtemplate.Aqusmodtemplate;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Aqusmodtemplate.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }

    public ModelFile.ExistingModelFile getModelFromMC(String model) {
        return getExistingFile(mcLoc(model));
    }

    public ModelFile.ExistingModelFile getModelFromMod(String model) {
        return getExistingFile(modLoc(model));
    }

    public ItemModelBuilder handheldItem(Item item) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
        return this.getBuilder(loc.toString()).parent(new ModelFile.UncheckedModelFile("item/handheld")).texture("layer0", new ResourceLocation(loc.getNamespace(), "item/" + loc.getPath()));
    }

    public ItemModelBuilder spawnEggItem(Item item) {
        ResourceLocation loc = ForgeRegistries.ITEMS.getKey(item);
        return this.getBuilder(loc.toString()).parent(new ModelFile.UncheckedModelFile("item/template_spawn_egg"));
    }
}
