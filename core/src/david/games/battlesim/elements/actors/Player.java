package david.games.battlesim.elements.actors;

import static david.games.battlesim.util.GameUtil.findAngleBetweenPoints;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static david.games.battlesim.BattleGame.assetManager;

import java.util.ArrayList;

import david.games.battlesim.assets.AssetDescriptors;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.config.database.PlayerConfig;
import david.games.battlesim.elements.data.DamageAction;
import david.games.battlesim.elements.data.StatusEffect;
import david.games.battlesim.elements.spawners.BulletSpawner;
import david.games.battlesim.elements.spawners.ForceFieldSpawner;
import david.games.battlesim.util.GameUtil;

public class Player {
    // Sprites, textures, config, sound
    public Circle hitbox, shieldHitbox;
    Texture texture, shieldTexture;
    public final PlayerConfig config;
    private final Sound damagedSound, shootSound, ultimateSound;


    // Game parameters
    public Vector2 velocity, position, inputDirection;
    public float health, speed, shieldHealth;
    public float faceAngle;

    // Timers
    public float fireRateTimer = 0.0f;
    public float phaseCooldownTimer = 0.0f, phaseActiveTimer = 0.0f;
    public float forceFieldCooldownTimer = 0.0f;
    public float shieldRechargeTimer = 0.0f;
    public float rootedTimer = 0.0f;
    public float slowedTimer = 0.0f;
    public float invincibleTimer = 0.0f;
    public float disarmedTimer = 0.0f;

    // Boolean states
    public boolean shielding = false, invincible = false, disarmed = false;

