package org.celestialworkshop.behemoths.client.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.models.HollowcorperModel;
import org.celestialworkshop.behemoths.entities.projectile.Hollowcorper;

public class HollowcorperRenderer extends EntityRenderer<Hollowcorper> {

    private static final ResourceLocation LOCATION = Behemoths.prefix("textures/entity/projectile/hollowcorper.png");
    private final HollowcorperModel<Hollowcorper> model;

    public HollowcorperRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new HollowcorperModel<>(pContext.bakeLayer(HollowcorperModel.LAYER_LOCATION));
    }

    @Override
    public void render(Hollowcorper pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot()) - 180));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot())));
        pPoseStack.translate(0, -1, 0);
        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(LOCATION)), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(Hollowcorper pEntity) {
        return null;
    }
}
