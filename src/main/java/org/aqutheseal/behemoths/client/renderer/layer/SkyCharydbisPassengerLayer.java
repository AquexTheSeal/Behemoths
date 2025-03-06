package org.aqutheseal.behemoths.client.renderer.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.Entity;
import org.aqutheseal.behemoths.client.model.SkyCharydbisModelOld;
import org.aqutheseal.behemoths.entity.SkyCharydbis;

public class SkyCharydbisPassengerLayer extends RenderLayer<SkyCharydbis, SkyCharydbisModelOld<SkyCharydbis>> {

    private final EntityRenderDispatcher dispatcher;

    public SkyCharydbisPassengerLayer(EntityRendererProvider.Context pContext, RenderLayerParent<SkyCharydbis, SkyCharydbisModelOld<SkyCharydbis>> pRenderer) {
        super(pRenderer);
        this.dispatcher = pContext.getEntityRenderDispatcher();
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, SkyCharydbis pEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        Entity passenger = pEntity.getFirstPassenger();
        if (passenger != null && this.shouldRenderPassenger(passenger)) {
            pPoseStack.pushPose();
            //pPoseStack.translate(0, 0, 2);
            float riderScale = 1 / pEntity.getSizeMultiplier();
            pPoseStack.mulPose(Axis.XP.rotationDegrees(180));
            pPoseStack.scale(riderScale, riderScale, riderScale);
            pPoseStack.mulPose(Axis.YP.rotationDegrees(pEntity.getYRot()));
            dispatcher.render(passenger, 0, 0.2 + pEntity.getSizeMultiplier(), 0, 0, pPartialTick, pPoseStack, pBuffer, 0);
            pPoseStack.popPose();
        }
    }

    public boolean shouldRenderPassenger(Entity passenger) {
        if (Minecraft.getInstance().player == passenger) {
            return !Minecraft.getInstance().options.getCameraType().isFirstPerson();
        }
        return true;
    }
}
