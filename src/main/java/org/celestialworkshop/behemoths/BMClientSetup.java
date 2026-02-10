package org.celestialworkshop.behemoths;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.celestialworkshop.behemoths.client.guis.itemdecorations.BehemothHeartDecoration;
import org.celestialworkshop.behemoths.client.guis.overlays.BanishingStampedeJumpMeterOverlay;
import org.celestialworkshop.behemoths.client.guis.overlays.VotingProgressOverlay;
import org.celestialworkshop.behemoths.client.guis.tooltips.HeartTooltip;
import org.celestialworkshop.behemoths.client.guis.tooltips.SpecialtyTooltip;
import org.celestialworkshop.behemoths.client.models.*;
import org.celestialworkshop.behemoths.client.renderers.*;
import org.celestialworkshop.behemoths.particles.VFXParticle;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMItems;
import org.celestialworkshop.behemoths.registries.BMParticleTypes;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BMClientSetup {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(BMEntityTypes.ARCHZOMBIE.get(), ArchzombieRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.BANISHING_STAMPEDE.get(), BanishingStampedeRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.HOLLOWBORNE.get(), HollowborneRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.HOLLOWBORNE_TURRET.get(), HollowborneTurretRenderer::new);
        event.registerEntityRenderer(BMEntityTypes.HOLLOWCORPER.get(), HollowcorperRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityModels(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ArchzombieModel.LAYER_LOCATION, ArchzombieModel::createBodyLayer);
        event.registerLayerDefinition(BanishingStampedeModel.LAYER_LOCATION, BanishingStampedeModel::createBodyLayer);
        event.registerLayerDefinition(HollowborneModel.LAYER_LOCATION, HollowborneModel::createBodyLayer);
        event.registerLayerDefinition(HollowborneTurretModel.LAYER_LOCATION, HollowborneTurretModel::createBodyLayer);
        event.registerLayerDefinition(HollowcorperModel.LAYER_LOCATION, HollowcorperModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerGuiOverlays(RegisterGuiOverlaysEvent ev) {
        ev.registerAbove(VanillaGuiOverlay.JUMP_BAR.id(), "banishing_stampede", BanishingStampedeJumpMeterOverlay::render);
        ev.registerAbove(VanillaGuiOverlay.BOSS_EVENT_PROGRESS.id(), "voting_progress", VotingProgressOverlay::render);
    }

    @SubscribeEvent
    public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
        event.register(BMItems.BEHEMOTH_HEART.get(), new BehemothHeartDecoration());
    }

    @SubscribeEvent
    public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BMParticleTypes.VFX.get(), VFXParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((stage, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor) ->
                CompletableFuture.runAsync(VFXParticle::clearCaches, backgroundExecutor).thenCompose(stage::wait)
        );
    }

    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(SpecialtyTooltip.class, component -> component);
        event.register(HeartTooltip.class, component -> component);
    }
}
