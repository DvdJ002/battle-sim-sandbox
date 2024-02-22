package david.games.battlesim.elements;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Objects;

import david.games.battlesim.config.GameConfig;

public class Enemy implements Steerable<Vector2> {
    public Rectangle hitbox;
    Texture texture;
    SteerableTargetObj target;

    protected static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    SteeringBehavior<Vector2> steeringBehavior;
    protected Vector2 linearVelocity = new Vector2(1f, 1f), position;
    protected float orientation, angularVelocity, zeroLinearSpeedThreshold = 5f;
    protected float maxLinearSpeed = 1500.0f, maxLinearAcceleration = 200.0f;
    protected float maxAngularSpeed = 1500.0f, maxAngularAcceleration = 200.0f;
    protected float health = 100f;
    public boolean tagged, isAlive = true;
    public Enemy(float x, float y) {
        hitbox = new Rectangle();
        hitbox.x = x;
        hitbox.y = y;
        hitbox.width = GameConfig.WIDTH/14;
        hitbox.height = GameConfig.WIDTH/14;
        position = new Vector2(hitbox.x, hitbox.y);
        target = new SteerableTargetObj(100f, 100f);
    }

    public void draw(SpriteBatch batch){
        batch.draw(
                texture, hitbox.x, hitbox.y, hitbox.width, hitbox.height,
                0, 0, texture.getWidth(), texture.getHeight(), false, false
        );
    }

    public void update(float delta, Vector2 playerPosition){
        if (steeringBehavior != null){
            target.updatePosition(playerPosition.x, playerPosition.y);
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, delta);
            applyBounds();
        }
    }

    private void applySteering (SteeringAcceleration<Vector2> steering, float delta) {
        linearVelocity.mulAdd(steering.linear, delta).limit(getMaxLinearSpeed());
        position.mulAdd(linearVelocity, delta);
        hitbox.x = position.x;
        hitbox.y = position.y;
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

    public void takeHit(String type){
        if (Objects.equals(type, "bullet")) { health -= 10f; }
        // Check if health went over 100 or under 0
        if (health <= 0f){ isAlive = false; }
        else if (health > 100) { health = 100f; }
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
        return 300f;
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
        return new Enemy(100f, 100f);
    }
}
