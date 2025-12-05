package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.ColossangrimScreen;
import org.celestialworkshop.behemoths.client.guis.screens.MobDataScreen;

import javax.annotation.Nullable;

public class ColossangrimMobEntryElement extends AnimatedUIElement<ColossangrimScreen> {

    private final Entity entity;
    @Nullable private final Item spawnEgg;
    private final Font font;
    private final Component displayName;

    public ColossangrimMobEntryElement(ColossangrimScreen screen, EntityType<?> type, @Nullable Item spawnEgg, Font font) {
        super(screen);
        this.entity = createEntity(type);
        this.spawnEgg = spawnEgg;
        this.font = font;
        this.displayName = entity != null ? entity.getDisplayName() : Component.literal("Unknown");
    }

    @Nullable
    private Entity createEntity(EntityType<?> type) {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return null;
        }
        return type.create(level);
    }

    @Override
    public int getWidth() {
        return ColossangrimScreen.ITEM_BASE + ColossangrimScreen.GAP_SIZE + font.width(displayName);
    }

    @Override
    public int getHeight() {
        return ColossangrimScreen.ITEM_BASE;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {

        int offsetFromItem = ColossangrimScreen.ITEM_BASE + ColossangrimScreen.GAP_SIZE;
        int textX = getRenderX() + offsetFromItem;
        int textY = getRenderY() + ColossangrimScreen.ITEM_BASE / 4;

        boolean hovered = isHovered(mouseX, mouseY);
        int color = hovered ? ColossangrimScreen.COLOR_HOVERED : ColossangrimScreen.COLOR_NORMAL;

        font.drawInBatch8xOutline(
                displayName.getVisualOrderText(),
                textX,
                textY,
                color,
                ColossangrimScreen.COLOR_BACKGROUND,
                guiGraphics.pose().last().pose(),
                guiGraphics.bufferSource(),
                LightTexture.FULL_BRIGHT
        );

        if (spawnEgg != null) {
            guiGraphics.renderItem(new ItemStack(spawnEgg), getRenderX(), getRenderY());
        }
    }

    @Override
    public void onClick() {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1f));

        Screen screen = new MobDataScreen(entity);
        Minecraft.getInstance().setScreen(screen);
    }
}