package org.celestialworkshop.behemoths.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.SkyCharydbisModel;
import org.celestialworkshop.behemoths.client.renderers.layers.SkyCharydbisGlowLayer;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;

public class SkyCharydbisRenderer extends MobRenderer<SkyCharydbis, SkyCharydbisModel<SkyCharydbis>> {

    public SkyCharydbisRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SkyCharydbisModel<>(pContext.bakeLayer(SkyCharydbisModel.LAYER_LOCATION)), 3.5F);
        this.addLayer(new SkyCharydbisGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SkyCharydbis pEntity) {
        return Behemoths.prefix("textures/entity/sky_charydbis.png");
    }

    @Override
    protected void setupRotations(SkyCharydbis pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(pPartialTicks, pEntityLiving.xRotO, pEntityLiving.getXRot())));
    }
}
