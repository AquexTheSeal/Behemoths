package org.celestialworkshop.behemoths.client.animations.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;

public class BreakerItemAnimationExtension extends KeyframeClientItemExtension {

    @Override
    public boolean animateHand(HandStatus status, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick) {
        if (player.isUsingItem() && player.getUseItem() == itemInHand) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            float tick = player.getTicksUsingItem() + partialTick;

            status.xRot = getValueInWindow(tick, 0, 9, status.xRot, -90, InterpolationTypes.EASE_IN_OUT_CUBIC);
            status.xRot = getValueInWindow(tick, 10, 30, status.xRot, 45, InterpolationTypes.EASE_OUT_BACK);
            status.xRot = getValueInWindow(tick, 30, 200, status.xRot, 90, InterpolationTypes.LINEAR);
            status.xRot += Mth.sin((tick * 0.2F)) * 5F;

            status.x = getValueInWindow(tick, 0, 9, status.x, -0.2F * (float)i, InterpolationTypes.EASE_IN_OUT_CUBIC);

            status.y = getValueInWindow(tick, 0, 9, status.y, -0.2F, InterpolationTypes.EASE_IN_OUT_CUBIC);
            status.y = getValueInWindow(tick, 10, 25, status.y, 0.6F, InterpolationTypes.EASE_OUT_BACK);
            status.y = getValueInWindow(tick, 25, 200, status.y, 1.3F, InterpolationTypes.LINEAR);

            status.z = getValueInWindow(tick, 0, 15, 0, -0.3F, InterpolationTypes.EASE_OUT_CUBIC);

            return true;
        }

        return false;
    }

    @Override
    public void animateModel(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm, ItemStack itemInHand, float partialTick) {
        if (entity.isUsingItem() && entity.getUseItem() == itemInHand) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            float tick = entity.getTicksUsingItem() + partialTick;
            PartStatus status = new PartStatus();

            ModelPart mainArm = arm == HumanoidArm.RIGHT ? model.rightArm : model.leftArm;
            status.load(mainArm);
            status.xRot = getValueInWindow(tick, 0, 9, status.xRot, 45, InterpolationTypes.EASE_IN_OUT_CUBIC);
            status.xRot = getValueInWindow(tick, 10, 30, status.xRot, -145, InterpolationTypes.EASE_OUT_BACK);
            status.xRot = getValueInWindow(tick, 30, 200, status.xRot, -180, InterpolationTypes.LINEAR);

            status.zRot = getValueInWindow(tick, 10, 30, status.zRot, 22.5F * -i, InterpolationTypes.EASE_IN_OUT_CUBIC);
            status.zRot = getValueInWindow(tick, 10, 30, status.zRot, 22.5F * i, InterpolationTypes.EASE_OUT_BACK);
            status.save(mainArm);

            ModelPart oppositeArm = arm == HumanoidArm.RIGHT ? model.leftArm : model.rightArm;
            status.load(oppositeArm);
            status.xRot = getValueInWindow(tick, 0, 9, status.xRot, 45, InterpolationTypes.EASE_IN_OUT_CUBIC);
            status.xRot = getValueInWindow(tick, 10, 30, status.xRot, -145, InterpolationTypes.EASE_OUT_BACK);
            status.xRot = getValueInWindow(tick, 30, 200, status.xRot, -180, InterpolationTypes.LINEAR);

            status.zRot = getValueInWindow(tick, 10, 30, status.zRot, 22.5F * i, InterpolationTypes.EASE_OUT_BACK);
            status.zRot = getValueInWindow(tick, 10, 30, status.zRot, 22.5F * -i, InterpolationTypes.EASE_OUT_BACK);
            status.save(oppositeArm);
        }
    }
}
