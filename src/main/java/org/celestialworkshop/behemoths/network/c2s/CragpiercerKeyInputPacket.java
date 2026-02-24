package org.celestialworkshop.behemoths.network.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.entities.misc.Cragpiercer;

import java.util.function.Supplier;

public record CragpiercerKeyInputPacket(int entityId, byte action) {

    public static void encode(CragpiercerKeyInputPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeByte(packet.action);
    }

    public static CragpiercerKeyInputPacket decode(FriendlyByteBuf buffer) {
        return new CragpiercerKeyInputPacket(buffer.readInt(), buffer.readByte());
    }

    public static void handle(CragpiercerKeyInputPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            ServerLevel level = sender.serverLevel();
            Entity capture = level.getEntity(packet.entityId());
            if (capture instanceof Cragpiercer cragpiercer && cragpiercer.getControllingPlayerUUID().equals(sender.getUUID())) {
                cragpiercer.handleServerKeyInput(packet.action());
            }
        });
        context.get().setPacketHandled(true);
    }
}
