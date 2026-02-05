package org.celestialworkshop.behemoths.world.clientdata;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.client.guis.screens.VotingResultsScreen;
import org.celestialworkshop.behemoths.client.guis.screens.VotingSelectionScreen;
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

    public static void updateVoteData(int[] voteResults) {
        if (!(Minecraft.getInstance().screen instanceof PauseScreen)) {
            Minecraft.getInstance().setScreen(new VotingResultsScreen(voteResults));
            PandemoniumMusicManager.stop();
        }
    }
}
