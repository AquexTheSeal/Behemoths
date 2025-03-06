package org.aqutheseal.behemoths.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.network.packet.ScreenShakePacket;
import org.aqutheseal.behemoths.network.packet.SyncFreezingPacket;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BMNetwork {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Behemoths.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    public static int id = 0;

    @SubscribeEvent
    public static void fullRegister(FMLCommonSetupEvent event) {
        registerMessages();
    }

    public static void registerMessages() {
        INSTANCE.registerMessage(id++, ScreenShakePacket.class, ScreenShakePacket::encode, ScreenShakePacket::decode, ScreenShakePacket::handle);
        BMNetwork.INSTANCE.registerMessage(id++, SyncFreezingPacket.class, SyncFreezingPacket::encode, SyncFreezingPacket::decode, SyncFreezingPacket::handle);
    }
}