package org.celestialworkshop.behemoths.client.guis.screens;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.api.client.gui.SimpleUIScreen;

import java.awt.*;
import java.util.List;

public abstract class BMVotingScreen extends SimpleUIScreen {

    protected final List<Dot> dots = new ObjectArrayList<>();

    protected BMVotingScreen(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
    }

    @Override
    public void tick() {
        dots.removeIf(Dot::shouldDie);

        for (Dot dot : dots) {
            dot.tick();
        }

        super.tick();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int fr = new Color(158, 81, 3, (int) (0.3f * 255)).getRGB();
        int to = new Color(26, 19, 0, (int) (0.8f * 255)).getRGB();
        guiGraphics.fillGradient(0, 0, this.width, this.height, fr, to);

        for (Dot dot : dots) {
            dot.render(guiGraphics, partialTick);
        }

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    protected static class Dot {

        public final int dur;
        public final int speed;
        public final float whiteFactor;
        public boolean isLarge;
        public final int x;
        public final int y;

        public int tick = 0;

        public Dot(int dur, int speed, float whiteFactor, boolean isLarge, int x, int y) {
            this.dur = dur;
            this.speed = speed;
            this.whiteFactor = whiteFactor;
            this.isLarge = isLarge;
            this.x = x;
            this.y = y;
        }

        public boolean shouldDie() {
            return tick >= dur;
        }

        public void tick() {
            tick++;
        }

        public void render(GuiGraphics guiGraphics, float partial) {
            float t = (tick + partial) / dur;
            t = Mth.clamp(t, 0.0F, 1.0F);

            float yy = y - (tick + partial) * speed;

            int alpha = 255 - (int)(InterpolationTypes.apply(InterpolationTypes.LINEAR, t) * 255);

            Color c = new Color(255, 214 + Mth.floor(whiteFactor * 41), 161 + Mth.floor(whiteFactor * 94), alpha);

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, yy, 0);
            guiGraphics.fill(0, 0, isLarge ? 2 : 1, isLarge ? 2 : 1, c.getRGB());
            guiGraphics.pose().popPose();
        }
    }
}
