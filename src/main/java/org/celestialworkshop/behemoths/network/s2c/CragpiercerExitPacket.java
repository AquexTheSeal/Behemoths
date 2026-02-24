package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.api.camera.CragpiercerCameraManager;

import java.util.function.Supplier;

public record CragpiercerExitPacket() {

    public static void encode(CragpiercerExitPacket packet, FriendlyByteBuf buffer) {
    }

    public static CragpiercerExitPacket decode(FriendlyByteBuf buffer) {
        return new CragpiercerExitPacket();
    }

    public static void handle(CragpiercerExitPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            CragpiercerCameraManager.exitCamera();
        });
        context.get().setPacketHandled(true);
    }
}
