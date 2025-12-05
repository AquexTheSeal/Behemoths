package org.celestialworkshop.behemoths.client.models;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.client.animations.BanishingStampedeAnimations;
import org.celestialworkshop.behemoths.entities.BanishingStampede;

import java.util.List;

public class BanishingStampedeModel<T extends BanishingStampede> extends BMHierarchicalModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.prefix("banishingstampedemodel"), "main");
	private final ModelPart object;
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart neck;
	private final ModelPart mouth;
	private final ModelPart jaw;
	private final ModelPart mane;
	private final ModelPart head;
	private final ModelPart headskull;
	private final ModelPart left_ear;
	private final ModelPart right_ear;
	private final ModelPart front_left_leg;
	private final ModelPart wingleg;
	private final ModelPart front_right_leg;
	private final ModelPart wingleg2;
	private final ModelPart back_left_leg;
	private final ModelPart wingleg3;
	private final ModelPart back_right_leg;
	private final ModelPart wingleg4;

	public BanishingStampedeModel(ModelPart root) {
		this.object = root.getChild("object");
		this.body = this.object.getChild("body");
		this.tail = this.body.getChild("tail");
		this.neck = this.body.getChild("neck");
		this.mouth = this.neck.getChild("mouth");
		this.jaw = this.mouth.getChild("jaw");
		this.mane = this.neck.getChild("mane");
		this.head = this.neck.getChild("head");
		this.headskull = this.head.getChild("headskull");
		this.left_ear = this.head.getChild("left_ear");
		this.right_ear = this.head.getChild("right_ear");
		this.front_left_leg = this.object.getChild("front_left_leg");
		this.wingleg = this.front_left_leg.getChild("wingleg");
		this.front_right_leg = this.object.getChild("front_right_leg");
		this.wingleg2 = this.front_right_leg.getChild("wingleg2");
		this.back_left_leg = this.object.getChild("back_left_leg");
		this.wingleg3 = this.back_left_leg.getChild("wingleg3");
		this.back_right_leg = this.object.getChild("back_right_leg");
		this.wingleg4 = this.back_right_leg.getChild("wingleg4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition object = partdefinition.addOrReplaceChild("object", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = object.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 40).addBox(-5.0F, -8.0F, -17.0F, 10.0F, 10.0F, 22.0F, new CubeDeformation(0.05F))
		.texOffs(0, 0).addBox(-6.0F, -8.5F, -18.0F, 12.0F, 16.0F, 24.0F, new CubeDeformation(0.05F)), PartPose.offset(0.0F, -13.0F, 6.0F));

		PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(42, 72).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 14.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 5.0F));

		PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(72, 0).addBox(-2.05F, -11.0F, -4.0F, 4.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, -13.0F));

		PartDefinition mouth = neck.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(16, 89).addBox(-2.0F, -11.0F, -7.0F, 4.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -2.0F));

		PartDefinition jaw = mouth.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(0, 72).addBox(-4.0F, -2.0F, -11.0F, 8.0F, 4.0F, 13.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 2.0F));

		PartDefinition mane = neck.addOrReplaceChild("mane", CubeListBuilder.create().texOffs(34, 89).addBox(-1.0F, -11.0F, 5.01F, 2.0F, 16.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -2.01F));

		PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create().texOffs(72, 19).addBox(-3.0F, -11.0F, -2.0F, 6.0F, 5.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -5.0F, -2.0F));

		PartDefinition headskull = head.addOrReplaceChild("headskull", CubeListBuilder.create().texOffs(64, 40).addBox(-5.0F, -2.0F, -14.0F, 10.0F, 5.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(64, 61).addBox(-7.0F, -2.0F, 2.0F, 14.0F, 0.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.5F, 4.0F));

		PartDefinition left_ear = head.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(72, 31).addBox(0.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -0.01F));

		PartDefinition right_ear = head.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(72, 35).addBox(-2.55F, -13.0F, 4.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, -0.01F));

		PartDefinition front_left_leg = object.addOrReplaceChild("front_left_leg", CubeListBuilder.create().texOffs(56, 76).addBox(-3.0F, -1.0F, -1.9F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -10.0F, -9.0F));

		PartDefinition wingleg = front_left_leg.addOrReplaceChild("wingleg", CubeListBuilder.create(), PartPose.offset(1.0F, 3.0F, 0.0F));

		PartDefinition cube_r1 = wingleg.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -17.0F, 0.0F, 8.0F, 22.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition front_right_leg = object.addOrReplaceChild("front_right_leg", CubeListBuilder.create().texOffs(72, 76).addBox(-1.0F, -1.0F, -1.9F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -10.0F, -9.0F));

		PartDefinition wingleg2 = front_right_leg.addOrReplaceChild("wingleg2", CubeListBuilder.create(), PartPose.offset(-1.0F, 3.0F, 0.0F));

		PartDefinition cube_r2 = wingleg2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-7.0F, -17.0F, 0.0F, 8.0F, 22.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition back_left_leg = object.addOrReplaceChild("back_left_leg", CubeListBuilder.create().texOffs(88, 76).addBox(-3.0F, -1.0F, -1.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -10.0F, 8.0F));

		PartDefinition wingleg3 = back_left_leg.addOrReplaceChild("wingleg3", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-8.0F, -17.0F, 0.0F, 8.0F, 22.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-9.0F, 3.0F, 1.0F));

		PartDefinition back_right_leg = object.addOrReplaceChild("back_right_leg", CubeListBuilder.create().texOffs(0, 89).addBox(-1.0F, -1.0F, -1.0F, 4.0F, 11.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -10.0F, 8.0F));

		PartDefinition wingleg4 = back_right_leg.addOrReplaceChild("wingleg4", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -17.0F, 0.0F, 8.0F, 22.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(9.0F, 3.0F, 1.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

    @Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.parts().forEach(ModelPart::resetPose);

        this.object.xRot = (float) Mth.clamp(-entity.getDeltaMovement().y() * 45, -90, 90) * Mth.DEG_TO_RAD;

        this.neck.xRot = headPitch * Mth.DEG_TO_RAD;
        this.neck.yRot = netHeadYaw * Mth.DEG_TO_RAD;

        this.animateManager(entity, ageInTicks);
        this.animateWalk(BanishingStampedeAnimations.RUN, limbSwing, limbSwingAmount, 1.0F, 2F);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		object.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

    @Override
    public ModelPart root() {
        return this.object;
    }

    public List<ModelPart> parts() {
        return ObjectArrayList.of(object, body, tail, neck, mouth, jaw, mane, head, headskull, left_ear, right_ear, front_left_leg, wingleg, front_right_leg, wingleg2, back_left_leg, wingleg3, back_right_leg, wingleg4);
    }
}