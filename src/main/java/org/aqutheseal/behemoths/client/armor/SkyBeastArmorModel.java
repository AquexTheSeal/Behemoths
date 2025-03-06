package org.aqutheseal.behemoths.client.armor;// Made with Blockbench 4.12.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import org.aqutheseal.behemoths.Behemoths;

public class SkyBeastArmorModel extends HumanoidModel<LivingEntity> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.location("sky_beast_armor"), "main");
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart left_arm;
	private final ModelPart right_arm;
	private final ModelPart left_shoe;
	private final ModelPart right_shoe;

	public SkyBeastArmorModel(ModelPart root) {
        super(root);
        this.head = root.getChild("head");
		this.body = root.getChild("body");
		this.left_arm = root.getChild("left_arm");
		this.right_arm = root.getChild("right_arm");
		this.left_shoe = root.getChild("left_leg");
		this.right_shoe = root.getChild("right_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 1);
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.75F))
		.texOffs(0, 32).addBox(-3.0F, -6.8F, -9.0F, 6.0F, 2.0F, 4.0F, new CubeDeformation(0.75F))
		.texOffs(32, 0).addBox(-4.0F, -7.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(1.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r1 = head.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 55).mirror().addBox(1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.4F, 0.0F, -2.0F, 1.3963F, 0.0F, -0.8727F));

		PartDefinition cube_r2 = head.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 55).mirror().addBox(1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.4F, 0.0F, 1.0F, 1.5708F, 0.0F, -0.8727F));

		PartDefinition cube_r3 = head.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 55).mirror().addBox(1.0F, -2.0F, -4.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(6.4F, 0.0F, 2.0F, -1.3963F, 0.0F, -0.8727F));

		PartDefinition cube_r4 = head.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 55).addBox(-1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.4F, 0.0F, 1.0F, 1.5708F, 0.0F, 0.8727F));

		PartDefinition cube_r5 = head.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 55).addBox(-1.0F, -2.0F, -4.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.4F, 0.0F, 2.0F, -1.3963F, 0.0F, 0.8727F));

		PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 55).addBox(-1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.4F, 0.0F, -2.0F, 1.3963F, 0.0F, 0.8727F));

		PartDefinition cube_r7 = head.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 49).mirror().addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-3.0F, -5.0F, 1.0F, 0.132F, -0.1298F, -0.0172F));

		PartDefinition cube_r8 = head.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, -5.0F, 1.0F, 0.132F, 0.1298F, 0.0172F));

		PartDefinition cube_r9 = head.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 49).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 2.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -5.0F, 1.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r10 = head.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(23, 15).mirror().addBox(-1.0F, -10.0F, -3.0F, 0.0F, 15.0F, 17.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.5F, -6.0F, -1.0F, 0.132F, -0.1298F, -0.0172F));

		PartDefinition cube_r11 = head.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(23, 15).addBox(1.0F, -10.0F, -3.0F, 0.0F, 15.0F, 17.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.5F, -6.0F, -1.0F, 0.132F, 0.1298F, 0.0172F));

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition left_arm = partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(40, 16).mirror().addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false)
		.texOffs(0, 38).mirror().addBox(-1.0F, -2.5F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.75F)).mirror(false), PartPose.offset(5.0F, 2.0F, 0.0F));

		PartDefinition cube_r12 = left_arm.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 55).mirror().addBox(1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 7.0F, -2.0F, 1.3963F, 0.0F, -0.2618F));

		PartDefinition cube_r13 = left_arm.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 55).mirror().addBox(1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 7.0F, 1.0F, 1.5708F, 0.0F, -0.2618F));

		PartDefinition cube_r14 = left_arm.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(0, 55).mirror().addBox(1.0F, -2.0F, -4.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, 7.0F, 2.0F, -1.3963F, 0.0F, -0.2618F));

		PartDefinition right_arm = partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(40, 16).addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F))
		.texOffs(0, 38).addBox(-4.0F, -2.5F, -3.0F, 5.0F, 5.0F, 6.0F, new CubeDeformation(0.75F)), PartPose.offset(-5.0F, 2.0F, 0.0F));

		PartDefinition cube_r15 = right_arm.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(0, 55).addBox(-1.0F, -2.0F, -4.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 7.0F, 2.0F, -1.3963F, 0.0F, 0.2618F));

		PartDefinition cube_r16 = right_arm.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(0, 55).addBox(-1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 7.0F, -2.0F, 1.3963F, 0.0F, 0.2618F));

		PartDefinition cube_r17 = right_arm.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 55).addBox(-1.0F, -2.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 7.0F, 1.0F, 1.5708F, 0.0F, 0.2618F));

		PartDefinition left_shoe = partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)).mirror(false), PartPose.offset(1.9F, 12.0F, 0.0F));

		PartDefinition right_shoe = partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.75F)), PartPose.offset(-1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		head.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_arm.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		left_shoe.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
		right_shoe.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(LivingEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

	}
}