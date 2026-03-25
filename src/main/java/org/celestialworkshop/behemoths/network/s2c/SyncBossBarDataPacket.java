package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.client.clientdata.ClientBossBarData;

import java.util.UUID;
import java.util.function.Supplier;

public record SyncBossBarDataPacket(UUID bossBarId, ClientBossBarData.DynamicData bossIdx) {

    public static void encode(SyncBossBarDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeUUID(packet.bossBarId);
        buffer.writeInt(packet.bossIdx.id());
        buffer.writeMap(packet.bossIdx.values(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
    }

    public static SyncBossBarDataPacket decode(FriendlyByteBuf buffer) {
        return new SyncBossBarDataPacket(buffer.readUUID(), new ClientBossBarData.DynamicData(buffer.readInt(), buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt)));
    }

    public static void handle(SyncBossBarDataPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientBossBarData.setBossIdx(packet.bossBarId, packet.bossIdx.id());
        });
        context.get().setPacketHandled(true);
    }
}