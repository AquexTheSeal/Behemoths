package org.celestialworkshop.behemoths.mixin;

import net.minecraft.world.entity.projectile.AbstractArrow;
import org.celestialworkshop.behemoths.misc.mixinhelpers.IMixinArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractArrow.class)
public class AbstractArrowMixin implements IMixinArrow {

    @Unique public boolean behemoths$isBoostedArrow;

    @Override
    public boolean behemoths$isBoostedArrow() {
        return behemoths$isBoostedArrow;
    }

    @Override
    public void behemoths$setBoostedArrow(boolean val) {
        behemoths$isBoostedArrow = val;
    }
}
