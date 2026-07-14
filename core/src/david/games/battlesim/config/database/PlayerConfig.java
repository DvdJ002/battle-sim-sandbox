package david.games.battlesim.config.database;

public class PlayerConfig {
    public float baseSpeed, speedDampening;
    public float maxHealth, maxShieldHealth;
    public float phaseDuration, phaseCooldown;
    public float shieldRechargeDuration;
    public float bulletSpeed, bulletDamage, bulletSize;
    public float forceFieldDamage, forceFieldDuration, forceFieldSize, forceFieldCooldown;

    public PlayerConfig(
            float speed,
            float speedDampening,
            float maxHealth,
            float maxShieldHealth,
            float phaseDuration,
            float phaseCooldown,
            float shieldRechargeDuration,
            float bulletSpeed,
            float bulletDamage,
            float bulletSize,
            float forceFieldDamage,
            float forceFieldDuration,
            float forceFieldSize,
            float forceFieldCooldown
    ) {
        this.baseSpeed = speed;
        this.speedDampening = speedDampening;
        this.maxHealth = maxHealth;
        this.maxShieldHealth = maxShieldHealth;
        this.phaseDuration = phaseDuration;
        this.phaseCooldown = phaseCooldown;
        this.shieldRechargeDuration = shieldRechargeDuration;
        this.bulletSpeed = bulletSpeed;
        this.bulletDamage = bulletDamage;
        this.bulletSize = bulletSize;
        this.forceFieldDamage = forceFieldDamage;
        this.forceFieldDuration = forceFieldDuration;
        this.forceFieldSize = forceFieldSize;
        this.forceFieldCooldown = forceFieldCooldown;

    }
}
