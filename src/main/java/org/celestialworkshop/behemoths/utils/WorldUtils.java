package org.celestialworkshop.behemoths.utils;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.PandemoniumCurse;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.OpenPandemoniumSelectionPacket;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.world.savedata.WorldPandemoniumData;

import java.util.*;

public class WorldUtils {

    public static boolean hasPandemoniumCurse(Level level, PandemoniumCurse curse) {
        if (level.isClientSide) return false;

        return WorldPandemoniumData.get((ServerLevel) level).getActivePandemoniumCurses().contains(curse);
    }

    public static void openPandemoniumSelection(Level level) {
        if (!level.isClientSide) {
            WorldPandemoniumData data = WorldPandemoniumData.get((ServerLevel) level);

            if (data.isVotingActive()) {
                Behemoths.LOGGER.warn("Tried to open Pandemonium Selection, but Voting is active.");
                return;
            }

            if (data.getSelectableCurses().isEmpty()) {

                data.setRemainingTime(1000);

                List<PandemoniumCurse> curseLookup = new ArrayList<>(BMPandemoniumCurses.REGISTRY.get().getEntries().stream()
                        .map(Map.Entry::getValue)
                        .filter(curse -> !data.getActivePandemoniumCurses().contains(curse))
                        .toList());

                Collections.shuffle(curseLookup);
                int sublistEnd = Math.min(3, curseLookup.size());
                if (sublistEnd == 0) {
                    Behemoths.LOGGER.warn("No available curses to select.");
                    return;
                }

                List<PandemoniumCurse> sublist = curseLookup.subList(0, sublistEnd);
                data.clearSelectableCurses();
                for (int i = 0; i < sublistEnd; i++) {
                    data.addSelectableCurse(sublist.get(i));
                }

                BMNetwork.sendToAll(new OpenPandemoniumSelectionPacket(data.getSelectableCurses().stream().map(PandemoniumCurse::getId).toList(), data.getRemainingTime()));

            } else {
                Behemoths.LOGGER.warn("Tried to open Pandemonium Selection, but Selectable Curses are not empty.");
            }
        }
    }

    public static void endPandemoniumSelection(Level level, boolean forceStopClients) {
        if (level instanceof ServerLevel server) {
            WorldPandemoniumData data = WorldPandemoniumData.get(server);

            Behemoths.LOGGER.debug("(Server) Pandemonium Curse Selection ended. Votes: ");
            for (Object2IntMap.Entry<UUID> entry : data.getVoteData().object2IntEntrySet()) {
                Behemoths.LOGGER.debug("\t-\tPlayer " + level.getPlayerByUUID(entry.getKey()).getDisplayName().getString() + " voted for " + data.getSelectableCurses().get(entry.getIntValue()).getDisplayName().getString());
            }

            data.finishVotingCalculations(forceStopClients);
            data.clearVoteData();
            data.clearSelectableCurses();
        }
    }

    public static int[] getVoteResultsFromData(Object2IntMap<UUID> voteData, int selectableSize) {
        int[] counts = new int[]{-1, -1, -1};

        for (int i = 0; i < selectableSize; i++) {
            counts[i] = 0;
        }

        voteData.object2IntEntrySet().forEach(entry -> {
            int v = entry.getIntValue();
            if (v >= 0 && v < selectableSize) {
                counts[v]++;
            }
        });

        if (selectableSize == 0) {
            return new int[]{-1, -1, -1, -1};
        }

        int max = 0;
        for (int i = 0; i < selectableSize; i++) {
            max = Math.max(max, counts[i]);
        }

        Random rand = new Random();

        if (max == 0) {
            return new int[]{counts[0], counts[1], counts[2], rand.nextInt(selectableSize)};
        }

        int[] tieIndices = new int[selectableSize];
        int tieCount = 0;

        for (int i = 0; i < selectableSize; i++) {
            if (counts[i] == max) {
                tieIndices[tieCount++] = i;
            }
        }

        int chosen = tieIndices[rand.nextInt(tieCount)];

        return new int[]{counts[0], counts[1], counts[2], chosen};
    }



}
