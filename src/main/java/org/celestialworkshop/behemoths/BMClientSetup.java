package org.celestialworkshop.behemoths;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.celestialworkshop.behemoths.client.guis.BanishingStampedeJumpMeterOverlay;
import org.celestialworkshop.behemoths.client.models.ArchzombieModel;
import org.celestialworkshop.behemoths.client.models.BanishingStampedeModel;
import org.celestialworkshop.behemoths.client.renderers.ArchzombieRenderer;
import org.celestialworkshop.behemoths.client.renderers.BanishingStampedeRenderer;
import org.celestialworkshop.behemoths.particles.VFXParticle;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMParticleTypes;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BMClientSetup {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BMEntityTypes.ARCHZOMBIE.get(), ArchzombieRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.BANISHING_STAMPEDE.get(), BanishingStampedeRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ArchzombieModel.LAYER_LOCATION, ArchzombieModel::createBodyLayer);
        event.registerLayerDefinition(BanishingStampedeModel.LAYER_LOCATION, BanishingStampedeModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent ev) {
        ev.registerAbove(VanillaGuiOverlay.JUMP_BAR.id(), "banishing_stampede", BanishingStampedeJumpMeterOverlay::render);
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BMParticleTypes.VFX.get(), VFXParticle.Provider::new);
    }
}
