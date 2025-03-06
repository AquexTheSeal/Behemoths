package org.aqutheseal.behemoths.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.capability.FreezingCapability;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class FreezingEffectRenderer {

    public static <T extends LivingEntity, M extends EntityModel<T>> void renderVanillaFrozenOverlay(LivingEntityRenderer<T, M> renderer, T pEntity, PoseStack pMatrixStack, MultiBufferSource pBuffer) {
        pEntity.getCapability(FreezingCapability.CAPABILITY).ifPresent(cap -> {
            if (cap.isFreezing()) {
                VertexConsumer vertexconsumer = pBuffer.getBuffer(RenderType.entityTranslucent(renderer.getTextureLocation(pEntity)));
                renderer.getModel().renderToBuffer(pMatrixStack, vertexconsumer,
                        LightTexture.FULL_BRIGHT, OverlayTexture.pack(10, 10), 0.0F, 0.25F + Mth.sin(pEntity.tickCount * 0.2F) * 0.2F, 1.0F, 0.3F
                );
            }
        });
    }
}
