package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

import java.util.function.Supplier;

public record SendVoteDataPacket(int[] voteResults) {

    public static void encode(SendVoteDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeVarIntArray(packet.voteResults());
    }

    public static SendVoteDataPacket decode(FriendlyByteBuf buffer) {
        int[] voteData = buffer.readVarIntArray();
        return new SendVoteDataPacket(voteData);
    }

    public static void handle(SendVoteDataPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientPandemoniumData.updateVoteData(packet.voteResults());
        });
        context.get().setPacketHandled(true);
    }
}
