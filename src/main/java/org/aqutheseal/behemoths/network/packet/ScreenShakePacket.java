package org.aqutheseal.behemoths.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.aqutheseal.behemoths.util.mixin.IScreenShaker;

import java.util.function.Supplier;

public class ScreenShakePacket {
    private final float intensity;

    public ScreenShakePacket(float intensity) {
        this.intensity = intensity;
    }

    public static void encode(ScreenShakePacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.intensity);
    }

    public static ScreenShakePacket decode(FriendlyByteBuf buf) {
        return new ScreenShakePacket(buf.readFloat());
    }

    public static void handle(ScreenShakePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (Minecraft.getInstance().player instanceof IScreenShaker shaker) {
                shaker.behemoths$setShakeIntensity(msg.intensity);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}