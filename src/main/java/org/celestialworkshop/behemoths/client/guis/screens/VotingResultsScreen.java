package org.celestialworkshop.behemoths.client.guis.screens;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.celestialworkshop.behemoths.Behemoths;
import org.celestialworkshop.behemoths.api.client.animation.InterpolationTypes;
import org.celestialworkshop.behemoths.api.client.gui.AnimatedUIElement;
import org.celestialworkshop.behemoths.api.client.gui.SimpleUIScreen;
import org.celestialworkshop.behemoths.client.guis.uielements.VotingCandidateElement;
import org.celestialworkshop.behemoths.client.guis.uielements.VotingResultElement;

public class VotingResultsScreen extends SimpleUIScreen {
    public static final ResourceLocation MAIN_TEXTURE = Behemoths.prefix("textures/gui/voting_results.png");
    public int screenTick = 0;

    public final int[] voteResults;

    public VotingResultElement result;
    public VotingCandidateElement candid;
    public VotingCandidateElement candid1;
    public VotingCandidateElement candid2;

    public VotingResultsScreen(int[] voteResults) {
        super(Component.empty());
        this.voteResults = voteResults;
    }

    @Override
    public void initUIElements() {
        screenTick = 0;

        candid = new VotingCandidateElement(this, 0);
        candid.setPos((this.width / 2) - (candid.getWidth()) - 4, this.height / 2);
        this.uiElements.add(candid);
        candid.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).fadeIn().from(0, -100).build();

        if (voteResults[1] != -1) {
            candid1 = new VotingCandidateElement(this, 1);
            candid1.setPos(this.width / 2, this.height / 2);
            this.uiElements.add(candid1);
            candid1.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(30).fadeIn().from(0, -100).build();
        }

        if (voteResults[2] != -1) {
            candid2 = new VotingCandidateElement(this, 2);
            candid2.setPos((this.width / 2) + (candid2.getWidth()) + 4, this.height / 2);
            this.uiElements.add(candid2);
            candid2.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(40).fadeIn().from(0, -100).build();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        screenTick++;
        if (screenTick == 40) {
            candid.setPos(candid.getRenderX(), candid.getRenderY() - 80);
            candid.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).from(0, 80).build();

            if (candid1 != null) {
                candid1.setPos(candid1.getRenderX(), candid1.getRenderY() - 80);
                candid1.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(30).from(0, 80).build();
            }

            if (candid2 != null) {
                candid2.setPos(candid2.getRenderX(), candid2.getRenderY() - 80);
                candid2.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(40).from(0, 80).build();
            }

            result = new VotingResultElement(this);
            result.setPos(this.width / 2, (this.height / 2) + 50);
            this.uiElements.add(result);
            result.animation = new AnimatedUIElement.AnimationState.Builder().type(InterpolationTypes.EASE_OUT_CUBIC).duration(20).delay(20).fadeIn().from(0, 200).build();
        }
    }
}
