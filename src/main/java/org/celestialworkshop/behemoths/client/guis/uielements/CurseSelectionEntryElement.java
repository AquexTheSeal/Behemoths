package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.LivingEntity;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.PandemoniumCurseSelectionScreen;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;
import org.joml.Quaternionf;

import javax.annotation.Nullable;

public class CurseSelectionEntryElement extends AnimatedUIElement<PandemoniumCurseSelectionScreen> {

    public CurseSelectionButtonElement button;

    public final int slotIndex;
    public final PandemoniumCurse curse;
    public @Nullable LivingEntity entity;

    public CurseSelectionEntryElement(PandemoniumCurseSelectionScreen screen, int slotIndex) {
        super(screen);
        this.slotIndex = slotIndex;
        this.curse = screen.getSelectableCurses().get(slotIndex);
        this.entity = curse.tryCreateDisplayEntity(minecraft.level, minecraft.player);
    }

    @Override
    public int getWidth() {
        return 256;
    }

    @Override
    public int getHeight() {
        return 80;
    }

    @Override
    public void postInit() {
        button = new CurseSelectionButtonElement(screen, slotIndex);
        button.setPos(getRenderX() + 100 - button.getWidth() / 2, getRenderY() - button.getHeight() / 2);
        screen.uiElements.add(button);

        button.animation = this.animation;
    }

    @Override
    public void tick() {
        super.tick();
        if (entity != null) {
            entity.tickCount++;
            entity.tick();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int blitY = 0;
        if (slotIndex == ClientPandemoniumData.localSelectedIndex) {
            blitY = 80;
        }
        guiGraphics.blit(PandemoniumCurseSelectionScreen.MAIN_TEXTURE, getRenderX() - getWidth()/2, getRenderY() - getHeight()/2, 0, blitY, getWidth(), getHeight());

        float delta = entity.tickCount + partialTick;
        Quaternionf quaternionf = new Quaternionf().rotationXYZ(0.5F, 90.0F + delta * 0.05F, (float) Math.PI);
        InventoryScreen.renderEntityInInventory(guiGraphics, getRenderX() - 100, getRenderY() + 20, 20, quaternionf, null, entity);

        Font font = minecraft.font;
        font.drawInBatch8xOutline(
                curse.getDisplayName().getVisualOrderText(),
                getRenderX() - 105,
                getRenderY() - 32,
                0xffffff,
                0x000000,
                guiGraphics.pose().last().pose(),
                guiGraphics.bufferSource(),
                LightTexture.FULL_BRIGHT
        );

        guiGraphics.hLine(getRenderX() - 105, getRenderX() + 105, getRenderY() - 22, -1);

        this.drawShadowedWordWrap(guiGraphics, font, curse.getDescription(), getRenderX() - 72, getRenderY() - 18, 160, -1);
    }
}
