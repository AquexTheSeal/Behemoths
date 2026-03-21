package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.client.clientdata.ClientBossBarData;

import java.util.UUID;
import java.util.function.Supplier;

public record SyncBossBarDataPacket(UUID bossBarId, int bossIdx) {

    public static void encode(SyncBossBarDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.bossBarId);
        buffer.writeInt(packet.bossIdx);
    }

    public static SyncBossBarDataPacket decode(FriendlyByteBuf buffer) {
        return new SyncBossBarDataPacket(buffer.readUUID(), buffer.readInt());
    }

    public static void handle(SyncBossBarDataPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientBossBarData.setBossIdx(packet.bossBarId, packet.bossIdx);
        });
        context.get().setPacketHandled(true);
    }
}