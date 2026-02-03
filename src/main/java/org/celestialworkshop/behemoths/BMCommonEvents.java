package org.celestialworkshop.behemoths;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.api.entity.BehemothProperties;
import org.celestialworkshop.behemoths.api.item.specialty.ItemSpecialtyCapabilityProvider;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;
import org.celestialworkshop.behemoths.commands.PandemoniumCommand;
import org.celestialworkshop.behemoths.config.BMConfigManager;
import org.celestialworkshop.behemoths.datagen.reloadlisteners.BehemothPropertiesReloadListener;
import org.celestialworkshop.behemoths.datagen.reloadlisteners.ItemSpecialtyReloadListener;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.SyncSpecialtiesDataPacket;
import org.celestialworkshop.behemoths.registries.BMAdvancementTriggers;
import org.celestialworkshop.behemoths.registries.BMCapabilities;
import org.celestialworkshop.behemoths.utils.WorldUtils;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;
import org.celestialworkshop.behemoths.world.savedata.WorldPandemoniumData;

import java.util.List;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMCommonEvents {

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (event.side == LogicalSide.SERVER) {
                WorldPandemoniumData.get((ServerLevel) event.level).tickPandemoniumWorld(event.level);
            } else {
                ClientPandemoniumData.tickPandemoniumClient();
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Entity killer = event.getSource().getEntity();
        ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());

        if (entityId == null) return;

        if (!entity.level().isClientSide) {
            if (killer instanceof ServerPlayer player) {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof BehemothHeartItem) {
                        BehemothHeartItem.addHeartEnergy(stack, BehemothProperties.getHeartEnergy(entity));
                    }
                }

                if (BehemothProperties.isBehemoth(entity)) {
                    BMAdvancementTriggers.KILL_BEHEMOTH.trigger(player);
                }

                List<String> onceList = BMConfigManager.COMMON.pandemoniumOnceEntities.get().stream().map(s -> (String) s).toList();
                List<String> repeatList = BMConfigManager.COMMON.pandemoniumRepeatEntities.get().stream().map(s -> (String) s).toList();

                WorldPandemoniumData data = WorldPandemoniumData.get(player.serverLevel());

                if (onceList.contains(entityId.toString())) {
                    if (data.hasEntityTriggeredVoting(entityId)) return;

                    data.markEntityTriggeredVoting(entityId);
                    WorldUtils.openPandemoniumSelection(player.level());
                    return;
                }

                if (repeatList.contains(entityId.toString())) {
                    WorldUtils.openPandemoniumSelection(player.level());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        List<String> onceList = BMConfigManager.COMMON.pandemoniumOnceAdvancements.get().stream().map(s -> (String) s).toList();
        List<String> repeatList = BMConfigManager.COMMON.pandemoniumRepeatAdvancements.get().stream().map(s -> (String) s).toList();

        if (onceList.contains(event.getAdvancement().getId().toString())) {
            WorldUtils.openPandemoniumSelection(event.getEntity().level());

        } else if (repeatList.contains(event.getAdvancement().getId().toString())) {
            if (WorldUtils.)
        }
    }

    @SubscribeEvent
    public static void onEntityDropItem(LivingDropsEvent event) {
        if (event.getEntity() instanceof Player || event.getDrops().isEmpty()) {
            return;
        }
        for (ItemEntity itemEntity : event.getDrops()) {
            ItemStack stack = itemEntity.getItem();
            if (stack.getItem() instanceof BehemothHeartItem heart) {
                int maxEnergy = heart.getMaxHeartEnergy();
                int minEnergy = Math.round(maxEnergy * 0.35F);
                int maxEnergyRange = Math.round(maxEnergy * 0.65F);
                int randomEnergy = event.getEntity().getRandom().nextInt(minEnergy, maxEnergyRange + 1);
                BehemothHeartItem.setHeartEnergy(stack, randomEnergy);
            }
        }
    }

    @SubscribeEvent
    public static void onCommandRegistry(RegisterCommandsEvent event) {
        PandemoniumCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        event.addListener(new ItemSpecialtyReloadListener());
        event.addListener(new BehemothPropertiesReloadListener());
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        ServerPlayer player = event.getPlayer();
        if (player != null) {
            BMNetwork.sendToPlayer(player, new SyncSpecialtiesDataPacket(ItemSpecialtyReloadListener.SPECIALTY_DATA));
        } else {
            BMNetwork.sendToAll(new SyncSpecialtiesDataPacket(ItemSpecialtyReloadListener.SPECIALTY_DATA));
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<ItemStack> event) {
        ItemStack stack = event.getObject();
        if (!stack.isEmpty()) {
            ResourceLocation itemId = ForgeRegistries.ITEMS.getKey(stack.getItem());

            if (ItemSpecialtyReloadListener.hasSpecialtyData(itemId)) {

                ItemSpecialtyCapabilityProvider provider = new ItemSpecialtyCapabilityProvider();
                event.addCapability(Behemoths.prefix("item_specialties"), provider);
                event.addListener(provider::invalidate);

                provider.getCapability(BMCapabilities.ITEM_SPECIALTY).ifPresent(handler -> {

                    List<SpecialtyInstance> data = ItemSpecialtyReloadListener.getSpecialtyData(itemId);
                    if (data.isEmpty()) return;

                    for (SpecialtyInstance instance : data) {
                        handler.addSpecialtyInstance(instance);
                    }
                });
            }
        }
    }
}
