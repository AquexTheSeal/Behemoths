package org.celestialworkshop.behemoths.registries;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.mobeffect.BMMobEffect;

public class BMMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Behemoths.MODID);

    public static final RegistryObject<MobEffect> SOFTFOOTED = MOB_EFFECTS.register("softfooted", () -> new BMMobEffect(MobEffectCategory.BENEFICIAL, 0x1aff00));
}
