package david.games.battlesim.elements.actors;

import static com.badlogic.gdx.math.Intersector.overlaps;
import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.GameUtil.findAngleBetweenPoints;
import static david.games.battlesim.util.GameUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.database.EnemyConfig;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.damage.DamageType;
import david.games.battlesim.elements.spawners.BulletSpawner;
import david.games.battlesim.util.GameUtil;

public class ShooterEnemy extends Enemy {
    private final EnemyConfig.ShooterConfig shooterConfig;
    private float reloadTimer = 0f;
    private float  reloadDuration, bulletSpeed, evadeRange;
    private float speedEvade, speedChase;
    public boolean evading = true, reloading = true;
    public ShooterEnemy(EnemyConfig enemyConfig, float x, float y){
        super(enemyConfig, x, y);
        this.shooterConfig = (EnemyConfig.ShooterConfig) enemyConfig;
        this.reloadDuration = shooterConfig.reloadDuration;
        this.evadeRange = shooterConfig.evadeRange;
        this.speedEvade = shooterConfig.speedEvade;
        this.speedChase = shooterConfig.speedChase;
        this.bulletSpeed = shooterConfig.bulletSpeed;

        texture = assetManager.get(AssetPaths.SHOOTER, Texture.class);
        steeringBehavior = new Evade<>(this, target);
    }

    @Override
    public void update(float delta, GameContext context){
        super.update(delta, context);

        Player player = context.player;
        Vector2 playerPosition = context.player.position;

        if (!reloading){
            shootBullet(player.position, context.bulletSpawner);
        }

        // Player damages shooter enemy if touching
        if (overlaps(player.hitbox, hitbox)) {
            DamageAction damageAct = GameUtil.getDamageAction(DamageType.NONE, collideDamage, 0f, 0f);
            damageAct.sourcePosition = new Vector2(player.hitbox.x, player.hitbox.y);
            takeHit(damageAct);
        }

        updateBehavior(playerPosition);

        if (reloading) {
            reloadTimer += delta;
            if (reloadTimer > reloadDuration) {
                reloadTimer = 0f;
                reloading = false;
            }
        }
    }

    public void updateBehavior(Vector2 playerPosition){
        if (!evading && isNear(playerPosition.x, playerPosition.y, hitbox.x, hitbox.y, evadeRange)) {
            this.evade();
        } else if (evading && !isNear(playerPosition.x, playerPosition.y, hitbox.x, hitbox.y, evadeRange)) {
            this.chase();
        }
    }

    public void evade(){
        steeringBehavior = new Evade<>(this, target);
        evading = true;
        steeringState.maxLinearAcceleration = speedEvade;
        steeringState.maxAngularAcceleration = speedEvade;
    }

    public void chase(){
        steeringBehavior = new Arrive<>(this, target);
        evading = false;
        steeringState.maxLinearAcceleration = speedChase;
        steeringState.maxAngularAcceleration = speedChase;
    }

    public void shootBullet(Vector2 playerPosition, BulletSpawner spawner){
        float angle = findAngleBetweenPoints(hitbox.x + (hitbox.width/2), hitbox.y + (hitbox.height/2), playerPosition.x, playerPosition.y);
        spawner.spawn(hitbox.x + (hitbox.width/2), hitbox.y + (hitbox.height/2), angle, bulletSpeed, damage, false);
        reloading = true;
    }
}
