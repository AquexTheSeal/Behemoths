package org.celestialworkshop.behemoths.registries;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BMKeybinds {

    public static final String CATEGORY = "key.categories.behemoths";

    public static KeyMapping openVotingProgress;

    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        openVotingProgress = new KeyMapping("key.behemoths.open_voting_progress", GLFW.GLFW_KEY_Z, CATEGORY);
        event.register(openVotingProgress);
    }
}
