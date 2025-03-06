package org.aqutheseal.behemoths.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.worldgen.placement.SkyGroupedPlacement;

public class BMPlacementModifierTypes {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, Behemoths.MODID);

    public static final RegistryObject<PlacementModifierType<SkyGroupedPlacement>> SKY_GROUPED_PLACEMENT = PLACEMENT_MODIFIERS.register(
            "sky_grouped_placement", () -> () -> SkyGroupedPlacement.CODEC
    );
}