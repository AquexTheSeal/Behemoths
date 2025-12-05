package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

import java.util.function.Supplier;

public record ShortenVoteTimerPacket(int newTime) {
    
    public static void encode(ShortenVoteTimerPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.newTime);
    }

    public static ShortenVoteTimerPacket decode(FriendlyByteBuf buffer) {
        return new ShortenVoteTimerPacket(buffer.readInt());
    }

    public static void handle(ShortenVoteTimerPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientPandemoniumData.localRemainingTime = packet.newTime();
        });
        context.get().setPacketHandled(true);
    }
}
