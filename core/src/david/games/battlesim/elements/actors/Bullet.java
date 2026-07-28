package david.games.battlesim.elements.actors;


import static com.badlogic.gdx.math.Intersector.overlaps;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import static david.games.battlesim.BattleGame.assetManager;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.damage.StatusEffect;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.util.GameUtil;

public class Bullet implements Pool.Poolable {
    public Circle hitbox;
    Texture texture;
    public float angle, speed, damage, size;
    public boolean isAlive = true, fromPlayer;

    public Bullet(float x, float y, float angle, float speed, float damage, float size, boolean fromPlayer) {
        texture = assetManager.get(AssetPaths.BULLET_BLUE, Texture.class);
        hitbox = new Circle();
        hitbox.radius = size;
        hitbox.x = x;
        hitbox.y = y;
        this.angle = angle;
        this.speed = speed;
        this.damage = damage;
        this.size = size;
        this.fromPlayer = fromPlayer;
    }
    public Bullet(){
        this(0f, 0f, 90f, 20f, 20f, 30f, true);
    }

    public void draw(SpriteBatch batch){
        batch.draw(
                texture, hitbox.x - hitbox.radius, hitbox.y - hitbox.radius, hitbox.radius * 2, hitbox.radius * 2,
                0, 0, texture.getWidth(), texture.getHeight(), false, false
        );
    }

    public void update(float delta, GameContext context){
        for (Enemy enemy : context.enemies) {
            // Checks if bullet hit enemy
            if (isAlive && fromPlayer && overlaps(hitbox, enemy.hitbox)) {
                DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.NONE, damage, 0f, 0f);
                damageAct.sourcePosition = new Vector2(hitbox.x, hitbox.y);
                enemy.takeHit(damageAct);
                isAlive = false;
            }
        }
        // Checks if bullet hit player
        if (isAlive && !fromPlayer) {
            if ((context.player.shielding && overlaps(hitbox, context.player.shieldHitbox)) || (!context.player.shielding && overlaps(hitbox, context.player.hitbox))) {
                DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.NONE, damage, 0f, 0f);
                damageAct.sourcePosition = new Vector2(hitbox.x, hitbox.y);
                context.player.takeHit(damageAct);
                isAlive = false;
            }
        }

        hitbox.x += MathUtils.cosDeg(angle) * speed * delta;
        hitbox.y += MathUtils.sinDeg(angle) * speed * delta;
        applyBounds();
    }

    public void applyBounds(){
        if (hitbox.y + hitbox.radius > GameConfig.HEIGHT || hitbox.y + hitbox.radius < 0f ||
            hitbox.x + hitbox.radius > GameConfig.WIDTH || hitbox.x + hitbox.radius < 0f)
        {
            isAlive = false;
        }
    }

    public void initFromPool(float x, float y, float angle, float speed, float damage, float size, boolean fromPlayer){
        hitbox.x = x;
        hitbox.y = y;
        hitbox.radius = size;

        this.angle = angle;
        this.speed = speed;
        this.damage = damage;
        this.size = size;
        this.fromPlayer = fromPlayer;
    }

    @Override
    public void reset() {
        isAlive = true;
        hitbox.x = 0f;
        hitbox.y = 0f;
    }
}
