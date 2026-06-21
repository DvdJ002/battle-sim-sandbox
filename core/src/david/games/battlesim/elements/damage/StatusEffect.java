package david.games.battlesim.elements.damage;

public enum StatusEffect {
    NONE("none"),
    KNOCKBACK ("knockback"), // Intensity: Knockback speed
    PROJECTILE ("projectile"), // Intensity:
    SLOWED("slowed"), // Intensity: Slow speed
    ICED("iced"), // Intensity: Dampening
    POISONED("poison"), // Intensity: Dmg per tick
    SUCKED("suck"), // Intensity: Suck speed
    INVINCIBLE("invincible"); // Intensity: Suck speed

    public final String label;

    StatusEffect(String label) {
        this.label = label;
    }
}
