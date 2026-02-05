package org.celestialworkshop.behemoths.misc.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import org.celestialworkshop.behemoths.config.BMConfigManager;

public class PandemoniumMusicManager {
    private static PandemoniumMusicInstance currentMusic;

    public static void play(SoundEvent sound) {
        stop();

        if (BMConfigManager.CLIENT.enableVotingMusic.get()) {
            currentMusic = new PandemoniumMusicInstance(sound);
            Minecraft.getInstance().getSoundManager().play(currentMusic);
        }
    }

    public static void stop() {
        if (currentMusic != null) {
            currentMusic.forceStop();
            currentMusic = null;
        }
    }

    public static boolean isPlaying() {
        return currentMusic != null && !Minecraft.getInstance().getSoundManager().isActive(currentMusic);
    }
}
