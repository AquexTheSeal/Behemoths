package org.celestialworkshop.behemoths.network.s2c;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

import java.util.function.Supplier;

public record FinishVotingPacket(Object2IntMap<String> playerVoteData, int[] voteResults) {

    public static void encode(FinishVotingPacket packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.playerVoteData(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeInt);
        buffer.writeVarIntArray(packet.voteResults());
    }

    public static FinishVotingPacket decode(FriendlyByteBuf buffer) {
        Object2IntMap<String> playerVoteData = new Object2IntArrayMap<>(buffer.readMap(FriendlyByteBuf::readUtf, FriendlyByteBuf::readInt));
        int[] voteResults = buffer.readVarIntArray();
        return new FinishVotingPacket(playerVoteData, voteResults);
    }

    public static void handle(FinishVotingPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ClientPandemoniumData.openResultsScreen(packet.playerVoteData(), packet.voteResults());
            ClientPandemoniumData.stopVoting();
        });
        context.get().setPacketHandled(true);
    }
}
