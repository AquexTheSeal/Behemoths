package org.celestialworkshop.behemoths.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.PlayerRideableJumping;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.entities.ai.BMCustomJumpableEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.objectweb.asm.Opcodes; // Make sure to import this

import javax.annotation.Nullable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends Player {

    @Shadow private float jumpRidingScale;
    @Shadow public Input input;
    @Final @Shadow protected Minecraft minecraft;

    public LocalPlayerMixin(Level pLevel, BlockPos pPos, float pYRot, GameProfile pGameProfile) {
        super(pLevel, pPos, pYRot, pGameProfile);
    }

    @Nullable
    private BMCustomJumpableEntity getActiveCustomJumpable() {
        LocalPlayer self = (LocalPlayer) (Object) this;
        if (self.getVehicle() instanceof PlayerRideableJumping jumpable && self.getVehicle() instanceof BMCustomJumpableEntity customJumpable && customJumpable.hasCustomJumpCalculation() && jumpable.getJumpCooldown() == 0) {
            return customJumpable;
        }
        return null;
    }

    @Redirect(method = "aiStep",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 2
            )
    )
    private void redirectJumpScaleHoldLogic1(LocalPlayer instance, float vanillaValue) {
        BMCustomJumpableEntity customJumpable = getActiveCustomJumpable();
        if (customJumpable != null) {
            this.jumpRidingScale = Mth.clamp(customJumpable.calculateCustomJumpScale(true), 0.0F, 1.0F);
        } else {
            this.jumpRidingScale = vanillaValue;
        }
    }

    @Redirect(method = "aiStep",
            at = @At(value = "FIELD",
                    target = "Lnet/minecraft/client/player/LocalPlayer;jumpRidingScale:F",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 3
            )
    )
    private void redirectJumpScaleHoldLogic2(LocalPlayer instance, float vanillaValue) {
        BMCustomJumpableEntity customJumpable = getActiveCustomJumpable();
        if (customJumpable != null) {
            this.jumpRidingScale = Mth.clamp(customJumpable.calculateCustomJumpScale(true), 0.0F, 1.0F);
        } else {
            this.jumpRidingScale = vanillaValue;
        }
    }

    @Inject(method = "aiStep", at = @At("TAIL"))
    private void updateAndResetCustomJumpState(CallbackInfo ci) {
        if (this.input == null || this.input.jumping) {
            return;
        }

        LocalPlayer self = (LocalPlayer) (Object) this;
        if (self.getVehicle() instanceof BMCustomJumpableEntity customJumpable &&
                customJumpable.hasCustomJumpCalculation()) {

            this.jumpRidingScale = Mth.clamp(customJumpable.calculateCustomJumpScale(false), 0.0F, 1.0F);
        }
    }
}