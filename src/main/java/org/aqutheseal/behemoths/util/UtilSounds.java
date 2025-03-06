package org.aqutheseal.behemoths.util;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class UtilSounds {

    public static void playSoundFromServer(Level level, Vec3 pos, SoundEvent sound, float vol, float pitch) {
        if (level instanceof ServerLevel server) {
            server.playSound(null, pos.x(), pos.y(), pos.z(), sound, SoundSource.HOSTILE, vol, pitch);
        }
    }

    public static void playSoundFromServer(Level level, Entity source, SoundEvent sound, float vol, float pitch) {
        playSoundFromServer(level, source.position(), sound, vol, pitch);
    }

    public static void playSoundFromServer(Level level, Entity source, SoundEvent sound) {
        playSoundFromServer(level, source, sound, 1, 1);
    }

    public static void playSoundFromServer(Level level, Vec3 pos, SoundEvent sound) {
        playSoundFromServer(level, pos, sound, 1, 1);
    }
}
