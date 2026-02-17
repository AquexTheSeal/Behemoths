package org.celestialworkshop.behemoths.client.guis.screens;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.api.client.gui.SimpleUIScreen;
import org.celestialworkshop.behemoths.client.guis.uielements.ColossangrimArrowsElement;
import org.celestialworkshop.behemoths.client.guis.uielements.ColossangrimBGElement;
import org.celestialworkshop.behemoths.client.guis.uielements.ColossangrimMobEntryElement;
import org.celestialworkshop.behemoths.client.guis.uielements.ColossangrimTitleElement;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;

import java.util.HashMap;
import java.util.Map;

public class ColossangrimScreen extends SimpleUIScreen {
    public static final ResourceLocation MAIN_TEXTURE = Behemoths.prefix("textures/gui/colossangrim.png");

    public static final int COLOR_NORMAL = 0xffdfab;
    public static final int COLOR_HOVERED = 0xFFFF00;
    public static final int COLOR_BACKGROUND = 0x523d2a;

    public static final int ITEMS_PER_PAGE = 8;
    public static final int ITEM_BASE = 16;
    public static final int GAP_SIZE = 2;

    public final Object2ObjectArrayMap<EntityType<?>, Item> spawnEggAssociationMap = new Object2ObjectArrayMap<>();

    public int pageNumber;

    public ColossangrimScreen() {
        super(Component.translatable("screen.behemoths.colossangrim"));
        this.collectMobEntries();
    }

    private void collectMobEntries() {
        Map<EntityType<?>, Item> eggLookup = new HashMap<>();

        ForgeRegistries.ITEMS.getEntries().stream().map(Map.Entry::getValue).forEach(i -> {
            if (i instanceof SpawnEggItem egg) {
                EntityType<?> t = egg.getType(null);
                eggLookup.put(t, i);
            }
        });

        for (RegistryObject<EntityType<?>> type : BMEntityTypes.ENTITY_TYPES.getEntries()) {
            EntityType<?> t = type.get();
            if (Minecraft.getInstance().level != null && t.create(Minecraft.getInstance().level) instanceof Mob) {
                Item egg = eggLookup.getOrDefault(t, null);
                spawnEggAssociationMap.put(t, egg);
            }
        }
    }

    public void initUIElements() {
        // Title
        ColossangrimTitleElement title = new ColossangrimTitleElement(this, font);
        title.setPos((width / 2) - (title.getWidth() / 2), (height / 2) - 100);
        this.uiElements.add(title);

        title.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_QUART).duration(30).fadeIn().from(0, -70).build();

        // Background
        ColossangrimBGElement background = new ColossangrimBGElement(this);

        int targetX = width / 2 - background.getWidth() / 2;
        int targetY = height / 2 - background.getHeight() / 2;

        background.setPos(targetX, targetY);
        this.uiElements.add(background);

        background.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeIn().from(0, -100).build();

        // Arrows
        ColossangrimArrowsElement leftArrow = new ColossangrimArrowsElement(this, true);
        ColossangrimArrowsElement rightArrow = new ColossangrimArrowsElement(this, false);

        int laX = (width / 2) - (background.getWidth() / 2) - leftArrow.getWidth() + 6;
        int laY = (height / 2) - (leftArrow.getHeight() / 2);

        int raX = (width / 2) + (background.getWidth() / 2) - 6;
        int raY = (height / 2) - (rightArrow.getHeight() / 2);

        leftArrow.setPos(laX, laY);
        rightArrow.setPos(raX, raY);

        this.uiElements.add(leftArrow);
        this.uiElements.add(rightArrow);

        leftArrow.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeIn().from(-16, 0).build();
        rightArrow.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeIn().from(16, 0).build();

        this.refreshMobEntries();
    }

    public void refreshMobEntries() {
        uiElements.removeIf(e -> e instanceof ColossangrimMobEntryElement);

        int startIndex = pageNumber * ITEMS_PER_PAGE;
        int endIndex = startIndex + ITEMS_PER_PAGE;

        int idx = 0;
        for (EntityType<?> type : spawnEggAssociationMap.keySet()) {
            if (idx < startIndex) {
                idx++;
                continue;
            }
            if (idx >= endIndex) break;

            Item spawnEgg = spawnEggAssociationMap.get(type);
            ColossangrimMobEntryElement entry = new ColossangrimMobEntryElement(this, type, spawnEgg, font);

            int yOffBasedOnIndex = (idx - startIndex) * (ITEM_BASE + GAP_SIZE);
            int entryTargetX = (width / 2) - 60;
            int entryTargetY = (height / 2) + yOffBasedOnIndex - 75;

            entry.setPos(entryTargetX, entryTargetY);
            uiElements.add(entry);

            entry.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).alpha(0, 1).delay((idx - startIndex) * 2).from(-20, 0).build();

            idx++;
        }
    }

    public int getMaxPages() {
        return (spawnEggAssociationMap.size() / ITEMS_PER_PAGE);
    }
}