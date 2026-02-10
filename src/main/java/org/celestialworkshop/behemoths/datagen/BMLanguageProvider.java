package org.celestialworkshop.behemoths.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.text.WordUtils;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.item.specialty.ItemSpecialty;
import org.celestialworkshop.behemoths.api.pandemonium.PandemoniumCurse;
import org.celestialworkshop.behemoths.registries.*;

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
        this.add("screen.behemoths.voting_results.votes", "%s Vote/s");
        this.add("screen.behemoths.voting_results.winner", "Winner: %s");

        this.add("overlay.behemoths.voting_progress", "Voting in progress. Press [%s] to open voting screen.");

        this.add("chat.behemoths.voting_result_header", "Pandemonium Voting is over!");
        this.add("chat.behemoths.voting_result_player_breakdown_header", "Player Voting Breakdown: ");
        this.add("chat.behemoths.voting_result_player_breakdown", "Player %s voted for %s.");
        this.add("chat.behemoths.voting_result_tally_breakdown_header", "Result Tally Breakdown: ");
        this.add("chat.behemoths.voting_result_tally_breakdown", "%s has gained %s votes in total.");
        this.add("chat.behemoths.voting_final_curse", "Selected Curse: %s");

        // PANDEMONIUM CURSE

        this.addPandemoniumCurse(BMPandemoniumCurses.FRAGILITY.get(), "Mortal Frailty", "Natural regeneration is slower and hunger depletes more quickly.");

        this.addPandemoniumCurse(BMPandemoniumCurses.RELENTLESS.get(), "Relentless", "Zombies move x1.5 faster at night and can detect players from a much greater distance.");
        this.addPandemoniumCurse(BMPandemoniumCurses.FERAL_HORDE.get(), "Feral Horde", "Zombies gain increased attack power when near other undead entities.");

        this.addPandemoniumCurse(BMPandemoniumCurses.ARCHZOMBIE_DOMINION.get(), "Archzombie Dominion", "Archzombie leaders can now spawn along with 2-4 Archzombies surrounding them.");
        this.addPandemoniumCurse(BMPandemoniumCurses.GRAVEBREAKER_MOMENTUM.get(), "Gravebreaker Momentum", "Banishing Stampede chasing speed: x1.3. Banishing Stampedes ridden by Archzombies can now also ram.");
        this.addPandemoniumCurse(BMPandemoniumCurses.PHANTOM_STEED.get(), "Phantom Steed", "Chance for an Archzombie to spawn with a Banishing Stampede: 5% -> 25%.");

        this.addPandemoniumCurse(BMPandemoniumCurses.OVERSEER.get(), "Overseer", "Hollowborne Turret projectile impact: x2. Hollowborne Turrets now gain a 30% damage reduction against any projectiles.");
        this.addPandemoniumCurse(BMPandemoniumCurses.DEATH_LEAP.get(), "Death Leap", "Hollowbornes can now leap towards the target and crush them when the turret is destroyed.");

        // TOOLTIP

        this.add("tooltip.behemoths.heart_energy_desc", "Can only be used as a crafting material when the Heart Energy is full, then all of it is consumed when used. Heart Energy slowly replenishes while in your inventory, but each Behemoth Heart you carry reduces the recovery speed. Defeating Behemoths restores a large amount of Heart Energy.");
        this.add("tooltip.behemoths.heart_energy_count", "Heart Energy: %s / %s");

        this.add("tooltip.behemoths.specialty", "Behemoth Weapon Specialties");

        this.addSpecialty(BMItemSpecialties.BEHEMOTH_DAMAGE_BONUS.get(), "Behemoth Damage Bonus");
        this.addSpecialtyDescription(BMItemSpecialties.BEHEMOTH_DAMAGE_BONUS.get(), "Deals %s damage against all behemoth entities.");

        // ADVANCEMENTS

        this.addAdvancement("kill_behemoth", "Bane of the Colossus", "Slay a Behemoth.");
        this.addAdvancement("obtain_behemoth_heart", "Essence of the Colossus", "Obtain a Behemoth's heart by slaying it.");
        this.addAdvancement("obtain_magnalyth_ingot", "An Abnormal Material", "Fill a Behemoth Heart's energy and use it to craft a Magnalyth ingot.");

        // REGISTRIES

        for (RegistryObject<Item> item : BMItems.ITEMS.getEntries()) {
            if (!(item.get() instanceof BlockItem)) {
                this.addItem(item, WordUtils.capitalize(item.getId().getPath().replace("_", " ")));
            }
        }

        for (RegistryObject<Block> block : BMBlocks.BLOCKS.getEntries()) {
            this.addBlock(block, WordUtils.capitalize(block.getId().getPath().replace("_", " ")));
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

    private void addPandemoniumCurse(PandemoniumCurse curse, String value, String desc) {
        this.add(curse.getDisplayName().getString(), value);
        this.add(curse.getDescription().getString(), desc);
    }

    private void addAdvancement(String name, String title, String desc) {
        this.add("advancement.behemoths." + name + ".title", title);
        this.add("advancement.behemoths." + name + ".description", desc);
    }
}