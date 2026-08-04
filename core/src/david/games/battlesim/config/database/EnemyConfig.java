package david.games.battlesim.config.database;

import java.util.List;

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
        // General (phase 1 and 2)
        public float baseSpeed, baseIdleDuration, closeDetectionRange;
        public float enragedSpeed, enragedAcceleration, enragedIdleDuration, enragedHealthThreshold;

        // Attack: Bullet barrage
        public float bulletSpeed, bulletDamage, bulletSize, bulletFireRate, bulletAttackDuration;
        // Attack: Slam/force field
        public float forceFieldDamage, forceFieldDuration, forceFieldSize;
        // Attack: Dash sequence
        public float dashIntensity, dashLastIntensity;
        public int dashCount;
        public float dashBaseCooldown, dashLastCooldown;
        // Attack: Summon offensives/healers
        public float enemySpawnDistance, maxEnemiesSpawned;
        // Attack: Explosion
        public float explosionGraceDuration, explosionSeekSpeed, explosionSeekAccel, explosionSeekDuration, explosionKbIntensity, explosionDetectionRange, explosionDamage;
        // Attack: Cannon balls
        public float cannonDuration, cannonFireRate, cannonDamage, cannonSpeed, cannonSize;
        // Attack: Summon kamikaze
        public float kamikazeAttackDuration, kamikazeSpawnPeriod;
        // Attack: Pursuit
        public float pursuitDuration, pursuitSpeed, pursuitAccel, pursuitCollideDamage;
        // Attack pools
        public List<Integer> closeRangeAttackPool, longRangeAttackPool, enragedAttackPool;

        public BossConfig(
                float collideDamage, float damage, float maxHealth, float size, float baseSpeed, float baseIdleDuration, float closeDetectionRange,
                float enragedSpeed, float enragedAcceleration, float enragedIdleDuration, float enragedHealthThreshold,
                float bulletSpeed, float bulletDamage, float bulletSize, float bulletAttackDuration, float bulletFireRate, float forceFieldDamage, float forceFieldDuration, float forceFieldSize,
                float dashIntensity, float dashLastIntensity, int dashCount, float dashBaseCooldown, float dashLastCooldown, float enemySpawnDistance, float maxEnemiesSpawned,
                float explosionGraceDuration, float explosionSeekSpeed, float explosionSeekAccel, float explosionSeekDuration, float explosionKbIntensity, float explosionDetectionRange, float explosionDamage,
                float cannonDuration, float cannonFireRate, float cannonDamage, float cannonSpeed, float cannonSize, float kamikazeAttackDuration, float kamikazeSpawnPeriod,
                float pursuitDuration, float pursuitSpeed, float pursuitAccel, float pursuitCollideDamage,
                List<Integer> closeRangeAttackPool, List<Integer> longRangeAttackPool, List<Integer> enragedAttackPool
        ) {
            super(collideDamage, damage, maxHealth, size);
            this.baseSpeed = baseSpeed;
            this.baseIdleDuration = baseIdleDuration;
            this.closeDetectionRange = closeDetectionRange;
            this.enragedSpeed = enragedSpeed;
            this.enragedAcceleration = enragedAcceleration;
            this.enragedIdleDuration = enragedIdleDuration;
            this.enragedHealthThreshold = enragedHealthThreshold;
            this.bulletSpeed = bulletSpeed;
            this.bulletDamage = bulletDamage;
            this.bulletSize = bulletSize;
            this.bulletAttackDuration = bulletAttackDuration;
            this.bulletFireRate = bulletFireRate;
            this.forceFieldDamage = forceFieldDamage;
            this.forceFieldDuration = forceFieldDuration;
            this.forceFieldSize = forceFieldSize;
            this.dashIntensity = dashIntensity;
            this.dashLastIntensity = dashLastIntensity;
            this.dashCount = dashCount;
            this.dashBaseCooldown = dashBaseCooldown;
            this.dashLastCooldown = dashLastCooldown;
            this.maxEnemiesSpawned = maxEnemiesSpawned;
            this.enemySpawnDistance = enemySpawnDistance;
            this.explosionGraceDuration = explosionGraceDuration;
            this.explosionSeekSpeed = explosionSeekSpeed;
            this.explosionSeekAccel = explosionSeekAccel;
            this.explosionSeekDuration = explosionSeekDuration;
            this.explosionKbIntensity = explosionKbIntensity;
            this.explosionDetectionRange = explosionDetectionRange;
            this.explosionDamage = explosionDamage;
            this.cannonDuration = cannonDuration;
            this.cannonFireRate = cannonFireRate;
            this.cannonDamage = cannonDamage;
            this.kamikazeAttackDuration = kamikazeAttackDuration;
            this.cannonSize = cannonSize;
            this.cannonSpeed = cannonSpeed;
            this.kamikazeSpawnPeriod = kamikazeSpawnPeriod;
            this.pursuitDuration = pursuitDuration;
            this.pursuitSpeed = pursuitSpeed;
            this.pursuitAccel = pursuitAccel;
            this.pursuitCollideDamage = pursuitCollideDamage;
            this.closeRangeAttackPool = closeRangeAttackPool;
            this.longRangeAttackPool = longRangeAttackPool;
            this.enragedAttackPool = enragedAttackPool;
        }
    }
}




