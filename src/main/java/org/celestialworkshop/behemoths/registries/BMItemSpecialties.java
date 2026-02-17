package org.celestialworkshop.behemoths.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.item.specialty.ItemSpecialty;
import org.celestialworkshop.behemoths.items.specialties.BehemothDamageBonusSpecialty;
import org.celestialworkshop.behemoths.items.specialties.UndeadDamageBonusSpecialty;

import java.util.function.Supplier;

public class BMItemSpecialties {
    public static final ResourceKey<Registry<ItemSpecialty>> ITEM_SPECIALTY_KEY = ResourceKey.createRegistryKey(Behemoths.prefix("item_specialties"));
    public static final DeferredRegister<ItemSpecialty> ITEM_SPECIALTIES = DeferredRegister.create(ITEM_SPECIALTY_KEY, Behemoths.MODID);
    public static final Supplier<IForgeRegistry<ItemSpecialty>> REGISTRY = ITEM_SPECIALTIES.makeRegistry(() -> new RegistryBuilder<ItemSpecialty>().disableSaving().disableOverrides().setDefaultKey(Behemoths.prefix("behemoth_damage_bonus")));

    public static final RegistryObject<ItemSpecialty> BEHEMOTH_DAMAGE_BONUS = ITEM_SPECIALTIES.register("behemoth_damage_bonus", BehemothDamageBonusSpecialty::new);
    public static final RegistryObject<ItemSpecialty> UNDEAD_DAMAGE_BONUS = ITEM_SPECIALTIES.register("undead_damage_bonus", UndeadDamageBonusSpecialty::new);

}
