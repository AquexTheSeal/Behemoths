package org.celestialworkshop.behemoths.network.c2s;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.entities.misc.Cragpiercer;

import java.util.function.Supplier;

public record CragpiercerRotationInputPacket(int entityId, byte xInput, byte yInput) {

    public static void encode(CragpiercerRotationInputPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeByte(packet.xInput);
        buffer.writeByte(packet.yInput);
    }

    public static CragpiercerRotationInputPacket decode(FriendlyByteBuf buffer) {
        return new CragpiercerRotationInputPacket(buffer.readInt(), buffer.readByte(), buffer.readByte());
    }

    public static void handle(CragpiercerRotationInputPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer sender = context.get().getSender();
            ServerLevel level = sender.serverLevel();
            Entity capture = level.getEntity(packet.entityId());
            if (capture instanceof Cragpiercer cragpiercer && cragpiercer.getControllingPlayerUUID().equals(sender.getUUID())) {
                cragpiercer.xRotInput = packet.xInput();
                cragpiercer.yRotInput = packet.yInput();
            }
        });
        context.get().setPacketHandled(true);
    }
}
