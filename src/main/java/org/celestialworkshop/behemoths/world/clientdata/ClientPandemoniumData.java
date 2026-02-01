package org.celestialworkshop.behemoths.world.clientdata;

import net.minecraft.client.Minecraft;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.client.guis.screens.PandemoniumCurseSelectionScreen;
import org.celestialworkshop.behemoths.client.guis.screens.VotingResultsScreen;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.c2s.CurseSelectionIndexPacket;

import java.util.ArrayList;
import java.util.List;

public class ClientPandemoniumData {
    public static List<PandemoniumCurse> localSelectableCurses = new ArrayList<>();

    public static int localSelectedIndex = -1;
    public static int localRemainingTime = -1;
    public static int localMaxTime = 0;

    // Management

    public static void stopVoting() {
        localSelectedIndex = -1;
        localRemainingTime = -1;
        localMaxTime = 0;
    }

    public static void openPandemoniumSelection(List<PandemoniumCurse> curses, boolean clearSelection) {
        if (clearSelection) localSelectedIndex = -1;
        Minecraft.getInstance().setScreen(new PandemoniumCurseSelectionScreen(curses));
    }

    public static void tickPandemoniumClient() {
        if (localRemainingTime > 0) {
            localRemainingTime--;
        } else {
            if (localRemainingTime == 0) {
                if (localSelectedIndex >= 0) {
                    BMNetwork.sendToServer(new CurseSelectionIndexPacket(localSelectedIndex));
                }
                localRemainingTime = -1;
            }
        }
    }

    public static void updateVoteData(int[] voteResults) {
        Minecraft.getInstance().setScreen(new VotingResultsScreen(voteResults));
    }
}
