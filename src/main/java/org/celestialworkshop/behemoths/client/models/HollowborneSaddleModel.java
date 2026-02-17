package org.celestialworkshop.behemoths.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.Hollowborne;

public class HollowborneSaddleModel<T extends Hollowborne> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.prefix("hollowbornesaddlemodel"), "main");
	private final ModelPart saddle;
	private final ModelPart chain;
	private final ModelPart chain2;

	public HollowborneSaddleModel(ModelPart root) {
		this.saddle = root.getChild("saddle");
		this.chain = this.saddle.getChild("chain");
		this.chain2 = this.saddle.getChild("chain2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition saddle = partdefinition.addOrReplaceChild("saddle", CubeListBuilder.create().texOffs(0, 0).addBox(-17.0F, -20.0F, -20.0F, 34.0F, 21.0F, 40.0F, new CubeDeformation(0.0F))
				.texOffs(0, 90).addBox(-8.0F, -39.0F, -8.0F, 16.0F, 19.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 61).addBox(-12.0F, -44.0F, -12.0F, 24.0F, 5.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(0, 90).addBox(-27.0F, -28.0F, -30.0F, 16.0F, 19.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 90).mirror().addBox(11.0F, -28.0F, 14.0F, 16.0F, 19.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 90).addBox(-27.0F, -28.0F, 14.0F, 16.0F, 19.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(0, 90).mirror().addBox(11.0F, -28.0F, -30.0F, 16.0F, 19.0F, 16.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 23.0F, 0.0F));

		PartDefinition cube_r1 = saddle.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 61).mirror().addBox(-12.0F, -3.0F, -10.0F, 24.0F, 5.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-17.0F, -28.0F, 20.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition cube_r2 = saddle.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 61).addBox(-12.0F, -3.0F, -10.0F, 24.0F, 5.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0F, -28.0F, 20.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r3 = saddle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 61).mirror().addBox(-12.0F, -3.0F, -14.0F, 24.0F, 5.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-17.0F, -28.0F, -20.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition cube_r4 = saddle.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 61).addBox(-12.0F, -3.0F, -14.0F, 24.0F, 5.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(17.0F, -28.0F, -20.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition chain = saddle.addOrReplaceChild("chain", CubeListBuilder.create(), PartPose.offset(29.0F, -11.0F, 0.0F));

		PartDefinition cube_r5 = chain.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(32, 61).addBox(-12.0F, 0.0F, -50.0F, 24.0F, 0.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -3.0F, 18.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition chain2 = saddle.addOrReplaceChild("chain2", CubeListBuilder.create(), PartPose.offsetAndRotation(-29.0F, -11.0F, 0.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition cube_r6 = chain2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(32, 61).addBox(-27.0F, 0.0F, -50.0F, 24.0F, 0.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 12.0F, 18.0F, 0.0F, 0.0F, 1.5708F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Hollowborne entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.chain.resetPose();
		this.chain.zRot += 5 + Mth.sin(limbSwing * 0.5F) * 15 * Mth.DEG_TO_RAD;
		this.chain2.resetPose();
		this.chain2.zRot += -5 + -Mth.sin(limbSwing * 0.5F) * 15 * Mth.DEG_TO_RAD;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		saddle.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}