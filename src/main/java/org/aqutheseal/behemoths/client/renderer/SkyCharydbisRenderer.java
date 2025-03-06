package org.aqutheseal.behemoths.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.client.model.SkyCharydbisModel;
import org.aqutheseal.behemoths.entity.SkyCharydbis;

public class SkyCharydbisRenderer extends MobRenderer<SkyCharydbis, SkyCharydbisModel<SkyCharydbis>> {

    public SkyCharydbisRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new SkyCharydbisModel<>(pContext.bakeLayer(SkyCharydbisModel.LAYER_LOCATION)), 2.5F);
    }

    @Override
    public void render(SkyCharydbis pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        float scale = pEntity.getSizeMultiplier();
        pPoseStack.scale(scale, scale, scale);
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
        pPoseStack.popPose();
    }

    @Override
    protected void setupRotations(SkyCharydbis pEntityLiving, PoseStack pPoseStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
        super.setupRotations(pEntityLiving, pPoseStack, pAgeInTicks, pRotationYaw, pPartialTicks);

        pPoseStack.mulPose(Axis.XP.rotationDegrees(-Mth.lerp(pPartialTicks, pEntityLiving.xRotO, pEntityLiving.getXRot())));
    }

    public ResourceLocation getTextureLocation(SkyCharydbis pEntity) {
        return Behemoths.location("textures/entity/sky_charydbis_" + pEntity.variant.id + ".png");
    }

    @Override
    protected float getFlipDegrees(SkyCharydbis pLivingEntity) {
        return 0;
    }
}
