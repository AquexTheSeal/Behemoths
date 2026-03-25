package org.celestialworkshop.behemoths.items;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.client.animations.item.BreakerItemAnimationExtension;
import org.celestialworkshop.behemoths.entities.misc.AutomatedAttackEntity;
import org.celestialworkshop.behemoths.particles.VFXParticleData;
import org.celestialworkshop.behemoths.particles.VFXTypes;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;

import java.util.function.Consumer;

public class BehebuggerItem extends Item {

    public BehebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        if (!level.isClientSide) {
            AutomatedAttackEntity aae = BMEntityTypes.AUTOMATED_ATTACK.get().create(level);
            Vec3 pos = pContext.getClickLocation();
            aae.moveTo(pos.x, pos.y, pos.z, pContext.getRotation(), 0);
            aae.setBehavior(pContext.getPlayer(), 20, new AutomatedAttackEntity.AttackBehavior() {
                @Override
                public void onStart(AutomatedAttackEntity entity) {
                    VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("phantashroom_jump"))
                            .type(VFXTypes.FLAT).fadeIn()
                            .scale(5, 0, InterpolationTypes.EASE_OUT_QUAD)
                            .lifetime(20);
                    ((ServerLevel) entity.level()).sendParticles(data.build(), entity.getX(), entity.getY() + 0.2, entity.getZ(), 0, 0, 0, 0, 0);
                }

                @Override
                public void onEnd(AutomatedAttackEntity entity) {
                    VFXParticleData.Builder data = new VFXParticleData.Builder().textureName(Behemoths.prefix("charydbis_lightning"))
                            .type(VFXTypes.CHAIN_LOOK)
                            .lifetime(15)
                            .scale(1.5F);
                    ((ServerLevel) entity.level()).sendParticles(data.build(), entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0, 0, 0);
                }
            });
            level.addFreshEntity(aae);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    //    @Override
//    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
//        ItemStack item = pPlayer.getItemInHand(pUsedHand);
//        if (!pPlayer.getCooldowns().isOnCooldown(this)) {
//            pPlayer.startUsingItem(pUsedHand);
//            return InteractionResultHolder.success(item);
//        }
//        return InteractionResultHolder.pass(item);
//    }
//
//    @Override
//    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity, int pTimeCharged) {
//        if (pLivingEntity instanceof Player player) {
////            player.setDeltaMovement(0, -10, 0);
//
//            if (pLevel.isClientSide) {
//                VFXParticleData.Builder particle = new VFXParticleData.Builder().textureName(Behemoths.prefix("charydbis_lightning"))
//                        .type(VFXTypes.CHAIN_LOOK)
//                        .lifetime(15)
//                        .scale(2.0F);
//                pLevel.addParticle(particle.build(), player.getX(), player.getY(), player.getZ(), 0, 0, 0);
//            }
//
//            player.getCooldowns().addCooldown(pStack.getItem(), 2);
//        }
//    }

//    @Override
//    public int getUseDuration(ItemStack pStack) {
//        return 72000;
//    }
//
//    @Override
//    public UseAnim getUseAnimation(ItemStack pStack) {
//        return UseAnim.CUSTOM;
//    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new BreakerItemAnimationExtension());
    }

}
