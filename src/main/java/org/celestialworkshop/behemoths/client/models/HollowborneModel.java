package org.celestialworkshop.behemoths.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.Hollowborne;

import java.util.List;

public class HollowborneModel<T extends Hollowborne> extends BMHierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.prefix("hollowbornemodel"), "main");

	public final Leg limbl;
	public final Leg limbr;
	public final Leg limblb;
	public final Leg limbrb;

	private final ModelPart mob;
	private final ModelPart subroot;
	private final ModelPart spiner;
	private final ModelPart spinel;
	private final ModelPart armml;
	private final ModelPart armml1;
	private final ModelPart armml2;
	private final ModelPart armml3;
	private final ModelPart armmr;
	private final ModelPart armmr1;
	private final ModelPart armmr2;
	private final ModelPart armmr3;
	private final ModelPart armmrb;
	private final ModelPart armmrb1;
	private final ModelPart armmrb2;
	private final ModelPart armmrb3;
	private final ModelPart armmlb;
	private final ModelPart armmlb1;
	private final ModelPart armmlb2;
	private final ModelPart armmlb3;

	public HollowborneModel(ModelPart root) {
		this.mob = root.getChild("mob");
		this.subroot = this.mob.getChild("subroot");
		this.spiner = this.subroot.getChild("spiner");
		this.spinel = this.subroot.getChild("spinel");
		this.armml = this.subroot.getChild("armml");
		this.armml1 = this.armml.getChild("armml1");
		this.armml2 = this.armml1.getChild("armml2");
		this.armml3 = this.armml2.getChild("armml3");
		this.armmr = this.subroot.getChild("armmr");
		this.armmr1 = this.armmr.getChild("armmr1");
		this.armmr2 = this.armmr1.getChild("armmr2");
		this.armmr3 = this.armmr2.getChild("armmr3");
		this.armmrb = this.subroot.getChild("armmrb");
		this.armmrb1 = this.armmrb.getChild("armmrb1");
		this.armmrb2 = this.armmrb1.getChild("armmrb2");
		this.armmrb3 = this.armmrb2.getChild("armmrb3");
		this.armmlb = this.subroot.getChild("armmlb");
		this.armmlb1 = this.armmlb.getChild("armmlb1");
		this.armmlb2 = this.armmlb1.getChild("armmlb2");
		this.armmlb3 = this.armmlb2.getChild("armmlb3");

		this.limbl = new Leg(armml1, armml3, 0);
		this.limbr = new Leg(armmr1, armmr3, 1);
		this.limblb = new Leg(armmlb1, armmlb3, 2);
		this.limbrb = new Leg(armmrb1, armmrb3, 3);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition mob = partdefinition.addOrReplaceChild("mob", CubeListBuilder.create(), PartPose.offset(0.0F, 8.0F, 0.0F));

		PartDefinition subroot = mob.addOrReplaceChild("subroot", CubeListBuilder.create().texOffs(100, 52).addBox(-3.0F, -50.0F, -18.0F, 6.0F, 6.0F, 36.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-15.0F, -48.0F, -16.0F, 30.0F, 20.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition spiner = subroot.addOrReplaceChild("spiner", CubeListBuilder.create(), PartPose.offset(0.0F, -49.0F, 0.0F));

		PartDefinition cube_r1 = spiner.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 52).mirror().addBox(-1.0F, -23.0F, -27.0F, 0.0F, 23.0F, 50.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 2.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition spinel = subroot.addOrReplaceChild("spinel", CubeListBuilder.create(), PartPose.offset(0.0F, -49.0F, 0.0F));

		PartDefinition cube_r2 = spinel.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 52).addBox(1.0F, -23.0F, -27.0F, 0.0F, 23.0F, 50.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 2.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition armml = subroot.addOrReplaceChild("armml", CubeListBuilder.create(), PartPose.offsetAndRotation(10.0F, -49.0F, -8.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition armml1 = armml.addOrReplaceChild("armml1", CubeListBuilder.create().texOffs(100, 94).addBox(-1.0F, -3.0F, -3.0F, 23.0F, 22.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition armml2 = armml1.addOrReplaceChild("armml2", CubeListBuilder.create().texOffs(124, 0).addBox(-7.0F, -1.0F, -1.0F, 29.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.48F));

		PartDefinition armml3 = armml2.addOrReplaceChild("armml3", CubeListBuilder.create().texOffs(88, 128).addBox(-4.0F, -17.0F, 1.0F, 8.0F, 24.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 125).addBox(-6.0F, 7.0F, -2.0F, 12.0F, 34.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r3 = armml3.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(124, 16).addBox(-1.0F, -8.0F, 0.0F, 8.0F, 25.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 43.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		PartDefinition armmr = subroot.addOrReplaceChild("armmr", CubeListBuilder.create(), PartPose.offsetAndRotation(-10.0F, -49.0F, -8.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition armmr1 = armmr.addOrReplaceChild("armmr1", CubeListBuilder.create().texOffs(100, 94).mirror().addBox(-22.0F, -3.0F, -3.0F, 23.0F, 22.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, -3.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition armmr2 = armmr1.addOrReplaceChild("armmr2", CubeListBuilder.create().texOffs(124, 0).mirror().addBox(-22.0F, -1.0F, -1.0F, 29.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-23.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.48F));

		PartDefinition armmr3 = armmr2.addOrReplaceChild("armmr3", CubeListBuilder.create().texOffs(88, 128).mirror().addBox(-4.0F, -17.0F, 1.0F, 8.0F, 24.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 125).mirror().addBox(-6.0F, 7.0F, -2.0F, 12.0F, 34.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-23.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r4 = armmr3.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(124, 16).mirror().addBox(-7.0F, -8.0F, 0.0F, 8.0F, 25.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, 43.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition armmrb = subroot.addOrReplaceChild("armmrb", CubeListBuilder.create(), PartPose.offsetAndRotation(-10.0F, -49.0F, 8.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition armmrb1 = armmrb.addOrReplaceChild("armmrb1", CubeListBuilder.create().texOffs(100, 94).mirror().addBox(-22.0F, -3.0F, -9.0F, 23.0F, 22.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, -0.3491F));

		PartDefinition armmrb2 = armmrb1.addOrReplaceChild("armmrb2", CubeListBuilder.create().texOffs(124, 0).mirror().addBox(-22.0F, -1.0F, -7.0F, 29.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-23.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.48F));

		PartDefinition armmrb3 = armmrb2.addOrReplaceChild("armmrb3", CubeListBuilder.create().texOffs(88, 128).mirror().addBox(-4.0F, -17.0F, -5.0F, 8.0F, 24.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 125).mirror().addBox(-6.0F, 7.0F, -8.0F, 12.0F, 34.0F, 10.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-23.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition cube_r5 = armmrb3.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(124, 16).mirror().addBox(-7.0F, -8.0F, -6.0F, 8.0F, 25.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.0F, 43.0F, 0.0F, 0.0F, 0.0F, -0.5236F));

		PartDefinition armmlb = subroot.addOrReplaceChild("armmlb", CubeListBuilder.create(), PartPose.offsetAndRotation(10.0F, -49.0F, 8.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition armmlb1 = armmlb.addOrReplaceChild("armmlb1", CubeListBuilder.create().texOffs(100, 94).addBox(-1.0F, -3.0F, -9.0F, 23.0F, 22.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.0F, 0.0F, 0.3491F));

		PartDefinition armmlb2 = armmlb1.addOrReplaceChild("armmlb2", CubeListBuilder.create().texOffs(124, 0).addBox(-7.0F, -1.0F, -7.0F, 29.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.48F));

		PartDefinition armmlb3 = armmlb2.addOrReplaceChild("armmlb3", CubeListBuilder.create().texOffs(88, 128).addBox(-4.0F, -17.0F, -5.0F, 8.0F, 24.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 125).addBox(-6.0F, 7.0F, -8.0F, 12.0F, 34.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition cube_r6 = armmlb3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(124, 16).addBox(-1.0F, -8.0F, -6.0F, 8.0F, 25.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 43.0F, 0.0F, 0.0F, 0.0F, 0.5236F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

		this.parts().forEach(ModelPart::resetPose);

		this.armml1.yRot += (Mth.sin(limbSwing * 0.4F) * 25 * limbSwingAmount) * Mth.DEG_TO_RAD;
		this.armml1.zRot += (-Mth.cos(limbSwing * 0.4F) * 25 * limbSwingAmount) * Mth.DEG_TO_RAD;

		this.armmr1.yRot += (Mth.cos(limbSwing * 0.4F) * 25 * limbSwingAmount) * Mth.DEG_TO_RAD;
		this.armmr1.zRot += (Mth.sin(limbSwing * 0.4F) * 25 * limbSwingAmount) * Mth.DEG_TO_RAD;

		this.armmlb1.yRot += (Mth.cos(limbSwing * 0.4F) * 25 * limbSwingAmount) * Mth.DEG_TO_RAD;
		this.armmlb1.zRot += (Mth.sin(limbSwing * 0.4F) * 25 * limbSwingAmount) * Mth.DEG_TO_RAD;

		this.armmrb1.yRot += (Mth.sin(limbSwing * 0.4F) * 25 * limbSwingAmount) * Mth.DEG_TO_RAD;
		this.armmrb1.zRot += (-Mth.cos(limbSwing * 0.4F) * 25 * limbSwingAmount) * Mth.DEG_TO_RAD;


		this.limbr.solve(entity, ageInTicks - entity.tickCount, true);
		this.limbl.solve(entity, ageInTicks - entity.tickCount, false);
		this.limbrb.solve(entity, ageInTicks - entity.tickCount, true);
		this.limblb.solve(entity, ageInTicks - entity.tickCount, false);

		this.animateManager(entity, ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		mob.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return mob;
	}

	@Override
	public List<ModelPart> parts() {
		return ImmutableList.of(
				this.mob, this.subroot, this.spiner, this.spinel, this.armml, this.armml1, this.armml2, this.armml3, this.armmr, this.armmr1,
				this.armmr2, this.armmr3, this.armmrb, this.armmrb1, this.armmrb2, this.armmrb3, this.armmlb, this.armmlb1,
				this.armmlb2, this.armmlb3
		);
	}

	public static class Leg {

		public final ModelPart leg;
		public final ModelPart knee;
		private final int legIndex;

		public Leg(ModelPart leg, ModelPart knee, int index) {
			this.leg = leg;
			this.knee = knee;
			this.legIndex = index;
		}

		public void solve(Hollowborne entity, float partialTick, boolean invert) {
			float depthO = entity.targetDepthsO[legIndex];
			float depthC = entity.targetDepths[legIndex];
			float lerpedDepth = Mth.lerp(partialTick, depthO, depthC);

			int inv = invert ? -1 : 1;
			if (lerpedDepth > 0) {
				leg.zRot += (-22.5F * lerpedDepth * inv) * Mth.DEG_TO_RAD;
				knee.zRot += (12.5F * lerpedDepth * inv) * Mth.DEG_TO_RAD;
			} else {
				leg.zRot = Mth.clamp(leg.zRot + (-18.5F * lerpedDepth * inv) * Mth.DEG_TO_RAD, -120 * Mth.DEG_TO_RAD, 120 * Mth.DEG_TO_RAD);
				knee.zRot = Mth.clamp(knee.zRot + (25.5F * lerpedDepth * inv) * Mth.DEG_TO_RAD, -80 * Mth.DEG_TO_RAD, 80 * Mth.DEG_TO_RAD);
			}
		}
	}
}