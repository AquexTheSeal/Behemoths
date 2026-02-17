package org.celestialworkshop.behemoths.api.pandemonium;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.config.BMConfigManager;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.OpenPandemoniumSelectionPacket;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.world.savedata.WorldPandemoniumData;

import java.util.*;

public class PandemoniumVotingSystem {

    public static final List<RegistryObject<PandemoniumCurse>> DISABLED_CURSES = List.of(
//            BMPandemoniumCurses.HEAVY_ARROW,
//            BMPandemoniumCurses.BURNING_ARCHER,
//            BMPandemoniumCurses.DEATH_LEAP,
//            BMPandemoniumCurses.OVERSEER
    );

    public static boolean hasPandemoniumCurse(Level level, PandemoniumCurse curse) {
        if (level.isClientSide) return false;

        return WorldPandemoniumData.get((ServerLevel) level).getActivePandemoniumCurses().contains(curse);
    }

    public static boolean hasPandemoniumCurse(Level level, RegistryObject<PandemoniumCurse> curse) {
        return hasPandemoniumCurse(level, curse.get());
    }

    public static PandemoniumVoteResult openPandemoniumSelection(Level level) {
        if (level.isClientSide) {
            return PandemoniumVoteResult.fail(Component.literal("Cannot start Pandemonium voting on client side."));
        }

        WorldPandemoniumData data = WorldPandemoniumData.get((ServerLevel) level);

        int maxCurses = BMConfigManager.COMMON.maxPandemoniumCurses.get();
        int activeCurseSize = data.getActivePandemoniumCurses().size();

        if (maxCurses != -1) {
            if (data.isVotingActive()) {
                int overallCurseSize = activeCurseSize + data.getPendingRequests() + 1;
                if (overallCurseSize >= maxCurses) {
                    return PandemoniumVoteResult.fail(Component.literal("Could not queue Pandemonium voting session. The configured maximum curses has been reached: " + overallCurseSize + "/" + maxCurses));
                }
            } else {
                if (activeCurseSize >= maxCurses) {
                    return PandemoniumVoteResult.fail(Component.literal("Could not start Pandemonium voting session. The configured maximum curses has been reached: " + activeCurseSize + "/" + maxCurses));
                }
            }
        }

        if (data.isVotingActive()) {
            data.markVotingRequested();
            return PandemoniumVoteResult.success(Component.literal("Pandemonium voting session has been queued. Queue: " + data.getPendingRequests() + "."));
        }

        if (!data.getSelectableCurses().isEmpty()) {
            return PandemoniumVoteResult.fail(Component.literal("Could not start Pandemonium voting session. Selectable Curses are not empty."));
        }

        double configTime = BMConfigManager.COMMON.pandemoniumVotingTimer.get();
        data.setRemainingTime(Mth.floor(configTime * 20));

        List<PandemoniumCurse> curseLookup = new ArrayList<>(BMPandemoniumCurses.REGISTRY.get().getEntries().stream()
                .filter(curseObj -> {
                    List<? extends String> blackListConfig = BMConfigManager.COMMON.pandemoniumVotingBlacklist.get();
                    List<ResourceLocation> blacklist = blackListConfig.stream().map(ResourceLocation::parse).toList();
                    return !blacklist.contains(curseObj.getKey().location()) && !DISABLED_CURSES.stream().map(RegistryObject::getKey).toList().contains(curseObj.getKey());
                })
                .map(Map.Entry::getValue)
                .filter(curse -> !data.getActivePandemoniumCurses().contains(curse))
                .toList());

        Collections.shuffle(curseLookup);
        int sublistEnd = Math.min(3, curseLookup.size());
        if (sublistEnd == 0) {
            Behemoths.LOGGER.warn("No available curses to select.");
            data.removePendingRequests();
            return PandemoniumVoteResult.fail(Component.literal("Could not start Pandemonium voting session. No more available curses to select."));
        }

        List<PandemoniumCurse> sublist = curseLookup.subList(0, sublistEnd);
        data.clearSelectableCurses();
        for (int i = 0; i < sublistEnd; i++) {
            data.addSelectableCurse(sublist.get(i));
        }

        BMNetwork.sendToAll(new OpenPandemoniumSelectionPacket(data.getSelectableCurses().stream().map(PandemoniumCurse::getId).toList(), data.getRemainingTime()));

        return PandemoniumVoteResult.success(Component.literal("Pandemonium voting session has started!"));
    }

    public static PandemoniumVoteResult endPandemoniumSelection(Level level) {
        if (level instanceof ServerLevel server) {
            WorldPandemoniumData data = WorldPandemoniumData.get(server);

            if (!data.isVotingActive()) {
                return PandemoniumVoteResult.fail(Component.literal("No active Pandemonium voting session to end."));
            }

            data.finishVotingCalculations(server);
            data.clearSelectableCurses();
            data.clearVoteData();

            if (data.getPendingRequests() > 0) {
                data.decrementPendingRequests();
                openPandemoniumSelection(level);
            }

            return PandemoniumVoteResult.success(Component.literal("Pandemonium voting session ended."));
        } else {
            return PandemoniumVoteResult.fail(Component.literal("Cannot stop Pandemonium voting on client side."));
        }
    }

    public static void resolveInterruptedVote(ServerLevel level) {
        WorldPandemoniumData data = WorldPandemoniumData.get(level);

        if (data.isVotingActive()) {
            int randomIndex = level.random.nextInt(data.getSelectableCurses().size());
            data.addActivePandemoniumCurse(data.getSelectableCurses().get(randomIndex));
            data.clearSelectableCurses();
            data.clearVoteData();
        }

        int pending = data.getPendingRequests();
        if (pending > 0) {
            List<PandemoniumCurse> available = new ArrayList<>(BMPandemoniumCurses.REGISTRY.get().getValues().stream()
                    .filter(c -> !data.getActivePandemoniumCurses().contains(c))
                    .toList());

            Collections.shuffle(available);

            for (int i = 0; i < pending && i < available.size(); i++) {
                data.addActivePandemoniumCurse(available.get(i));
            }

            while(data.getPendingRequests() > 0) data.decrementPendingRequests();
        }

        data.setRemainingTime(0);
        data.setDirty();
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
