package org.celestialworkshop.behemoths.client.models;// Made with Blockbench 5.0.7
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.misc.PhantashroomGlutton;

import java.util.List;

public class PhantashroomGluttonModel<T extends PhantashroomGlutton> extends BMHierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.prefix("phantashroomgluttonmodel"), "main");
	private final ModelPart root;
	private final ModelPart main;
	private final ModelPart cap;
	private final ModelPart petals;
	private final ModelPart ptgr;
	private final ModelPart pt1;
	private final ModelPart pt2;
	private final ModelPart ptgr2;
	private final ModelPart pt3;
	private final ModelPart pt4;

	public PhantashroomGluttonModel(ModelPart root) {
		this.root = root.getChild("root");
		this.main = this.root.getChild("main");
		this.cap = this.main.getChild("cap");
		this.petals = this.main.getChild("petals");
		this.ptgr = this.petals.getChild("ptgr");
		this.pt1 = this.ptgr.getChild("pt1");
		this.pt2 = this.ptgr.getChild("pt2");
		this.ptgr2 = this.petals.getChild("ptgr2");
		this.pt3 = this.ptgr2.getChild("pt3");
		this.pt4 = this.ptgr2.getChild("pt4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition main = root.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 55).addBox(-8.0F, -18.0F, -8.0F, 16.0F, 18.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cap = main.addOrReplaceChild("cap", CubeListBuilder.create().texOffs(0, 27).addBox(-10.0F, -8.0F, -10.0F, 20.0F, 8.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -18.0F, 0.0F));

		PartDefinition petals = main.addOrReplaceChild("petals", CubeListBuilder.create(), PartPose.offset(0.0F, -20.0F, 0.0F));

		PartDefinition ptgr = petals.addOrReplaceChild("ptgr", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition pt1 = ptgr.addOrReplaceChild("pt1", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, 0.0F, -1.0F, 24.0F, 0.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition pt2 = ptgr.addOrReplaceChild("pt2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cube_r1 = pt2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, 0.0F, -26.0F, 24.0F, 0.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -25.0F, 0.0F, 3.1416F, 0.0F));

		PartDefinition ptgr2 = petals.addOrReplaceChild("ptgr2", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition pt3 = ptgr2.addOrReplaceChild("pt3", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, 0.0F, -1.0F, 24.0F, 0.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 8.0F, -0.5236F, 0.0F, 0.0F));

		PartDefinition pt4 = ptgr2.addOrReplaceChild("pt4", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 0.0F, -8.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition cube_r2 = pt4.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-12.0F, 0.0F, -26.0F, 24.0F, 0.0F, 27.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, -25.0F, 0.0F, 3.1416F, 0.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return root;
	}

	@Override
	public List<ModelPart> parts() {
		return List.of(main, cap, petals, ptgr, pt1, pt2, ptgr2, pt3, pt4);
	}

	@Override
	public void setupAnim(T pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
		this.parts().forEach(ModelPart::resetPose);

		this.main.yRot += Mth.sin(pAgeInTicks) * 3 * pEntity.hurtTime * Mth.DEG_TO_RAD;

		this.pt1.xRot += Mth.sin(pAgeInTicks * 0.1F) * 10 * Mth.DEG_TO_RAD;
		this.pt2.xRot += Mth.cos(pAgeInTicks * 0.1F) * 10 * Mth.DEG_TO_RAD;
		this.pt3.xRot += Mth.sin(pAgeInTicks * 0.1F) * 10 * Mth.DEG_TO_RAD;
		this.pt4.xRot += Mth.cos(pAgeInTicks * 0.1F) * 10 * Mth.DEG_TO_RAD;
		this.animateManager(pEntity, pAgeInTicks);
	}
}