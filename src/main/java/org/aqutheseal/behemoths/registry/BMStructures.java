package org.aqutheseal.behemoths.registry;

import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.worldgen.structure.CharydbisIslesPiece;
import org.aqutheseal.behemoths.worldgen.structure.CharydbisIslesStructure;

public class BMStructures {
    public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPE = DeferredRegister.create(Registries.STRUCTURE_TYPE, Behemoths.MODID);
    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE = DeferredRegister.create(Registries.STRUCTURE_PIECE, Behemoths.MODID);

    public static final RegistryObject<StructureType<CharydbisIslesStructure>> CHARYDBIS_ISLES_TYPE = STRUCTURE_TYPE.register("charydbis_isles_structure", () -> () -> CharydbisIslesStructure.CODEC);

    public static final RegistryObject<StructurePieceType> CHARYDBIS_ISLES_PIECE = STRUCTURE_PIECE.register("charydbis_isles_piece", () -> (StructurePieceType.ContextlessType) CharydbisIslesPiece::new);

    public static final ResourceKey<Structure> CHARYDBIS_ISLES = ResourceKey.create(Registries.STRUCTURE, Behemoths.location("charydbis_isles"));

    public static final ResourceKey<StructureSet> CHARYDBIS_ISLES_SET = ResourceKey.create(Registries.STRUCTURE_SET, Behemoths.location("charydbis_isles"));

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }
}
