package david.games.battlesim.elements.actors;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.GameUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.database.EnemyConfig;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.damage.StatusEffect;
import david.games.battlesim.elements.spawners.ForceFieldSpawner;
import david.games.battlesim.util.GameUtil;

public class SuckerEnemy extends Enemy {
    private final EnemyConfig.SuckerConfig suckerConfig;
    private float detectionRange, suckIntensity, forceFieldDuration, chaseSpeed;
    private float forceFieldTimer = 0f;

    public SuckerEnemy(EnemyConfig enemyConfig, float x, float y){
        super(enemyConfig, x, y);
        this.suckerConfig = (EnemyConfig.SuckerConfig) enemyConfig;
        this.detectionRange = suckerConfig.detectionRange;
        this.suckIntensity = suckerConfig.suckIntensity;
        this.forceFieldDuration = suckerConfig.forceFieldDuration;
        this.chaseSpeed = suckerConfig.chaseSpeed;

        texture = assetManager.get(AssetPaths.SUCKER, Texture.class);
        steeringBehavior = new Arrive<>(this, target);
    }

    @Override
    public void update(float delta, GameContext context){
        Player player = context.player;
        Vector2 playerPosition = context.player.position;
        updateSteeringTarget(playerPosition.x, playerPosition.y);

        super.update(delta, context);

        // Sucker reached necessary distance from player and activated suck
        if (!isInvincible && isNear(hitbox.x, hitbox.y, playerPosition.x, playerPosition.y, detectionRange)){
            DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.KNOCKBACK, 0f, -suckIntensity, 0f);
            damageAct.sourcePosition = new Vector2(hitbox.x, hitbox.y);
            player.takeHit(damageAct);

            activateForceField(context.forceFieldSpawner);
        }

        if (forceFieldTimer > 0f) {
            forceFieldTimer -= delta;
            if (forceFieldTimer <= 0f) {
                forceFieldTimer = 0f;
                chase();
            }
        }
    }

    // Spawn the force field at the location, make the enemy invincible and stationary
    public void activateForceField(ForceFieldSpawner spawner){
        spawner.spawn(position.x + hitbox.width/2, position.y  +  hitbox.height/2, damage, forceFieldDuration, detectionRange, false, true);
        isInvincible = true;
        isStatic = true;

        forceFieldTimer = forceFieldDuration;
    }

    private void chase() {
        isInvincible = false;
        isStatic = false;
    }
}
