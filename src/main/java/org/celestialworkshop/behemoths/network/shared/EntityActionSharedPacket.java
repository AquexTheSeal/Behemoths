package org.celestialworkshop.behemoths.network.shared;

import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.entities.BanishingStampede;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public record EntityActionSharedPacket(int entityId, Action action, Map<String, Object> parameters) {

    @SafeVarargs
    public EntityActionSharedPacket(int entityId, Action action, Pair<String, Object>... params) {
        this(entityId, action, pairToMap(params));
    }

    @SafeVarargs
    private static Map<String, Object> pairToMap(Pair<String, Object>... pairs) {
        Map<String, Object> map = new Object2ObjectOpenHashMap<>();
        for (Pair<String, Object> pair : pairs) {
            map.put(pair.key(), pair.value());
        }
        return map;
    }

    public static void encode(EntityActionSharedPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.entityId);
        buffer.writeEnum(packet.action);

        buffer.writeInt(packet.parameters.size());
        for (Map.Entry<String, Object> entry : packet.parameters.entrySet()) {
            buffer.writeUtf(entry.getKey());
            Object value = entry.getValue();
            if (value instanceof Integer i) {
                buffer.writeByte(0);
                buffer.writeInt(i);
            } else if (value instanceof Float f) {
                buffer.writeByte(1);
                buffer.writeFloat(f);
            } else if (value instanceof Double d) {
                buffer.writeByte(2);
                buffer.writeDouble(d);
            } else if (value instanceof Boolean b) {
                buffer.writeByte(3);
                buffer.writeBoolean(b);
            } else if (value instanceof String s) {
                buffer.writeByte(4);
                buffer.writeUtf(s);
            } else {
                throw new IllegalArgumentException("Unsupported parameter type: " + value.getClass());
            }
        }
    }

    public static EntityActionSharedPacket decode(FriendlyByteBuf buffer) {
        int entityId = buffer.readInt();
        Action action = buffer.readEnum(Action.class);

        int count = buffer.readInt();
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < count; i++) {
            String key = buffer.readUtf();
            byte type = buffer.readByte();
            Object value = switch (type) {
                case 0 -> buffer.readInt();
                case 1 -> buffer.readFloat();
                case 2 -> buffer.readDouble();
                case 3 -> buffer.readBoolean();
                case 4 -> buffer.readUtf();
                default -> throw new IllegalArgumentException("Unknown parameter type: " + type);
            };
            parameters.put(key, value);
        }

        return new EntityActionSharedPacket(entityId, action, parameters);
    }

    public static void handle(EntityActionSharedPacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            LogicalSide side = context.getDirection().getReceptionSide();
            if (side.isClient()) {
                handleS2C(packet);
            } else {
                handleC2S(packet, context);
            }
        });
        context.setPacketHandled(true);
    }

    // Handling of Server -> Client
    private static void handleS2C(EntityActionSharedPacket packet) {
        Entity entity = Minecraft.getInstance().level.getEntity(packet.entityId);
        if (entity == null) return;

        switch (packet.action()) {
            case FORCE_JUMP_STAMPEDE -> {
                if (entity instanceof BanishingStampede bm) {
                    if (packet.parameters.containsKey("strength")) {
                        bm.executeRidersJump((Float) packet.parameters.get("strength"), Vec3.ZERO);
                    }
                }
            }
            case SET_Y_ROTATION -> {
                if (entity instanceof BanishingStampede bm) {
                    if (packet.parameters.containsKey("val")) {
                        bm.setYRot((Float) packet.parameters.get("val"));
                        bm.yRotO = bm.getYRot();
                    }
                }
            }
            default -> throw new IllegalArgumentException("Packet action is invalid for Server -> Client: " + packet.action());
        }
    }

    // Handling of Client -> Server
    private static void handleC2S(EntityActionSharedPacket packet, NetworkEvent.Context context) {
        Entity entity = context.getSender().level().getEntity(packet.entityId);
        if (entity == null) return;

        switch (packet.action()) {
            case START_STAMPEDE_RAMMING -> {
                if (entity instanceof BanishingStampede bm) {
                    bm.setRamming(true);
                }
            }
            default -> throw new IllegalArgumentException("Packet action is invalid for Client -> Server: " + packet.action());
        }
    }

    public enum Action {
        // C2S
        START_STAMPEDE_RAMMING,

        // S2C
        FORCE_JUMP_STAMPEDE,
        SET_Y_ROTATION;
    }
}
