package org.aqutheseal.behemoths.entity.projectile;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.aqutheseal.behemoths.registry.BMEntityTypes;

public class CharydbisOrb extends Projectile {
    public CharydbisOrb(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public CharydbisOrb(Level pLevel, LivingEntity pShooter) {
        this(BMEntityTypes.CHARYDBIS_ORB.get(), pLevel);
        this.setOwner(pShooter);
        BlockPos blockpos = pShooter.blockPosition();
        double d0 = (double)blockpos.getX() + 0.5D;
        double d1 = (double)blockpos.getY() + 0.5D;
        double d2 = (double)blockpos.getZ() + 0.5D;
        this.moveTo(d0, d1, d2, this.getYRot(), this.getXRot());
    }

    @Override
    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();
            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult)) {
                this.onHit(hitresult);
            }
            this.checkInsideBlocks();
            Vec3 delta = this.getDeltaMovement();
            double xx = this.getX() + delta.x;
            double yy = this.getY() + delta.y;
            double zz = this.getZ() + delta.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            this.setDeltaMovement(delta.scale(0.95));
            this.setPos(xx, yy, zz);
        } else {
            this.discard();
        }

        this.setDeltaMovement(0, -0.5, 0);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        level().explode(this, this.getX(), this.getY(), this.getZ(), 5.0F, Level.ExplosionInteraction.BLOCK);
        this.discard();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
