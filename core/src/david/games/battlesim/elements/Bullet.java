package david.games.battlesim.elements;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;

import static david.games.battlesim.BattleGame.assetManager;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;

public class Bullet implements Pool.Poolable {
    public Circle hitbox;
    Texture texture;
    public float angle, speed = 1750f;
    public boolean isAlive = true;

    public Bullet(float x, float y, float angle) {
        texture = assetManager.get(AssetPaths.BULLET_BLUE, Texture.class);
        hitbox = new Circle();
        hitbox.radius = GameConfig.WIDTH/45;
        hitbox.x = x;
        hitbox.y = y;
        this.angle = angle;
    }
    public Bullet(){
        this(0f, 0f, 90f);
    }

    public void draw(SpriteBatch batch){
        batch.draw(
                texture, hitbox.x, hitbox.y, hitbox.radius, hitbox.radius,
                0, 0, texture.getWidth(), texture.getHeight(), false, false
        );
    }

    // (angle + 90) because the angle is displaced by 90 for some reason
    public void update(float delta){
        hitbox.x += MathUtils.cosDeg(angle + 90f) * speed * delta;
        hitbox.y += MathUtils.sinDeg(angle + 90f) * speed * delta;
        applyBounds();
    }

    public void applyBounds(){
        if (hitbox.y + hitbox.radius > GameConfig.HEIGHT + 10f || hitbox.y + hitbox.radius < 0f ||
            hitbox.x + hitbox.radius > GameConfig.WIDTH + 10f || hitbox.x + hitbox.radius < 0f) {
            isAlive = false;
        }
    }

    public void initFromPool(float x, float y, float angle){
        hitbox.x = x;
        hitbox.y = y;
        this.angle = angle;
    }

    @Override
    public void reset() {
        isAlive = true;
        hitbox.x = 0f;
        hitbox.y = 0f;
    }
}
