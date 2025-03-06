package org.aqutheseal.behemoths.events;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.client.armor.SkyBeastArmorModel;
import org.aqutheseal.behemoths.client.gui.CustomCrosshairGui;
import org.aqutheseal.behemoths.client.model.SkyCharydbisModel;
import org.aqutheseal.behemoths.client.renderer.EmptyRenderer;
import org.aqutheseal.behemoths.client.renderer.SkyCharydbisRenderer;
import org.aqutheseal.behemoths.particle.BasicAnimatedParticle;
import org.aqutheseal.behemoths.particle.OrientLockedParticle;
import org.aqutheseal.behemoths.registry.BMEntityTypes;
import org.aqutheseal.behemoths.registry.BMItems;
import org.aqutheseal.behemoths.registry.BMParticleTypes;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModClientSetup {

    public static final IGuiOverlay CUSTOM_CROSSHAIR = CustomCrosshairGui::render;

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SkyCharydbisModel.LAYER_LOCATION, SkyCharydbisModel::createBodyLayer);

        event.registerLayerDefinition(SkyBeastArmorModel.LAYER_LOCATION, SkyBeastArmorModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void fmlClientSetup(final FMLClientSetupEvent event) {
        ItemProperties.register(BMItems.BEHEMOTH_BALLISTA.get(), new ResourceLocation("pull"), (stack, level, entity, seed) -> {
            if (entity == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(stack) ? 0.0F : (float)(stack.getUseDuration() - entity.getUseItemRemainingTicks()) / (float)CrossbowItem.getChargeDuration(stack);
            }
        });
        ItemProperties.register(BMItems.BEHEMOTH_BALLISTA.get(), new ResourceLocation("pulling"), (stack, level, entity, seed) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack && !CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
        ItemProperties.register(BMItems.BEHEMOTH_BALLISTA.get(), new ResourceLocation("charged"), (stack, level, entity, seed) -> CrossbowItem.isCharged(stack) ? 1.0F : 0.0F);
        ItemProperties.register(BMItems.BEHEMOTH_BALLISTA.get(), new ResourceLocation("firework"), (stack, level, entity, seed) -> CrossbowItem.isCharged(stack) && CrossbowItem.containsChargedProjectile(stack, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
    }

    @SubscribeEvent
    public static void registerHudOverlays(RegisterGuiOverlaysEvent ev) {
        ev.registerAbove(VanillaGuiOverlay.CROSSHAIR.id(), "crosshair", CUSTOM_CROSSHAIR);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BMEntityTypes.BARREN_SKY_CHARYDBIS.get(), SkyCharydbisRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.LUSH_SKY_CHARYDBIS.get(), SkyCharydbisRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.NORTHERN_SKY_CHARYDBIS.get(), SkyCharydbisRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.NETHER_SKY_CHARYDBIS.get(), SkyCharydbisRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.SOUL_SKY_CHARYDBIS.get(), SkyCharydbisRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.VOID_SKY_CHARYDBIS.get(), SkyCharydbisRenderer::new);

        event.registerEntityRenderer(BMEntityTypes.SHOCKWAVE.get(), EmptyRenderer::new);
    }

    @SubscribeEvent
    public static void registerRenderers(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BMParticleTypes.SHOCKWAVE_RING.get(), OrientLockedParticle.ShockwaveRingProvider::new);
        event.registerSpriteSet(BMParticleTypes.SHOCKWAVE_STREAK.get(), BasicAnimatedParticle.ShockwaveStreakProvider::new);
    }
}
