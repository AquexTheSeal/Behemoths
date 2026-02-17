package org.celestialworkshop.behemoths.entities.ai;

import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public interface CustomSaddleable extends Saddleable {

    boolean canEquipSaddle(ItemStack stack);

    void equipCustomSaddle(@Nullable SoundSource pSource);

    /**
     * Keep blank so that vanilla saddles won't just equip.
     */
    @Override
    default void equipSaddle(@Nullable SoundSource pSource) {
    }
}
