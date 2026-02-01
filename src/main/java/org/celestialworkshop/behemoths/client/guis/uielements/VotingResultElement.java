package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.VotingResultsScreen;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;
import org.joml.Quaternionf;

import javax.annotation.Nullable;

public class VotingResultElement extends AnimatedUIElement<VotingResultsScreen> {
    public PandemoniumCurse winnerCurse;
    public @Nullable LivingEntity entity;

    public VotingResultElement(VotingResultsScreen screen) {
        super(screen);
        this.winnerCurse = ClientPandemoniumData.localSelectableCurses.get(screen.voteResults[3]);
        entity = winnerCurse.tryCreateDisplayEntity(minecraft.level, minecraft.player);
    }

    @Override
    public int getWidth() {
        return 256;
    }

    @Override
    public int getHeight() {
        return 125;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(VotingResultsScreen.MAIN_TEXTURE, this.getRenderX() - this.getWidth() / 2, this.getRenderY() - this.getHeight() / 2, 0, 83, this.getWidth(), this.getHeight());

        entity.tick();
        float delta = entity.tickCount + partialTick;
        Quaternionf quaternionf = new Quaternionf().rotationXYZ(0.5F, 90.0F + delta * 0.025F, (float) Math.PI);
        InventoryScreen.renderEntityInInventory(guiGraphics, getRenderX() - 95, getRenderY() + 15, 30, quaternionf, null, entity);

        Component component = Component.translatable("screen.behemoths.voting_results.winner", winnerCurse.getDisplayName().copy().withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GOLD);
        minecraft.font.drawInBatch8xOutline(component.getVisualOrderText(),
                getRenderX() - 60, getRenderY() - 50,
                0xffffff, 0x000000,
                guiGraphics.pose().last().pose(), guiGraphics.bufferSource(),
                LightTexture.FULL_BRIGHT
        );

        guiGraphics.hLine(getRenderX() - 61, getRenderX() + 120, getRenderY() - 38, -1);

        guiGraphics.drawWordWrap(minecraft.font, winnerCurse.getDescription(), getRenderX() - 60, getRenderY() - 32, 180, 0xffffff);
    }

    @Override
    public void tick() {
        super.tick();
        if (entity != null) {
            entity.tickCount++;
            entity.tick();
        }
    }
}
