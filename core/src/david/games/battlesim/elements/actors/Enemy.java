package david.games.battlesim.elements.actors;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.config.database.EnemyConfig;
import david.games.battlesim.config.EnemySteeringState;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.GameContext;

public class Enemy implements Steerable<Vector2> {
    public Rectangle hitbox;
    Texture texture;
    SteerableTargetObj target;
    EnemyConfig enemyConfig;
    protected static final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<>(new Vector2());
    SteeringBehavior<Vector2> steeringBehavior;
    protected Vector2 linearVelocity = new Vector2(1f, 1f), position;
    public EnemySteeringState steeringState;
    public float damage, collideDamage, health, size;
    public boolean tagged, isAlive = true, isInvincible = false;
    public Enemy(EnemyConfig enemyConfig, float x, float y) {
        hitbox = new Rectangle();

        this.enemyConfig = enemyConfig;
        this.collideDamage = enemyConfig.collideDamage;
        this.size = enemyConfig.size;
        this.health = enemyConfig.maxHealth;
        this.damage = enemyConfig.baseDamage;
        this.steeringState = enemyConfig.steeringState;

        hitbox.x = x;
        hitbox.y = y;
        hitbox.width = enemyConfig.size;
        hitbox.height = enemyConfig.size;
        position = new Vector2(hitbox.x, hitbox.y);

        target = new SteerableTargetObj(100f, 100f);
    }

    public void draw(SpriteBatch batch){
        batch.draw(
                texture, hitbox.x, hitbox.y, hitbox.width, hitbox.height,
                0, 0, texture.getWidth(), texture.getHeight(), false, false
        );
    }

    public void update(float delta, GameContext context){
        Player player = context.player;
        Vector2 playerPosition = player.position;
        if (steeringBehavior != null){
            target.updatePosition(playerPosition.x, playerPosition.y);
            steeringBehavior.calculateSteering(steeringOutput);
            applySteering(steeringOutput, delta);
            applyBounds();
        }
    }

    /********************* OPERATIONS *********************/
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

    public void takeHit(DamageAction damageAct){
        if (isInvincible) {
            return;
        }

        health -= damageAct.amount;
        // Check if health went over 100 or under 0
        if (health <= 0f){ isAlive = false; }
        else if (health > enemyConfig.maxHealth) { health = enemyConfig.maxHealth; }

        // Effects, particles etc.
    }

    public float getHealthPercentage() {
        return health/enemyConfig.maxHealth;
    }

    /********************* MOVEMENT BEHAVIOR & METHOD OVERRIDES *********************/
    private void applySteering (SteeringAcceleration<Vector2> steering, float delta) {
        linearVelocity.mulAdd(steering.linear, delta).limit(getMaxLinearSpeed());
        position.mulAdd(linearVelocity, delta);
        hitbox.x = position.x;
        hitbox.y = position.y;
    }

    @Override
    public Vector2 getLinearVelocity() {
        return linearVelocity;
    }

    @Override
    public float getAngularVelocity() {
        return steeringState.angularVelocity;
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
        return steeringState.zeroLinearSpeedThreshold;
    }

    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        steeringState.zeroLinearSpeedThreshold = value;
    }

    @Override
    public float getMaxLinearSpeed() {
        return steeringState.maxLinearSpeed;
    }

    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        steeringState.maxLinearSpeed = maxLinearSpeed;
    }

    @Override
    public float getMaxLinearAcceleration() {
        return steeringState.maxLinearAcceleration;
    }

    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        steeringState.maxLinearAcceleration = maxLinearAcceleration;
    }

    @Override
    public float getMaxAngularSpeed() {
        return steeringState.maxAngularSpeed;
    }

    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        steeringState.maxAngularSpeed = maxAngularSpeed;
    }

    @Override
    public float getMaxAngularAcceleration() {
        return steeringState.maxAngularAcceleration;
    }

    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        steeringState.maxAngularAcceleration = maxAngularAcceleration;
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public float getOrientation() {
        return steeringState.orientation;
    }

    @Override
    public void setOrientation(float orientation) {
        steeringState.orientation = orientation;
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
        return new Enemy(new EnemyConfig(0f, 0f, 0f, 0f), 100f, 100f);
    }
}
