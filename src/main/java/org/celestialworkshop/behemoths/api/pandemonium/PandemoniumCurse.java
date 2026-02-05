package org.celestialworkshop.behemoths.api.pandemonium;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;
import org.celestialworkshop.behemoths.misc.utils.ClientUtils;

import javax.annotation.Nullable;

public class PandemoniumCurse {

    private final @Nullable EntityType<?> entityType;

    public PandemoniumCurse(@Nullable EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public PandemoniumCurse() {
        this(null);
    }

    public Component getDisplayName() {
        return Component.translatable("pandemonium_curse." + this.getId().getNamespace() + "." + this.getId().getPath());
    }

    public Component getDescription() {
        return Component.translatable("pandemonium_curse." + this.getId().getNamespace() + "." + this.getId().getPath() + "_description");
    }

    public ResourceLocation getId() {
        return BMPandemoniumCurses.REGISTRY.get().getKey(this);
    }

    public LivingEntity tryCreateDisplayEntity(Level level, Player player) {
        if (level.isClientSide) {
            return ClientUtils.tryCreateDisplayEntity(level, player, this.entityType);
        }
        return null;
    }
}
