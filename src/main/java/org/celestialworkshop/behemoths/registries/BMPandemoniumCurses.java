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
    public static final Supplier<IForgeRegistry<PandemoniumCurse>> REGISTRY = PANDEMONIUM_CURSES.makeRegistry(() -> new RegistryBuilder<PandemoniumCurse>().disableSaving().disableOverrides());

    public static final RegistryObject<PandemoniumCurse> PLAYER_DAMAGE_NERF = PANDEMONIUM_CURSES.register("player_damage_nerf", PandemoniumCurse::new);

    public static final RegistryObject<PandemoniumCurse> ZOMBIE_BABY_CHANCE = PANDEMONIUM_CURSES.register("zombie_baby_chance", () -> new PandemoniumCurse(EntityType.ZOMBIE));
    public static final RegistryObject<PandemoniumCurse> ZOMBIE_REVIVAL = PANDEMONIUM_CURSES.register("zombie_revival", () -> new PandemoniumCurse(EntityType.ZOMBIE));

    public static final RegistryObject<PandemoniumCurse> ARCHZOMBIE_LEADER = PANDEMONIUM_CURSES.register("archzombie_leader", () -> new PandemoniumCurse(BMEntityTypes.ARCHZOMBIE.get()));
    public static final RegistryObject<PandemoniumCurse> ARCHZOMBIE_STAMPEDE_CHANCE = PANDEMONIUM_CURSES.register("archzombie_stampede_chance", () -> new PandemoniumCurse(BMEntityTypes.ARCHZOMBIE.get()));
    public static final RegistryObject<PandemoniumCurse> ARCHZOMBIE_SPEED = PANDEMONIUM_CURSES.register("archzombie_speed", () -> new PandemoniumCurse(BMEntityTypes.ARCHZOMBIE.get()));
}
