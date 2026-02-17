package org.celestialworkshop.behemoths.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;

import java.util.function.Supplier;

public class BMPandemoniumCurses {
    public static final ResourceKey<Registry<PandemoniumCurse>> PANDEMONIUM_CURSES_KEY = ResourceKey.createRegistryKey(Behemoths.prefix("pandemonium_curses"));
    public static final DeferredRegister<PandemoniumCurse> PANDEMONIUM_CURSES = DeferredRegister.create(PANDEMONIUM_CURSES_KEY, Behemoths.MODID);
    public static final Supplier<IForgeRegistry<PandemoniumCurse>> REGISTRY = PANDEMONIUM_CURSES.makeRegistry(() -> new RegistryBuilder<PandemoniumCurse>().disableOverrides());

    public static final RegistryObject<PandemoniumCurse> FRAGILITY = PANDEMONIUM_CURSES.register("fragility", PandemoniumCurse::new);

    public static final RegistryObject<PandemoniumCurse> RELENTLESS = PANDEMONIUM_CURSES.register("relentless", () -> new PandemoniumCurse(EntityType.ZOMBIE));
    public static final RegistryObject<PandemoniumCurse> FERAL_HORDE = PANDEMONIUM_CURSES.register("feral_horde", () -> new PandemoniumCurse(EntityType.ZOMBIE));

    public static final RegistryObject<PandemoniumCurse> ARCHZOMBIE_DOMINION = PANDEMONIUM_CURSES.register("archzombie_dominion", () -> new PandemoniumCurse(BMEntityTypes.ARCHZOMBIE.get()));
    public static final RegistryObject<PandemoniumCurse> GRAVEBREAKER_MOMENTUM = PANDEMONIUM_CURSES.register("gravebreaker_momentum", () -> new PandemoniumCurse(BMEntityTypes.ARCHZOMBIE.get()));
    public static final RegistryObject<PandemoniumCurse> PHANTOM_STEED = PANDEMONIUM_CURSES.register("phantom_steed", () -> new PandemoniumCurse(BMEntityTypes.ARCHZOMBIE.get()));

    public static final RegistryObject<PandemoniumCurse> QUICKDRAW = PANDEMONIUM_CURSES.register("quickdraw", () -> new PandemoniumCurse(EntityType.SKELETON));
    public static final RegistryObject<PandemoniumCurse> HEAVY_ARROW = PANDEMONIUM_CURSES.register("heavy_arrow", () -> new PandemoniumCurse(EntityType.SKELETON));

    public static final RegistryObject<PandemoniumCurse> OVERSEER = PANDEMONIUM_CURSES.register("overseer", () -> new PandemoniumCurse(BMEntityTypes.HOLLOWBORNE_TURRET.get()));
    public static final RegistryObject<PandemoniumCurse> DEADLY_HOLLOWCORPER = PANDEMONIUM_CURSES.register("deadly_hollowcorper", () -> new PandemoniumCurse(BMEntityTypes.HOLLOWBORNE_TURRET.get()));
}
