package org.celestialworkshop.behemoths.network.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.world.savedata.WorldPandemoniumData;

import java.util.function.Supplier;

public record CurseSelectionIndexPacket(int index) {

    public static void encode(CurseSelectionIndexPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.index());
    }

    public static CurseSelectionIndexPacket decode(FriendlyByteBuf buffer) {
        return new CurseSelectionIndexPacket(buffer.readInt());
    }

    public static void handle(CurseSelectionIndexPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            WorldPandemoniumData.get(sender.serverLevel()).addVoteData(sender.getUUID(), packet.index());
            Behemoths.LOGGER.debug("Added Vote Data to Server from player: " + sender.getDisplayName().getString() + " with index: " + packet.index());
        });
        context.get().setPacketHandled(true);
    }
}
