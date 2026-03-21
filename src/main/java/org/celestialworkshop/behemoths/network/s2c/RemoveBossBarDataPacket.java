package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.client.clientdata.ClientBossBarData;

import java.util.UUID;
import java.util.function.Supplier;

public record RemoveBossBarDataPacket(UUID bossBarId) {

    public static void encode(RemoveBossBarDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.bossBarId);
    }

    public static RemoveBossBarDataPacket decode(FriendlyByteBuf buffer) {
        return new RemoveBossBarDataPacket(buffer.readUUID());
    }

    public static void handle(RemoveBossBarDataPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientBossBarData.removeBossBar(packet.bossBarId);
        });
        context.get().setPacketHandled(true);
    }
}