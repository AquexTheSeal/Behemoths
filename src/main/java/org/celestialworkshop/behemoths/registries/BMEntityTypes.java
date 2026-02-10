package org.celestialworkshop.behemoths.registries;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.entities.Hollowborne;
import org.celestialworkshop.behemoths.entities.HollowborneTurret;
import org.celestialworkshop.behemoths.entities.projectile.Hollowcorper;

public class BMEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Behemoths.MODID);

    public static final RegistryObject<EntityType<Archzombie>> ARCHZOMBIE = ENTITY_TYPES.register("archzombie", () -> EntityType.Builder.of(
            Archzombie::new, MobCategory.MONSTER).sized(1.0F, 2.5F).build("archzombie")
    );

    public static final RegistryObject<EntityType<BanishingStampede>> BANISHING_STAMPEDE = ENTITY_TYPES.register("banishing_stampede", () -> EntityType.Builder.of(
            BanishingStampede::new, MobCategory.MONSTER).sized(1.4F, 1.6F).build("banishing_stampede")
    );

    public static final RegistryObject<EntityType<Hollowborne>> HOLLOWBORNE = ENTITY_TYPES.register("hollowborne", () -> EntityType.Builder.of(
            Hollowborne::new, MobCategory.MONSTER).sized(3.0F, 4.0F).build("hollowborne")
    );

    public static final RegistryObject<EntityType<HollowborneTurret>> HOLLOWBORNE_TURRET = ENTITY_TYPES.register("hollowborne_turret", () -> EntityType.Builder.of(
            HollowborneTurret::new, MobCategory.MONSTER).sized(2.0F, 3.0F).build("hollowborne_turret")
    );

    // PROJECTILE

    public static final RegistryObject<EntityType<Hollowcorper>> HOLLOWCORPER = ENTITY_TYPES.register("hollowcorper", () -> EntityType.Builder.<Hollowcorper>of(
            Hollowcorper::new, MobCategory.MISC).sized(1F, 1F).build("hollowcorper")
    );
}
