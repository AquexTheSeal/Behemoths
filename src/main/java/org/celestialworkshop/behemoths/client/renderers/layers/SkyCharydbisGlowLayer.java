package org.celestialworkshop.behemoths.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.SkyCharydbisModel;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;

public class SkyCharydbisGlowLayer extends RenderLayer<SkyCharydbis, SkyCharydbisModel<SkyCharydbis>> {

    public SkyCharydbisGlowLayer(RenderLayerParent<SkyCharydbis, SkyCharydbisModel<SkyCharydbis>> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, SkyCharydbis pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        float ageInTicks = pLivingEntity.tickCount + pPartialTick;

        float amp = pLivingEntity.isSleeping() ? 0.3F : 1.0F;

        float r0 = Mth.cos(ageInTicks * 0.5F * amp) * 0.5F + 0.5F;
        float c0 = Mth.sin(ageInTicks * 0.8F * amp) * 0.5F + 0.5F;
        VertexConsumer v0 = pBuffer.getBuffer(RenderType.entityTranslucentEmissive(Behemoths.prefix("textures/entity/layers/sky_charydbis_glow_0.png")));
        this.getParentModel().renderToBuffer(pPoseStack, v0, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, c0, r0 * amp);

        float r1 = Mth.sin(ageInTicks * 0.3F * amp) * 0.5F + 0.5F;
        float c1 = Mth.cos(ageInTicks * 0.8F * amp) * 0.5F + 0.5F;
        VertexConsumer v1 = pBuffer.getBuffer(RenderType.entityTranslucentEmissive(Behemoths.prefix("textures/entity/layers/sky_charydbis_glow_1.png")));
        this.getParentModel().renderToBuffer(pPoseStack, v1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, c1, r1 * amp);
    }
}
