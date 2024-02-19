package david.games.battlesim.elements;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.MovementUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.Evade;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;

public class ShooterEnemy extends Enemy{
    private float shootingRate = 2f, evadeRange = 300f;
    public boolean evading = true;
    public ShooterEnemy(float x, float y){
        super(x, y);
        texture = assetManager.get(AssetPaths.SHOOTER, Texture.class);
        steeringBehavior = new Evade<>(this, target);
    }

    @Override
    public void update(float delta, Vector2 playerPosition){
        super.update(delta, playerPosition);
        updateBehavior(playerPosition);
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
}
