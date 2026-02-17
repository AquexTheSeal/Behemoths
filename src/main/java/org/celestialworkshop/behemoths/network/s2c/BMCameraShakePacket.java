package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.api.camera.ScreenShakeHandler;

import java.util.function.Supplier;

public record BMCameraShakePacket(float intensity, int duration, float frequency) {

    public static void encode(BMCameraShakePacket packet, FriendlyByteBuf buffer) {
        buffer.writeFloat(packet.intensity);
        buffer.writeInt(packet.duration);
        buffer.writeFloat(packet.frequency);
    }

    public static BMCameraShakePacket decode(FriendlyByteBuf buffer) {
        return new BMCameraShakePacket(buffer.readFloat(), buffer.readInt(), buffer.readFloat());
    }

    public static void handle(BMCameraShakePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ScreenShakeHandler.shakeLocal(packet.intensity, packet.duration, packet.frequency));
        context.get().setPacketHandled(true);
    }
}
