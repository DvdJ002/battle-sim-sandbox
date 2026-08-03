package david.games.battlesim.elements.data;

public enum StatusEffect {
    NONE("none"),
    KNOCKBACK ("knockback"), // Intensity: Knockback speed
    SLOWED("slowed"), // Intensity: Slow speed
    ICED("iced"), // Intensity: Dampening
    POISONED("poison"), // Intensity: Dmg per tick
    INVINCIBLE("invincible"), // Intensity: Suck speed
    DISARMED("disarmed"), // Intensity: /
    ROOTED("rooted"); // Intensity: /

    public final String label;

    StatusEffect(String label) {
        this.label = label;
    }
}
