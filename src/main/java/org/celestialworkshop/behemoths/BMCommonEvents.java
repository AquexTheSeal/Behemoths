package org.celestialworkshop.behemoths;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.api.entity.heartenergy.HeartEnergyReloadListener;
import org.celestialworkshop.behemoths.api.item.specialty.ItemSpecialtyCapabilityProvider;
import org.celestialworkshop.behemoths.datagen.reloadlisteners.ItemSpecialtyReloadListener;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;
import org.celestialworkshop.behemoths.commands.PandemoniumCommand;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.SyncSpecialtiesDataPacket;
import org.celestialworkshop.behemoths.registries.BMCapabilities;
import org.celestialworkshop.behemoths.registries.BMTags;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;
import org.celestialworkshop.behemoths.world.savedata.WorldPandemoniumData;

import java.util.Collection;
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
        LivingEntity killer = entity.getKillCredit();

        if (!entity.level().isClientSide) {
            if (killer instanceof Player player) {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty() && stack.getItem() instanceof BehemothHeartItem) {
                        ResourceLocation entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
                        int energyGiven = HeartEnergyReloadListener.VALUES.getOrDefault(entityId, 0);
                        BehemothHeartItem.addHeartEnergy(stack, energyGiven);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityDropItem(LivingDropsEvent event) {

        if (event.getEntity() instanceof Player) return;

        Collection<ItemEntity> drops = event.getDrops();
        for (ItemEntity drop : drops) {
            ItemStack stack = drop.getItem();
            if (!stack.isEmpty() && stack.getItem() instanceof BehemothHeartItem heart) {
                int maxEnergy = heart.getMaxHeartEnergy();
                int givenEnergy = event.getEntity().getRandom().nextIntBetweenInclusive(maxEnergy - (int) (maxEnergy * 0.5F), maxEnergy);
                BehemothHeartItem.setHeartEnergy(stack, givenEnergy);
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
        event.addListener(new HeartEnergyReloadListener());
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
