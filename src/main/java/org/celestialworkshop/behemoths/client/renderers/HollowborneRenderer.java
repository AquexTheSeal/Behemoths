package org.celestialworkshop.behemoths.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.HollowborneModel;
import org.celestialworkshop.behemoths.entities.Hollowborne;

public class HollowborneRenderer extends MobRenderer<Hollowborne, HollowborneModel<Hollowborne>> {

    public HollowborneRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HollowborneModel<>(pContext.bakeLayer(HollowborneModel.LAYER_LOCATION)), 2.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(Hollowborne pEntity) {
        return Behemoths.prefix("textures/entity/hollowborne.png");
    }

    @Override
    protected float getFlipDegrees(Hollowborne pLivingEntity) {
        return 0;
    }
}
