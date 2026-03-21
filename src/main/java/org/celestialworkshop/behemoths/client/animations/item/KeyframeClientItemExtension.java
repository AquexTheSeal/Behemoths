package org.celestialworkshop.behemoths.client.animations.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;

public abstract class KeyframeClientItemExtension implements IClientItemExtensions {

    public abstract boolean animateHand(HandStatus status, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick);

    public abstract void animateModel(HumanoidModel<?> model, LivingEntity entity, HumanoidArm arm, ItemStack itemInHand, float partialTick);

    // todo: Make a custom transformer because some parts like the arms y position cannot be modified.
    public static final HumanoidModel.ArmPose CUSTOM_MODEL_ANIM = HumanoidModel.ArmPose.create("KeyframeClientItemArmPose", false, (model, entity, arm) -> {
        ItemStack stack = entity.getItemInHand(arm == HumanoidArm.RIGHT ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);
        if (IClientItemExtensions.of(stack) instanceof KeyframeClientItemExtension ext) {
            model.attackTime = 0.0F;
            ext.animateModel(model, entity, arm, stack, Minecraft.getInstance().getPartialTick());
        }
    });

    @Override
    public HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand, ItemStack stack) {
        return CUSTOM_MODEL_ANIM;
    }

    @Override
    public boolean applyForgeHandTransform(PoseStack poseStack, LocalPlayer player, HumanoidArm arm, ItemStack itemInHand, float partialTick, float equipProcess, float swingProcess) {
        HandStatus status = new HandStatus();
        if (animateHand(status, player, arm, itemInHand, partialTick)) {
            int i = arm == HumanoidArm.RIGHT ? 1 : -1;
            // Default Item Transform
            poseStack.translate((float)i * 0.56F, -0.52F, -0.72F);
            poseStack.pushPose();
            status.apply(poseStack);
            return true;
        }
        return false;
    }

    public float getValueInWindow(float currentTick, float startTick, float endTick, float startVal, float endVal, InterpolationTypes type) {
        if (currentTick <= startTick) return startVal;
        if (currentTick >= endTick) return endVal;

        float duration = endTick - startTick;
        float localProgress = (currentTick - startTick) / duration;

        float easedProgress = InterpolationTypes.apply(type, localProgress);
        return Mth.lerp(easedProgress, startVal, endVal);
    }

    public float getValueInWindow(float currentTick, float startTick, float endTick, float startVal, float endVal) {
        return getValueInWindow(currentTick, startTick, endTick, startVal, endVal, InterpolationTypes.LINEAR);
    }

    public static class HandStatus {
        public float x = 0, y = 0, z = 0;
        public float xRot = 0, yRot = 0, zRot = 0;

        public void apply(PoseStack poseStack) {
            poseStack.translate(x, y, z);
            poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot));
            poseStack.mulPose(Axis.ZP.rotationDegrees(zRot));
        }
    }

    public static class PartStatus {
        public float x = 0, y = 0, z = 0;
        public float xRot = 0, yRot = 0, zRot = 0;

        public void load(ModelPart part) {
            this.x = part.x;
            this.y = part.y;
            this.z = part.z;
            this.xRot = part.xRot * Mth.RAD_TO_DEG;
            this.yRot = part.yRot * Mth.RAD_TO_DEG;
            this.zRot = part.zRot * Mth.RAD_TO_DEG;
        }

        public void save(ModelPart part) {
            part.x = this.x;
            part.y = this.y;
            part.z = this.z;
            part.xRot = this.xRot * Mth.DEG_TO_RAD;
            part.yRot = this.yRot * Mth.DEG_TO_RAD;
            part.zRot = this.zRot * Mth.DEG_TO_RAD;
        }
    }
}
