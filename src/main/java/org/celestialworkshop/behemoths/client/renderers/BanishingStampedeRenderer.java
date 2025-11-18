package org.celestialworkshop.behemoths.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.BanishingStampedeModel;
import org.celestialworkshop.behemoths.entities.BanishingStampede;

public class BanishingStampedeRenderer extends MobRenderer<BanishingStampede, BanishingStampedeModel<BanishingStampede>> {

    public BanishingStampedeRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new BanishingStampedeModel<>(pContext.bakeLayer(BanishingStampedeModel.LAYER_LOCATION)), 0.9F);
    }

    @Override
    public ResourceLocation getTextureLocation(BanishingStampede pEntity) {
        return Behemoths.prefix("textures/entity/banishing_stampede.png");
    }
}
