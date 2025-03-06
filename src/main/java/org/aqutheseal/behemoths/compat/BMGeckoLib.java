package org.aqutheseal.behemoths.compat;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.aqutheseal.behemoths.capability.FreezingCapability;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.event.GeoRenderEvent;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

//todo: Register this class with the mod event bus
public class BMGeckoLib {

    @SubscribeEvent
    public static void onGeoEntityRender(GeoRenderEvent.Entity.CompileRenderLayers event) {
        event.addLayer(new FrrezingGeoLayer<>(event.getRenderer()));
    }

    public static class FrrezingGeoLayer<T extends GeoAnimatable> extends GeoRenderLayer<T> {

        public FrrezingGeoLayer(GeoRenderer<T> entityRendererIn) {
            super(entityRendererIn);
        }

        protected RenderType getRenderType(T animatable) {
            return RenderType.entityTranslucent(getTextureResource(animatable));
        }

        @Override
        public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
            if (animatable instanceof LivingEntity living) {
                living.getCapability(FreezingCapability.CAPABILITY).ifPresent(cap -> {
                    if (cap.isFreezing()) {
                        getRenderer().reRender(bakedModel, poseStack, bufferSource, animatable, getRenderType(animatable), bufferSource.getBuffer(getRenderType(animatable)), partialTick,
                                LightTexture.FULL_BRIGHT, OverlayTexture.pack(10, 10), 0.0F, 0.25F + Mth.sin(living.tickCount * 0.2F) * 0.2F, 1.0F, 0.3F
                        );
                    }
                });
            }
        }
    }
}
