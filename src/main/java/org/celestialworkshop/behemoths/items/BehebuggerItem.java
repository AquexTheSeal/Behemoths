package org.celestialworkshop.behemoths.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.celestialworkshop.behemoths.misc.utils.ClientUtils;

public class BehebuggerItem extends Item {

    public BehebuggerItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (pLevel.isClientSide) {
            ClientUtils.openColossangrimScreen();
        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (!pContext.getLevel().isClientSide) {
            Level level = pContext.getLevel();
            int ww = 3;
            int hh = (level.getMaxBuildHeight() - 5) - pContext.getClickedPos().getY();
            for (int x = -ww; x <= ww; x++) {
                for (int z = -ww; z <= ww; z++) {
                    for (int y = -hh; y <= hh; y++) {
                        BlockPos pos = pContext.getClickedPos().offset(x, y, z);
                        level.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), 3);
                    }
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
