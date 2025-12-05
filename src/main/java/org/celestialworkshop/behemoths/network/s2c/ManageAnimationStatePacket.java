package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.entities.ai.BMEntity;

import java.util.function.Supplier;

public record ManageAnimationStatePacket(int entityId, String animationId, Action action) {

    public static void encode(ManageAnimationStatePacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeUtf(packet.animationId);
        buffer.writeEnum(packet.action);
    }

    public static ManageAnimationStatePacket decode(FriendlyByteBuf buffer) {
        return new ManageAnimationStatePacket(buffer.readInt(), buffer.readUtf(), buffer.readEnum(Action.class));
    }

    public static void handle(ManageAnimationStatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
            if (entity instanceof BMEntity bm && bm.getAnimationManager() != null) {
                if (packet.action == Action.START) {
                    bm.getAnimationManager().getAnimationState(packet.animationId).startIfStopped(entity.tickCount);
                } else if (packet.action == Action.FORCE_sTART) {
                    bm.getAnimationManager().getAnimationState(packet.animationId).start(entity.tickCount);
                } else if (packet.action == Action.STOP) {
                    bm.getAnimationManager().getAnimationState(packet.animationId).stop();
                }
            }
        });
        context.get().setPacketHandled(true);
    }

    public enum Action {
        START,
        FORCE_sTART,
        STOP
    }
}
