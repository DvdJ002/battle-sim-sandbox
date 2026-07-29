package david.games.battlesim.elements.actors;

import static com.badlogic.gdx.math.Intersector.overlaps;
import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.util.GameUtil.findNearestPathToPoint;
import static david.games.battlesim.util.GameUtil.isNear;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetDescriptors;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
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
    private final Sound explosionSound, slamSound, spawnSound;

    // State enums
    public BossState state;
    public BossPhase phase;
    BossAttack currentAttack;

    // Timers
    public float idleTimer = 0.0f;
    public float nextDashTimer = 0.0f;
    public float slamTimer = 0.0f;
    public float bulletTimer = 0.0f, reloadTimer = 0.0f;
    public float explosionGraceTimer = 0.0f, explosionSeekTimer = 0.0f;
    public float cannonTimer = 0.0f, cannonReloadTimer = 0.0f;
    public float kamikazeDurationTimer = 0.0f, kamikazeSpawnTimer = 0.0f;
    public float pursuitTimer = 0.0f;

    // Boolean and integer states
    public int dashesLeft = 0;

    public Boss(EnemyConfig bossConfig, float x, float y){
        super(bossConfig, x, y);
        this.config = (EnemyConfig.BossConfig) bossConfig;

        texture = assetManager.get(AssetPaths.BOSS_1, Texture.class);
        explosionSound = assetManager.get(AssetDescriptors.KAMIKAZE_EXPLOSION_SOUND);
        slamSound = assetManager.get(AssetDescriptors.PLAYER_ULTIMATE_SOUND);
        spawnSound = assetManager.get(AssetDescriptors.ENEMY_SPAWN_SOUND);

        // Start boss in phase 1 idle state
        phase = BossPhase.PHASE_1;
        finishAttacking();

        steeringBehavior = new Arrive<>(this, target);
    }

    @Override
    public void update(float delta, GameContext context){
        super.update(delta, context);

        checkHealth(context);
        checkPhaseChange();

        Player player = context.player;
        updateTracking(player.position);

        // Damage player if touching
        if ((player.shielding && overlaps(player.shieldHitbox, hitbox)) || (!player.shielding && overlaps(player.hitbox, hitbox))) {
            DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.NONE, collideDamage, 0f, 0f);
            damageAct.sourcePosition = new Vector2(position.x, position.y);
            player.takeHit(damageAct);
        }

        // Target location might be updated here!
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
                chooseAttack(context);
                initializeAttack(context);
                idleTimer = phase == BossPhase.PHASE_1 ? config.baseIdleDuration : config.enragedIdleDuration;
            }
        }
    }

    private void updateTracking(Vector2 playerPosition) {
        if (phase == BossPhase.PHASE_1) {
            updateMirroringTargetLocation(playerPosition);
        }
        else {
            updateSteeringTarget(playerPosition.x, playerPosition.y);
        }
    }

    private void checkPhaseChange() {
        if (health <= config.maxHealth * config.enragedHealthThreshold && phase == BossPhase.PHASE_1) {
            phase = BossPhase.PHASE_2;
            texture = assetManager.get(AssetPaths.BOSS_2, Texture.class);

            steeringState.maxLinearSpeed = config.enragedSpeed;
            steeringState.maxAngularSpeed = config.enragedSpeed;
            steeringState.maxLinearAcceleration = config.enragedAcceleration;
            steeringState.maxAngularAcceleration = config.enragedAcceleration;
        }
    }
    // Boss tries to mirror the player across the center of the arena
    private void updateMirroringTargetLocation(Vector2 playerPosition) {
        float anglePlayerToCenter = GameUtil.findAngleBetweenPoints(playerPosition.x, playerPosition.y, GameConfig.WIDTH/2f, GameConfig.HEIGHT/2f);
        float distancePlayerToCenter = Vector2.dst(playerPosition.x, playerPosition.y, GameConfig.WIDTH/2f, GameConfig.HEIGHT/2f);
        // Apply bounds
        Vector2 mirroredLocation = GameUtil.angleToCirclePoints(GameConfig.WIDTH/2f, GameConfig.HEIGHT/2f,  distancePlayerToCenter, anglePlayerToCenter);
        if (mirroredLocation.x > GameConfig.WIDTH * 0.8f) {
            mirroredLocation.x = GameConfig.WIDTH * 0.8f;
        }
        if (mirroredLocation.y > GameConfig.HEIGHT * 0.8f) {
            mirroredLocation.y = GameConfig.HEIGHT * 0.8f;
        }
        updateSteeringTarget(mirroredLocation.x, mirroredLocation.y);
    }

    // When the boss dies it is a special case because all the spawned enemies must die as well
    private void checkHealth(GameContext context) {
        if (health <= 0f){
            for (Enemy enemy : context.enemies) {
                enemy.isAlive = false;
            }
        }
    }

    // Boss checks its own health so this method is not applicable
    @Override
    public void die() { }


    /*******************************************************************************/
    /******************************** ATTACK GENERAL *******************************/
    /*******************************************************************************/
    // Called only once after idle timer runs out, chooses the new attack that is later to be initialized
    private void chooseAttack(GameContext context) {
        int enemyCount = context.enemies.size();

        // Choose attacks based on phase
        if (phase == BossPhase.PHASE_1) {
            // If close range, choose from closeRangeAttackPool, otherwise from longRangeAttackPool
            currentAttack = isNear(position.x, position.y, context.player.position.x, context.player.position.y, config.closeDetectionRange)
                    ? BossAttack.values()[config.closeRangeAttackPool.get(MathUtils.random(config.closeRangeAttackPool.size() - 1))]
                    : BossAttack.values()[config.longRangeAttackPool.get(MathUtils.random(config.longRangeAttackPool.size() - 1))];

            //------------ Applying possible attack rules, limitations, etc. here ------------//
            if (currentAttack == BossAttack.SUMMON_OFFENSIVES && enemyCount > 1) {
                // Don't summon new attackers if enemies are already present as RNG can get too overwhelming, summon healers instead
                currentAttack = BossAttack.SUMMON_HEALERS;
            }
            if (currentAttack == BossAttack.SUMMON_HEALERS && enemyCount > 4) {
                // Don't spawn healers if there are already more than 3 enemies spawned
                currentAttack = BossAttack.DASH;
            }
        }
        else {
            // Summon enemies in phase 2 before starting pursuit. Only start pursuit if existing kamikazes
            currentAttack = (enemyCount > 1) ? BossAttack.PURSUIT : BossAttack.SUMMON_KAMIKAZES;
        }

        //currentAttack = BossAttack.PURSUIT;

        state = BossState.ATTACKING;
    }

    private void initializeAttack(GameContext context) {
        texture = (phase == BossPhase.PHASE_1) ? assetManager.get(AssetPaths.BOSS_1_ATTACK, Texture.class) : assetManager.get(AssetPaths.BOSS_2_ATTACK, Texture.class);

        switch (currentAttack) {
            case DASH:
                initializeDash();
                break;
            case BULLETS:
                initializeBullets();
                break;
            case SLAM:
                initializeSlam(context);
                break;
            case EXPLOSION:
                initializeExplosion();
                break;
            case CANNONBALLS:
                initializeCannonballs(context);
                break;
            case SUMMON_KAMIKAZES:
                initializeSummonKamikazes(context);
                break;
            case PURSUIT:
                initializePursuit();
                break;
                // Those two need no initialization because they're finished in one iteration
            case SUMMON_OFFENSIVES:
            case SUMMON_HEALERS:
            default:
                break;
        }
    }

    private void updateAttack(float delta, GameContext context) {
        switch (currentAttack) {
            case DASH:
                updateDash(delta, context);
                break;
            case BULLETS:
                updateBullets(delta, context);
                break;
            case SUMMON_OFFENSIVES:
                updateSummonOffensives(context);
                break;
            case SUMMON_HEALERS:
                updateSummonHealers(context);
                break;
            case SLAM:
                updateSlam(delta);
                break;
            case EXPLOSION:
                updateExplosion(delta, context);
                break;
            case CANNONBALLS:
                updateCannonballs(delta, context);
                break;
            case SUMMON_KAMIKAZES:
                updateSummonKamikazes(delta, context);
                break;
            case PURSUIT:
                updatePursuit(delta);
                break;
            default:
                break;
        }
    }
    private void finishAttacking() {
        state = BossState.IDLE;
        currentAttack = BossAttack.NONE;
        idleTimer = config.baseIdleDuration;

        texture = (phase == BossPhase.PHASE_1) ? assetManager.get(AssetPaths.BOSS_1, Texture.class) : assetManager.get(AssetPaths.BOSS_2, Texture.class);
    }


    /*******************************************************************************/
    /******************************* ATTACK SPECIFIC *******************************/
    /*******************************************************************************/
    /* -------------------------- DASH ------------------------- */
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
    }

    /* -------------------------- BULLET HAUL ------------------------- */
    private void initializeBullets() {
        bulletTimer = config.bulletAttackDuration;
        reloadTimer = config.bulletFireRate;
    }
    private void updateBullets(float delta, GameContext context) {
        reloadTimer -= delta;
        if (reloadTimer <= 0f) {
            shootBulletSet(context);
            reloadTimer = config.bulletFireRate;
        }

        bulletTimer -= delta;
        if (bulletTimer <= 0f) {
            bulletTimer = 0f;
            reloadTimer = 0f;
            finishAttacking();
        }
    }

    // First find angle of enemy to player and left and right spawn points, then shoot bullets from those spawn points
    private void shootBulletSet(GameContext context) {
        Vector2 bossCenter = new Vector2(position.x + hitbox.width/2f, position.y + hitbox.height/2f);
        float angleEnemyCenterToPlayer = GameUtil.findAngleBetweenPoints(bossCenter.x, bossCenter.y, context.player.position.x, context.player.position.y);

        Vector2 spawnPointLeft = GameUtil.angleToCirclePoints(bossCenter.x, bossCenter.y, 30f, angleEnemyCenterToPlayer + 90f);
        float angleLeftPointToPlayer = GameUtil.findAngleBetweenPoints(spawnPointLeft.x, spawnPointLeft.y, context.player.position.x, context.player.position.y);
        Vector2 spawnPointRight = GameUtil.angleToCirclePoints(bossCenter.x, bossCenter.y, 30f, angleEnemyCenterToPlayer - 90f);
        float angleRightPointToPlayer = GameUtil.findAngleBetweenPoints(spawnPointRight.x, spawnPointRight.y, context.player.position.x, context.player.position.y);

        context.bulletSpawner.spawn(spawnPointLeft.x, spawnPointLeft.y, angleLeftPointToPlayer, config.bulletSpeed, config.bulletDamage, config.bulletSize,false);
        context.bulletSpawner.spawn(spawnPointRight.x, spawnPointRight.y, angleRightPointToPlayer, config.bulletSpeed, config.bulletDamage, config.bulletSize,false);
    }

    /* -------------------------- SUMMON ATTACK - OFFENSIVES ------------------------- */
    private void updateSummonOffensives(GameContext context) {
        summonOffensives(context);
        finishAttacking();
    }
    private void summonOffensives(GameContext context) {
        Vector2 bossCenter = new Vector2(position.x + hitbox.width/2f, position.y + hitbox.height/2f);
        float angleToPlayer = GameUtil.findAngleBetweenPoints(bossCenter.x, bossCenter.y, context.player.position.x, context.player.position.y);

        Vector2 spawnPointLeft = GameUtil.angleToCirclePoints(bossCenter.x, bossCenter.y, config.enemySpawnDistance, angleToPlayer + 90f);
        Vector2 spawnPointRight = GameUtil.angleToCirclePoints(bossCenter.x, bossCenter.y, config.enemySpawnDistance, angleToPlayer - 90f);

        context.enemies.add(new SlasherEnemy(context.enemyConfigDatabase.get("slasher"), spawnPointLeft.x, spawnPointLeft.y));
        context.enemies.add(new ShooterEnemy(context.enemyConfigDatabase.get("shooter"), spawnPointRight.x, spawnPointRight.y));

        spawnSound.play(GameConfig.VOLUME_DEFAULT);
    }

    /* -------------------------- SUMMON ATTACK - HEALERS ------------------------- */
    private void updateSummonHealers(GameContext context) {
        summonHealers(context);
        finishAttacking();
    }
    private void summonHealers(GameContext context) {
        Vector2 bossCenter = new Vector2(position.x + hitbox.width/2f, position.y + hitbox.height/2f);
        float angleToPlayer = GameUtil.findAngleBetweenPoints(bossCenter.x, bossCenter.y, context.player.position.x, context.player.position.y);

        //Vector2 spawnPointLeft = GameUtil.angleToCirclePoints(bossCenter.x, bossCenter.y, config.kamikazeSpawnDistance, angleToPlayer + 150f);
        //Vector2 spawnPointRight = GameUtil.angleToCirclePoints(bossCenter.x, bossCenter.y, config.kamikazeSpawnDistance, angleToPlayer + 210f);
        Vector2 spawnPointBehind = GameUtil.angleToCirclePoints(bossCenter.x, bossCenter.y, config.enemySpawnDistance, angleToPlayer + 180f);

        context.enemies.add(new HealerEnemy(context.enemyConfigDatabase.get("healer"), spawnPointBehind.x, spawnPointBehind.y));

        spawnSound.play(GameConfig.VOLUME_DEFAULT);
    }

    /* -------------------------- SLAM ATTACK ------------------------- */
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

        slamSound.play(GameConfig.VOLUME_LOUD);
    }

    /* -------------------------- EXPLOSION ATTACK ------------------------- */
    private void initializeExplosion() {
        steeringState.maxLinearSpeed = config.explosionSeekSpeed;
        steeringState.maxAngularSpeed = config.explosionSeekSpeed;
        steeringState.maxLinearAcceleration = config.explosionSeekAccel;
        steeringState.maxAngularAcceleration = config.explosionSeekAccel;

        explosionGraceTimer = config.explosionGraceDuration;
        explosionSeekTimer = config.explosionSeekDuration;
        isStatic = true;
    }
    private void updateExplosion(float delta, GameContext context) {
        // In the grace period boss stands still (telemarked attack)
        if (explosionGraceTimer > 0.0f) {
            explosionGraceTimer -= delta;
            if (explosionGraceTimer <= 0f) {
                explosionGraceTimer = 0f;
                isStatic = false;
            }
        }

        if (!isStatic) {
            // Finish attack if timer ran out
            explosionSeekTimer -= delta;
            if (explosionSeekTimer <= 0f) {
                finishExplodeAttack();
            }

            // Boss starts moving towards the player and explodes if in range
            updateSteeringTarget(context.player.position.x, context.player.position.y);
            if (isNear(hitbox.x, hitbox.y, context.player.position.x, context.player.position.y, config.explosionDetectionRange)) {
                explode(context);
                finishExplodeAttack();
            }
        }
    }
    private void explode(GameContext context) {
        DamageAction damageAct = GameUtil.getDamageAction(StatusEffect.KNOCKBACK, config.explosionDamage, config.explosionKbIntensity, 0f);
        damageAct.sourcePosition = new Vector2(position.x, position.y);
        context.player.takeHit(damageAct);

        explosionSound.play(GameConfig.VOLUME_DEFAULT);
    }

    private void finishExplodeAttack() {
        steeringState.maxLinearSpeed = config.baseSpeed;
        steeringState.maxAngularSpeed = config.baseSpeed;
        steeringState.maxLinearAcceleration = GameConfig.DEFAULT_ACCEL;
        steeringState.maxAngularAcceleration = GameConfig.DEFAULT_ACCEL;
        finishAttacking();
    }

    /* -------------------------- CANNONBALL ATTACK ------------------------- */
    private void initializeCannonballs(GameContext context) {
        cannonTimer = config.cannonDuration;
        cannonReloadTimer = config.cannonFireRate;
        shootCannonballs(context);
    }
    private void updateCannonballs(float delta, GameContext context) {
        cannonReloadTimer -= delta;
        if (cannonReloadTimer <= 0f) {
            shootCannonballs(context);
            cannonReloadTimer = config.cannonFireRate;
        }

        cannonTimer -= delta;
        if (cannonTimer <= 0f) {
            cannonTimer = 0f;
            cannonReloadTimer = 0f;
            finishAttacking();
        }
    }
    private void shootCannonballs(GameContext context) {
        Vector2 bossCenter = new Vector2(position.x + hitbox.width/2f, position.y + hitbox.height/2f);
        float angleEnemyCenterToPlayer = GameUtil.findAngleBetweenPoints(bossCenter.x, bossCenter.y, context.player.position.x, context.player.position.y);

        context.bulletSpawner.spawn(bossCenter.x, bossCenter.y, angleEnemyCenterToPlayer, config.cannonSpeed, config.cannonDamage, config.cannonSize,false);
    }

    /* -------------------------- SUMMON KAMIKAZES ------------------------- */
    private void initializeSummonKamikazes(GameContext context) {
        kamikazeDurationTimer = config.kamikazeAttackDuration;
        kamikazeSpawnTimer = config.kamikazeSpawnPeriod;
        summonKamikaze(context);
    }
    private void updateSummonKamikazes(float delta, GameContext context) {
        kamikazeSpawnTimer -= delta;
        if (kamikazeSpawnTimer <= 0f) {
            summonKamikaze(context);
            kamikazeSpawnTimer = config.kamikazeSpawnPeriod;
        }

        kamikazeDurationTimer -= delta;
        if (kamikazeDurationTimer <= 0f) {
            kamikazeDurationTimer = 0f;
            kamikazeSpawnTimer = 0f;
            finishAttacking();
        }
    }
    private void summonKamikaze(GameContext context) {
        Vector2 bossCenter = new Vector2(position.x + hitbox.width/2f, position.y + hitbox.height/2f);
        float angleToPlayer = GameUtil.findAngleBetweenPoints(bossCenter.x, bossCenter.y, context.player.position.x, context.player.position.y);
        Vector2 spawnPointFront = GameUtil.angleToCirclePoints(bossCenter.x, bossCenter.y, config.enemySpawnDistance, angleToPlayer);

        context.enemies.add(new KamikazeEnemy(context.enemyConfigDatabase.get("kamikaze"), spawnPointFront.x, spawnPointFront.y));

        spawnSound.play(GameConfig.VOLUME_DEFAULT);
    }

    /* -------------------------- PURSUIT ATTACK ------------------------- */
    private void initializePursuit() {
        pursuitTimer = config.pursuitDuration;
        isInvincible = true;
        collideDamage = config.pursuitCollideDamage;

        steeringState.maxLinearSpeed = config.pursuitSpeed;
        steeringState.maxAngularSpeed = config.pursuitSpeed;
        steeringState.maxLinearAcceleration = config.pursuitAccel;
        steeringState.maxAngularAcceleration = config.pursuitAccel;
    }
    private void updatePursuit(float delta) {
        pursuitTimer -= delta;
        if (pursuitTimer <= 0f) {
            isInvincible = false;
            collideDamage = config.collideDamage;

            steeringState.maxLinearSpeed = config.enragedSpeed;
            steeringState.maxAngularSpeed = config.enragedSpeed;
            steeringState.maxLinearAcceleration = config.enragedAcceleration;
            steeringState.maxAngularAcceleration = config.enragedAcceleration;
            finishAttacking();
        }
    }
}
