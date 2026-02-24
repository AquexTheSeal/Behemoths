package org.celestialworkshop.behemoths.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.registries.BMItems;

@JeiPlugin
public class BMJEI implements IModPlugin {
    private static final ResourceLocation ID = Behemoths.prefix("jeiplugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registerJeiItemDescription(registration, BMItems.BEHEMOTH_HEART);
        registerJeiItemDescription(registration, BMItems.MORTYX_UPGRADE_SMITHING_TEMPLATE);
    }

    public void registerJeiItemDescription(IRecipeRegistration registration, RegistryObject<Item> item) {
        registration.addIngredientInfo(item.get(), Component.translatable(Util.makeDescriptionId("item", item.getId()) + ".jei_description"));
    }
}
