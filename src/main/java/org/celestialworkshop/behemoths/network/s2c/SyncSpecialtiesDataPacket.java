package org.celestialworkshop.behemoths.network.s2c;

import net.minecraft.nbt.NbtOps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;
import org.celestialworkshop.behemoths.datagen.reloadlisteners.ItemSpecialtyReloadListener;

import java.util.Map;
import java.util.function.Supplier;

public record SyncSpecialtiesDataPacket(Map<ResourceLocation, SpecialtyInstance.Wrapper> data) {

    @SuppressWarnings("deprecation")
    public static void encode(SyncSpecialtiesDataPacket packet, FriendlyByteBuf buffer) {
        buffer.writeMap(packet.data(),
                FriendlyByteBuf::writeResourceLocation,
                (buf, list) -> buf.writeWithCodec(NbtOps.INSTANCE, SpecialtyInstance.Wrapper.CODEC, list)
        );
    }

    public static SyncSpecialtiesDataPacket decode(FriendlyByteBuf buffer) {
        Map<ResourceLocation, SpecialtyInstance.Wrapper> data = buffer.readMap(
                FriendlyByteBuf::readResourceLocation,
                buf -> buf.readWithCodec(NbtOps.INSTANCE, SpecialtyInstance.Wrapper.CODEC)
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
