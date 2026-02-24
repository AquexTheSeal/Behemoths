package org.celestialworkshop.behemoths;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AdvancementEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.celestialworkshop.behemoths.api.entity.BehemothProperties;
import org.celestialworkshop.behemoths.api.item.specialty.ItemSpecialtyCapabilityProvider;
import org.celestialworkshop.behemoths.api.item.specialty.SpecialtyInstance;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumVotingSystem;
import org.celestialworkshop.behemoths.commands.PandemoniumCommand;
import org.celestialworkshop.behemoths.config.BMConfigManager;
import org.celestialworkshop.behemoths.datagen.reloadlisteners.BehemothPropertiesReloadListener;
import org.celestialworkshop.behemoths.datagen.reloadlisteners.ItemSpecialtyReloadListener;
import org.celestialworkshop.behemoths.entities.ai.mount.CustomJumpingMount;
import org.celestialworkshop.behemoths.items.BehemothHeartItem;
import org.celestialworkshop.behemoths.misc.mixinhelpers.IMixinArrow;
import org.celestialworkshop.behemoths.network.BMNetwork;
import org.celestialworkshop.behemoths.network.s2c.OpenPandemoniumSelectionPacket;
import org.celestialworkshop.behemoths.network.s2c.SyncSpecialtiesDataPacket;
import org.celestialworkshop.behemoths.registries.BMAdvancementTriggers;
import org.celestialworkshop.behemoths.registries.BMCapabilities;
import org.celestialworkshop.behemoths.registries.BMMobEffects;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
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
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof CustomJumpingMount<?> mount) {
            mount.getMountJumpManager().entityTick();
        }

        if (entity.hasEffect(BMMobEffects.SOFTFOOTED.get()) && entity.onGround()) {
            entity.resetFallDistance();
            entity.removeEffect(BMMobEffects.SOFTFOOTED.get());
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(BMMobEffects.SOFTFOOTED.get())) {
                livingEntity.removeEffect(BMMobEffects.SOFTFOOTED.get());
                event.cancel();
            }
        }
    }

    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (event.getProjectile() instanceof AbstractArrow arrow) {
            if (arrow.getOwner() instanceof Skeleton) {
                if (event.getRayTraceResult() instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity target) {
                    if (IMixinArrow.isBoosted(arrow)) {
                        target.hasImpulse = true;
                        target.knockback(arrow.getDeltaMovement().length(), arrow.getX() - target.getX(), arrow.getZ() - target.getZ());
                        arrow.playSound(SoundEvents.WITHER_BREAK_BLOCK, 1.0F, 1.0F);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        PandemoniumVotingSystem.resolveInterruptedVote(event.getServer().getLevel(Level.OVERWORLD));
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().level().isClientSide) {
            WorldPandemoniumData data = WorldPandemoniumData.get((ServerLevel) event.getEntity().level());

            if (data.isVotingActive()) {
                BMNetwork.sendToPlayer((ServerPlayer) event.getEntity(), new OpenPandemoniumSelectionPacket(
                        data.getSelectableCurses().stream().map(PandemoniumCurse::getId).toList(), data.getRemainingTime())
                );
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

                if (BehemothProperties.isBehemoth(entity)) {
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (!stack.isEmpty() && stack.getItem() instanceof BehemothHeartItem) {
                            BehemothHeartItem.addHeartEnergy(stack, BehemothProperties.getHeartEnergy(entity));
                        }
                    }

                    BMAdvancementTriggers.KILL_BEHEMOTH.trigger(player);
                }

                WorldPandemoniumData data = WorldPandemoniumData.get(player.serverLevel());
                List<String> onceList = BMConfigManager.COMMON.pandemoniumOnceEntities.get().stream().map(s -> (String) s).toList();;
                List<String> repeatList = BMConfigManager.COMMON.pandemoniumRepeatEntities.get().stream().map(s -> (String) s).toList();;
                String idString = entityId.toString();

                if (repeatList.contains(idString)) {
                    PandemoniumVotingSystem.openPandemoniumSelection(player.level());
                } else if (onceList.contains(idString)) {
                    if (!data.hasEntityTriggeredVoting(entityId)) {
                        data.markEntityTriggeredVoting(entityId);
                        PandemoniumVotingSystem.openPandemoniumSelection(player.level());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerAdvancement(AdvancementEvent.AdvancementEarnEvent event) {
        if (!event.getEntity().level().isClientSide) {
            List<String> onceList = BMConfigManager.COMMON.pandemoniumOnceAdvancements.get().stream().map(s -> (String) s).toList();
            List<String> repeatList = BMConfigManager.COMMON.pandemoniumRepeatAdvancements.get().stream().map(s -> (String) s).toList();

            ResourceLocation id = event.getAdvancement().getId();

            if (repeatList.contains(id.toString())) {
                PandemoniumVotingSystem.openPandemoniumSelection(event.getEntity().level());

            } else if (onceList.contains(id.toString())) {
                WorldPandemoniumData data = WorldPandemoniumData.get((ServerLevel) event.getEntity().level());
                if (!data.hasAdvancementTriggeredVoting(id)) {
                    data.markAdvancementTriggeredVoting(id);
                    PandemoniumVotingSystem.openPandemoniumSelection(event.getEntity().level());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEntityFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        Mob entity = event.getEntity();
        ServerLevel level = event.getLevel().getLevel();
        if (entity instanceof Zombie) {
            if (PandemoniumVotingSystem.hasPandemoniumCurse(level, BMPandemoniumCurses.RELENTLESS)) {
                entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.5);
                entity.getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(entity.getAttributeValue(Attributes.FOLLOW_RANGE) * 2);
            }
        }
        if (entity instanceof Skeleton) {
            if (PandemoniumVotingSystem.hasPandemoniumCurse(level, BMPandemoniumCurses.QUICKDRAW)) {
                entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(entity.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.5);
            }
        }
    }

    @SubscribeEvent
    public static void onEntityHurt(LivingHurtEvent event) {
        Level level = event.getEntity().level();
        if (!level.isClientSide) {
            Entity attacker = event.getSource().getEntity();

            if (attacker != null) {
                if (attacker instanceof Zombie) {
                    if (PandemoniumVotingSystem.hasPandemoniumCurse(level, BMPandemoniumCurses.FERAL_HORDE)) {
                        List<Mob> nearUndead = level.getEntitiesOfClass(Mob.class, attacker.getBoundingBox().inflate(8)).stream().filter(e -> e.getMobType() == MobType.UNDEAD).toList();
                        event.setAmount(event.getAmount() * (1 + (nearUndead.size() * 0.2F)));
                    }
                }
            }
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
