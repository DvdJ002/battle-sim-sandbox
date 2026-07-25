package david.games.battlesim.elements.actors;

import static com.badlogic.gdx.math.Intersector.overlaps;
import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.GameUtil.findNearestPathToPoint;
import static david.games.battlesim.util.GameUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetDescriptors;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.database.EnemyConfig;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.elements.damage.BossAttack;
import david.games.battlesim.elements.damage.BossPhase;
import david.games.battlesim.elements.damage.BossState;
import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.damage.StatusEffect;
import david.games.battlesim.util.GameUtil;

public class Boss extends Enemy {

    public final EnemyConfig.BossConfig config;
    private final Sound sound;

    // State enums
    BossState state;
    BossPhase phase;
    BossAttack currentAttack;

    // Timers
    public float idleTimer = 0.0f;
    public float nextDashTimer = 0.0f;
    public float slamTimer = 0.0f;

    // Boolean and integer states
    public int dashesLeft = 0;

    public Boss(EnemyConfig bossConfig, float x, float y){
        super(bossConfig, x, y);
        this.config = (EnemyConfig.BossConfig) bossConfig;

        texture = assetManager.get(AssetPaths.BOSS, Texture.class);
        sound = assetManager.get(AssetDescriptors.KAMIKAZE_EXPLOSION_SOUND);

        // Start boss in idle state
        finishAttacking();

        steeringBehavior = new Arrive<>(this, target);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Draws the boss itself
        super.draw(batch);
    }

    @Override
    public void update(float delta, GameContext context){
        Player player = context.player;
        Vector2 playerPosition = player.position;
        updateSteeringTarget(playerPosition.x, playerPosition.y);

        super.update(delta, context);

        // Damage player if touching
        if ((player.shielding && overlaps(player.shieldHitbox, hitbox)) || (!player.shielding && overlaps(player.hitbox, hitbox))) {
            DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.NONE, collideDamage, 0f, 0f);
            damageAct.sourcePosition = new Vector2(position.x, position.y);
            player.takeHit(damageAct);
        }

