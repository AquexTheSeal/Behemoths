package org.celestialworkshop.behemoths.network.s2c;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.misc.sounds.PandemoniumMusicManager;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

import java.util.List;
import java.util.function.Supplier;

public record OpenPandemoniumSelectionPacket(List<ResourceLocation> selectableCurseIds, int time) {

    public static void encode(OpenPandemoniumSelectionPacket packet, FriendlyByteBuf buffer) {
        buffer.writeInt(packet.selectableCurseIds().size());
        for (ResourceLocation id : packet.selectableCurseIds()) {
            buffer.writeResourceLocation(id);
        }
        buffer.writeInt(packet.time());
    }

    public static OpenPandemoniumSelectionPacket decode(FriendlyByteBuf buffer) {
        int selectableSize = buffer.readInt();
        List<ResourceLocation> selectableCurseIds = new ObjectArrayList<>();
        for (int i = 0; i < selectableSize; i++) {
            selectableCurseIds.add(buffer.readResourceLocation());
        }

        return new OpenPandemoniumSelectionPacket(selectableCurseIds, buffer.readInt());
    }

    public static void handle(OpenPandemoniumSelectionPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            List<PandemoniumCurse> list = packet.selectableCurseIds().stream().map(BMPandemoniumCurses.REGISTRY.get()::getValue).toList();
            PandemoniumMusicManager.play(BMSoundEvents.VOTING_AMBIENT.get());
            ClientPandemoniumData.openPandemoniumSelection(list, true);
            ClientPandemoniumData.localSelectableCurses.addAll(list);
            ClientPandemoniumData.localRemainingTime = packet.time();
            ClientPandemoniumData.localMaxTime = packet.time();
        });
        context.get().setPacketHandled(true);
    }
}
