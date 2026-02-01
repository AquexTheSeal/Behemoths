package org.celestialworkshop.behemoths.client.guis.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.api.client.gui.SimpleUIScreen;
import org.celestialworkshop.behemoths.client.guis.uielements.CurseSelectionEntryElement;
import org.celestialworkshop.behemoths.client.guis.uielements.CurseSelectionTitleElement;
import org.celestialworkshop.behemoths.world.clientdata.ClientPandemoniumData;

import java.util.List;

public class PandemoniumCurseSelectionScreen extends SimpleUIScreen {
    public static final ResourceLocation MAIN_TEXTURE = Behemoths.prefix("textures/gui/pandemonium_curse_selection.png");

    private final List<PandemoniumCurse> selectableCurses;

    private CurseSelectionEntryElement entry;
    private CurseSelectionEntryElement entry1;
    private CurseSelectionEntryElement entry2;

    public PandemoniumCurseSelectionScreen(List<PandemoniumCurse> curses) {
        super(Component.empty());
        this.selectableCurses = curses;
    }

    public List<PandemoniumCurse> getSelectableCurses() {
        return selectableCurses;
    }

    @Override
    public void initUIElements() {

        int allEntriesYOff = 20;

        CurseSelectionTitleElement title = new CurseSelectionTitleElement(this);
        int yyT = ((height / 2) - title.getHeight()) + allEntriesYOff - 140;
        title.setPos(width/2, yyT);
        this.uiElements.add(title);
        title.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeIn().from(100, 0).build();

        entry = new CurseSelectionEntryElement(this, 0);
        int yy = ((height / 2) - entry.getHeight()) + allEntriesYOff - 2;
        entry.setPos(width/2, yy);
        this.uiElements.add(entry);
        entry.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeIn().from(100, 0).build();

        if (selectableCurses.size() >= 2) {
            entry1 = new CurseSelectionEntryElement(this, 1);
            int yy1 = height / 2 + allEntriesYOff;
            entry1.setPos(width / 2, yy1);
            this.uiElements.add(entry1);
            entry1.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeIn().from(120, 0).build();
        }

        if (selectableCurses.size() >= 3) {
            entry2 = new CurseSelectionEntryElement(this, 2);
            int yy2 = ((height / 2) + entry.getHeight()) + allEntriesYOff + 2;
            entry2.setPos(width / 2, yy2);
            this.uiElements.add(entry2);
            entry2.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeIn().from(140, 0).build();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if (ClientPandemoniumData.localRemainingTime == 0) {
            entry.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeOut().to(-300, 0).build();
            entry.button.animation = entry.animation;
            if (entry1 != null) {
                entry1.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeOut().to(-320, 0).build();
                entry1.button.animation = entry1.animation;
            }
            if (entry2 != null) {
                entry2.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeOut().to(-340, 0).build();
                entry2.button.animation = entry2.animation;
            }
        }
    }
}
