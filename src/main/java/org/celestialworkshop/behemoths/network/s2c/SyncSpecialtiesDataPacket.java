package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstancesHolder;
import org.celestialworkshop.behemoths.datagen.reloadlisteners.ItemSpecialtyReloadListener;

import java.util.Map;
import java.util.function.Supplier;

public record SyncSpecialtiesDataPacket(Map<ResourceLocation, SpecialtyInstancesHolder> data) {

    @SuppressWarnings("deprecation")
    public static void encode(SyncSpecialtiesDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.data(),
                FriendlyByteBuf::writeResourceLocation,
                (buf, list) -> buf.writeWithCodec(NbtOps.INSTANCE, SpecialtyInstancesHolder.CODEC, list)
        );
    }

    public static SyncSpecialtiesDataPacket decode(FriendlyByteBuf buffer) {
        Map<ResourceLocation, SpecialtyInstancesHolder> data = buffer.readMap(
                FriendlyByteBuf::readResourceLocation,
                buf -> buf.readWithCodec(NbtOps.INSTANCE, SpecialtyInstancesHolder.CODEC)
        );
        return new SyncSpecialtiesDataPacket(data);
    }

    public static void handle(SyncSpecialtiesDataPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ItemSpecialtyReloadListener.SPECIALTY_DATA.clear();
            ItemSpecialtyReloadListener.SPECIALTY_DATA.putAll(packet.data());
        });
        context.get().setPacketHandled(true);
    }
}
