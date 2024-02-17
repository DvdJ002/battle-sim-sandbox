package david.games.battlesim.elements;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.MovementUtil.findNearestPathToPoint;
import static david.games.battlesim.util.MovementUtil.isNear;

public class SlasherEnemy extends Enemy {
    boolean dashing = false;
    private float dashTimer = 0f, dashCooldown = 1f;

    public SlasherEnemy(float x, float y) {
        super(x, y);
        texture = assetManager.get(AssetPaths.SLASHER, Texture.class);
        steeringBehavior =
                new Arrive<>(this, target).setDecelerationRadius(GameConfig.WIDTH * 1.5f);
    }

    @Override
    public void update(float delta, Vector2 playerPosition){
        super.update(delta, playerPosition);

        if (dashing) {
            dashTimer += delta;
            if (dashTimer > dashCooldown) {
                dashTimer = 0f;
                dashing = false;
                texture = assetManager.get(AssetPaths.SLASHER, Texture.class);
            }
        } else if (isNear(playerPosition.x, playerPosition.y, hitbox.x, hitbox.y, 200f)){
            this.dash(400f, playerPosition);
        }
    }

    public void dash(float intensity, Vector2 playerPo){
        if (!dashing) {
            texture = assetManager.get(AssetPaths.SLASHER_ATTACK, Texture.class);
            dashing = true;
            Vector2 movementVec = findNearestPathToPoint(position.x, position.y, playerPo.x, playerPo.y);
            linearVelocity.x += movementVec.x * intensity;
            linearVelocity.y += movementVec.y * intensity;
        }
    }
}
