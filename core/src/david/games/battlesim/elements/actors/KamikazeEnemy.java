package david.games.battlesim.elements.actors;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.GameUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.database.EnemyConfig;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.damage.StatusEffect;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.util.GameUtil;

public class KamikazeEnemy extends Enemy {
    private final EnemyConfig.KamikazeConfig kamikazeConfig;
    private float explodingRange, knockbackIntensity;

    public KamikazeEnemy(EnemyConfig enemyConfig, float x, float y) {
        super(enemyConfig, x, y);
        this.kamikazeConfig = (EnemyConfig.KamikazeConfig) enemyConfig;
        this.explodingRange = kamikazeConfig.explosionRange;
        this.knockbackIntensity = kamikazeConfig.knockbackIntensity;

        texture = assetManager.get(AssetPaths.KAMIKAZE, Texture.class);
        steeringBehavior = new Arrive<>(this, target).setDecelerationRadius(GameConfig.WIDTH * 0.1f);
    }

    @Override
    public void update(float delta, GameContext context){
        Player player = context.player;
        Vector2 playerPosition = player.position;
        updateSteeringTarget(playerPosition.x, playerPosition.y);

        super.update(delta, context);

        // Kamikaze reached necessary distance from player and exploded
        if (isNear(hitbox.x, hitbox.y, playerPosition.x, playerPosition.y, explodingRange)){
            DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.KNOCKBACK, this.damage, knockbackIntensity, 0f);
            damageAct.sourcePosition = new Vector2(hitbox.x, hitbox.y);
            player.takeHit(damageAct);
            isAlive = false;
        }
    }
}