        if (state == BossState.ATTACKING) {
            updateAttack(delta, context);
        }
        else {
            updateIdle(delta, context);
        }
    }

    private void updateIdle(float delta, GameContext context) {
        if (idleTimer > 0f) {
            idleTimer -= delta;
            if (idleTimer <= 0f) {
                idleTimer = 0f;
                chooseAttack(context.player.position);
                initializeAttack(context);
            }
        }
    }

    /********************* ATTACK GENERAL *********************/
    // Called only once after idle timer runs out, chooses the new attack that is later to be initialized
    private void chooseAttack(Vector2 playerPosition) {
        // Close range vs long range attacks (TODO)
        /*if (isNear(position.x, position.y, playerPosition.x, playerPosition.y, config.closeDetectionRange)) {
            int attackCount = BossAttack.values().length;
            float attackRnd = MathUtils.random(0f, attackCount);
            currentAttack = BossAttack.values()[(int) attackRnd];
            if (currentAttack == BossAttack.BULLETS) {
                currentAttack = BossAttack.DASH;
            }
        }
        else {
            currentAttack = BossAttack.SUMMON;
        */

        // Pick from 1...length because 0 is NONE
        int attackCount = BossAttack.values().length;
        float attackRnd = MathUtils.random(1f, attackCount);
        currentAttack = BossAttack.values()[(int) attackRnd];
        if (currentAttack == BossAttack.BULLETS) {
            currentAttack = BossAttack.DASH;
        }

        state = BossState.ATTACKING;
        System.out.println("Chose attack: " + currentAttack.name());
    }

    private void initializeAttack(GameContext context) {
        switch (currentAttack) {
            case DASH:
                initializeDash();
                break;
            case SLAM:
                initializeSlam(context);
                break;
            case SUMMON:
            default:
                break;
        }
    }

    private void updateAttack(float delta, GameContext context) {
        switch (currentAttack) {
            case SUMMON:
                updateSummon(context);
                break;
            case DASH:
                updateDash(delta, context);
                break;
            case SLAM:
                updateSlam(delta);
                break;
            default:
                break;
        }
    }
    private void finishAttacking() {
        state = BossState.IDLE;
        currentAttack = BossAttack.NONE;
        idleTimer = config.idleDuration;

        System.out.println("Finished attack");
    }

    /********************* ATTACK SPECIFIC *********************/
    private void initializeSlam(GameContext context) {
        slam(context);
        slamTimer = config.forceFieldDuration;
    }
    private void updateSlam(float delta) {
        slamTimer -= delta;
        if (slamTimer <= 0f) {
            slamTimer = 0f;
            isInvincible = false;
            isStatic = false;
            finishAttacking();
        }
    }

    private void slam(GameContext context) {
        context.forceFieldSpawner.spawn(position.x + hitbox.width/2f, position.y + hitbox.height/2f, config.forceFieldDamage, config.forceFieldDuration, config.forceFieldSize, false, true);
        isInvincible = true;
        isStatic = true;
    }

    private void initializeDash() {
        // First dash will happen immediately
        nextDashTimer = 0.0f;
        dashesLeft = config.dashCount;
    }
    private void updateDash(float delta, GameContext context) {
        nextDashTimer -= delta;
        if (nextDashTimer <= 0f) {
            dash(context.player.position, (dashesLeft == 1) ? config.dashLastIntensity : config.dashIntensity);
            // The delay between second to last and last dash is faster (dashLastCooldown)
            nextDashTimer = dashesLeft == 2 ? config.dashLastCooldown : config.dashBaseCooldown;
            dashesLeft--;

            if (dashesLeft < 0) {
                steeringState.maxLinearSpeed = config.baseSpeed;
                steeringState.maxAngularSpeed = config.baseSpeed;
                nextDashTimer = 0.0f;
                finishAttacking();
            }

            if (dashesLeft == 0) {
                nextDashTimer = config.dashBaseCooldown;
                dashesLeft--;
            }
        }
    }

    private void dash(Vector2 playerPosition, float intensity) {
        // Uncap the speed
        steeringState.maxLinearSpeed = config.dashLastIntensity;
        steeringState.maxAngularSpeed = config.dashLastIntensity;

        Vector2 movementVec = findNearestPathToPoint(position.x, position.y, playerPosition.x, playerPosition.y);
        linearVelocity.x += movementVec.x * intensity;
        linearVelocity.y += movementVec.y * intensity;

        System.out.println("Dashing");
    }

    private void updateSummon(GameContext context) {
        summon(context);
        finishAttacking();
    }

    private void summon(GameContext context) {
        float angleToPlayer = GameUtil.findAngleBetweenPoints(position.x, position.y, context.player.position.x, context.player.position.y);

        Vector2 spawnPointLeft = GameUtil.angleToCirclePoints(position.x, position.y, config.kamikazeSpawnDistance, angleToPlayer + 90f);
        Vector2 spawnPointRight = GameUtil.angleToCirclePoints(position.x, position.y, config.kamikazeSpawnDistance, angleToPlayer - 90f);
        Vector2 spawnPointMiddle = GameUtil.angleToCirclePoints(position.x, position.y, config.kamikazeSpawnDistance, angleToPlayer);

        context.enemies.add(new SlasherEnemy(context.enemyConfigDatabase.get("slasher"), spawnPointLeft.x, spawnPointLeft.y));
        context.enemies.add(new ShooterEnemy(context.enemyConfigDatabase.get("shooter"), spawnPointRight.x, spawnPointRight.y));
        context.enemies.add(new KamikazeEnemy(context.enemyConfigDatabase.get("kamikaze"), spawnPointMiddle.x, spawnPointMiddle.y));
    }
}
