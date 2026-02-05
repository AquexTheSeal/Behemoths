package org.celestialworkshop.behemoths.misc.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class PandemoniumMusicInstance extends AbstractTickableSoundInstance {

    private boolean shouldStop = false;
    private final float maxVolume;


    public PandemoniumMusicInstance(SoundEvent sound, float volume, float pitch) {
        super(sound, SoundSource.MUSIC, SoundInstance.createUnseededRandom());
        this.looping = true;
        this.delay = 0;
        this.maxVolume = volume;
        this.volume = volume;
        this.pitch = pitch;
        this.relative = true;
    }

    public PandemoniumMusicInstance(SoundEvent sound, float pitch) {
        this(sound, 1.0F, pitch);
    }

    public PandemoniumMusicInstance(SoundEvent sound) {
        this(sound, 1.0F, 1.0F);
    }

    @Override
    public void tick() {
        if (this.shouldStop) {
            this.stop();
        }

        if (Minecraft.getInstance().screen == null) {
            this.volume = maxVolume * 0.3F;
        } else {
            this.volume = maxVolume;
        }
    }

    public void forceStop() {
        this.shouldStop = true;
    }
}
