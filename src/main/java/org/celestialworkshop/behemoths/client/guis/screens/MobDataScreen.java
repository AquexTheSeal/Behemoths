package org.celestialworkshop.behemoths.client.guis.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.api.client.gui.SimpleUIScreen;
import org.celestialworkshop.behemoths.client.guis.uielements.MobDataEntityElement;

public class MobDataScreen extends SimpleUIScreen {

    public final Entity entity;

    public MobDataScreen(Entity entity) {
        super(Component.empty());
        this.entity = entity;
    }

    public void initUIElements() {
        MobDataEntityElement data = new MobDataEntityElement(this, entity);
        data.setPos(width / 2, height / 2);
        this.uiElements.add(data);
        data.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeIn().from(0, -100).build();

    }
}
