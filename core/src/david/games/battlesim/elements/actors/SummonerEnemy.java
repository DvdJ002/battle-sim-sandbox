package david.games.battlesim.elements.actors;

import static com.badlogic.gdx.math.Intersector.overlaps;
import static david.games.battlesim.BattleGame.assetManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.assets.AssetDescriptors;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.config.database.EnemyConfig;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.elements.data.StatusEffect;
import david.games.battlesim.util.GameUtil;

public class SummonerEnemy extends Enemy  {
    private final EnemyConfig.SummonerConfig summonerConfig;
    private final Vector2 mirroredLocation = new Vector2();
    private Sound summonSound;
    public int summonAmount, enemiesToSummon;
    public final float movementPadding, cooldownTime, summonDelayTime;
    public String kamikazeIdentifier, slasherIdentifier;
    // States
    private float summonTimer = 0.0f, cooldownTimer;
    private boolean summoning, spawnKamikaze = true;

    public SummonerEnemy(EnemyConfig enemyConfig, float x, float y){
        super(enemyConfig, x, y);
        this.summonerConfig = (EnemyConfig.SummonerConfig) enemyConfig;
        this.summonAmount = summonerConfig.spawnAmount;
        this.movementPadding = summonerConfig.movementPadding;
        this.cooldownTime = summonerConfig.cooldownTime;
        this.summonDelayTime = summonerConfig.spawnDelayTime;
        this.enemiesToSummon = summonerConfig.spawnAmount;
        this.kamikazeIdentifier = summonerConfig.kamikazeIdentifier;
        this.slasherIdentifier = summonerConfig.slasherIdentifier;

        texture = assetManager.get(AssetPaths.SUMMONER, Texture.class);
        summonSound = assetManager.get(AssetDescriptors.ENEMY_SPAWN_SOUND);
        steeringBehavior = new Arrive<>(this, target);
        damageAct = GameUtil.getDamageAction(StatusEffect.NONE, collideDamage, 0f, 0f);
        cooldownTimer = cooldownTime;
    }

    @Override
    public void update(float delta, GameContext context){
        Player player = context.player;
        Vector2 playerPosition = context.player.position;
        updateMirroringTargetLocation(playerPosition);

        super.update(delta, context);

        // Player damages summoner enemy if touching
        if (overlaps(player.hitbox, hitbox)) {
            damageAct.sourcePosition.set(player.position);
            takeHit(damageAct);
        }


        if (summoning) {
            summonTimer -= delta;
            if (summonTimer <= 0f) {
                handleSummon(context);
            }
        }

        if (cooldownTimer > 0f) {
            cooldownTimer -= delta;
            if (cooldownTimer <= 0f) {
                startSummoning();
            }
        }
    }

    private void startSummoning() {
        summoning = true;
        enemiesToSummon = summonAmount;
        summonTimer = summonDelayTime;
        cooldownTimer = 0.0f;
    }

    private void finishSummoning() {
        summoning = false;
        spawnKamikaze = !spawnKamikaze;
        cooldownTimer = cooldownTime;
    }

    private void handleSummon(GameContext context) {
        Enemy toSummon = spawnKamikaze ? new KamikazeEnemy(context.enemyConfigDatabase.get(kamikazeIdentifier), position.x, position.y)
                                       : new SlasherEnemy(context.enemyConfigDatabase.get(slasherIdentifier), position.x, position.y);
        context.enemies.add(toSummon);
        enemiesToSummon--;
        summonSound.play(GameConfig.VOLUME_DEFAULT);

        // Check if all enemies have been summoned
        if (enemiesToSummon <= 0) {
            finishSummoning();
        }
        else {
            summonTimer = summonDelayTime;
        }
    }

    private void updateMirroringTargetLocation(Vector2 playerPosition) {
        float anglePlayerToCenter = GameUtil.findAngleBetweenPoints(playerPosition.x, playerPosition.y, GameConfig.WIDTH/2f, GameConfig.HEIGHT/2f);
        // Apply bounds
        GameUtil.angleToCirclePoints(GameConfig.WIDTH/2f, GameConfig.HEIGHT/2f,  GameConfig.WIDTH * movementPadding, anglePlayerToCenter, mirroredLocation);
        if (mirroredLocation.x > GameConfig.WIDTH * movementPadding) {
            mirroredLocation.x = GameConfig.WIDTH * movementPadding;
        }
        if (mirroredLocation.y > GameConfig.HEIGHT * movementPadding) {
            mirroredLocation.y = GameConfig.HEIGHT * movementPadding;
        }

        if (mirroredLocation.x < movementPadding) {
            mirroredLocation.x = GameConfig.WIDTH - GameConfig.WIDTH * movementPadding;
        }
        if (mirroredLocation.y < movementPadding) {
            mirroredLocation.y = GameConfig.HEIGHT - GameConfig.HEIGHT * movementPadding;
        }

        updateSteeringTarget(mirroredLocation.x, mirroredLocation.y);
    }
}
