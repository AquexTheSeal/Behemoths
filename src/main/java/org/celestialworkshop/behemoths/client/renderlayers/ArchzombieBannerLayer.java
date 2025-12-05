package org.celestialworkshop.behemoths.client.renderlayers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.celestialworkshop.behemoths.client.models.ArchzombieModel;
import org.celestialworkshop.behemoths.entities.Archzombie;

public class ArchzombieBannerLayer extends RenderLayer<Archzombie, ArchzombieModel<Archzombie>> {

    public static ItemStack cachedBanner;

    public ArchzombieBannerLayer(RenderLayerParent<Archzombie, ArchzombieModel<Archzombie>> pRenderer) {
        super(pRenderer);
    }

    @Override
    public void render(PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, Archzombie pLivingEntity, float pLimbSwing, float pLimbSwingAmount, float pPartialTick, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {

        if (!pLivingEntity.isLeader()) {
            return;
        }

        if (cachedBanner == null) {
            ItemStack banner = new ItemStack(Items.BLACK_BANNER);
            CompoundTag compoundtag = new CompoundTag();
            ListTag listtag = (new BannerPattern.Builder())
                    .addPattern(BannerPatterns.GRADIENT, DyeColor.GREEN)
                    .addPattern(BannerPatterns.BORDER, DyeColor.WHITE)
                    .addPattern(BannerPatterns.SKULL, DyeColor.WHITE)
                    .toListTag();
            compoundtag.put("Patterns", listtag);
            BlockItem.setBlockEntityData(banner, BlockEntityType.BANNER, compoundtag);
            cachedBanner = banner;
        }

        pPoseStack.pushPose();
        this.getParentModel().root.translateAndRotate(pPoseStack);
        this.getParentModel().body.translateAndRotate(pPoseStack);

        pPoseStack.pushPose();
        pPoseStack.scale(4, 4, 4);
        pPoseStack.translate(0, -0.05, 0.1);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-15));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(180));
        Minecraft.getInstance().getItemRenderer().renderStatic(cachedBanner, ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, null, 0);
        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.scale(3F, 3F, 3F);
        pPoseStack.translate(-0.15, -0.025, 0.1);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-30));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(180 + 22.5F));
        Minecraft.getInstance().getItemRenderer().renderStatic(cachedBanner, ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, null, 0);
        pPoseStack.popPose();

        pPoseStack.pushPose();
        pPoseStack.scale(3F, 3F, 3F);
        pPoseStack.translate(0.15, -0.025, 0.1);
        pPoseStack.mulPose(Axis.XP.rotationDegrees(-30));
        pPoseStack.mulPose(Axis.YP.rotationDegrees(180));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(180 - 22.5F));
        Minecraft.getInstance().getItemRenderer().renderStatic(cachedBanner, ItemDisplayContext.GROUND, pPackedLight, OverlayTexture.NO_OVERLAY, pPoseStack, pBuffer, null, 0);
        pPoseStack.popPose();

        pPoseStack.popPose();
    }
}
