package org.celestialworkshop.behemoths.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMBlocks;
import org.celestialworkshop.behemoths.registries.BMMobEffects;
import org.celestialworkshop.behemoths.registries.BMSoundEvents;

public class PhantashroomBlock extends BushBlock implements BonemealableBlock {
    protected static final VoxelShape SHAPE = Shapes.or(
            Block.box(3.0D, 0.0D, 3.0D, 13.0D, 2.0D, 13.0D),
            Block.box(1.0D, 2.0D, 1.0D, 15.0D, 12.0D, 15.0D)
    );

    public PhantashroomBlock(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(Blocks.MUD);
    }

    @Override
    public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction facing, IPlantable plantable) {
        return state.is(Blocks.MUD);
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        bounceEntity(pEntity);
    }

    @Override
    public float getMaxHorizontalOffset() {
        return 0.15F;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Vec3 vec3 = pState.getOffset(pLevel, pPos);
        return SHAPE.move(vec3.x, vec3.y, vec3.z);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader pLevel, BlockPos pPos, BlockState pState, boolean pIsClient) {
        return pLevel.getBlockState(pPos.below()).is(Blocks.MUD);
    }

    @Override
    public boolean isBonemealSuccess(Level pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        return pRandom.nextFloat() < 0.4D;
    }

    @Override
    public void performBonemeal(ServerLevel pLevel, RandomSource pRandom, BlockPos pPos, BlockState pState) {
        generateHugeMushroom(pLevel, pRandom, pPos);
    }

    public static void bounceEntity(Entity entity) {
        if (!(entity instanceof Mob) && !(entity instanceof Player)) return;

        Vec3 base = entity.getDeltaMovement();
        entity.setDeltaMovement(base.x * 3F, 1.5F, base.z * 3F);
        entity.resetFallDistance();
        entity.setOnGround(false);
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(BMMobEffects.SOFTFOOTED.get(), 200, 0));
        }
        RandomSource random = entity.level().random;
        if (!entity.level().isClientSide) {
            float scale = 1.5F + random.nextFloat() * 0.5F;
            VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("phantashroom_jump"))
                    .type(VFXTypes.FLAT).fadeOut()
                    .scale(0, scale, InterpolationTypes.EASE_OUT_QUAD)
                    .lifetime(20 + random.nextInt(10));
            ((ServerLevel) entity.level()).sendParticles(data.build(), entity.getX(), entity.getY() + 0.2, entity.getZ(), 0, 0, 0, 0, 0);
        } else {
            for (int i = 0; i < 15; i++) {
                double xx = entity.getX() + random.nextGaussian() * 0.2;
                double yy = entity.getY() + random.nextGaussian() * 0.2;
                double zz = entity.getZ() + random.nextGaussian() * 0.2;
                double vx = random.nextGaussian() * 0.2;
                double vy = random.nextGaussian() * 0.2;
                double vz = random.nextGaussian() * 0.2;
                VFXParticleData.Builder particleData = new VFXParticleData.Builder().textureName(Behemoths.prefix("phantashroom_jump_orb"))
                        .type(VFXTypes.FLAT_LOOK).fadeOut().scale(0.2F + random.nextFloat() * 0.2F)
                        .lifetime(20 + random.nextInt(10));
                entity.level().addParticle(particleData.build(), xx, yy, zz, vx, vy, vz);
            }
        }

        entity.playSound(BMSoundEvents.PHANTASHROOM_BOUNCE.get(), 1.0F, 1.5F + entity.level().random.nextFloat() * 0.2F);
    }

    public static void generateHugeMushroom(WorldGenLevel level, RandomSource random, BlockPos pos) {
        int stemHeight = 2 + random.nextInt(6);
        for (int i = 0; i < stemHeight; i++) {
            BlockPos offsetPos = pos.above(i);
            if (level.getBlockState(offsetPos).isAir() || level.getBlockState(offsetPos).is(BMBlocks.PHANTASHROOM.get())) {
                level.setBlock(offsetPos, Blocks.MUSHROOM_STEM.defaultBlockState(), 2);
            }
        }
        level.setBlock(pos.above(stemHeight), BMBlocks.PHANTASHROOM_BLOCK.get().defaultBlockState(), 2);
        for (Direction dir : Direction.Plane.HORIZONTAL.stream().toList()) {
            BlockPos offsetPos = pos.above(stemHeight).relative(dir);
            if (level.getBlockState(offsetPos).isAir()) {
                level.setBlock(offsetPos, BMBlocks.PHANTASHROOM_BLOCK.get().defaultBlockState(), 2);
            }
        }
        int radius = 2 + random.nextInt(2);
        for (int xx = -radius; xx <= radius; xx++) {
            for (int zz = -radius; zz <= radius; zz++) {
                if (Math.abs(xx) == radius && Math.abs(zz) == radius) continue;
                BlockPos offsetPos = pos.above(stemHeight + 1).offset(xx, 0, zz);

                if (Math.abs(xx) == radius || Math.abs(zz) == radius) {
                    offsetPos = offsetPos.below();
                }

                if (level.getBlockState(offsetPos).isAir()) {
                    level.setBlock(offsetPos, BMBlocks.PHANTASHROOM_BLOCK.get().defaultBlockState(), 2);
                }
            }
        }
    }
}
