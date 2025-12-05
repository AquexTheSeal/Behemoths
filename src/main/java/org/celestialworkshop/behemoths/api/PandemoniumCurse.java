package org.celestialworkshop.behemoths.api;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.celestialworkshop.behemoths.registries.BMPandemoniumCurses;

import javax.annotation.Nullable;

public class PandemoniumCurse {

    private final @Nullable EntityType<?> entityType;

    public PandemoniumCurse(@Nullable EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public PandemoniumCurse() {
        this.entityType = null;
    }

    public @Nullable EntityType<?> getEntityType() {
        return this.entityType;
    }

    public ResourceLocation getTexture() {
        return ResourceLocation.fromNamespaceAndPath(this.getId().getNamespace(), "textures/pandemonium_modifiers/" + this.getId().getPath());
    }

    public Component getDisplayName() {
        return Component.translatable("pandemonium_modifier." + this.getId().getNamespace() + "." + this.getId().getPath());
    }

    public Component getDescription() {
        return Component.translatable("pandemonium_modifier." + this.getId().getNamespace() + "." + this.getId().getPath() + "_description");
    }

    public ResourceLocation getId() {
        return BMPandemoniumCurses.REGISTRY.get().getKey(this);
    }

    public LivingEntity tryCreateDisplayEntity(Level level, Player player) {
        if (level.isClientSide) {
            LivingEntity entity;
            EntityType<?> entityType = this.getEntityType();
            if (entityType != null && entityType.create(level) instanceof LivingEntity living) {
                entity = living;
            } else {
                entity = new RemotePlayer((ClientLevel) level, player.getGameProfile()) {
                    @Override
                    public Component getDisplayName() {
                        return Component.empty();
                    }
                };
            }
            return entity;
        } else {
            throw new UnsupportedOperationException("Cannot create display entity on server");
        }
    }
}
