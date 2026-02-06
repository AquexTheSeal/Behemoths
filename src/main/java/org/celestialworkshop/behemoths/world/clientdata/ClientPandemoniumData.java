package org.celestialworkshop.behemoths.world.clientdata;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.client.guis.screens.VotingResultsScreen;
import org.celestialworkshop.behemoths.client.guis.screens.VotingSelectionScreen;
import org.celestialworkshop.behemoths.config.BMConfigManager;
import org.celestialworkshop.behemoths.misc.sounds.PandemoniumMusicManager;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.c2s.CurseSelectionIndexPacket;

import java.util.List;

public class ClientPandemoniumData {
    public static List<PandemoniumCurse> localSelectableCurses = new ObjectArrayList<>();

    public static int localSelectedIndex = -1;
    public static int localRemainingTime = -1;
    public static int localMaxTime = 0;

    public static void stopVoting() {
        localSelectedIndex = -1;
        localRemainingTime = -1;
        localMaxTime = 0;
    }

    public static void reset() {
        localSelectableCurses.clear();
        stopVoting();
    }

    public static void openPandemoniumSelection(List<PandemoniumCurse> curses, boolean clearSelection) {
        if (clearSelection) localSelectedIndex = -1;
        if (!(Minecraft.getInstance().screen instanceof PauseScreen)) {
            Minecraft.getInstance().setScreen(new VotingSelectionScreen(curses));
        }
    }

    public static void tickPandemoniumClient() {
        if (localRemainingTime > 0) {
            localRemainingTime--;
        } else {
            if (localRemainingTime == 0) {
                if (localSelectedIndex >= 0) {
                    BMNetwork.sendToServer(new CurseSelectionIndexPacket(localSelectedIndex));
                }
                PandemoniumMusicManager.stop();
                localRemainingTime = -1;
            }
        }
    }

    public static void openResultsScreen(Object2IntMap<String> playerVoteData, int[] voteResults) {
        if (!(Minecraft.getInstance().screen instanceof PauseScreen)) {
            Minecraft.getInstance().setScreen(new VotingResultsScreen(voteResults, ClientPandemoniumData.localSelectedIndex));
            PandemoniumMusicManager.stop();
        }

        ClientPandemoniumData.logVotingResultsInChat(playerVoteData, voteResults);
    }

    public static void logVotingResultsInChat(Object2IntMap<String> playerVoteData, int[] voteResults) {

        if (!BMConfigManager.CLIENT.enableResultsChatLogging.get()) return;

        LocalPlayer player = Minecraft.getInstance().player;
        player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 0.5F);
        player.sendSystemMessage(Component.translatable("chat.behemoths.voting_result_header").withStyle(ChatFormatting.RED));

        if (!playerVoteData.isEmpty()) {
            player.sendSystemMessage(Component.translatable("chat.behemoths.voting_result_player_breakdown_header").withStyle(ChatFormatting.LIGHT_PURPLE));

            for (Object2IntMap.Entry<String> entry : playerVoteData.object2IntEntrySet()) {
                String votedCurse = localSelectableCurses.get(entry.getIntValue()).getDisplayName().getString();
                Component voterNameComponent = Component.literal(entry.getKey()).withStyle(ChatFormatting.AQUA);
                Component votedCurseComponent = Component.literal(votedCurse).withStyle(ChatFormatting.YELLOW);
                player.sendSystemMessage(
                        Component.literal("• ").withStyle(ChatFormatting.DARK_GRAY)
                                .append(Component.translatable("chat.behemoths.voting_result_player_breakdown", voterNameComponent, votedCurseComponent))
                );
            }
        }

        player.sendSystemMessage(Component.translatable("chat.behemoths.voting_result_tally_breakdown_header").withStyle(ChatFormatting.LIGHT_PURPLE));
        for (int i = 0; i < 3; i++) {
            if (voteResults[i] == -1) continue;

            PandemoniumCurse curse = localSelectableCurses.get(i);
            String indexCurse = curse.getDisplayName().getString();
            int indexVotes = voteResults[i];

            Component indexCurseComponent = Component.literal(indexCurse).withStyle(ChatFormatting.DARK_RED);
            Component indexVotesComponent = Component.literal(String.valueOf(indexVotes)).withStyle(ChatFormatting.YELLOW);

            player.sendSystemMessage(
                    Component.literal("• ").withStyle(ChatFormatting.DARK_GRAY)
                            .append(Component.translatable("chat.behemoths.voting_result_tally_breakdown", indexCurseComponent, indexVotesComponent))
            );
        }

        String winningCurse = localSelectableCurses.get(voteResults[3]).getDisplayName().getString();
        Component winningCurseComponent = Component.literal(winningCurse).withStyle(ChatFormatting.RED);
        player.sendSystemMessage(Component.translatable("chat.behemoths.voting_final_curse", winningCurseComponent).withStyle(ChatFormatting.GRAY));
    }
}
