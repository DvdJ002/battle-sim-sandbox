package david.games.battlesim.elements.actors;

import static david.games.battlesim.util.GameUtil.findAngleBetweenPoints;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import static david.games.battlesim.BattleGame.assetManager;

import java.util.ArrayList;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.config.database.PlayerConfig;
import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.damage.DamageType;
import david.games.battlesim.elements.spawners.BulletSpawner;
import david.games.battlesim.util.GameUtil;

public class Player {
    // Sprites, textures, config
    public Circle hitbox, shieldHitbox;
    Texture texture, shieldTexture;
    public final PlayerConfig config;

    // Game parameters
    public Vector2 velocity, position, inputDirection;
    public float health, speed, shieldHealth;
    public float faceAngle;

    // Timers
    public float phaseCooldownTimer = 0f, phaseActiveTimer = 0f;
    public float shieldRechargeTimer = 0.0f;
    public float disableMovementTimer = 0.0f;
    public float slowedTimer = 0.0f;

    // Boolean states
    public boolean shielding = false;

    public Player(PlayerConfig config, float x, float y){
        texture = assetManager.get(AssetPaths.PLAYER, Texture.class);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

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

    private void updateTimers(float delta){
        // Phase cooldown timer
        if (phaseCooldownTimer > 0f) {
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

        // Shield recharge timer
        if (shieldRechargeTimer > 0f) {
            shieldRechargeTimer -= delta;
            if (shieldRechargeTimer <= 0f){
                shieldHealth = config.maxShieldHealth;
            }
        }
        // Disable movement timer
        if (disableMovementTimer > 0f) {
            disableMovementTimer -= delta;
        }
        // Slowed effect timer
        if (slowedTimer > 0f) {
            slowedTimer -= delta;
            if (slowedTimer <= 0f){
                speed = config.baseSpeed;
            }
        }
    }

    public void reset() {
        // Reset health
        health = config.maxHealth;
        shieldHealth = config.maxShieldHealth;

        // Reset timers
        phaseCooldownTimer = 0f;
        phaseActiveTimer = 0f;
        shieldRechargeTimer = 0f;
        disableMovementTimer = 0f;
        slowedTimer = 0f;
    }

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

    public void disableMovement(float intensity) {
        disableMovementTimer = intensity/100;
    }

    public void takeHit(DamageAction damageAct){
        changeHealth(-damageAct.amount);
        System.out.println("Player took damage, TYPE: " + damageAct.type);

        switch (damageAct.type) {
            case KNOCKBACK:
                applyKnockback(damageAct.sourcePosition, damageAct.intensity);
                break;
            case PROJECTILE:
                break;
            case SLOWED:
                applySlowed(damageAct.intensity, damageAct.duration);
                break;
        }
        // Particle effects/animations
    }

    public void changeHealth(float change){
        if (shielding){
            shieldHealth += change;
            if (shieldHealth <= 0f){
                shieldHealth = 0f;
                rechargeShield();
            }
        } else {
            health += change;
            if (health <= 0f){
                health = 0f;
            }
            else if (health > config.maxHealth) { health = config.maxHealth; }
        }
        System.out.println("Player health: " + health);
        System.out.println("Shield health: " + shieldHealth);
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

    public void shootBullet(BulletSpawner bulletSpawner){
        if (!shielding){
            bulletSpawner.spawn(hitbox.x, hitbox.y, faceAngle, config.bulletSpeed, config.bulletDamage,true);
        }
    }

    public void applyKnockback(Vector2 sourcePos, float intensity) {
        float kbAngle = GameUtil.findAngleBetweenPoints(sourcePos.x, sourcePos.y, position.x, position.y);
        velocity.set(
                MathUtils.cos(MathUtils.degreesToRadians * kbAngle),
                MathUtils.sin(MathUtils.degreesToRadians * kbAngle)
        ).scl(speed * intensity);

        // Briefly disable input with timer
        disableMovement(intensity);
    }

    public void applySlowed(float intensity, float duration) {
        if (slowedTimer <= 0f) {
            // Slow player by intensity for duration
            speed -= intensity;
            slowedTimer = duration;
        }
    }

    public void setShieldState(boolean active) {
        shielding = active && shieldHealth > 0f;
    }

    public boolean isMovementDisabled() {
        return disableMovementTimer > 0f;
    }

    public ArrayList<DamageType> getActiveEffects() {
        ArrayList<DamageType> effects = new ArrayList<>();
        if (disableMovementTimer > 0f) effects.add(DamageType.KNOCKBACK);
        if (slowedTimer > 0f) effects.add(DamageType.SLOWED);

        return effects;
    }
}
