package david.games.battlesim.elements;

import static david.games.battlesim.util.MovementUtil.findAngleBetweenPoints;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import static david.games.battlesim.BattleGame.assetManager;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;

public class Player {
    public Circle hitbox;
    Texture texture;
    float diameter, speed;
    private float phaseDuration = 2.0f, phaseTimer = 0.0f, phaseCooldown;
    private boolean phasing;
    public Player(float x, float y){
        texture = assetManager.get(AssetPaths.PLAYER, Texture.class);

        hitbox = new Circle();
        hitbox.radius = GameConfig.WIDTH/20;
        hitbox.x = x;
        hitbox.y = y;
        this.diameter = hitbox.radius * 2;
        this.speed = 280;
    }

    public void draw(SpriteBatch batch, float turnX, float turnY){
        float angle = findAngleBetweenPoints(hitbox.x, hitbox.y, turnX, turnY);
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        batch.draw(
                texture, hitbox.x - hitbox.radius, hitbox.y - hitbox.radius, diameter/2, diameter/2,
                diameter, diameter, 1, 1, angle, 0, 0,
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

    public void drawHitbox(){
        ShapeRenderer renderer = new ShapeRenderer();
        renderer.begin(ShapeRenderer.ShapeType.Line);

        renderer.setColor(Color.RED);
        renderer.circle(hitbox.x, hitbox.y, hitbox.radius);

        renderer.end();
    }

    public void onCollision(){
        // System.out.println("Colliding \n .............");
    }

    public Vector2 getPositionVector(){
        return new Vector2(hitbox.x, hitbox.y);
    }
}
