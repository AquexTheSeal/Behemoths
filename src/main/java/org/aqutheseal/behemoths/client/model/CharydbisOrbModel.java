package org.aqutheseal.behemoths.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.aqutheseal.behemoths.Behemoths;
import org.aqutheseal.behemoths.entity.projectile.CharydbisOrb;

public class CharydbisOrbModel<T extends CharydbisOrb> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.location("charydbisorbmodel"), "main");
	private final ModelPart root;
	private final ModelPart base;
	private final ModelPart wind;

	public CharydbisOrbModel(ModelPart root) {
		this.root = root.getChild("root");
		this.base = this.root.getChild("base");
		this.wind = this.root.getChild("wind");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition base = root.addOrReplaceChild("base", CubeListBuilder.create().texOffs(0, 32).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition wind = root.addOrReplaceChild("wind", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(CharydbisOrb entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root.getAllParts().forEach(ModelPart::resetPose);
		this.base.xRot = Mth.wrapDegrees(ageInTicks * 0.5F);
		this.base.zRot = Mth.wrapDegrees(ageInTicks * 0.25F);

		this.wind.xRot = Mth.wrapDegrees(-ageInTicks * 0.5F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}