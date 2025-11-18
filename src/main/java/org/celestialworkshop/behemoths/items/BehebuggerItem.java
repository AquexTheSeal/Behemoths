package org.celestialworkshop.behemoths.items;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.Vec3;
import org.celestialworkshop.behemoths.entities.Archzombie;
import org.celestialworkshop.behemoths.entities.BanishingStampede;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMItems;

public class BehebuggerItem extends Item {

    public BehebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getPlayer().isCrouching()) {
            Archzombie archzombie = new Archzombie(BMEntityTypes.ARCHZOMBIE.get(), pContext.getLevel());
            archzombie.setPos(Vec3.atCenterOf(pContext.getClickedPos().above()));
            archzombie.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(BMItems.ROTTEN_OATHKEEPER.get()));
            pContext.getLevel().addFreshEntity(archzombie);
        }

        BanishingStampede stampede = new BanishingStampede(BMEntityTypes.BANISHING_STAMPEDE.get(), pContext.getLevel());
        stampede.setPos(Vec3.atCenterOf(pContext.getClickedPos().above()));
        stampede.setOwnerUUID(pContext.getPlayer().getUUID());
        stampede.setTamed(true);
        stampede.equipSaddle(SoundSource.AMBIENT);
        pContext.getLevel().addFreshEntity(stampede);

        return super.useOn(pContext);
    }
}
