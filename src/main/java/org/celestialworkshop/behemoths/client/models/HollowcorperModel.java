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
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.entities.projectile.Hollowcorper;

public class HollowcorperModel<T extends Hollowcorper> extends EntityModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.prefix( "hollowcorpermodel"), "main");

	private final ModelPart weapon;

	public HollowcorperModel(ModelPart root) {
		this.weapon = root.getChild("weapon");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition weapon = partdefinition.addOrReplaceChild("weapon", CubeListBuilder.create().texOffs(20, 22).addBox(-1.5F, 2.6095F, -19.65F, 3.0F, 3.0F, 44.0F, new CubeDeformation(0.0F))
				.texOffs(0, -10).addBox(0.0F, 0.1095F, -29.35F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 24.5F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r1 = weapon.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, -10).addBox(1.0F, -8.0F, 0.0F, 0.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 5.1095F, -29.35F, 0.0F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Hollowcorper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		weapon.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}