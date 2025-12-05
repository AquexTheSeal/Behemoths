package org.celestialworkshop.behemoths.client.guis.uielements;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.client.guis.screens.ColossangrimScreen;
import org.celestialworkshop.behemoths.client.guis.screens.MobDataScreen;
import org.joml.Quaternionf;

public class MobDataEntityElement extends AnimatedUIElement<MobDataScreen> {

    public static final Quaternionf ARMOR_STAND_ANGLE = (new Quaternionf()).rotationXYZ(0.5F, 90.0F, (float)Math.PI);

    public final Entity entity;

    public MobDataEntityElement(MobDataScreen screen, Entity entity) {
        super(screen);
        this.entity = entity;
        this.entity.tickCount = 0;
    }

    @Override
    public int getWidth() {
        return 80;
    }

    @Override
    public int getHeight() {
        return 82;
    }

    @Override
    public void tick() {
        super.tick();
        if (entity instanceof LivingEntity living) {
            living.tickCount++;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (entity instanceof LivingEntity living) {

            guiGraphics.blit(ColossangrimScreen.MAIN_TEXTURE, getRenderX() - getWidth()/2, getRenderY() - getHeight()/2, 176, 0, getWidth(), getHeight());

            living.tick();
            float delta = living.tickCount + partialTick;
            living.yHeadRot = delta;
            living.yHeadRotO = delta;
            living.yBodyRot = delta;
            living.setYRot(delta);
            InventoryScreen.renderEntityInInventory(guiGraphics, getRenderX(), getRenderY() + 30, 30, ARMOR_STAND_ANGLE, null, living);
        }
    }
}
