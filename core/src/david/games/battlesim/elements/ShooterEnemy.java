package david.games.battlesim.elements;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.MovementUtil.findAngleBetweenPoints;
import static david.games.battlesim.util.MovementUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;

import david.games.battlesim.assets.AssetPaths;

public class ShooterEnemy extends Enemy{
    private float reloadTimer = 0f, reloadDuration = 2f, evadeRange = 300f;
    public boolean evading = true, reloading = true;
    public ShooterEnemy(float x, float y){
        super(x, y);
        texture = assetManager.get(AssetPaths.SHOOTER, Texture.class);
        steeringBehavior = new Evade<>(this, target);
    }

    @Override
    public void update(float delta, Vector2 playerPosition){
        super.update(delta, playerPosition);
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
        maxLinearAcceleration = 600.0f;
        maxAngularAcceleration = 600.0f;
    }

    public void chase(){
        steeringBehavior = new Arrive<>(this, target);
        evading = false;
        maxLinearAcceleration = 40.0f;
        maxAngularAcceleration = 40.0f;
    }

    public void shootBullet(Vector2 playerPosition, Pool<Bullet> bulletPool, ArrayList<Bullet> bullets){
        float angle = findAngleBetweenPoints(
                hitbox.x + (hitbox.width/2), hitbox.y + (hitbox.height/2), playerPosition.x, playerPosition.y
        );
        // if (bulletPool.getFree() != 0){ System.out.println("Enemy obtained bullet from pool!"); }
        Bullet bullet = bulletPool.obtain();
        bullet.initFromPool(hitbox.x + (hitbox.width/2), hitbox.y + (hitbox.height/2), angle, false);
        bullets.add(bullet);
        reloading = true;
    }
}
