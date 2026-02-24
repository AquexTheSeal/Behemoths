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
import org.celestialworkshop.behemoths.client.animations.SkyCharydbisAnimations;
import org.celestialworkshop.behemoths.entities.SkyCharydbis;

import java.util.List;

public class SkyCharydbisModel<T extends SkyCharydbis> extends BMHierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(Behemoths.prefix("skycharydbismodel"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart bodyin;
	private final ModelPart finthingr2;
	private final ModelPart finthingl2;
	private final ModelPart bodyin2;
	private final ModelPart bodyin3;
	private final ModelPart finthingr4;
	private final ModelPart bodyin4;
	private final ModelPart finthingr5;
	private final ModelPart tail;
	private final ModelPart finthingr6;
	private final ModelPart finthingl5;
	private final ModelPart finthingl4;
	private final ModelPart finthingr3;
	private final ModelPart finthingl3;
	private final ModelPart wingr;
	private final ModelPart wingr2;
	private final ModelPart jawr;
	private final ModelPart stringr;
	private final ModelPart stringr2;
	private final ModelPart stringr3;
	private final ModelPart finthingr;
	private final ModelPart wingl;
	private final ModelPart wingl2;
	private final ModelPart jawl;
	private final ModelPart stringl;
	private final ModelPart stringl2;
	private final ModelPart stringl3;
	private final ModelPart finthingl;

	public SkyCharydbisModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.bodyin = this.body.getChild("bodyin");
		this.finthingr2 = this.bodyin.getChild("finthingr2");
		this.finthingl2 = this.bodyin.getChild("finthingl2");
		this.bodyin2 = this.bodyin.getChild("bodyin2");
		this.bodyin3 = this.bodyin2.getChild("bodyin3");
		this.finthingr4 = this.bodyin3.getChild("finthingr4");
		this.bodyin4 = this.bodyin3.getChild("bodyin4");
		this.finthingr5 = this.bodyin4.getChild("finthingr5");
		this.tail = this.bodyin4.getChild("tail");
		this.finthingr6 = this.tail.getChild("finthingr6");
		this.finthingl5 = this.bodyin4.getChild("finthingl5");
		this.finthingl4 = this.bodyin3.getChild("finthingl4");
		this.finthingr3 = this.bodyin2.getChild("finthingr3");
		this.finthingl3 = this.bodyin2.getChild("finthingl3");
		this.wingr = this.body.getChild("wingr");
		this.wingr2 = this.wingr.getChild("wingr2");
		this.jawr = this.body.getChild("jawr");
		this.stringr = this.body.getChild("stringr");
		this.stringr2 = this.stringr.getChild("stringr2");
		this.stringr3 = this.stringr2.getChild("stringr3");
		this.finthingr = this.body.getChild("finthingr");
		this.wingl = this.body.getChild("wingl");
		this.wingl2 = this.wingl.getChild("wingl2");
		this.jawl = this.body.getChild("jawl");
		this.stringl = this.body.getChild("stringl");
		this.stringl2 = this.stringl.getChild("stringl2");
		this.stringl3 = this.stringl2.getChild("stringl3");
		this.finthingl = this.body.getChild("finthingl");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, -16.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(500, 49).addBox(-23.0F, -24.0F, -5.0F, 24.0F, 24.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-13.7071F, 27.9203F, 3.0F, 0.0F, -0.7854F, 1.5708F));

		PartDefinition cube_r2 = body.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(560, 284).addBox(-27.0F, -28.0F, 22.0F, 24.0F, 24.0F, 13.0F, new CubeDeformation(0.0F))
		.texOffs(500, 0).addBox(-29.0F, -30.0F, 35.0F, 28.0F, 28.0F, 21.0F, new CubeDeformation(0.0F))
		.texOffs(416, 284).addBox(-31.0F, -38.0F, 56.0F, 32.0F, 44.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7071F, 14.9203F, -76.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition bodyin = body.addOrReplaceChild("bodyin", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 20.0F));

		PartDefinition cube_r3 = bodyin.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(500, 97).addBox(-19.0F, -20.0F, -5.0F, 20.0F, 20.0F, 20.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.7071F, 23.9203F, 23.0F, 0.0F, -0.7854F, 1.5708F));

		PartDefinition cube_r4 = bodyin.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(480, 492).addBox(-29.0F, -32.0F, -5.0F, 28.0F, 32.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7071F, 14.9203F, 5.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition finthingr2 = bodyin.addOrReplaceChild("finthingr2", CubeListBuilder.create().texOffs(536, 420).addBox(3.0F, -16.0F, -20.0F, 0.0F, 18.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, -12.0F, 20.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition finthingl2 = bodyin.addOrReplaceChild("finthingl2", CubeListBuilder.create().texOffs(536, 420).mirror().addBox(-3.0F, -16.0F, -20.0F, 0.0F, 18.0F, 40.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(14.0F, -12.0F, 20.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition bodyin2 = bodyin.addOrReplaceChild("bodyin2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 41.0F));

		PartDefinition cube_r5 = bodyin2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(224, 408).addBox(-25.0F, -26.0F, -1.0F, 20.0F, 20.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7071F, 14.9203F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition bodyin3 = bodyin2.addOrReplaceChild("bodyin3", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 64.0F));

		PartDefinition cube_r6 = bodyin3.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(420, 144).addBox(-21.0F, -22.0F, -5.0F, 12.0F, 12.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7071F, 14.9203F, 4.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition finthingr4 = bodyin3.addOrReplaceChild("finthingr4", CubeListBuilder.create().texOffs(352, 492).addBox(1.0F, -10.0F, -20.0F, 0.0F, 18.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -8.0F, 19.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition bodyin4 = bodyin3.addOrReplaceChild("bodyin4", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 64.0F));

		PartDefinition cube_r7 = bodyin4.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(392, 420).addBox(-19.0F, -20.0F, -5.0F, 8.0F, 8.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7071F, 14.9203F, 4.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition finthingr5 = bodyin4.addOrReplaceChild("finthingr5", CubeListBuilder.create().texOffs(0, 496).addBox(2.0F, -5.0F, -20.0F, 0.0F, 14.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -8.0F, 19.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition tail = bodyin4.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 64.0F));

		PartDefinition cube_r8 = tail.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 248).addBox(-15.0F, -84.0F, -5.0F, 0.0F, 136.0F, 112.0F, new CubeDeformation(0.0F))
		.texOffs(526, 368).addBox(-17.0F, -20.0F, -5.0F, 4.0F, 8.0F, 32.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.7071F, 14.9203F, 4.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition finthingr6 = tail.addOrReplaceChild("finthingr6", CubeListBuilder.create(), PartPose.offsetAndRotation(-7.0F, -8.0F, 19.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition finthingl5 = bodyin4.addOrReplaceChild("finthingl5", CubeListBuilder.create().texOffs(0, 496).mirror().addBox(-2.0F, -5.0F, -20.0F, 0.0F, 14.0F, 64.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.0F, -8.0F, 19.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition finthingl4 = bodyin3.addOrReplaceChild("finthingl4", CubeListBuilder.create().texOffs(352, 492).mirror().addBox(-1.0F, -10.0F, -20.0F, 0.0F, 18.0F, 64.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.0F, -8.0F, 19.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition finthingr3 = bodyin2.addOrReplaceChild("finthingr3", CubeListBuilder.create().texOffs(224, 492).addBox(3.0F, -16.0F, -30.0F, 0.0F, 18.0F, 64.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-7.0F, -8.0F, 29.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition finthingl3 = bodyin2.addOrReplaceChild("finthingl3", CubeListBuilder.create().texOffs(224, 492).mirror().addBox(-3.0F, -16.0F, -30.0F, 0.0F, 18.0F, 64.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(7.0F, -8.0F, 29.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition wingr = body.addOrReplaceChild("wingr", CubeListBuilder.create().texOffs(392, 408).addBox(-104.8F, -2.4F, -4.8F, 106.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-22.0F, 0.0F, -10.0F));

		PartDefinition cube_r9 = wingr.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 144).addBox(-104.8F, 0.6F, -4.8F, 106.0F, 0.0F, 104.0F, new CubeDeformation(0.0F))
		.texOffs(416, 272).addBox(-104.8F, -2.4F, -4.8F, 106.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-99.6F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r10 = wingr.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(416, 248).addBox(-104.8F, -2.4F, -4.8F, 106.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-99.6F, 0.0F, 0.0F, 0.0F, 2.5307F, 0.0F));

		PartDefinition wingr2 = wingr.addOrReplaceChild("wingr2", CubeListBuilder.create().texOffs(224, 396).addBox(-145.6F, -2.4F, -4.8F, 145.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-104.0F, 0.0F, 0.0F));

		PartDefinition cube_r11 = wingr2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-104.8F, -0.175F, -44.8F, 106.0F, 0.0F, 144.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-100.6F, 0.775F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition cube_r12 = wingr2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(416, 260).addBox(-104.8F, -2.4F, -4.8F, 106.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.4F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		PartDefinition jawr = body.addOrReplaceChild("jawr", CubeListBuilder.create(), PartPose.offset(-11.7071F, 0.9203F, -24.0F));

		PartDefinition cube_r13 = jawr.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(224, 248).addBox(-1.0F, -1.0F, -87.0F, 0.0F, 52.0F, 96.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.0F, -5.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition stringr = body.addOrReplaceChild("stringr", CubeListBuilder.create(), PartPose.offsetAndRotation(-22.0F, -16.0F, 0.0F, -1.0472F, 0.0F, -1.1345F));

		PartDefinition cube_r14 = stringr.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(208, 496).addBox(0.0F, -56.0F, -3.0F, 0.0F, 56.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition stringr2 = stringr.addOrReplaceChild("stringr2", CubeListBuilder.create(), PartPose.offset(0.0F, -56.0F, 0.0F));

		PartDefinition cube_r15 = stringr2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(208, 558).addBox(0.0F, -56.0F, -3.0F, 0.0F, 56.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition stringr3 = stringr2.addOrReplaceChild("stringr3", CubeListBuilder.create(), PartPose.offset(0.0F, -56.0F, 0.0F));

		PartDefinition cube_r16 = stringr3.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(560, 321).addBox(0.0F, -26.0F, -9.0F, 0.0F, 26.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(480, 564).addBox(0.0F, 0.0F, -3.0F, 0.0F, 56.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -56.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition finthingr = body.addOrReplaceChild("finthingr", CubeListBuilder.create().texOffs(128, 496).addBox(-1.0F, -30.0F, -20.0F, 0.0F, 30.0F, 40.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.0F, -14.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition wingl = body.addOrReplaceChild("wingl", CubeListBuilder.create().texOffs(392, 408).mirror().addBox(-1.2F, -2.4F, -4.8F, 106.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(22.0F, 0.0F, -10.0F));

		PartDefinition cube_r17 = wingl.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(0, 144).mirror().addBox(-1.2F, 0.6F, -4.8F, 106.0F, 0.0F, 104.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(416, 272).mirror().addBox(-1.2F, -2.4F, -4.8F, 106.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(99.6F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r18 = wingl.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(416, 248).mirror().addBox(-1.2F, -2.4F, -4.8F, 106.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(99.6F, 0.0F, 0.0F, 0.0F, -2.5307F, 0.0F));

		PartDefinition wingl2 = wingl.addOrReplaceChild("wingl2", CubeListBuilder.create().texOffs(224, 396).mirror().addBox(0.6F, -2.4F, -4.8F, 145.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(104.0F, 0.0F, 0.0F));

		PartDefinition cube_r19 = wingl2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(0, 0).mirror().addBox(-1.2F, 0.6F, -44.8F, 106.0F, 0.0F, 144.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(100.6F, 0.525F, 0.0F, 0.0F, -1.5708F, 0.0F));

		PartDefinition cube_r20 = wingl2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(416, 260).mirror().addBox(-1.2F, -2.4F, -4.8F, 106.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-4.4F, 0.0F, 0.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition jawl = body.addOrReplaceChild("jawl", CubeListBuilder.create(), PartPose.offset(12.7071F, -3.0797F, -28.0F));

		PartDefinition cube_r21 = jawl.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(224, 248).mirror().addBox(1.0F, -1.0F, -87.0F, 0.0F, 52.0F, 96.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.0F, 2.0F, -1.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition stringl = body.addOrReplaceChild("stringl", CubeListBuilder.create(), PartPose.offsetAndRotation(22.0F, -16.0F, 0.0F, -1.0472F, 0.0F, 1.1345F));

		PartDefinition cube_r22 = stringl.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(208, 496).mirror().addBox(0.0F, -56.0F, -3.0F, 0.0F, 56.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition stringl2 = stringl.addOrReplaceChild("stringl2", CubeListBuilder.create(), PartPose.offset(0.0F, -56.0F, 0.0F));

		PartDefinition cube_r23 = stringl2.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(208, 558).mirror().addBox(0.0F, -56.0F, -3.0F, 0.0F, 56.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition stringl3 = stringl2.addOrReplaceChild("stringl3", CubeListBuilder.create(), PartPose.offset(0.0F, -56.0F, 0.0F));

		PartDefinition cube_r24 = stringl3.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(560, 321).mirror().addBox(0.0F, -26.0F, -9.0F, 0.0F, 26.0F, 18.0F, new CubeDeformation(0.0F)).mirror(false)
		.texOffs(480, 564).mirror().addBox(0.0F, 0.0F, -3.0F, 0.0F, 56.0F, 6.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -56.0F, 0.0F, 0.0F, 1.5708F, 0.0F));

		PartDefinition finthingl = body.addOrReplaceChild("finthingl", CubeListBuilder.create().texOffs(128, 496).mirror().addBox(1.0F, -30.0F, -20.0F, 0.0F, 30.0F, 40.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(16.0F, -14.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 640, 640);
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.parts().forEach(ModelPart::resetPose);

		float partialTick = ageInTicks - entity.tickCount;

		float bodyYaw = Mth.lerp(partialTick, entity.yRotO, entity.getYRot());
		float tailYaw = Mth.lerp(partialTick, entity.tailYRotO, entity.tailYRot);

		float bodyPitch = Mth.lerp(partialTick, entity.xRotO, entity.getXRot());
		float tailPitch = Mth.lerp(partialTick, entity.tailXRotO, entity.tailXRot);

		this.bodyin2.yRot = this.bodyin3.yRot = this.bodyin4.yRot = Mth.wrapDegrees(tailYaw - bodyYaw) * Mth.DEG_TO_RAD;
		this.bodyin2.xRot = this.bodyin3.xRot = this.bodyin4.xRot = Mth.wrapDegrees(tailPitch - bodyPitch) * Mth.DEG_TO_RAD;

		this.stringr.xRot += this.stringr2.xRot = this.stringr3.xRot = -Mth.wrapDegrees(tailYaw - bodyYaw) * Mth.DEG_TO_RAD;
		this.stringr.zRot += this.stringr.yRot = this.stringr3.zRot = Mth.wrapDegrees(tailPitch - bodyPitch) * Mth.DEG_TO_RAD;
		this.stringl.xRot += this.stringl2.xRot = this.stringl3.xRot = Mth.wrapDegrees(tailYaw - bodyYaw) * Mth.DEG_TO_RAD;
		this.stringl.zRot += this.stringl.yRot = this.stringl3.zRot = -Mth.wrapDegrees(tailPitch - bodyPitch) * Mth.DEG_TO_RAD;


		this.body.zRot += Mth.clamp(Mth.wrapDegrees(tailYaw - bodyYaw) * 2, -45.5F, 45.5F) * Mth.DEG_TO_RAD;

		this.animateManager(entity, ageInTicks);
		if (entity.isSleeping()) {
			this.animateScaled(SkyCharydbisAnimations.SLEEP, ageInTicks, 1.0F, 1.0F);
		} else {
			this.animateWalk(SkyCharydbisAnimations.FLY, limbSwing, limbSwingAmount, 1.0F, 1.0F);
		}

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public List<ModelPart> parts() {
		return List.of(
				this.root,
				this.body,
				this.bodyin,
				this.finthingr2,
				this.finthingl2,
				this.bodyin2,
				this.bodyin3,
				this.finthingr4,
				this.bodyin4,
				this.finthingr5,
				this.tail,
				this.finthingr6,
				this.finthingl5,
				this.finthingl4,
				this.finthingr3,
				this.finthingl3,
				this.wingr,
				this.wingr2,
				this.jawr,
				this.stringr,
				this.stringr2,
				this.stringr3,
				this.finthingr,
				this.wingl,
				this.wingl2,
				this.jawl,
				this.stringl,
				this.stringl2,
				this.stringl3,
				this.finthingl
		);
	}
}