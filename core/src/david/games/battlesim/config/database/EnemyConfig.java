package david.games.battlesim.config.database;

import david.games.battlesim.config.EnemySteeringState;

public class EnemyConfig {
    public final float collideDamage;
    public final float baseDamage;
    public final float maxHealth;
    public final float size;
    // For gdx.ai behaviors
    public EnemySteeringState steeringState;

    public EnemyConfig(float collideDamage, float baseDamage, float maxHealth, float size) {
        this.collideDamage = collideDamage;
        this.baseDamage = baseDamage;
        this.maxHealth = maxHealth;
        this.size = size;
    }

    public static class KamikazeConfig extends EnemyConfig {
        public final float explosionRange, knockbackIntensity;
        public KamikazeConfig(float collideDamage, float damage, float maxHealth, float size, float explosionRange, float knockbackIntensity) {
            super(collideDamage, damage, maxHealth, size);
            this.explosionRange = explosionRange;
            this.knockbackIntensity = knockbackIntensity;
        }
    }

    public static class ShooterConfig extends EnemyConfig {
        public final float reloadDuration, bulletSpeed, bulletSize;
        public final float evadeRange;
        public final float speedEvade, speedChase;
        public ShooterConfig(float collideDamage, float damage, float maxHealth, float size, float reloadDuration, float evadeRange, float speedEvade, float speedChase, float bulletSpeed, float bulletSize) {
            super(collideDamage, damage, maxHealth, size);
            this.reloadDuration = reloadDuration;
            this.evadeRange = evadeRange;
            this.speedEvade = speedEvade;
            this.speedChase = speedChase;
            this.bulletSpeed = bulletSpeed;
            this.bulletSize = bulletSize;
        }
    }

    public static class SlasherConfig extends EnemyConfig {
        public final float dashCooldown, dashIntensity, detectionRadius;
        public final float slowIntensity, slowDuration;
        public SlasherConfig(float collideDamage, float damage, float maxHealth, float size, float dashCooldown, float dashIntensity, float slowIntensity, float slowDuration, float detectionRadius) {
            super(collideDamage, damage, maxHealth, size);
            this.dashCooldown = dashCooldown;
            this.dashIntensity = dashIntensity;
            this.slowIntensity = slowIntensity;
            this.slowDuration = slowDuration;
            this.detectionRadius = detectionRadius;
        }
    }

    public static class SuckerConfig extends EnemyConfig {
        public final float detectionRange, suckIntensity, forceFieldDuration, chaseSpeed;
        public SuckerConfig(float collideDamage, float damage, float maxHealth, float size, float detectionRange, float suckIntensity, float forceFieldDuration, float chaseSpeed) {
            super(collideDamage, damage, maxHealth, size);
            this.detectionRange = detectionRange;
            this.suckIntensity = suckIntensity;
            this.forceFieldDuration = forceFieldDuration;
            this.chaseSpeed = chaseSpeed;
        }
    }

    public static class HealerConfig extends EnemyConfig {
        public final float detectionRange, healAmount, roamSpeed, chaseSpeed, roamLocationPeriod, beamWidth;
        public HealerConfig(float collideDamage, float damage, float maxHealth, float size, float detectionRange, float healAmount, float roamSpeed, float chaseSpeed, float roamLocationPeriod, float beamWidth) {
            super(collideDamage, damage, maxHealth, size);
            this.detectionRange = detectionRange;
            this.healAmount = healAmount;
            this.roamSpeed = roamSpeed;
            this.chaseSpeed = chaseSpeed;
            this.roamLocationPeriod = roamLocationPeriod;
            this.beamWidth = beamWidth;
        }
    }

    public static class BossConfig extends EnemyConfig {
        public float baseSpeed, idleDuration, closeDetectionRange;
        // Attack: Bullet barrage
        public float bulletSpeed, bulletDamage, bulletSize, fireRate;
        // Attack: Slam/force field
        public float forceFieldDamage, forceFieldDuration, forceFieldSize;
        // Attack: Dash sequence
        public float dashIntensity, dashLastIntensity;
        public int dashCount;
        public float dashBaseCooldown, dashLastCooldown;
        // Attack: Kamikaze assault. Spawns 2 kamikaze in front of the boss
        public float kamikazeSpawnDistance;

        public BossConfig(
                float collideDamage, float damage, float maxHealth, float size, float baseSpeed, float idleDuration, float closeDetectionRange,
                float bulletSpeed, float bulletDamage, float bulletSize, float fireRate, float forceFieldDamage, float forceFieldDuration, float forceFieldSize,
                float dashIntensity, float dashLastIntensity, int dashCount, float dashBaseCooldown, float dashLastCooldown, float kamikazeSpawnDistance
        ) {
            super(collideDamage, damage, maxHealth, size);
            this.baseSpeed = baseSpeed;
            this.idleDuration = idleDuration;
            this.closeDetectionRange = closeDetectionRange;
            this.bulletSpeed = bulletSpeed;
            this.bulletDamage = bulletDamage;
            this.bulletSize = bulletSize;
            this.fireRate = fireRate;
            this.forceFieldDamage = forceFieldDamage;
            this.forceFieldDuration = forceFieldDuration;
            this.forceFieldSize = forceFieldSize;
            this.dashIntensity = dashIntensity;
            this.dashLastIntensity = dashLastIntensity;
            this.dashCount = dashCount;
            this.dashBaseCooldown = dashBaseCooldown;
            this.dashLastCooldown = dashLastCooldown;
            this.kamikazeSpawnDistance = kamikazeSpawnDistance;
        }
    }
}




