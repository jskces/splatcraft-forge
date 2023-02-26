package net.splatcraft.forge.items.weapons.settings;

public interface IDamageCalculator {
    float calculateDamage(int tickCount, boolean airborne, float charge, boolean isOnRollCooldown);
}