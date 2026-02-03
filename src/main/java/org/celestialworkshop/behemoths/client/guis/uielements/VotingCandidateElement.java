package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.VotingResultsScreen;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;
import org.joml.Quaternionf;

import javax.annotation.Nullable;

public class VotingCandidateElement extends AnimatedUIElement<VotingResultsScreen> {

    public final int index;
    public PandemoniumCurse curse;
    public @Nullable LivingEntity entity;
    public int pulse = 0;

    public VotingCandidateElement(VotingResultsScreen screen, int index) {
        super(screen);
        this.index = index;
        this.curse = ClientPandemoniumData.localSelectableCurses.get(index);
        this.entity = curse.tryCreateDisplayEntity(minecraft.level, minecraft.player);
    }

    @Override
    public int getWidth() {
        return 72;
    }

    @Override
    public int getHeight() {
        return 67;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int uOff = 0;
        if (ClientPandemoniumData.localSelectedIndex == index && pulse >= 5) {
            uOff = getWidth();
        }
        guiGraphics.blit(VotingResultsScreen.MAIN_TEXTURE, this.getRenderX() - this.getWidth() / 2, this.getRenderY() - this.getHeight() / 2, uOff, 0, this.getWidth(), this.getHeight());

        if (this.entity != null) {
            entity.tick();
            float delta = entity.tickCount + partialTick;
            Quaternionf quaternionf = new Quaternionf().rotationXYZ(0.5F, 90.0F + delta * 0.025F, (float) Math.PI);
            InventoryScreen.renderEntityInInventory(guiGraphics, getRenderX(), getRenderY() + 15, 25, quaternionf, null, entity);
        }

        Font font = minecraft.font;
        int maxLineWidth = 75;
        int titleHeight = this.getRenderY() - (this.getHeight() / 2) - 10 - (font.lineHeight * (font.split(this.curse.getDisplayName(), maxLineWidth).size() - 1));
        this.drawCenteredOutlinedWordWrap(guiGraphics, font, this.curse.getDisplayName(), this.getRenderX(), titleHeight, maxLineWidth, 0xffd6a1, 0x4f1714);

        guiGraphics.drawCenteredString(font, Component.translatable("screen.behemoths.voting_results.votes", screen.voteResults[index]), this.getRenderX(), this.getRenderY() + (this.getHeight() / 2) + 4, 0xffd6a1);
    }

    @Override
    public void tick() {
        super.tick();
        if (entity != null) {
            entity.tickCount++;
            entity.tick();
        }
        if (pulse++ > 10) pulse = 0;

    }
}
