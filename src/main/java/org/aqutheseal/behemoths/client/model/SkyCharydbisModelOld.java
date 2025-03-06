package org.aqutheseal.behemoths.client.model;// Made with Blockbench 4.12.3
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.entity.SkyCharydbis;

public class SkyCharydbisModelOld<T extends SkyCharydbis> extends HierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.location("sky_charydbis_model"), "main");
	private final ModelPart mob;
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart bodyin;
	private final ModelPart bodyin2;
	private final ModelPart bodyin3;
	private final ModelPart wingbotl;
	private final ModelPart wingtopl;
	private final ModelPart wingl;
	private final ModelPart wingl2;
	private final ModelPart wingbotr;
	private final ModelPart wingtopr;
	private final ModelPart wingr;
	private final ModelPart wingr2;

	public SkyCharydbisModelOld(ModelPart root) {
		this.mob = root.getChild("mob");
		this.body = this.mob.getChild("body");
		this.head = this.body.getChild("head");
		this.bodyin = this.body.getChild("bodyin");
		this.bodyin2 = this.bodyin.getChild("bodyin2");
		this.bodyin3 = this.bodyin2.getChild("bodyin3");
		this.wingbotl = this.body.getChild("wingbotl");
		this.wingtopl = this.body.getChild("wingtopl");
		this.wingl = this.body.getChild("wingl");
		this.wingl2 = this.wingl.getChild("wingl2");
		this.wingbotr = this.body.getChild("wingbotr");
		this.wingtopr = this.body.getChild("wingtopr");
		this.wingr = this.body.getChild("wingr");
		this.wingr2 = this.wingr.getChild("wingr2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition mob = partdefinition.addOrReplaceChild("mob", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition body = mob.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -16.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(202, 207).addBox(-31.0F, -32.0F, -1.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7071F, 21.9203F, 1.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-16.0F, 0.0F, -1.0F, 32.0F, 74.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-21.0F, 16.0F, -2.0F, -1.5708F, 0.0F, -0.7854F));

		PartDefinition cube_r3 = body.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, 0.0F, -1.0F, 32.0F, 74.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.0F, 16.0F, -2.0F, -1.5708F, 0.0F, 0.7854F));

		PartDefinition cube_r4 = body.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-16.0F, -74.0F, -1.0F, 32.0F, 74.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-21.0F, -16.0F, -78.0F, -1.5708F, 0.0F, 0.7854F));

		PartDefinition cube_r5 = body.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-16.0F, -74.0F, -1.0F, 32.0F, 74.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(21.0F, -16.0F, -78.0F, -1.5708F, 0.0F, -0.7854F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(-0.7071F, 0.9203F, -31.0F));

		PartDefinition cube_r6 = head.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(208, 303).addBox(-25.7071F, -28.0F, 16.0F, 22.7071F, 24.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 21.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bodyin = body.addOrReplaceChild("bodyin", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 32.0F));

		PartDefinition cube_r7 = bodyin.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(238, 128).addBox(-28.0F, -29.0F, -1.0F, 26.0F, 26.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7071F, 21.9203F, 1.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bodyin2 = bodyin.addOrReplaceChild("bodyin2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 33.0F));

		PartDefinition cube_r8 = bodyin2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(108, 271).addBox(-24.0F, -25.0F, -1.0F, 18.0F, 18.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7071F, 21.9203F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bodyin3 = bodyin2.addOrReplaceChild("bodyin3", CubeListBuilder.create().texOffs(208, 271).addBox(-11.0F, 0.0F, 31.0F, 22.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 32.0F));

		PartDefinition cube_r9 = bodyin3.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(208, 271).addBox(-26.0F, -15.0F, -1.0F, 22.0F, 0.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -15.0F, 32.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition cube_r10 = bodyin3.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 268).addBox(-26.0F, -27.0F, -1.0F, 22.0F, 22.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.7071F, 21.9203F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition wingbotl = body.addOrReplaceChild("wingbotl", CubeListBuilder.create().texOffs(-61, 323).addBox(0.0F, 0.0F, -16.0F, 40.0F, 0.0F, 61.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, 10.0F, 18.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition wingtopl = body.addOrReplaceChild("wingtopl", CubeListBuilder.create().texOffs(0, 207).addBox(1.0F, 0.0F, -16.0F, 40.0F, 0.0F, 61.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(7.0F, -10.0F, 18.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition wingl = body.addOrReplaceChild("wingl", CubeListBuilder.create().texOffs(0, 128).addBox(0.0F, -2.0F, -17.0F, 40.0F, 0.0F, 79.0F, new CubeDeformation(0.0F))
				.texOffs(0, 128).addBox(0.0F, 1.0F, -17.0F, 40.0F, 0.0F, 79.0F, new CubeDeformation(0.0F)), PartPose.offset(18.0F, 0.0F, 19.0F));

		PartDefinition wingl2 = wingl.addOrReplaceChild("wingl2", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -2.0F, -49.0F, 101.0F, 0.0F, 128.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(0.0F, 1.0F, -49.0F, 101.0F, 0.0F, 128.0F, new CubeDeformation(0.0F)), PartPose.offset(40.0F, 0.0F, 14.0F));

		PartDefinition wingbotr = body.addOrReplaceChild("wingbotr", CubeListBuilder.create().texOffs(-61, 323).mirror().addBox(-41.0F, 0.0F, -16.0F, 40.0F, 0.0F, 61.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.0F, 10.0F, 18.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition wingtopr = body.addOrReplaceChild("wingtopr", CubeListBuilder.create().texOffs(0, 207).mirror().addBox(-41.0F, 0.0F, -16.0F, 40.0F, 0.0F, 61.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-7.0F, -10.0F, 18.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition wingr = body.addOrReplaceChild("wingr", CubeListBuilder.create().texOffs(0, 128).mirror().addBox(-40.0F, -2.0F, -17.0F, 40.0F, 0.0F, 79.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 128).mirror().addBox(-40.0F, 1.0F, -17.0F, 40.0F, 0.0F, 79.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-18.0F, 0.0F, 19.0F));

		PartDefinition wingr2 = wingr.addOrReplaceChild("wingr2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-101.0F, -2.0F, -49.0F, 101.0F, 0.0F, 128.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).mirror().addBox(-101.0F, 1.0F, -49.0F, 101.0F, 0.0F, 128.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-40.0F, 0.0F, 14.0F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

	public ModelPart root() {
		return this.mob;
	}


	@Override
	public void setupAnim(SkyCharydbis entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		float partialTick = ageInTicks - entity.tickCount;
		float rollX = Mth.rotLerp(partialTick, entity.rollXO, entity.rollX);
		float rollY = Mth.rotLerp(partialTick, entity.rollYO, entity.rollY);

		mob.zRot = rollY * Mth.DEG_TO_RAD;

		bodyin.xRot = ((Mth.sin(ageInTicks * 0.1F) * 10) * Mth.DEG_TO_RAD) + rollX * 0.01F;
		bodyin.yRot = rollY * 0.01F;

		bodyin2.xRot = ((-Mth.cos(ageInTicks * 0.1F) * 10) * Mth.DEG_TO_RAD) + rollX * 0.02F;
		bodyin2.yRot = rollY * 0.02F;

		bodyin3.xRot = ((-Mth.cos(ageInTicks * 0.1F) * 10) * Mth.DEG_TO_RAD) + rollX * 0.03F;
		bodyin3.yRot = rollY * 0.03F;

		wingtopl.yRot = (Mth.sin(ageInTicks * 0.2F) * 5) * Mth.DEG_TO_RAD;
		wingtopr.yRot = -(Mth.sin(ageInTicks * 0.2F) * 5) * Mth.DEG_TO_RAD;

		wingbotl.yRot = (Mth.cos(ageInTicks * 0.2F) * 5) * Mth.DEG_TO_RAD;
		wingbotr.yRot = -(Mth.cos(ageInTicks * 0.2F) * 5) * Mth.DEG_TO_RAD;

		wingl.zRot = Mth.cos(limbSwing * 0.1F) * limbSwingAmount * 0.35F;
		wingr.zRot = -Mth.cos(limbSwing * 0.1F) * limbSwingAmount * 0.35F;
		wingl2.zRot = Mth.cos(limbSwing * 0.1F) * limbSwingAmount * 0.5F;
		wingr2.zRot = -Mth.cos(limbSwing * 0.1F) * limbSwingAmount * 0.5F;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		mob.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}