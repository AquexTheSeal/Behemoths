package org.celestialworkshop.behemoths.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.HollowborneModel;
import org.celestialworkshop.behemoths.client.models.HollowborneSaddleModel;
import org.celestialworkshop.behemoths.entities.Hollowborne;

public class HollowborneSaddleLayer extends RenderLayer<Hollowborne, HollowborneModel<Hollowborne>> {
    private static final ResourceLocation TEXTURE = Behemoths.prefix("textures/entity/layers/hollowborne_saddle.png");
    private final HollowborneSaddleModel<Hollowborne> model;

    public HollowborneSaddleLayer(RenderLayerParent<Hollowborne, HollowborneModel<Hollowborne>> pRenderer, EntityModelSet pModelSet) {
        super(pRenderer);
        this.model = new HollowborneSaddleModel<>(pModelSet.bakeLayer(HollowborneSaddleModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Hollowborne pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        if (pLivingEntity.isSaddled()) {
            HollowborneModel<Hollowborne> parentModel = this.getParentModel();
            parentModel.mob.translateAndRotate(pPoseStack);
            parentModel.subroot.translateAndRotate(pPoseStack);
            pPoseStack.translate(0, -3.5, 0);
            this.model.setupAnim(pLivingEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
            VertexConsumer vertexConsumer = pBuffer.getBuffer(RenderType.entityTranslucent(TEXTURE));
            this.model.renderToBuffer(pPoseStack, vertexConsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
