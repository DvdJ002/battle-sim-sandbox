package david.games.battlesim.elements.actors;

import static com.badlogic.gdx.math.Intersector.overlaps;
import static david.games.battlesim.BattleGame.assetManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.damage.StatusEffect;
import david.games.battlesim.util.GameUtil;

public class ForceField {
    public Circle hitbox;
    Texture texture;

    public float maxDamage, duration, size;
    public boolean isAlive = true, fromPlayer, waning;
    public float activeTimer;

    public ForceField(float x, float y, float maxDamage, float duration, float size, boolean fromPlayer, boolean waning) {
        // Player's is blue and enemy's is dark
        texture = fromPlayer ? assetManager.get(AssetPaths.FORCE_FIELD_BLUE, Texture.class) : assetManager.get(AssetPaths.FORCE_FIELD_DARK, Texture.class);
        hitbox = new Circle();
        hitbox.radius = size;
        hitbox.x = x;
        hitbox.y = y;
        this.maxDamage = maxDamage;
        this.duration = duration;
        this.size = size;
        this.fromPlayer = fromPlayer;
        this.waning = waning;

        activeTimer = duration;
    }

    public void draw(SpriteBatch batch){
        batch.setColor(1f, 1f, 1f, getCurrentDamagePercentage());
        batch.draw(
                texture, hitbox.x - hitbox.radius, hitbox.y - hitbox.radius, hitbox.radius*2, hitbox.radius*2,
                0, 0, texture.getWidth(), texture.getHeight(), false, false
        );
        batch.setColor(Color.WHITE);
    }

    public void update(float delta, GameContext context) {
        float damage = maxDamage * getCurrentDamagePercentage();
        for (Enemy enemy : context.enemies) {
            // Checks if field is hitting enemy
            if (isAlive && fromPlayer && overlaps(hitbox, enemy.hitbox)) {
                DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.NONE, damage, 0f, 0f);
                damageAct.sourcePosition = new Vector2(hitbox.x, hitbox.y);
                enemy.takeHit(damageAct);
            }
        }
        // Checks if field is hitting player
        if (isAlive && !fromPlayer) {
            if ((context.player.shielding && overlaps(hitbox, context.player.shieldHitbox)) || (!context.player.shielding && overlaps(hitbox, context.player.hitbox))) {
                DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.NONE, damage, 0f, 0f);
                damageAct.sourcePosition = new Vector2(hitbox.x, hitbox.y);
                context.player.takeHit(damageAct);
            }
        }

        updateTimers(delta);
    }

    private void updateTimers(float delta) {
        if (activeTimer > 0f) {
            activeTimer -= delta;
            if (activeTimer <= 0f) {
                activeTimer = 0f;
                isAlive = false;
            }
        }
    }

    private float getCurrentDamagePercentage(){
        return waning ? (0.2f + 0.8f * (activeTimer / duration)) : 100f;
    }
}
