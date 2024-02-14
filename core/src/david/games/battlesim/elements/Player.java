package david.games.battlesim.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;


import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;

public class Player {
    Rectangle hitbox;
    Texture playerImage;
    float width, height, speed;
    private float phaseDuration = 2.0f, phaseTimer = 0.0f;
    private float phaseCooldown;
    private boolean phasing;
    public Player(float x, float y){
        playerImage = new Texture(AssetPaths.PLAYER);

        hitbox = new Rectangle();
        hitbox.x = x;
        hitbox.y = y;
        hitbox.width = GameConfig.WIDTH/14;
        hitbox.height = GameConfig.WIDTH/14;
        width = GameConfig.WIDTH/14;
        height = GameConfig.WIDTH/14 * 1.94f;

        this.speed = 280;
    }

    public void draw(SpriteBatch batch, float turnX, float turnY){
        // Player is a circle, so height == width

        Vector2 centerVector = new Vector2();
        hitbox.getCenter(centerVector);
        float angle = (float) Math.atan2((turnY - centerVector.y), (turnX - centerVector.x));
        angle = (((float) Math.toDegrees(angle) + 360) % 360) - 90;

        // Texture texture, float x, float y, float originX, float originY, float width, float height,
        //float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight,
        //boolean flipX, boolean flipY
        batch.draw(
                playerImage, hitbox.x, hitbox.y, width/2, width/2, width, height,
                1, 1, angle,
                0, 0, playerImage.getWidth(), playerImage.getHeight(), false, false
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
                if (hitbox.y > GameConfig.HEIGHT - hitbox.getHeight() - 9f) { hitbox.y = GameConfig.HEIGHT - hitbox.getHeight() - 9f; }
                break;
            case "down":
                hitbox.y -= delta * speed;
                if (hitbox.y < 9f) { hitbox.y = 9f; }
                break;
            case "left":
                hitbox.x -= delta * speed;
                if (hitbox.x < 11f) { hitbox.x = 11f; }
                break;
            case "right":
                hitbox.x += delta * speed;
                if (hitbox.x > GameConfig.WIDTH - hitbox.getWidth() - 11f) { hitbox.x = GameConfig.WIDTH - hitbox.getWidth() - 11f; }
                break;
        }
    }

    public void phase(){
        float elapsedTime = (TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f);
        // Speed for 1 second
        if (elapsedTime - phaseCooldown > 1.25f) {
            speed += 1000;
            phaseTimer = phaseDuration;
            phasing = true;
            phaseCooldown = TimeUtils.nanosToMillis(TimeUtils.nanoTime()) / 1000f;
        }
    }

    public void dispose(){
        playerImage.dispose();
    }
}
