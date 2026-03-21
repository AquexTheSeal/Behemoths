package org.celestialworkshop.behemoths.client.renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.PhantashroomGluttonModel;
import org.celestialworkshop.behemoths.entities.misc.PhantashroomGlutton;

public class PhantashroomGluttonRenderer extends LivingEntityRenderer<PhantashroomGlutton, PhantashroomGluttonModel<PhantashroomGlutton>> {

    public static final ResourceLocation TEXTURE = Behemoths.prefix("textures/entity/phantashroom_glutton.png");

    public PhantashroomGluttonRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PhantashroomGluttonModel<>(pContext.bakeLayer(PhantashroomGluttonModel.LAYER_LOCATION)), 1.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(PhantashroomGlutton pEntity) {
        return TEXTURE;
    }

    @Override
    protected boolean shouldShowName(PhantashroomGlutton pEntity) {
        return false;
    }

    @Override
    protected float getFlipDegrees(PhantashroomGlutton pLivingEntity) {
        return 0;
    }
}
