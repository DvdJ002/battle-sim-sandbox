package david.games.battlesim.elements.actors;

import static com.badlogic.gdx.math.Intersector.overlaps;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.database.EnemyConfig;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.elements.data.StatusEffect;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.util.GameUtil;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.GameUtil.findNearestPathToPoint;
import static david.games.battlesim.util.GameUtil.isNear;

public class SlasherEnemy extends Enemy {
    private final EnemyConfig.SlasherConfig slasherConfig;
    private float dashTimer = 0f;
    private float dashCooldown, dashIntensity, detectionRadius;
    private float slowIntensity, slowDuration;

    public SlasherEnemy(EnemyConfig enemyConfig, float x, float y) {
        super(enemyConfig, x, y);
        this.slasherConfig = (EnemyConfig.SlasherConfig) enemyConfig;
        this.dashCooldown = slasherConfig.dashCooldown;
        this.dashIntensity = slasherConfig.dashIntensity;
        this.slowIntensity = slasherConfig.slowIntensity;
        this.slowDuration = slasherConfig.slowDuration;
        this.detectionRadius = slasherConfig.detectionRadius;

        texture = assetManager.get(AssetPaths.SLASHER, Texture.class);
        steeringBehavior = new Arrive<>(this, target).setDecelerationRadius(GameConfig.WIDTH * 1.5f);
        damageAct = GameUtil.getDamageAction(StatusEffect.SLOWED, collideDamage, slowIntensity, slowDuration);
    }

    @Override
    public void update(float delta, GameContext context){
        Player player = context.player;
        Vector2 playerPosition = player.position;
        updateSteeringTarget(playerPosition.x, playerPosition.y);

        super.update(delta, context);

        // Damage player if touching
        if ((player.shielding && overlaps(player.shieldHitbox, hitbox)) || (!player.shielding && overlaps(player.hitbox, hitbox))) {
            damageAct.sourcePosition.set(position);
            player.takeHit(damageAct);
        }

        if (dashTimer > 0f) {
            dashTimer -= delta;
            if (dashTimer <= 0f) {
                dashTimer = 0f;
                texture = assetManager.get(AssetPaths.SLASHER, Texture.class);
            }
        } else if (isNear(playerPosition.x, playerPosition.y, hitbox.x, hitbox.y, detectionRadius)){
            this.dash(dashIntensity, playerPosition);
        }
    }

    public void dash(float intensity, Vector2 playerPos){
        if (dashTimer <= 0f) {
            dashTimer = dashCooldown;

            Vector2 movementVec = findNearestPathToPoint(position.x, position.y, playerPos.x, playerPos.y);
            linearVelocity.x += movementVec.x * intensity;
            linearVelocity.y += movementVec.y * intensity;

            texture = assetManager.get(AssetPaths.SLASHER_ATTACK, Texture.class);
        }
    }
}
