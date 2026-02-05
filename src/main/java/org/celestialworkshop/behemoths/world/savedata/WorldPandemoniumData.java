package org.celestialworkshop.behemoths.world.savedata;

import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.config.BMConfigManager;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.SendVoteDataPacket;
import org.celestialworkshop.behemoths.network.s2c.ShortenVoteTimerPacket;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.misc.utils.WorldUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class WorldPandemoniumData extends SavedData {

    private final ObjectArrayList<PandemoniumCurse> activePandemoniumCurses = new ObjectArrayList<>();
    private final Set<ResourceLocation> votingTriggeredEntities = new HashSet<>();
    private final Set<ResourceLocation> votingTriggeredAdvancements = new HashSet<>();

    private final ObjectArrayList<PandemoniumCurse> selectableCurses = new ObjectArrayList<>();
    private final Object2IntMap<UUID> voteData = new Object2IntArrayMap<>();
    private int remainingTime = 0;
    private int pendingVotingRequests = 0;

    public WorldPandemoniumData() {}

    public boolean isVotingActive() {
        return !selectableCurses.isEmpty();
    }

    public static WorldPandemoniumData load(CompoundTag tag) {
        WorldPandemoniumData data = new WorldPandemoniumData();

        ListTag list = tag.getList("ActiveCurses", Tag.TAG_STRING);
        for (int i = 0; i < list.size(); i++) {
            PandemoniumCurse curse = BMPandemoniumCurses.REGISTRY.get().getValue(ResourceLocation.parse(list.getString(i)));
            if (curse != null) data.activePandemoniumCurses.add(curse);
        }

        list = tag.getList("VotingTriggeredEntities", Tag.TAG_STRING);
        for (Tag t : list) data.votingTriggeredEntities.add(ResourceLocation.parse(t.getAsString()));

        list = tag.getList("VotingTriggeredAdvancements", Tag.TAG_STRING);
        for (Tag t : list) data.votingTriggeredAdvancements.add(ResourceLocation.parse(t.getAsString()));

        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (PandemoniumCurse curse : activePandemoniumCurses) {
            ResourceLocation id = BMPandemoniumCurses.REGISTRY.get().getKey(curse);
            if (id != null) list.add(StringTag.valueOf(id.toString()));
        }
        tag.put("ActiveCurses", list);

        list = new ListTag();
        for (ResourceLocation id : votingTriggeredEntities) list.add(StringTag.valueOf(id.toString()));
        tag.put("VotingTriggeredEntities", list);

        list = new ListTag();
        for (ResourceLocation id : votingTriggeredAdvancements) list.add(StringTag.valueOf(id.toString()));
        tag.put("VotingTriggeredAdvancements", list);

        return tag;
    }

    public boolean hasEntityTriggeredVoting(ResourceLocation id) {
        return votingTriggeredEntities.contains(id);
    }

    public void markEntityTriggeredVoting(ResourceLocation id) {
        votingTriggeredEntities.add(id);
        setDirty();
    }

    public void clearEntityTriggeredVoting() {
        votingTriggeredEntities.clear();
        setDirty();
    }

    public boolean hasAdvancementTriggeredVoting(ResourceLocation id) {
        return votingTriggeredAdvancements.contains(id);
    }

    public void markAdvancementTriggeredVoting(ResourceLocation id) {
        votingTriggeredAdvancements.add(id);
        setDirty();
    }

    public void clearAdvancementTriggeredVoting() {
        votingTriggeredAdvancements.clear();
        setDirty();
    }

    public void tickPandemoniumWorld(Level level) {
        if (remainingTime > 0) {
            remainingTime--;
        } else if (remainingTime == 0) {
            remainingTime = -20;
        } else {
            remainingTime++;
            if (remainingTime == -1 && !selectableCurses.isEmpty()) {
                WorldUtils.endPandemoniumSelection(level, false);
            }
        }

        if (remainingTime > 60 && voteData.size() == level.players().size()) {
            remainingTime = 60;
            BMNetwork.sendToAll(new ShortenVoteTimerPacket(60));
        }
    }

    public void finishVotingCalculations(ServerLevel server, boolean forceStopClients) {
        int[] voteResults = WorldUtils.getVoteResultsFromData(voteData, selectableCurses.size());
        BMNetwork.sendToAll(new SendVoteDataPacket(voteResults, forceStopClients));

        int highestVoteIndex = voteResults[3];
        String winningCurse = selectableCurses
                .get(highestVoteIndex)
                .getDisplayName()
                .getString();

        if (BMConfigManager.COMMON.enableResultsChatLogging.get()) {
            server.players().forEach(player -> {

                player.playNotifySound(SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.5F, 0.5F);
                player.sendSystemMessage(Component.translatable("chat.behemoths.voting_result_header").withStyle(ChatFormatting.RED));

                if (BMConfigManager.COMMON.enableResultsShowVoters.get()) {
                    player.sendSystemMessage(Component.translatable("chat.behemoths.voting_result_player_breakdown_header").withStyle(ChatFormatting.LIGHT_PURPLE));

                    for (Object2IntMap.Entry<UUID> entry : this.getVoteData().object2IntEntrySet()) {

                        server.getServer().getProfileCache().get(entry.getKey()).ifPresent(profile -> {
                            String voterName = profile.getName();
                            String votedCurse = this.getSelectableCurses().get(entry.getIntValue()).getDisplayName().getString();

                            Component voterNameComponent = Component.literal(voterName).withStyle(ChatFormatting.AQUA);
                            Component votedCurseComponent = Component.literal(votedCurse).withStyle(ChatFormatting.YELLOW);
                            player.sendSystemMessage(
                                    Component.literal("• ").withStyle(ChatFormatting.DARK_GRAY)
                                            .append(Component.translatable("chat.behemoths.voting_result_player_breakdown", voterNameComponent, votedCurseComponent))
                            );
                        });
                    }
                }

                player.sendSystemMessage(Component.translatable("chat.behemoths.voting_result_tally_breakdown_header").withStyle(ChatFormatting.LIGHT_PURPLE));
                for (int i = 0; i < this.getSelectableCurses().size(); i++) {
                    PandemoniumCurse curse = this.getSelectableCurses().get(i);
                    String indexCurse = curse.getDisplayName().getString();
                    int indexVotes = voteResults[i];

                    Component indexCurseComponent = Component.literal(indexCurse).withStyle(ChatFormatting.DARK_RED);
                    Component indexVotesComponent = Component.literal(String.valueOf(indexVotes)).withStyle(ChatFormatting.YELLOW);

                    player.sendSystemMessage(
                            Component.literal("• ").withStyle(ChatFormatting.DARK_GRAY)
                                    .append(Component.translatable("chat.behemoths.voting_result_tally_breakdown", indexCurseComponent, indexVotesComponent))
                    );
                }

                Component winningCurseComponent = Component.literal(winningCurse).withStyle(ChatFormatting.RED);
                player.sendSystemMessage(Component.translatable("chat.behemoths.voting_final_curse", winningCurseComponent).withStyle(ChatFormatting.GRAY));
            });
        }

        addActivePandemoniumCurse(selectableCurses.get(highestVoteIndex));
    }

    public ObjectArrayList<PandemoniumCurse> getActivePandemoniumCurses() {
        return activePandemoniumCurses;
    }

    public void addActivePandemoniumCurse(PandemoniumCurse modifier) {
        activePandemoniumCurses.add(modifier);
        setDirty();
    }

    public void removeActivePandemoniumCurse(PandemoniumCurse modifier) {
        activePandemoniumCurses.remove(modifier);
        setDirty();
    }

    public void clearActivePandemoniumCurses() {
        activePandemoniumCurses.clear();
        setDirty();
    }

    public ObjectArrayList<PandemoniumCurse> getSelectableCurses() {
        return selectableCurses;
    }

    public void addSelectableCurse(PandemoniumCurse modifier) {
        selectableCurses.add(modifier);
        setDirty();
    }

    public void clearSelectableCurses() {
        selectableCurses.clear();
        setDirty();
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
        voteData.put(uuid, votedIndex);
    }

    public void clearVoteData() {
        voteData.clear();
    }

    public void markVotingRequested() {
        pendingVotingRequests++;
        setDirty();
    }

    public int getPendingRequests() {
        return pendingVotingRequests;
    }

    public void decrementPendingRequests() {
        if (pendingVotingRequests > 0) {
            pendingVotingRequests--;
            setDirty();
        }
    }

    public void removePendingRequests() {
        this.pendingVotingRequests = 0;
    }

    public static WorldPandemoniumData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(WorldPandemoniumData::load, WorldPandemoniumData::new, "behemoths_data");
    }
}
