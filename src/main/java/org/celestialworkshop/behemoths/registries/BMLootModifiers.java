package org.celestialworkshop.behemoths.registries;

import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.loot.CurseScalableLootModifier;

public class BMLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Behemoths.MODID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> CURSE_SCALABLE_LOOT_MODIFIER =
            LOOT_MODIFIERS.register("curse_scalable_loot_modifier", () -> CurseScalableLootModifier.CODEC);
}
