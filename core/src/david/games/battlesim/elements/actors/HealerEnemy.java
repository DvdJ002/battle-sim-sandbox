package david.games.battlesim.elements.actors;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.GameUtil.findAngleBetweenPoints;
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
    private Texture beamTexture;
    private final EnemyConfig.HealerConfig healerConfig;
    private Enemy healedEnemy;
    private float detectionRange, healAmount, roamSpeed, chaseSpeed, beamWidth;
    private boolean isHealing = false;
    private float roamLocationTimer, roamPeriod;

    public HealerEnemy(EnemyConfig enemyConfig, float x, float y){
        super(enemyConfig, x, y);
        this.healerConfig = (EnemyConfig.HealerConfig) enemyConfig;
        this.detectionRange = healerConfig.detectionRange;
        this.healAmount = healerConfig.healAmount;
        this.roamSpeed = healerConfig.roamSpeed;
        this.chaseSpeed = healerConfig.chaseSpeed;
        this.roamPeriod = healerConfig.roamLocationPeriod;
        this.beamWidth = healerConfig.beamWidth;

        texture = assetManager.get(AssetPaths.HEALER, Texture.class);
        beamTexture = assetManager.get(AssetPaths.HEALER_BEAM, Texture.class);
        steeringBehavior = new Arrive<>(this, target);

        roamLocationTimer = roamPeriod;
    }


    @Override
    public void draw(SpriteBatch batch) {
        if (canHealEnemy()) {
            // Draw the healing beam before the healer itself
            float angle = findAngleBetweenPoints(position.x, position.y, healedEnemy.position.x, healedEnemy.position.y);
            float height = Vector2.dst(position.x, position.y, healedEnemy.position.x, healedEnemy.position.y);
            batch.draw(
                    beamTexture, position.x + healedEnemy.hitbox.width/2, position.y + healedEnemy.hitbox.height/2, beamWidth / 2f, 0f, beamWidth, height,
                    1, 1, angle - 90f, 0, 0, texture.getWidth(), texture.getHeight(), false, false
            );
        }

        super.draw(batch);
    }

    @Override
    public void update(float delta, GameContext context){
        // If the healer is not currently locked on, seek for enemies to heal
        if (!isHealing) {
            // Detect enemies within range for healing lock-on. First statement filters the healer itself
            for (Enemy enemy : context.enemies) {
                if (enemy != this &&  isNear(position.x, position.y, enemy.position.x, enemy.position.y, detectionRange)){
                    lockToEnemy(enemy);
                }
            }
        }

        // After seeking, if the target has been found, heal it. Otherwise continue the roam new location timer
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

    // Lock to enemy and become faster
    public void lockToEnemy(Enemy targetEnemy){
        healedEnemy = targetEnemy;
        isHealing = true;

        steeringState.maxLinearAcceleration = chaseSpeed;
        steeringState.maxAngularAcceleration = chaseSpeed;
        steeringState.maxLinearSpeed = chaseSpeed;
        steeringState.maxAngularSpeed = chaseSpeed;
    }

    // Heal enemy and update target position
    public void healEnemy() {
        // If the target enemy dies:
        // The healer dies if it was actively healing it, but switches back to roam if it was not healing it
        if (!healedEnemy.isAlive) {
            if (!canHealEnemy()) {
                stopHealing();
                return;
            }

            isAlive = false;
        }

        // Only heal if within detection range
        if (canHealEnemy()){
            DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.NONE, -healAmount, 0f, 0f);
            damageAct.sourcePosition = new Vector2(position.x, position.y);
            healedEnemy.takeHit(damageAct);
        }

        updateSteeringTarget(healedEnemy.position.x + 60f, healedEnemy.position.y + 60f);
    }

    // Returns true if healer is both locked onto an enemy AND within detection range
    public boolean canHealEnemy() {
        return isHealing && isNear(position.x, position.y, healedEnemy.position.x, healedEnemy.position.y, detectionRange);
    }

    public void roam() {
        float x = MathUtils.random(GameConfig.WIDTH/4f,GameConfig.WIDTH/4f * 3);
        float y = MathUtils.random(GameConfig.HEIGHT/4f,GameConfig.HEIGHT/4f * 3);

        updateSteeringTarget(x, y);
    }

    // Only if the locked enemy did not die while it was being healed
    public void stopHealing(){
        healedEnemy = null;
        isHealing = false;

        steeringState.maxLinearAcceleration = roamSpeed;
        steeringState.maxAngularAcceleration = roamSpeed;
        steeringState.maxLinearSpeed = roamSpeed;
        steeringState.maxAngularSpeed = roamSpeed;

        roamLocationTimer = roamPeriod;
    }
}
