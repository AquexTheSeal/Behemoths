package org.celestialworkshop.behemoths.network;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.network.c2s.CurseSelectionIndexPacket;
import org.celestialworkshop.behemoths.network.s2c.*;
import org.celestialworkshop.behemoths.network.shared.EntityActionSharedPacket;

public class BMNetwork {
    private static final String PROTOCOL_VERSION = "1.0";
    private static int packetId = 0;
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            Behemoths.prefix("main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    
    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, ManageAnimationStatePacket.class, ManageAnimationStatePacket::encode, ManageAnimationStatePacket::decode, ManageAnimationStatePacket::handle);
        INSTANCE.registerMessage(id++, OpenPandemoniumSelectionPacket.class, OpenPandemoniumSelectionPacket::encode, OpenPandemoniumSelectionPacket::decode, OpenPandemoniumSelectionPacket::handle);
        INSTANCE.registerMessage(id++, SendVoteDataPacket.class, SendVoteDataPacket::encode, SendVoteDataPacket::decode, SendVoteDataPacket::handle);
        INSTANCE.registerMessage(id++, ShortenVoteTimerPacket.class, ShortenVoteTimerPacket::encode, ShortenVoteTimerPacket::decode, ShortenVoteTimerPacket::handle);
        INSTANCE.registerMessage(id++, SyncSpecialtiesDataPacket.class, SyncSpecialtiesDataPacket::encode, SyncSpecialtiesDataPacket::decode, SyncSpecialtiesDataPacket::handle);

        INSTANCE.registerMessage(id++, CurseSelectionIndexPacket.class, CurseSelectionIndexPacket::encode, CurseSelectionIndexPacket::decode, CurseSelectionIndexPacket::handle);

        INSTANCE.registerMessage(id++, EntityActionSharedPacket.class, EntityActionSharedPacket::encode, EntityActionSharedPacket::decode, EntityActionSharedPacket::handle);
    }

    public static void sendToAll(Object packet) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void sendToTrackingPlayers(Player targetPlayer, Object packet) {
        if (targetPlayer instanceof ServerPlayer serverPlayer) {
            INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), packet);
        }
    }

    public static void sendToPlayer(ServerPlayer player, Object packet) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }

    public static void sendToServer(Object packet) {
        INSTANCE.sendToServer(packet);
    }
}
