package org.aqutheseal.behemoths.client.model.anims;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 4.12.3
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 * @author AquTheSeal
 */
public class SkyCharydbisAnimations {
	public static final AnimationDefinition SHAKE = AnimationDefinition.Builder.withLength(2.0F).looping()
		.addAnimation("body", new AnimationChannel(AnimationChannel.Targets.ROTATION,
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, 2.6872F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bodyin", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -9.6322F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("bodyin2", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(0.0F, 0.0F, -2.6872F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("jawr", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.765F, -7.765F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.addAnimation("jawl", new AnimationChannel(AnimationChannel.Targets.ROTATION, 
			new Keyframe(0.0F, KeyframeAnimations.degreeVec(-7.765F, 7.765F, 0.0F), AnimationChannel.Interpolations.LINEAR)
		))
		.build();
}