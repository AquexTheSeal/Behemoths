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
import org.celestialworkshop.behemoths.client.models.CharydbisShardModel;
import org.celestialworkshop.behemoths.entities.projectile.CharydbisShard;

public class CharydbisShardRenderer extends EntityRenderer<CharydbisShard> {

    private final CharydbisShardModel<CharydbisShard> model;

    public CharydbisShardRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new CharydbisShardModel<>(pContext.bakeLayer(CharydbisShardModel.LAYER_LOCATION));
    }

    @Override
    public void render(CharydbisShard pEntity, float pEntityYaw, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();

        float age = pEntity.tickCount + pPartialTick;

        float xx = Mth.lerp(pPartialTick, pEntity.xRotO, pEntity.getXRot());
        float yy = Mth.lerp(pPartialTick, pEntity.yRotO, pEntity.getYRot());
        pPoseStack.mulPose(Axis.YP.rotationDegrees(yy - 180));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(xx + 90));

        double shakeX = pEntity.getStatusFlag() == CharydbisShard.SUSPENDED_STATUS ? Mth.sin(age * 3 * (pEntity.suspendedTicks / 400)) * 0.05 : 0;
        double shakeY = pEntity.getStatusFlag() == CharydbisShard.SUSPENDED_STATUS ? Mth.sin(age * 3 * (pEntity.suspendedTicks / 400)) * 0.05 : 0;
        double shakeZ = pEntity.getStatusFlag() == CharydbisShard.SUSPENDED_STATUS ? Mth.cos(age * 3 * (pEntity.suspendedTicks / 400)) * 0.05 : 0;
        pPoseStack.translate(shakeX, -1.75 + shakeY, shakeZ);

        float g = pEntity.getStatusFlag() == CharydbisShard.HOSTILE_STATUS ? 1.0F : 0.2F;
        float s = pEntity.getStatusFlag() == CharydbisShard.HOSTILE_STATUS ? 1.3F : 1.6F + Mth.sin(age * 0.4F) * 0.3F;
        float o = pEntity.getStatusFlag() == CharydbisShard.HOSTILE_STATUS ? -0.2F : -0.4F - Mth.sin(age * 0.4F) * 0.2F;

        pPoseStack.pushPose();
        pPoseStack.scale(s, s, s);
        pPoseStack.translate(0, o, 0);
        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.eyes(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, g, 1.0F, 1.0F);
        pPoseStack.popPose();

        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, g, 1.0F, 1.0F);
        this.model.renderToBuffer(pPoseStack, pBuffer.getBuffer(RenderType.entityTranslucentEmissive(this.getTextureLocation(pEntity))), pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, g, 1.0F, 1.0F);

        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTick, pPoseStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CharydbisShard pEntity) {
        return Behemoths.prefix("textures/entity/projectile/charydbis_shard.png");
    }
}
