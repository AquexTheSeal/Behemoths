package org.celestialworkshop.behemoths.items.specialties;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import org.celestialworkshop.behemoths.api.item.specialty.ItemSpecialty;

public class UndeadDamageBonusSpecialty extends ItemSpecialty {

    public UndeadDamageBonusSpecialty() {
        super(Category.BENEFICIAL);
    }

    @Override
    public float onDamageMelee(LivingEntity attacker, LivingEntity target, ItemStack itemStack, float originalDamage, boolean isCritical, int specialityLevel) {
       if (target.getMobType() == MobType.UNDEAD) {
           return originalDamage * calculatePower(specialityLevel);
       }
       return originalDamage;
    }

    @Override
    public String[] getDisplayVariables(int level) {
        return new String[]{
                ItemSpecialty.asPercentFormat(calculatePower(level))
        };
    }

    private static float calculatePower(int specialityLevel) {
        return 1.0F + ((specialityLevel + 1) * 0.3F);
    }
}