    public Player(PlayerConfig config, float x, float y){
        texture = assetManager.get(AssetPaths.PLAYER, Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        damagedSound = assetManager.get(AssetDescriptors.PLAYER_DAMAGE_SOUND);
        shootSound = assetManager.get(AssetDescriptors.GLOBAL_SHOOT_SOUND);
        ultimateSound = assetManager.get(AssetDescriptors.PLAYER_ULTIMATE_SOUND);

        hitbox = new Circle();
        hitbox.radius = GameConfig.DEFAULT_PLAYER_SIZE;

        shieldTexture = assetManager.get(AssetPaths.PLAYER_SHIELD, Texture.class);
        shieldHitbox = new Circle();
        shieldHitbox.radius = hitbox.radius * 1.25f;

        velocity = new Vector2(0, 0);
        inputDirection = new Vector2(0, 0);
        position = new Vector2(0, 0);
        setPosition(x, y);

        this.config = config;
        this.health = config.maxHealth;
        this.speed = config.baseSpeed;
        this.shieldHealth = config.maxHealth;
    }

    /********************* DRAW *********************/
    public void draw(SpriteBatch batch, float turnX, float turnY){
        float size = hitbox.radius * 2f;
        faceAngle = findAngleBetweenPoints(hitbox.x, hitbox.y, turnX, turnY);
        // angle - 90 because of texture offset, horrible hack
        batch.draw(
                texture, position.x - hitbox.radius, position.y - hitbox.radius, hitbox.radius, hitbox.radius,
                size, size, 1, 1, faceAngle - 90, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false
        );

        if (shielding) {
            // Also draw shield around player
            batch.draw(
                    shieldTexture, position.x - shieldHitbox.radius, position.y - shieldHitbox.radius,
                    shieldHitbox.radius, shieldHitbox.radius, shieldHitbox.radius*2, shieldHitbox.radius*2,
                    1, 1, faceAngle - 90, 0, 0,
                    shieldTexture.getWidth(), shieldTexture.getHeight(), false, false
            );
        }
    }

    /********************* UPDATE *********************/
    public void update(float delta) {
        // WASD input
        if (isMovementDisabled()) {
            inputDirection.set(0, 0);
        }

        // If input - normalize direction and multiply by speed
        // Only alter the relevant component of velocity so the other stays being dampened
        if (inputDirection.len() > 0) {
            Vector2 desired = new Vector2(inputDirection).nor().scl(speed);
            if (desired.x != 0) { velocity.x = desired.x; }
            if (desired.y != 0) { velocity.y = desired.y; }
        }

        // Damp speed if no input
        if (inputDirection.x == 0f) {
            velocity.x *= config.speedDampening;
        }
        if (inputDirection.y == 0f) {
            velocity.y *= config.speedDampening;
        }

        // Add the components of velocity to position and reset direction
        setPosition(position.x + (velocity.x * delta), position.y + (velocity.y * delta));
        inputDirection.set(0, 0);

        updateTimers(delta);
    }

    private void updateTimers(float delta){
        // Fire rate timer
        if (fireRateTimer > 0f) {
            fireRateTimer -= delta;
            if (fireRateTimer <= 0f) {
                fireRateTimer = 0f;
            }
        }

        // Phase cooldown timer, only if not shielding
        if (phaseCooldownTimer > 0f && !shielding) {
            phaseCooldownTimer -= delta;
            if (phaseCooldownTimer <= 0f) {
                phaseCooldownTimer = 0f;
            }
        }
        // Phase active timer
        if (phaseActiveTimer > 0f) {
            phaseActiveTimer -= delta;
            if (phaseActiveTimer <= 0f) {
                speed -= 1000;
                phaseActiveTimer = 0f;
            }
        }
        // Force field ability cooldown timer
        if (forceFieldCooldownTimer > 0f) {
            forceFieldCooldownTimer -= delta;
        }
        // Shield recharge timer
        if (shieldRechargeTimer > 0f) {
            shieldRechargeTimer -= delta;
            if (shieldRechargeTimer <= 0f){
                shieldHealth = config.maxShieldHealth;
            }
        }
        // Disable movement timer
        if (rootedTimer > 0f) {
            rootedTimer -= delta;
        }
        // Slowed effect timer
        if (slowedTimer > 0f) {
            slowedTimer -= delta;
            if (slowedTimer <= 0f){
                speed = config.baseSpeed;
            }
        }
        // Invincible timer
        if (invincibleTimer > 0f) {
            invincibleTimer -= delta;
            if (invincibleTimer <= 0f){
                invincibleTimer = 0.0f;
                invincible = false;
            }
        }
        // Disarmed timer
        if (disarmedTimer > 0f) {
            disarmedTimer -= delta;
            if (disarmedTimer <= 0f){
                disarmedTimer = 0.0f;
                disarmed = false;
            }
        }
    }

    /********************* IN-GAME ACTIONS *********************/
    public void phase(){
        if (phaseCooldownTimer <= 0f && phaseActiveTimer <= 0f) {
            phaseActiveTimer = config.phaseDuration;
            phaseCooldownTimer = config.phaseCooldown;
            speed += 1000;
        }
    }
    public void rechargeShield() {
        shielding = false;
        shieldRechargeTimer = config.shieldRechargeDuration;
    }

    public void shootBullet(BulletSpawner bulletSpawner){
        // Only shoot if the player is not shielding, the fire rate timer is not active, and the player is not disarmed
        if (!shielding && fireRateTimer <= 0f && !disarmed){
            bulletSpawner.spawn(hitbox.x, hitbox.y, faceAngle, config.bulletSpeed, config.bulletDamage, config.bulletSize,true);
            fireRateTimer = config.fireRate;
            shootSound.play(GameConfig.VOLUME_DEFAULT);
        }
    }

    public void forceField(ForceFieldSpawner forceFieldSpawner){
        if (!shielding && forceFieldCooldownTimer <= 0f){
            forceFieldSpawner.spawn(position.x, position.y, config.forceFieldDamage, config.forceFieldDuration, config.forceFieldSize, true, true);

            forceFieldCooldownTimer = config.forceFieldCooldown;
            applyDisarmed(config.forceFieldDuration);
            applyRooted(config.forceFieldDuration);
            applyInvincible(config.forceFieldDuration + config.forceFieldGracePeriod);

            ultimateSound.play(GameConfig.VOLUME_LOUD);
        }
    }


    /********************* OPERATIONS *********************/
    public void reset() {
        // Reset health
        health = config.maxHealth;
        shieldHealth = config.maxShieldHealth;

        // Reset timers
        phaseCooldownTimer = 0.01f;
        phaseActiveTimer = 0.01f;
        shieldRechargeTimer = 0.01f;
        rootedTimer = 0.01f;
        slowedTimer = 0.01f;
        forceFieldCooldownTimer = 0.01f;
        disarmedTimer = 0.01f;
        invincibleTimer = 0.01f;

        // Reset boolean states
        invincible = false;
        disarmed = false;
    }

    public void takeHit(DamageAction damageAct){
        if (invincible) { return; }

        changeHealth(-damageAct.amount);

        switch (damageAct.type) {
            case KNOCKBACK:
                applyKnockback(damageAct.sourcePosition, damageAct.intensity);
                break;
            case SLOWED:
                applySlowed(damageAct.intensity, damageAct.duration);
                break;
        }

        damagedSound.play(GameConfig.VOLUME_DEFAULT);
    }

    public void changeHealth(float change){
        if (shielding && change < 0f){
            shieldHealth += change;
            if (shieldHealth <= 0f){
                health += shieldHealth;
                shieldHealth = 0f;
                rechargeShield();
            }
        }
        else {
            health += change;
        }

        if (health <= 0f) { health = 0f; }
        else if (health > config.maxHealth) { health = config.maxHealth; }

        if (shieldHealth > config.maxShieldHealth) { shieldHealth = config.maxShieldHealth; }
    }

    // Applies bounds, sets new position, hitbox, and shield position
    public void setPosition(float x, float y){
        if (y > GameConfig.HEIGHT - hitbox.radius) { y = GameConfig.HEIGHT - hitbox.radius; }
        if (y < hitbox.radius) { y = hitbox.radius; }
        if (x < hitbox.radius) { x = hitbox.radius; }
        if (x > GameConfig.WIDTH - hitbox.radius) { x = GameConfig.WIDTH - hitbox.radius; }

        position.x = x;
        position.y = y;
        hitbox.x = x;
        hitbox.y = y;
        shieldHitbox.x = x;
        shieldHitbox.y = y;
    }

    public void setShieldState(boolean active) {
        shielding = active && shieldHealth > 0f;
    }

    /********************* STATUS EFFECTS *********************/
    public void applyKnockback(Vector2 sourcePos, float intensity) {
        float kbAngle = GameUtil.findAngleBetweenPoints(sourcePos.x, sourcePos.y, position.x, position.y);
        velocity.set(
                MathUtils.cos(MathUtils.degreesToRadians * kbAngle),
                MathUtils.sin(MathUtils.degreesToRadians * kbAngle)
        ).scl(speed * intensity);

        // Briefly disable input with timer, absolute value because intensity can be minus (suck)
        applyRooted(Math.abs(intensity/90));
    }

    public void applySlowed(float intensity, float duration) {
        if (slowedTimer <= 0f) {
            // Slow player by intensity for duration
            speed -= intensity;
            slowedTimer = duration;
        }
    }

    public void applyInvincible(float duration) {
        if (invincibleTimer <= 0f) {
            invincible = true;
            invincibleTimer = duration;
        }
    }

    public void applyRooted(float duration) {
        rootedTimer = duration;
    }

    public void applyDisarmed(float duration) {
        disarmed = true;
        disarmedTimer = duration;
    }

    public boolean isMovementDisabled() {
        return rootedTimer > 0f;
    }

    public ArrayList<StatusEffect> getActiveEffects() {
        ArrayList<StatusEffect> effects = new ArrayList<>();
        if (rootedTimer > 0f) effects.add(StatusEffect.ROOTED);
        if (slowedTimer > 0f) effects.add(StatusEffect.SLOWED);
        if (invincible) effects.add(StatusEffect.INVINCIBLE);
        if (disarmed) effects.add(StatusEffect.DISARMED);
        return effects;
    }
}
