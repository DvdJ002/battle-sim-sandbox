package david.games.battlesim.elements;

import static david.games.battlesim.util.MovementUtil.findAngleBetweenPoints;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import static david.games.battlesim.BattleGame.assetManager;

import java.util.ArrayList;
import java.util.Objects;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;

public class Player {
    public Circle hitbox, shieldHitbox;
    Texture texture, shieldTexture;
    public float speed = 280f, health = 100f, shieldHealth = 100f, faceAngle;
    private float phaseDuration = 2.0f, phaseTimer = 0.0f, phaseCooldown;
    public float shieldRechargeDuration = 50f, shieldRechargeTimer = 0.0f;
    public boolean phasing, shielding = false, shieldRecharging = false;
    public Player(float x, float y){
        texture = assetManager.get(AssetPaths.PLAYER, Texture.class);
        shieldTexture = assetManager.get(AssetPaths.PLAYER_SHIELD, Texture.class);
        hitbox = new Circle();
        hitbox.radius = GameConfig.WIDTH/20; // 40
        hitbox.x = x;
        hitbox.y = y;
        shieldHitbox = new Circle();
        shieldHitbox.radius = hitbox.radius * 1.25f; // 50
        shieldHitbox.x = x;
        shieldHitbox.y = y;
    }

    public void draw(SpriteBatch batch, float turnX, float turnY){
        faceAngle = findAngleBetweenPoints(hitbox.x, hitbox.y, turnX, turnY);
        batch.draw(
                texture, hitbox.x - hitbox.radius, hitbox.y - hitbox.radius, hitbox.radius, hitbox.radius,
                hitbox.radius*2, hitbox.radius*2, 1, 1, faceAngle, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false
        );

        if (shielding) {
            // Draw shield around player
            batch.draw(
                    shieldTexture, hitbox.x - shieldHitbox.radius, hitbox.y - shieldHitbox.radius,
                    shieldHitbox.radius, shieldHitbox.radius, shieldHitbox.radius*2, shieldHitbox.radius*2,
                    1, 1, faceAngle, 0, 0,
                    shieldTexture.getWidth(), shieldTexture.getHeight(), false, false
            );
        }
        updateTimers();
    }

    private void updateTimers(){
        // Phasing timer
        if (phasing) {
            phaseTimer -= 0.3f;
            if (phaseTimer <= 0f) {
                speed -= 1000;
                phasing = false;
            }
        }
        // Shield recharge timer
        if (shieldRecharging) {
            shieldRechargeTimer -= 0.1f;
            if (shieldRechargeTimer <= 0f){
                shieldHealth = 100f;
                shieldRecharging = false;
            }
        }
    }

    public void movePlayer(float delta, String direction) {
        switch (direction){
            case "up":
                hitbox.y += speed * delta;
                if (hitbox.y > GameConfig.HEIGHT - hitbox.radius) { hitbox.y = GameConfig.HEIGHT - hitbox.radius; }
                break;
            case "down":
                hitbox.y -= delta * speed;
                if (hitbox.y < hitbox.radius) { hitbox.y = hitbox.radius; }
                break;
            case "left":
                hitbox.x -= delta * speed;
                if (hitbox.x < hitbox.radius) { hitbox.x = hitbox.radius; }
                break;
            case "right":
                hitbox.x += delta * speed;
                if (hitbox.x > GameConfig.WIDTH - hitbox.radius) { hitbox.x = GameConfig.WIDTH - hitbox.radius; }
                break;
        }
        shieldHitbox.x = hitbox.x;
        shieldHitbox.y = hitbox.y;
    }

    public void phase(){
        float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
        // Speed up for a short burst, 1.25s cooldown
        if (elapsedTime - phaseCooldown > 1.25f) {
            speed += 1000;
            phaseTimer = phaseDuration;
            phasing = true;
            phaseCooldown = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
        }
    }

    public void takeHit(String type){
        if (Objects.equals(type, "bullet")) { changeHealth(-25f); }
        else if (Objects.equals(type, "collision")) { changeHealth(-2f); }
        else if (Objects.equals(type, "kamikaze")) { changeHealth(-75f); }
        // Particle effects/animations etc.
    }

    public void changeHealth(float change){
        if (shielding){
            shieldHealth += change;
            if (shieldHealth <= 0f){
                this.deactivateShield();
                shieldRechargeTimer = shieldRechargeDuration;
                shieldRecharging = true;
            }
        } else {
            health += change;
            if (health <= 0f){
                System.out.println("Game over!");
            }
            else if (health > 100) { health = 100f; }
        }
        System.out.println("Player health: " + health);
        System.out.println("Shield health: " + shieldHealth);
    }

    public void shootBullet(Pool<Bullet> bulletPool, ArrayList<Bullet> bullets){
        if (!shielding){
            // if (bulletPool.getFree() != 0){ System.out.println("Player obtained bullet from pool!"); }
            Bullet bullet = bulletPool.obtain();
            bullet.initFromPool(hitbox.x, hitbox.y, faceAngle, true);
            bullets.add(bullet);
        }
    }

    public void activateShield(){
        if (shieldHealth > 0f) { shielding = true; }
    }
    public void deactivateShield(){
        shielding = false;
    }

    public Vector2 getPositionVector(){
        return new Vector2(hitbox.x, hitbox.y);
    }
}
