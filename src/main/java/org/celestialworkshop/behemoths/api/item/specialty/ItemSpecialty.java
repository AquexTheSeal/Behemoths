package org.celestialworkshop.behemoths.api.item.specialty;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.celestialworkshop.behemoths.registries.BMItemSpecialties;

import java.text.DecimalFormat;

public abstract class ItemSpecialty {

    public final Category category;

    public ItemSpecialty(Category category) {
        this.category = category;
    }

    public float onDamageMelee(LivingEntity attacker, LivingEntity target, ItemStack itemStack, float originalDamage, boolean isCritical, int specialityLevel) {
        return originalDamage;
    }

    public float onDamageSweep(LivingEntity attacker, LivingEntity sweepTarget, ItemStack itemStack, float sweepDamage, float originalDamage, int specialityLevel) {
        return sweepDamage;
    }

    public void onPostMelee(LivingEntity attacker, LivingEntity target, ItemStack itemStack, boolean wasCritical, int specialityLevel) {
    }

//    public float onDamageRanged(LivingEntity attacker, LivingEntity target, ItemStack itemStack, Projectile ammo, float originalDamage, boolean wasFullyCharged, int specialityLevel) {
//        return originalDamage;
//    }

    public Category getCategory() {
        return category;
    }

    public abstract String[] getDisplayVariables(int level);

    public static String asPercentFormat(float value) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(value * 100) + "%";
    }

    public ResourceLocation getTextureLocation() {
        return ResourceLocation.fromNamespaceAndPath(this.id().getNamespace(), "textures/item_specialties/" + this.id().getPath() + ".png");
    }

    public String getDisplayNameKey() {
        return String.format("item_specialty.%s.%s", this.id().getNamespace(), this.id().getPath());
    }

    public String getDisplayDescriptionKey() {
        return String.format("item_specialty.%s.%s.description", this.id().getNamespace(), this.id().getPath());
    }

    public Component getDisplayName(int level) {
        return Component
                .translatable(this.getDisplayNameKey())
                .withStyle(style -> style.withColor(this.getCategory().getColor()))
                .append(Component.literal(" "))
                .append(Component.translatable("enchantment.level." + (level + 1)));
    }

    public Component getDisplayDescription(int level) {
        String[] vars = this.getDisplayVariables(level);

        // Color code vars
        Component[] coloredVars = new Component[vars.length];
        for (int i = 0; i < vars.length; i++) {
            coloredVars[i] = Component.literal(String.valueOf(vars[i])).withStyle(ChatFormatting.YELLOW);
        }

        return Component.translatable(this.getDisplayDescriptionKey(), (Object[]) coloredVars);
    }

    public ResourceLocation id() {
        return BMItemSpecialties.REGISTRY.get().getKey(this);
    }

    public enum Category {
        BENEFICIAL(ChatFormatting.GREEN),
        HARMFUL(ChatFormatting.RED),
        NEUTRAL(ChatFormatting.GRAY)
        ;

        public final ChatFormatting color;

        Category(ChatFormatting color) {
            this.color = color;
        }

        public ChatFormatting getColor() {
            return color;
        }
    }
}
