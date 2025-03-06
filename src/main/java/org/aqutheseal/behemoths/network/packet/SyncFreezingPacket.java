package org.aqutheseal.behemoths.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.aqutheseal.behemoths.capability.FreezingCapability;

import java.util.function.Supplier;

public class SyncFreezingPacket {
    private final int entityId;
    private final int freezeTime;

    public SyncFreezingPacket(int entityId, int freezeTime) {
        this.entityId = entityId;
        this.freezeTime = freezeTime;
    }

    public static void encode(SyncFreezingPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId);
        buf.writeInt(msg.freezeTime);
    }

    public static SyncFreezingPacket decode(FriendlyByteBuf buf) {
        return new SyncFreezingPacket(buf.readInt(), buf.readInt());
    }

    public static void handle(SyncFreezingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(msg.entityId);
            if (entity != null) {
                entity.getCapability(FreezingCapability.CAPABILITY).ifPresent(cap -> cap.setFreezeTime(msg.freezeTime));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
