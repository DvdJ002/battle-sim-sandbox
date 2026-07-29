package david.games.battlesim.elements.actors;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.GameUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetDescriptors;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.database.EnemyConfig;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.elements.damage.StatusEffect;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.util.GameUtil;

public class KamikazeEnemy extends Enemy {
    private final EnemyConfig.KamikazeConfig kamikazeConfig;
    private Sound explosionSound;
    private float explodingRange, knockbackIntensity;

    public KamikazeEnemy(EnemyConfig enemyConfig, float x, float y) {
        super(enemyConfig, x, y);
        this.kamikazeConfig = (EnemyConfig.KamikazeConfig) enemyConfig;
        this.explodingRange = kamikazeConfig.explosionRange;
        this.knockbackIntensity = kamikazeConfig.knockbackIntensity;

        texture = assetManager.get(AssetPaths.KAMIKAZE, Texture.class);
        explosionSound = assetManager.get(AssetDescriptors.KAMIKAZE_EXPLOSION_SOUND);
        steeringBehavior = new Arrive<>(this, target).setDecelerationRadius(GameConfig.WIDTH * 0.1f);
        damageAct = GameUtil.getDamageAction(StatusEffect.KNOCKBACK, this.damage, knockbackIntensity, 0f);
    }

    @Override
    public void update(float delta, GameContext context){
        Player player = context.player;
        Vector2 playerPosition = player.position;
        updateSteeringTarget(playerPosition.x, playerPosition.y);

        super.update(delta, context);

        // Kamikaze reached necessary distance from player and exploded
        if (isNear(hitbox.x, hitbox.y, playerPosition.x, playerPosition.y, explodingRange)){
            damageAct.sourcePosition.set(position);
            player.takeHit(damageAct);
            isAlive = false;

            explosionSound.play(GameConfig.VOLUME_DEFAULT);
        }
    }
}
