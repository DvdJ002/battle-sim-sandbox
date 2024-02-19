package david.games.battlesim.elements;

import static david.games.battlesim.util.MovementUtil.findAngleBetweenPoints;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import static david.games.battlesim.BattleGame.assetManager;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;

public class Player {
    public Circle hitbox;
    Texture texture;
    public float speed = 280f, health = 100f, faceAngle;
    private float phaseDuration = 2.0f, phaseTimer = 0.0f, phaseCooldown;
    private boolean phasing;
    public Player(float x, float y){
        texture = assetManager.get(AssetPaths.PLAYER, Texture.class);
        hitbox = new Circle();
        hitbox.radius = GameConfig.WIDTH/20;
        hitbox.x = x;
        hitbox.y = y;
    }

    public void draw(SpriteBatch batch, float turnX, float turnY){
        faceAngle = findAngleBetweenPoints(hitbox.x, hitbox.y, turnX, turnY);
        batch.draw(
                texture, hitbox.x - hitbox.radius, hitbox.y - hitbox.radius, hitbox.radius, hitbox.radius,
                hitbox.radius*2, hitbox.radius*2, 1, 1, faceAngle, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false
        );

        if (phasing) {
            phaseTimer -= 0.3f;
            if (phaseTimer <= 0f) {
                speed -= 1000;
                phasing = false;
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

    public void onCollision(){
        changeHealth(-5f);
        // Particle effects/animations etc.
    }

    public void changeHealth(float change){
        health += change;
        if (health <= 0f){
            //System.out.println("Game over!");
        }
        else if (health > 100) { health = 100f; }
    }

    public Vector2 getPositionVector(){
        return new Vector2(hitbox.x, hitbox.y);
    }
}
