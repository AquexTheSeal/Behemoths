package org.celestialworkshop.behemoths.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.ArchzombieModel;
import org.celestialworkshop.behemoths.client.renderers.layers.ArchzombieBannerLayer;
import org.celestialworkshop.behemoths.entities.Archzombie;

public class ArchzombieRenderer extends MobRenderer<Archzombie, ArchzombieModel<Archzombie>> {

    public ArchzombieRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new ArchzombieModel<>(pContext.bakeLayer(ArchzombieModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, pContext.getItemInHandRenderer()));
        this.addLayer(new ArchzombieBannerLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Archzombie pEntity) {
        return Behemoths.prefix("textures/entity/archzombie.png");
    }
}
