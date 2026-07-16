package david.games.battlesim.elements.actors;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.GameUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.config.database.EnemyConfig;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.damage.StatusEffect;
import david.games.battlesim.util.GameUtil;

public class HealerEnemy extends Enemy {
    private final EnemyConfig.HealerConfig healerConfig;
    private Enemy healedEnemy;
    private float detectionRange, healAmount, roamSpeed, chaseSpeed;
    private boolean isHealing = false;
    private float roamLocationTimer = 0f, roamPeriod;

    public HealerEnemy(EnemyConfig enemyConfig, float x, float y){
        super(enemyConfig, x, y);
        this.healerConfig = (EnemyConfig.HealerConfig) enemyConfig;
        this.detectionRange = healerConfig.detectionRange;
        this.healAmount = healerConfig.healAmount;
        this.roamSpeed = healerConfig.roamSpeed;
        this.chaseSpeed = healerConfig.chaseSpeed;
        this.roamPeriod = healerConfig.roamLocationPeriod;

        texture = assetManager.get(AssetPaths.HEALER, Texture.class);
        steeringBehavior = new Arrive<>(this, target);

        roamLocationTimer = roamPeriod;
    }


    @Override
    public void draw(SpriteBatch batch) {
        // Draw the stick here
        super.draw(batch);
    }

    @Override
    public void update(float delta, GameContext context){
        if (!isHealing) {
            // Detect enemies within range for healing lock-on. First statement filters the healer itself
            for (Enemy enemy : context.enemies) {
                if (enemy != this &&  isNear(hitbox.x, hitbox.y, enemy.hitbox.x, enemy.hitbox.y, detectionRange)){
                    System.out.println("LOCKED TO ENEMY: " + enemy.hitbox.x + ", " + enemy.hitbox.y);
                    lockToEnemy(enemy);
                }
            }
        }

        if (isHealing) { healEnemy(); }
        else {
            if (roamLocationTimer > 0f) {
                roamLocationTimer -= delta;
                if (roamLocationTimer <= 0f) {
                    roamLocationTimer = roamPeriod;
                    roam();
                }
            }
        }

        super.update(delta, context);
    }

    // Spawn the force field at the location, make the enemy invincible and stationary
    public void lockToEnemy(Enemy targetEnemy){
        healedEnemy = targetEnemy;
        isHealing = true;

        updateSteeringTarget(healedEnemy.hitbox.x, healedEnemy.hitbox.y);

        steeringState.maxLinearAcceleration = chaseSpeed;
        steeringState.maxAngularAcceleration = chaseSpeed;
        steeringState.maxLinearSpeed = chaseSpeed;
        steeringState.maxAngularSpeed = chaseSpeed;
    }

    // Heal enemy and update target position
    public void healEnemy() {
        // Healer dies if the target enemy dies
        if (!healedEnemy.isAlive) {
            isAlive = false;
        }
        // Only heal if within detection range in the first place
        if (isNear(hitbox.x, hitbox.y, healedEnemy.hitbox.x, healedEnemy.hitbox.y, detectionRange)){
            DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.NONE, -healAmount, 0f, 0f);
            damageAct.sourcePosition = new Vector2(hitbox.x, hitbox.y);
            healedEnemy.takeHit(damageAct);
        }


        updateSteeringTarget(healedEnemy.hitbox.x, healedEnemy.hitbox.y);
    }

    public void roam() {
        float x = MathUtils.random(GameConfig.WIDTH/4f,GameConfig.WIDTH/4f * 3);
        float y = MathUtils.random(GameConfig.HEIGHT/4f,GameConfig.HEIGHT/4f * 3);

        updateSteeringTarget(x, y);
    }
}
