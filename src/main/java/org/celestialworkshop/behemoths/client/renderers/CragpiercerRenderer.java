package org.celestialworkshop.behemoths.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.CragpiercerModel;
import org.celestialworkshop.behemoths.entities.misc.Cragpiercer;

public class CragpiercerRenderer extends LivingEntityRenderer<Cragpiercer, CragpiercerModel<Cragpiercer>> {

    public CragpiercerRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new CragpiercerModel<>(pContext.bakeLayer(CragpiercerModel.LAYER_LOCATION)), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Cragpiercer pEntity) {
        return Behemoths.prefix("textures/entity/cragpiercer/wood.png");
    }

    @Override
    protected boolean shouldShowName(Cragpiercer pEntity) {
        return pEntity.isCustomNameVisible();
    }
}
