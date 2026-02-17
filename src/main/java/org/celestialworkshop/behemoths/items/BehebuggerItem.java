package org.celestialworkshop.behemoths.items;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.client.guis.screens.ColossangrimScreen;
import org.celestialworkshop.behemoths.entities.Hollowborne;
import org.celestialworkshop.behemoths.entities.HollowborneTurret;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;

public class BehebuggerItem extends Item {

    public BehebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) {
            ColossangrimScreen dict = new ColossangrimScreen();
            Minecraft.getInstance().setScreen(dict);
        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide) {
            Hollowborne borne = BMEntityTypes.HOLLOWBORNE.get().spawn((ServerLevel) pContext.getLevel(), pContext.getClickedPos(), MobSpawnType.NATURAL);
            borne.equipCustomSaddle(null);
            for (int i = 0; i <= 5; i++) {
                HollowborneTurret turret = BMEntityTypes.HOLLOWBORNE_TURRET.get().create(pContext.getLevel());
                pContext.getLevel().addFreshEntity(turret);
                turret.startRiding(borne);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
