package david.games.battlesim.elements;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.MovementUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;

public class KamikazeEnemy extends Enemy {
    private final float explodingRange = 150f;

    public KamikazeEnemy(float x, float y) {
        super(x, y);
        // The kamikaze accelerates way faster than other enemies
        maxLinearAcceleration = 600.0f;
        maxAngularAcceleration = 600.0f;
        texture = assetManager.get(AssetPaths.KAMIKAZE, Texture.class);
        steeringBehavior =
                new Arrive<>(this, target).setDecelerationRadius(GameConfig.WIDTH * 0.1f);
    }

    @Override
    public void update(float delta, Vector2 playerPosition){
        super.update(delta, playerPosition);
        if (isNear(hitbox.x, hitbox.y, playerPosition.x, playerPosition.y, explodingRange)){
            isAlive = false;
        }
    }
}
