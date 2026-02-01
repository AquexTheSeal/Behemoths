package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.api.item.specialty.ItemSpecialty;
import org.celestialworkshop.behemoths.registries.BMEntityTypes;
import org.celestialworkshop.behemoths.registries.BMItemSpecialties;
import org.celestialworkshop.behemoths.registries.BMItems;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;

public class BMLanguageProvider extends LanguageProvider {

    public BMLanguageProvider(PackOutput output, String locale) {
        super(output, Behemoths.MODID, locale);
    }

    @Override
    protected void addTranslations() {

        this.add("item_group.behemoths.behemoths", "Behemoths");

        this.add("key.categories.behemoths", "Behemoths");

        // PANDEMONIUM

        this.add("key.behemoths.open_voting_progress", "Open Voting Progress");

        this.add("screen.behemoths.colossangrim", "The Colosssangrim");
        this.add("screen.behemoths.curse_selection.title", "SELECT YOUR CURSE.");
        this.add("screen.behemoths.curse_selection.hover_button", "Vote for this modifier.");
        this.add("screen.behemoths.voting_results.votes", "%s Votes");
        this.add("screen.behemoths.voting_results.winner", "Winner: %s");

        this.add("overlay.behemoths.voting_progress", "Voting in progress. Press [%s] to open voting screen.");

        this.addPandemoniumCurse(BMPandemoniumCurses.PLAYER_DAMAGE_NERF.get(), "Weakening");

        this.addPandemoniumCurse(BMPandemoniumCurses.ZOMBIE_BABY_CHANCE.get(), "Hoarding");
        this.addPandemoniumCurse(BMPandemoniumCurses.ZOMBIE_REVIVAL.get(), "Reanimation");

        this.addPandemoniumCurse(BMPandemoniumCurses.ARCHZOMBIE_LEADER.get(), "Archzombie Dominion");
        this.addPandemoniumCurse(BMPandemoniumCurses.ARCHZOMBIE_SPEED.get(), "Gravebreaker Momentum");
        this.addPandemoniumCurse(BMPandemoniumCurses.ARCHZOMBIE_STAMPEDE_CHANCE.get(), "Phantom Steed");

        this.addPandemoniumCurseDescription(BMPandemoniumCurses.PLAYER_DAMAGE_NERF.get(), "Player damage is now decreased by 20% against all entities.");

        this.addPandemoniumCurseDescription(BMPandemoniumCurses.ZOMBIE_BABY_CHANCE.get(), "Baby zombie spawn chance will now significantly increase.");
        this.addPandemoniumCurseDescription(BMPandemoniumCurses.ZOMBIE_REVIVAL.get(), "Normal zombies will now gain a small chance to revive to full health upon death.");

        this.addPandemoniumCurseDescription(BMPandemoniumCurses.ARCHZOMBIE_LEADER.get(), "Archzombie leaders can now spawn along with 2-4 Archzombies surrounding them.");
        this.addPandemoniumCurseDescription(BMPandemoniumCurses.ARCHZOMBIE_SPEED.get(), "Banishing Stampede chasing speed: x1.3. Banishing Stampedes ridden by Archzombies can now also ram.");
        this.addPandemoniumCurseDescription(BMPandemoniumCurses.ARCHZOMBIE_STAMPEDE_CHANCE.get(), "Chance for an Archzombie to spawn with a Banishing Stampede: 5% -> 25%.");

        // TOOLTIP

        this.add("tooltip.behemoths.heart_energy_desc", "Can only be used as a crafting material when the Heart Energy is full, then all of it is consumed when used. Heart Energy slowly replenishes while in your inventory, but each Behemoth Heart you carry reduces the recovery speed. Defeating Behemoths restores a large amount of Heart Energy.");
        this.add("tooltip.behemoths.heart_energy_count", "Heart Energy: %s / %s");

        this.add("tooltip.behemoths.specialty", "Behemoth Weapon Specialties");

        this.addSpecialty(BMItemSpecialties.BEHEMOTH_DAMAGE_BONUS.get(), "Behemoth Damage Bonus");
        this.addSpecialtyDescription(BMItemSpecialties.BEHEMOTH_DAMAGE_BONUS.get(), "Deals %s damage against all behemoth entities.");

        // REGISTRIES

        for (RegistryObject<Item> item : BMItems.ITEMS.getEntries()) {
            this.addItem(item, WordUtils.capitalize(item.getId().getPath().replace("_", " ")));
        }

        for (RegistryObject<EntityType<?>> en : BMEntityTypes.ENTITY_TYPES.getEntries()) {
            this.addEntityType(en, WordUtils.capitalize(en.getId().getPath().replace("_", " ")));
        }

    }

    private void addSpecialty(ItemSpecialty specialty, String value) {
        this.add(specialty.getDisplayNameKey(), value);
    }

    private void addSpecialtyDescription(ItemSpecialty specialty, String value) {
        this.add(specialty.getDisplayDescriptionKey(), value);
    }

    private void addPandemoniumCurse(PandemoniumCurse curse, String value) {
        this.add(curse.getDisplayName().getString(), value);
    }

    private void addPandemoniumCurseDescription(PandemoniumCurse curse, String value) {
        this.add(curse.getDescription().getString(), value);
    }
}