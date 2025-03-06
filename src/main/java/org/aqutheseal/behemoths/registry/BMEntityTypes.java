package org.aqutheseal.behemoths.registry;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.entity.SkyCharydbis;
import org.aqutheseal.behemoths.entity.misc.ShockwaveEntity;
import org.aqutheseal.behemoths.entity.variants.SkyCharydbisVariants;

public class BMEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Behemoths.MODID);

    public static final RegistryObject<EntityType<SkyCharydbis>> BARREN_SKY_CHARYDBIS = ENTITIES.register("barren_sky_charydbis",
            () -> EntityType.Builder.<SkyCharydbis>of((a, b) -> new SkyCharydbis(a, b, SkyCharydbisVariants.BARREN), MobCategory.MONSTER).sized(6.0f, 5.0f).fireImmune().clientTrackingRange(128).build(Behemoths.location("sky_charydbis").toString())
    );

    public static final RegistryObject<EntityType<SkyCharydbis>> LUSH_SKY_CHARYDBIS = ENTITIES.register("lush_sky_charydbis",
            () -> EntityType.Builder.<SkyCharydbis>of((a, b) -> new SkyCharydbis(a, b, SkyCharydbisVariants.LUSH), MobCategory.MONSTER).sized(6.0f, 5.0f).fireImmune().clientTrackingRange(128).build(Behemoths.location("lush_sky_charydbis").toString())
    );

    public static final RegistryObject<EntityType<SkyCharydbis>> NORTHERN_SKY_CHARYDBIS = ENTITIES.register("northern_sky_charydbis",
            () -> EntityType.Builder.<SkyCharydbis>of((a, b) -> new SkyCharydbis(a, b, SkyCharydbisVariants.NORTHERN), MobCategory.MONSTER).sized(6.0f, 5.0f).fireImmune().clientTrackingRange(128).build(Behemoths.location("northern_sky_charydbis").toString())
    );

    public static final RegistryObject<EntityType<SkyCharydbis>> NETHER_SKY_CHARYDBIS = ENTITIES.register("nether_sky_charydbis",
            () -> EntityType.Builder.<SkyCharydbis>of((a, b) -> new SkyCharydbis(a, b, SkyCharydbisVariants.NETHER), MobCategory.MONSTER).sized(6.0f, 5.0f).fireImmune().clientTrackingRange(128).build(Behemoths.location("nether_sky_charydbis").toString())
    );

    public static final RegistryObject<EntityType<SkyCharydbis>> SOUL_SKY_CHARYDBIS = ENTITIES.register("soul_sky_charydbis",
            () -> EntityType.Builder.<SkyCharydbis>of((a, b) -> new SkyCharydbis(a, b, SkyCharydbisVariants.SOUL), MobCategory.MONSTER).sized(6.0f, 5.0f).fireImmune().clientTrackingRange(128).build(Behemoths.location("soul_sky_charydbis").toString())
    );

    public static final RegistryObject<EntityType<SkyCharydbis>> VOID_SKY_CHARYDBIS = ENTITIES.register("void_sky_charydbis",
            () -> EntityType.Builder.<SkyCharydbis>of((a, b) -> new SkyCharydbis(a, b, SkyCharydbisVariants.VOID), MobCategory.MONSTER).sized(6.0f, 5.0f).fireImmune().clientTrackingRange(128).build(Behemoths.location("void_sky_charydbis").toString())
    );

    public static final RegistryObject<EntityType<ShockwaveEntity>> SHOCKWAVE = registerLifelessEntity("shockwave", ShockwaveEntity::new, 4.0F, 4.0F);

    public static <T extends Entity> RegistryObject<EntityType<T>> registerLifelessEntity(String name, EntityType.EntityFactory<T> factory, float hitboxWidth, float hitboxHeight) {
        return ENTITIES.register(name, () -> EntityType.Builder.of(factory, MobCategory.MISC).sized(hitboxWidth, hitboxHeight).clientTrackingRange(64).noSave().build(Behemoths.location(name).toString()));
    }
}
