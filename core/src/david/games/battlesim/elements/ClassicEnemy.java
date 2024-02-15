package david.games.battlesim.elements;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.MovementUtil.findNearestPathToPoint;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;

public class ClassicEnemy implements Steerable<Vector2> {
    public Rectangle hitbox;
    public EnemyType type;
    Texture texture;
    SteerableTargetObj target;

    private static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    private Vector2 linearVelocity = new Vector2(1f, 1f), position;
    SteeringBehavior<Vector2> steeringBehavior;
    private float orientation, angularVelocity;
    private float maxLinearSpeed = 1500.0f, maxLinearAcceleration = 200.0f, boundingRadius;
    private float maxAngularSpeed = 1500.0f, maxAngularAcceleration = 200.0f;
    private float zeroLinearSpeedThreshold = 5f;
    boolean tagged, dashing = false;
    private float dashTimer = 0f, dashCooldown = 1f;

    public ClassicEnemy(EnemyType type, float x, float y) {
        switch(type){
            case SLASHER:
                texture = assetManager.get(AssetPaths.SLASHER, Texture.class);
                hitbox = new Rectangle();
                hitbox.x = x;
                hitbox.y = y;
                hitbox.width = GameConfig.WIDTH/14;
                hitbox.height = GameConfig.WIDTH/14;
                this.type = type;
                position = new Vector2(hitbox.x, hitbox.y);
                target = new SteerableTargetObj(100f, 100f, 100f);
                steeringBehavior =
                        new Arrive<>(this, target).setDecelerationRadius(GameConfig.WIDTH * 1.5f);
                boundingRadius = 300f;
                break;

            case SHOOTER:
                break;
        }
    }

    public void draw(SpriteBatch batch){
        batch.draw(
                texture, hitbox.x, hitbox.y, hitbox.width, hitbox.height,
                0, 0, texture.getWidth(), texture.getHeight(), false, false
        );
    }

    public void update(float delta, Vector2 playerPosition){
        // System.out.println("New linear velocity: " + linearVelocity.x + ", " + linearVelocity.y);
        if (steeringBehavior != null){
            target.updatePosition(playerPosition.x, playerPosition.y);
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, delta);
            applyBounds();
            hitbox.x = position.x;
            hitbox.y = position.y;
        }

        if (dashing) {
            dashTimer += delta;
            if (dashTimer > dashCooldown) {
                dashTimer = 0f;
                dashing = false;
                texture = assetManager.get(AssetPaths.SLASHER, Texture.class);
            }
        }
    }

    private void applySteering (SteeringAcceleration<Vector2> steering, float delta) {
        linearVelocity.mulAdd(steering.linear, delta).limit(getMaxLinearSpeed());
        position.mulAdd(linearVelocity, delta);
    }
    private void applyBounds(){
        boolean corrected = false;
        if (position.y > GameConfig.HEIGHT - hitbox.height) {
            position.y = GameConfig.HEIGHT - hitbox.height - 1f;
            corrected = true;
        }
        if (position.y < 0f) {
            position.y = 1f;
            corrected = true;
        }
        if (position.x < 0f) {
            position.x = 1f;
            corrected = true;
        }
        if (position.x > GameConfig.WIDTH - hitbox.width) {
            position.x = GameConfig.WIDTH - hitbox.width - 1f;
            corrected = true;
        }

        if (corrected){
            linearVelocity.x = 0f;
            linearVelocity.y = 0f;
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

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return angularVelocity;
    }

    @Override
    public float getBoundingRadius() {
        return boundingRadius;
    }

    @Override
    public boolean isTagged() {
        return tagged;
    }

    @Override
    public void setTagged(boolean tagged) {
        this.tagged = tagged;
    }

    @Override
    public float getZeroLinearSpeedThreshold() {
        return zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.zeroLinearSpeedThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        this.orientation = orientation;
    }

    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }

    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }

    @Override
    public Location<Vector2> newLocation() {
        return new ClassicEnemy(EnemyType.SHOOTER, 100f, 100f);
    }
}
