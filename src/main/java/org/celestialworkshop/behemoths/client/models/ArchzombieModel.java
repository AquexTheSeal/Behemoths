package org.celestialworkshop.behemoths.client.models;
// Made with Blockbench 5.0.0
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.animations.ArchzombieAnimations;
import org.celestialworkshop.behemoths.entities.Archzombie;

import java.util.List;

public class ArchzombieModel<T extends Archzombie> extends BMHierarchicalModel<T> implements ArmedModel {

	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.prefix("archzombiemodel"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart right_arm;
    private final ModelPart right_hand;
    private final ModelPart left_arm;
    private final ModelPart left_hand;
    private final ModelPart head;
    private final ModelPart left_leg;
    private final ModelPart right_leg;

    public ArchzombieModel(ModelPart root) {
        this.root = root.getChild("root");
        this.body = this.root.getChild("body");
        this.right_arm = this.body.getChild("right_arm");
        this.right_hand = this.right_arm.getChild("right_hand");
        this.left_arm = this.body.getChild("left_arm");
        this.left_hand = this.left_arm.getChild("left_hand");
        this.head = this.body.getChild("head");
        this.left_leg = this.root.getChild("left_leg");
        this.right_leg = this.root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(72, 0).addBox(-4.0F, 9.0F, -2.0F, 8.0F, 3.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition body_r1 = body.addOrReplaceChild("body_r1", CubeListBuilder.create().texOffs(0, 45).addBox(-8.0F, -16.0F, -2.0F, 12.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(0, 24).addBox(-8.0F, -12.0F, -2.0F, 12.0F, 9.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.2F, 12.0F, -1.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(48, 0).addBox(-5.0F, -2.0F, -3.0F, 6.0F, 24.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(72, 20).addBox(-5.5F, 13.0F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, 2.0F, 0.0F));

        PartDefinition right_hand = right_arm.addOrReplaceChild("right_hand", CubeListBuilder.create(), PartPose.offset(-1.5F, 19.0F, -0.5F));

        PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(48, 30).addBox(5.0F, -2.0F, -3.0F, 6.0F, 24.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(72, 20).mirror().addBox(4.5F, 13.0F, -3.5F, 7.0F, 9.0F, 7.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, 2.0F, 0.0F));

        PartDefinition left_hand = left_arm.addOrReplaceChild("left_hand", CubeListBuilder.create(), PartPose.offset(7.5F, 19.0F, -0.5F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, -24.0F, 0.0F, 24.0F, 24.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(48, 60).addBox(-4.0F, -20.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(16, 61).addBox(-1.9F, 0.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(2.9F, 12.0F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 61).addBox(-2.1F, 0.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.9F, 12.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

	@Override
	public void setupAnim(Archzombie entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.parts().forEach(ModelPart::resetPose);

        if (entity.shouldSit()) {
            this.root.y += 12;
            this.root.z += 8;
        } else {
            this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
            this.head.xRot = headPitch * Mth.DEG_TO_RAD;
        }

        this.animate(entity.idleAnimationState, ArchzombieAnimations.IDLE, ageInTicks);
        this.animate(entity.ridingAnimationState, ArchzombieAnimations.RIDING, ageInTicks);
        this.animate(entity.ridingSweepAnimationState, ArchzombieAnimations.RIDING_SWEEP, ageInTicks);
        this.animate(entity.sweep0AnimationState, ArchzombieAnimations.SWEEP_0, ageInTicks);

        this.animateWalk(ArchzombieAnimations.WALK, limbSwing, limbSwingAmount, 2.0F, 1.0F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public void translateToHand(HumanoidArm pSide, PoseStack pPoseStack) {
        if (pSide == HumanoidArm.LEFT) {
            this.root.translateAndRotate(pPoseStack);
            this.body.translateAndRotate(pPoseStack);
            this.left_arm.translateAndRotate(pPoseStack);
            this.left_hand.translateAndRotate(pPoseStack);
            pPoseStack.translate(0, -0.5, 0.15);
        } else {
            this.root.translateAndRotate(pPoseStack);
            this.body.translateAndRotate(pPoseStack);
            this.right_arm.translateAndRotate(pPoseStack);
            this.right_hand.translateAndRotate(pPoseStack);
            pPoseStack.translate(0, -0.5, 0.15);
        }
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    public List<ModelPart> parts() {
        return ObjectArrayList.of(root, body, left_arm, right_arm, head, left_leg, right_leg, left_hand, right_hand);
    }
}