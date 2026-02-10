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
import org.celestialworkshop.behemoths.entities.HollowborneTurret;

import java.util.List;

public class HollowborneTurretModel<T extends HollowborneTurret> extends BMHierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.prefix("hollowborneweaponmodel"), "main");
	private final ModelPart weapon;
	private final ModelPart bow;

	public HollowborneTurretModel(ModelPart root) {
		this.weapon = root.getChild("weapon");
		this.bow = this.weapon.getChild("bow");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition weapon = partdefinition.addOrReplaceChild("weapon", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, 24.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r1 = weapon.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(101, 52).addBox(-2.0F, -5.0F, -18.0F, 4.0F, 4.0F, 36.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 18.0F, -3.0F, -1.5708F, 0.0F, 0.0F));

		PartDefinition bow = weapon.addOrReplaceChild("bow", CubeListBuilder.create().texOffs(0, 0).addBox(-15.0F, 1.0F, -16.0F, 30.0F, 20.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(100, 52).addBox(-3.0F, -1.0F, -18.0F, 6.0F, 6.0F, 36.0F, new CubeDeformation(0.0F))
		.texOffs(0, 169).addBox(-2.5F, 4.0F, -34.0F, 5.0F, 5.0F, 64.0F, new CubeDeformation(0.0F))
		.texOffs(113, 129).addBox(-4.5F, 2.0F, 22.0F, 9.0F, 9.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 35.0F, 0.0F));

		PartDefinition cube_r2 = bow.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 159).addBox(1.0F, -8.0F, 0.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 17.0F, -43.7F, 0.0F, 0.1309F, -1.5708F));

		PartDefinition cube_r3 = bow.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 159).addBox(1.0F, -8.0F, 0.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 20.0F, -43.7F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r4 = bow.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(20, 189).addBox(-1.0F, -6.0F, -28.0F, 4.0F, 4.0F, 44.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 15.0F, -6.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition cube_r5 = bow.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(95, 204).addBox(-1.0F, -6.0F, 0.0F, 4.0F, 4.0F, 22.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-24.0F, 13.5F, -14.0F, 0.0F, 2.618F, 0.0F));

		PartDefinition cube_r6 = bow.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(95, 204).mirror().addBox(-3.0F, -6.0F, 0.0F, 4.0F, 4.0F, 22.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(24.0F, 13.5F, -14.0F, 0.0F, -2.618F, 0.0F));

		PartDefinition cube_r7 = bow.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(81, 191).addBox(-2.0F, -6.0F, -16.0F, 5.0F, 5.0F, 35.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 13.0F, -32.0F, 0.0F, 1.5708F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.parts().forEach(ModelPart::resetPose);
		this.bow.xRot = -headPitch * Mth.DEG_TO_RAD;
		this.bow.yRot = -netHeadYaw * Mth.DEG_TO_RAD;

		this.animateManager(entity, ageInTicks);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		weapon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return weapon;
	}

	@Override
	public List<ModelPart> parts() {
		return List.of(weapon, bow);
	}
}