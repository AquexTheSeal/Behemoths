package org.aqutheseal.behemoths.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.client.model.CharydbisOrbModel;
import org.aqutheseal.behemoths.entity.projectile.CharydbisOrb;

public class CharydbisOrbRenderer extends EntityRenderer<CharydbisOrb> {
    private final CharydbisOrbModel<CharydbisOrb> model;

    public CharydbisOrbRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new CharydbisOrbModel<>(pContext.bakeLayer(CharydbisOrbModel.LAYER_LOCATION));
    }

    @Override
    public void render(CharydbisOrb pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.scale(4, 4, 4);
        pPoseStack.translate(0, -0.75, 0);
        this.model.setupAnim(pEntity, pPartialTick, 0.0F, pEntity.tickCount + pPartialTick, 0.0F, 0.0F);
        VertexConsumer vertexconsumer2 = pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity)));
        this.model.renderToBuffer(pPoseStack, vertexconsumer2, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.eyes(this.getTextureLocation(pEntity)));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CharydbisOrb pEntity) {
        return Behemoths.location("textures/entity/projectile/charydbis_orb.png");
    }
}
