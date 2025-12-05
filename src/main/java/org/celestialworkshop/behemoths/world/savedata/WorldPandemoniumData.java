package org.celestialworkshop.behemoths.world.savedata;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.PandemoniumCurse;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.SendVoteDataPacket;
import org.celestialworkshop.behemoths.network.s2c.ShortenVoteTimerPacket;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.utils.WorldUtils;

import java.util.List;
import java.util.UUID;

public class WorldPandemoniumData extends SavedData {

    private final ObjectArrayList<PandemoniumCurse> activePandemoniumCurses = new ObjectArrayList<>();
    private final ObjectArrayList<PandemoniumCurse> selectableCurses = new ObjectArrayList<>();
    private int remainingTime = 0;
    private final Object2IntMap<UUID> voteData = new Object2IntArrayMap<>();

    public WorldPandemoniumData() {
    }

    public static WorldPandemoniumData load(CompoundTag tag) {
        WorldPandemoniumData data = new WorldPandemoniumData();

        data.activePandemoniumCurses.clear();
        ListTag list = tag.getList("ActiveCurses", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            String idString = list.getString(i);
            ResourceLocation id = ResourceLocation.parse(idString);
            PandemoniumCurse modifier = BMPandemoniumCurses.REGISTRY.get().getValue(id);
            if (modifier != null) {
                data.activePandemoniumCurses.add(modifier);
            }
        }

        data.selectableCurses.clear();
        List<PandemoniumCurse> selectableCursesFromFile = new ObjectArrayList<>();
        list = tag.getList("SelectableCurses", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            String idString = list.getString(i);
            ResourceLocation id = ResourceLocation.parse(idString);
            PandemoniumCurse modifier = BMPandemoniumCurses.REGISTRY.get().getValue(id);
            if (modifier != null) {
                selectableCursesFromFile.add(modifier);
            }
        }

        if (!selectableCursesFromFile.isEmpty()) {
            data.addActivePandemoniumCurse(selectableCursesFromFile.get(RandomSource.create().nextInt(selectableCursesFromFile.size())));
            data.selectableCurses.clear();
            data.setDirty();
        }

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {

        ListTag list = new ListTag();
        for (PandemoniumCurse modifier : activePandemoniumCurses) {
            ResourceLocation id = BMPandemoniumCurses.REGISTRY.get().getKey(modifier);
            if (id != null) {
                list.add(StringTag.valueOf(id.toString()));
            }
        }
        tag.put("ActiveCurses", list);

        list = new ListTag();
        for (PandemoniumCurse modifier : selectableCurses) {
            ResourceLocation id = BMPandemoniumCurses.REGISTRY.get().getKey(modifier);
            if (id != null) {
                list.add(StringTag.valueOf(id.toString()));
            }
        }
        tag.put("SelectableCurses", list);

        return tag;
    }

    public void tickPandemoniumWorld(Level level) {
        if (remainingTime > 0) {
            remainingTime--;
        } else if (remainingTime == 0) {
            remainingTime = -20;
        } else {
            remainingTime++;
            if (remainingTime == -1 && !selectableCurses.isEmpty()) {
                WorldUtils.endPandemoniumSelection(level);
            }
        }

        if (remainingTime > 60 && voteData.size() == level.players().size()) {
            this.remainingTime = 60;
            BMNetwork.sendToAll(new ShortenVoteTimerPacket(60));
        }
    }

    public void finishVotingCalculations() {
        int[] voteResults = WorldUtils.getVoteResultsFromData(voteData, selectableCurses.size());
        BMNetwork.sendToAll(new SendVoteDataPacket(voteResults));

        int highestVoteIndex = voteResults[3];
        Behemoths.LOGGER.debug("\tSelected Curse: " + selectableCurses.get(highestVoteIndex).getDisplayName().getString());
        this.addActivePandemoniumCurse(selectableCurses.get(highestVoteIndex));
    }

    public ObjectArrayList<PandemoniumCurse> getActivePandemoniumCurses() {
        return activePandemoniumCurses;
    }

    public void addActivePandemoniumCurse(PandemoniumCurse modifier) {
        this.activePandemoniumCurses.add(modifier);
        this.setDirty();
    }

    public void removeActivePandemoniumCurse(PandemoniumCurse modifier) {
        this.activePandemoniumCurses.remove(modifier);
        this.setDirty();
    }

    public void clearActivePandemoniumCurses() {
        this.activePandemoniumCurses.clear();
        this.setDirty();
    }

    public ObjectArrayList<PandemoniumCurse> getSelectableCurses() {
        return selectableCurses;
    }

    public void addSelectableCurse(PandemoniumCurse modifier) {
        this.selectableCurses.add(modifier);
        this.setDirty();
    }

    public void  clearSelectableCurses() {
        this.selectableCurses.clear();
        this.setDirty();
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public Object2IntMap<UUID> getVoteData() {
        return voteData;
    }

    public void addVoteData(UUID uuid, int votedIndex) {
        this.voteData.put(uuid, votedIndex);
    }

    public void clearVoteData() {
        this.voteData.clear();
    }

    public static WorldPandemoniumData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                WorldPandemoniumData::load,
                WorldPandemoniumData::new,
                "behemoths_data"
        );
    }
}