package org.celestialworkshop.behemoths.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.HollowborneTurretModel;
import org.celestialworkshop.behemoths.entities.HollowborneTurret;

public class HollowborneTurretRenderer extends MobRenderer<HollowborneTurret, HollowborneTurretModel<HollowborneTurret>> {

    public HollowborneTurretRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new HollowborneTurretModel<>(pContext.bakeLayer(HollowborneTurretModel.LAYER_LOCATION)), 2.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(HollowborneTurret pEntity) {
        return Behemoths.prefix("textures/entity/hollowborne.png");
    }
}
