package org.celestialworkshop.behemoths.network.shared;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.entities.ai.mount.CustomJumpingMount;
import org.celestialworkshop.behemoths.entities.ai.mount.MountJumpManager;
import org.celestialworkshop.behemoths.network.BMNetwork;

import java.util.function.Supplier;

public record QueueJumpPacket(int entityId, int power) {

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.power);
    }

    public static QueueJumpPacket decode(FriendlyByteBuf buf) {
        return new QueueJumpPacket(buf.readInt(), buf.readInt());
    }

    public static void handle(QueueJumpPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LogicalSide side = ctx.get().getDirection().getReceptionSide();

            if (side.isServer()) {
                ServerPlayer player = ctx.get().getSender();
                if (player != null && player.level() instanceof ServerLevel serverLevel) {
                    Entity entity = serverLevel.getEntity(packet.entityId);
                    if (entity instanceof CustomJumpingMount rideable && player.getVehicle() == entity) {
                        MountJumpManager jumpManager = rideable.getMountJumpManager();
                        jumpManager.queuedJumpPower = packet.power;
                        jumpManager.queuedJump = true;
                        jumpManager.jumpPower = 0;
                        BMNetwork.sendToAll(new QueueJumpPacket(packet.entityId, packet.power));
                    }
                }
            } else {
                Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
                if (entity instanceof CustomJumpingMount rideable) {
                    MountJumpManager jumpManager = rideable.getMountJumpManager();
                    jumpManager.queuedJumpPower = packet.power;
                    jumpManager.queuedJump = true;
                    jumpManager.jumpPower = 0;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
