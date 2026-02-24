package org.celestialworkshop.behemoths.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.misc.Cragpiercer;

import java.util.List;

public class CragpiercerModel<T extends Cragpiercer> extends BMHierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.prefix("cragpiercermodel"), "main");
	private final ModelPart base;
	private final ModelPart stick;
	private final ModelPart head;
	private final ModelPart armr;
	private final ModelPart thingr;
	private final ModelPart stringr;
	private final ModelPart arml;
	private final ModelPart thingl;
	private final ModelPart stringl;

	public CragpiercerModel(ModelPart root) {
		this.base = root.getChild("base");
		this.stick = this.base.getChild("stick");
		this.head = this.stick.getChild("head");
		this.armr = this.head.getChild("armr");
		this.thingr = this.armr.getChild("thingr");
		this.stringr = this.thingr.getChild("stringr");
		this.arml = this.head.getChild("arml");
		this.thingl = this.arml.getChild("thingl");
		this.stringl = this.thingl.getChild("stringl");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition base = partdefinition.addOrReplaceChild("base", CubeListBuilder.create().texOffs(80, 50).addBox(-11.0F, -2.0F, -11.0F, 22.0F, 2.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition stick = base.addOrReplaceChild("stick", CubeListBuilder.create().texOffs(0, 114).addBox(-1.5F, -15.0F, -1.5F, 3.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(36, 107).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 9.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition head = stick.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -6.0F, -31.0F, 18.0F, 6.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(88, 95).addBox(-7.0F, -5.0F, 13.0F, 14.0F, 3.0F, 5.0F, new CubeDeformation(0.0F))
				.texOffs(80, 78).addBox(-8.0F, -11.0F, -33.0F, 16.0F, 9.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(104, 111).addBox(-2.0F, -10.0F, 7.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 50).addBox(-1.0F, -12.0F, -31.0F, 2.0F, 6.0F, 38.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -15.0F, 0.0F));

		PartDefinition armr = head.addOrReplaceChild("armr", CubeListBuilder.create(), PartPose.offset(-8.0F, -7.0F, -29.0F));

		PartDefinition thingr = armr.addOrReplaceChild("thingr", CubeListBuilder.create().texOffs(44, 95).mirror().addBox(-13.0F, -3.0F, -3.0F, 16.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.3927F, 0.0F));

		PartDefinition cube_r1 = thingr.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(82, 111).addBox(-3.0F, -4.0F, -3.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-12.0F, 0.0F, -1.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition cube_r2 = thingr.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 106).mirror().addBox(-14.0F, -2.0F, -1.0F, 14.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-14.0F, 0.0F, -1.0F, 0.0F, 0.2618F, 0.0F));

		PartDefinition stringr = thingr.addOrReplaceChild("stringr", CubeListBuilder.create(), PartPose.offset(-21.0F, 0.0F, 4.0F));

		PartDefinition cube_r3 = stringr.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(80, 74).addBox(-14.0F, 0.0F, 0.0F, 34.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(10.0F, 0.0F, 3.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition arml = head.addOrReplaceChild("arml", CubeListBuilder.create(), PartPose.offset(8.0F, -7.0F, -29.0F));

		PartDefinition thingl = arml.addOrReplaceChild("thingl", CubeListBuilder.create().texOffs(44, 95).addBox(-3.0F, -3.0F, -3.0F, 16.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -0.3927F, 0.0F));

		PartDefinition cube_r4 = thingl.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(82, 111).addBox(0.0F, -4.0F, -3.0F, 3.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(12.0F, 0.0F, -1.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition cube_r5 = thingl.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 106).addBox(0.0F, -2.0F, -1.0F, 14.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 0.0F, -1.0F, 0.0F, -0.2618F, 0.0F));

		PartDefinition stringl = thingl.addOrReplaceChild("stringl", CubeListBuilder.create(), PartPose.offset(21.0F, 0.0F, 4.0F));

		PartDefinition cube_r6 = stringl.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(80, 76).addBox(-20.0F, 0.0F, 0.0F, 34.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, 0.0F, 3.0F, 0.0F, 0.3927F, 0.0F));

		return LayerDefinition.create(meshdefinition, 176, 176);
	}

	@Override
	public void setupAnim(Cragpiercer entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		parts().forEach(ModelPart::resetPose);

		this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.head.xRot = headPitch * Mth.DEG_TO_RAD;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		base.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return base;
	}

	@Override
	public List<ModelPart> parts() {
		return List.of(base, stick, head, armr, stringr, arml, stringl, thingl, thingr);
	}
}