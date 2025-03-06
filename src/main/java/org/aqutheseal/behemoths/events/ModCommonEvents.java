package org.aqutheseal.behemoths.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.capability.FreezingCapability;
import org.aqutheseal.behemoths.entity.variants.SkyCharydbisVariants;
import org.aqutheseal.behemoths.item.ISkyBeastTool;
import org.aqutheseal.behemoths.item.SkyBeastArmorItem;
import org.aqutheseal.behemoths.util.compat.BetterCombatUtil;

@Mod.EventBusSubscriber(modid = Behemoths.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommonEvents {

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event) {
        debugTick(event);
    }

    private static void debugTick(LivingEvent.LivingTickEvent event) {
        event.getEntity().getCapability(FreezingCapability.CAPABILITY).ifPresent(FreezingCapability::tick);
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        for (ItemStack armor : event.getEntity().getArmorSlots()) {
            if (armor.getItem() instanceof SkyBeastArmorItem skyBeastArmor) {
                event.setAmount(skyBeastArmor.getFinalReceivedDamage(event.getEntity(), event.getSource(), event.getAmount()));
            }
        }
    }

    @SubscribeEvent
    public static void onCriticalHit(CriticalHitEvent event) {
        if (event.isVanillaCritical()) {
            if (BetterCombatUtil.getSwingingStack(event.getEntity()).getItem() instanceof ISkyBeastTool tool) {
                if (tool.getVariant() == SkyCharydbisVariants.LUSH) {
                    event.setDamageModifier(event.getDamageModifier() * 1.15F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(Behemoths.location("freezing"), new FreezingCapability.Provider(event.getObject()));
        }
    }
}
