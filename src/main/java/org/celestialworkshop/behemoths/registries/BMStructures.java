package org.celestialworkshop.behemoths.registries;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.world.structures.CharydbisZoneIslandPiece;
import org.celestialworkshop.behemoths.world.structures.CharydbisZoneStructure;

public class BMStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Behemoths.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, Behemoths.MODID);

    public static final RegistryObject<StructureType<CharydbisZoneStructure>> CHARYDBIS_ZONE_TYPE = STRUCTURE_TYPES.register("charydbis_zone", () -> CharydbisZoneStructure.CODEC::codec);
    public static final RegistryObject<StructurePieceType> CHARYDBIS_ZONE_CENTER_PIECE = STRUCTURE_PIECES.register("charydbis_zone_center_piece", () -> (StructurePieceType.ContextlessType) CharydbisZoneIslandPiece::new);

    public static final ResourceKey<Structure> CHARYDBIS_ZONE = ResourceKey.create(Registries.STRUCTURE, Behemoths.prefix("charydbis_zone"));
    public static final ResourceKey<StructureSet> CHARYDBIS_ZONE_SET = ResourceKey.create(Registries.STRUCTURE_SET, Behemoths.prefix("charydbis_zone"));
}
