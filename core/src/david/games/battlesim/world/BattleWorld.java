package david.games.battlesim.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import java.util.ArrayList;
import java.util.Iterator;

import static david.games.battlesim.BattleGame.assetManager;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.config.database.EnemyConfigDatabase;

import david.games.battlesim.config.database.EnemySpawnConfig;
import david.games.battlesim.config.database.LevelConfig;
import david.games.battlesim.config.database.PlayerConfigDatabase;
import david.games.battlesim.config.database.LevelWaveConfig;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.elements.actors.Bullet;
import david.games.battlesim.elements.actors.Enemy;
import david.games.battlesim.elements.actors.ForceField;
import david.games.battlesim.elements.actors.KamikazeEnemy;
import david.games.battlesim.elements.actors.Player;
import david.games.battlesim.elements.actors.ShooterEnemy;
import david.games.battlesim.elements.actors.SlasherEnemy;
import david.games.battlesim.elements.spawners.BulletSpawner;
import david.games.battlesim.elements.spawners.ForceFieldSpawner;
import david.games.battlesim.util.InputState;

public class BattleWorld {
    public WorldState state;
    public Vector3 mousePosition;
    private Texture gameBackground;
    public GameContext context;

    public Player player;
    public ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private Pool<Bullet> bulletPool;
    private ArrayList<ForceField> forceFields;
    EnemyConfigDatabase enemyConfigDatabase;
    public LevelConfig levelConfig;
    public int currentWave = 0;
    public float levelTimer = 0f;

    public BattleWorld() {
        state = WorldState.STOPPED;
        gameBackground = assetManager.get(AssetPaths.GAME_BACKGROUND, Texture.class);

        // Camera
        mousePosition = new Vector3(0,0,0);

        // Player, input, enemies
        player = new Player(new PlayerConfigDatabase().get(), 100f, 100f);

        enemies = new ArrayList<>();
        enemyConfigDatabase = new EnemyConfigDatabase();

        // Context creation
        context = new GameContext();
        context.enemies = enemies;
        context.player = player;

        /// Bullets (they are pooled)
        context.bulletSpawner = new BulletSpawner() {
            @Override
            public void spawn(float x, float y, float angle, float speed, float damage, boolean fromPlayer) {
                Bullet bullet = bulletPool.obtain();
                bullet.initFromPool(x, y, angle, speed, damage, fromPlayer);
                bullets.add(bullet);
            }
        };

        bullets = new ArrayList<>();
        bulletPool = Pools.get(Bullet.class, 15);
        bulletPool.fill(5);

        /// Force fields
        context.forceFieldSpawner = new ForceFieldSpawner() {
            @Override
            public void spawn(float x, float y, float damage, float duration, float size, boolean fromPlayer, boolean waning) {
                ForceField field = new ForceField(x, y, damage, duration, size, fromPlayer, waning);
                forceFields.add(field);
            }
        };

        forceFields = new ArrayList<>();
    }

    /********************* UPDATE *********************/
    public void update(float delta, InputState inputState){
        mousePosition = inputState.mousePosition;

        if (inputState.debugSpawnEnemy) {
            enemies.add(new ShooterEnemy(enemyConfigDatabase.get("shooter"), mousePosition.x, mousePosition.y));
            enemies.add(new KamikazeEnemy(enemyConfigDatabase.get("kamikaze"),mousePosition.x, mousePosition.y));
            enemies.add(new SlasherEnemy(enemyConfigDatabase.get("slasher"), mousePosition.x, mousePosition.y));
        }
        if (inputState.resetGamePressed) {
            reset();
        }

        updateLevelProgression();

        updatePlayer(delta, inputState);
        updateOtherActors(delta);
        updateEnemies(delta);

        updateTimers(delta);
    }
    private void updatePlayer(float delta, InputState inputState){
        player.inputDirection = inputState.direction;

        if (inputState.shootBulletPressed) {
            player.shootBullet(context.bulletSpawner);
        }
        if (inputState.phasePressed) {
            player.phase();
        }
        if (inputState.forceFieldPressed) {
            player.forceField(context.forceFieldSpawner);
        }
        player.setShieldState(inputState.shieldActive);

        player.update(delta);

        if (player.health <= 0f) {
            stopStage(WorldState.LOST);
        }
    }
    private void updateEnemies(float delta){
        for (Iterator<Enemy> it_e = enemies.iterator(); it_e.hasNext();) {
            Enemy enemy = it_e.next();
            enemy.update(delta, context);
            if (!enemy.isAlive) { it_e.remove(); }
        }

        if (!isWavesLeft() && enemies.isEmpty()) {
            stopStage(WorldState.WON);
        }
    }
    private void updateOtherActors(float delta){
        Bullet bullet;
        for (int i = bullets.size(); --i >= 0;) {
            bullet = bullets.get(i);
            if (!bullet.isAlive) {
                bullets.remove(i);
                bulletPool.free(bullet);
            } else {
                bullet.update(delta, context);
            }
        }

        ForceField forceField;
        for (int i = forceFields.size(); --i >= 0;) {
            forceField = forceFields.get(i);
            if (!forceField.isAlive) {
                forceFields.remove(i);
            } else {
                forceField.update(delta, context);
            }
        }
    }

    private void updateTimers(float delta) {
        // Slowed effect timer
        if (levelTimer > 0f) {
            levelTimer -= delta;
            if (levelTimer <= 0f){
                levelTimer = 0f;
                stopStage(WorldState.LOST);
            }
        }
    }

    /********************* DRAW *********************/
    public void draw(SpriteBatch batch){
        batch.draw(gameBackground, 0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);
        drawPlayer(batch);
        drawEnemies(batch);
        drawOtherActors(batch);
    }
    private void drawPlayer(SpriteBatch batch){
        player.draw(batch, mousePosition.x, mousePosition.y);
    }
    private void drawEnemies(SpriteBatch batch){
        for (Enemy enemy: enemies){
            enemy.draw(batch);
        }
    }
    private void drawOtherActors(SpriteBatch batch){
        for (Bullet bullet: bullets){
            bullet.draw(batch);
        }
        for (ForceField field: forceFields) {
            field.draw(batch);
        }
    }

    /********************* LEVEL LOGIC *********************/
    public void startLevel(LevelConfig levelConfig) {
        this.levelConfig = levelConfig;

        if (state == WorldState.RUNNING) {
            System.out.println("World already running, skipped start stage");
            return;
        }

        // Process config and set up stage
        player.setPosition(levelConfig.playerStart.x, levelConfig.playerStart.y);
        levelTimer = levelConfig.timeLimit;
        state = WorldState.RUNNING;
    }

    private void updateLevelProgression() {
        if (levelConfig.waves.isEmpty() || !isWavesLeft()) {
            return;
        }

        // Get the next wave (first element) and then remove it after processing it
        LevelWaveConfig nextWave = levelConfig.waves.get(currentWave);
        if (
           (nextWave.timeLeft != -1f && nextWave.timeLeft >= levelTimer) ||
           (nextWave.enemiesLeft != -1f && nextWave.enemiesLeft == enemies.size()))
        {
            spawnEnemiesFromConfig(nextWave.spawns);
            currentWave++;
        }
    }

    private void spawnEnemiesFromConfig(ArrayList<EnemySpawnConfig> spawns) {
        for (EnemySpawnConfig enemySpawn : spawns) {
            switch (enemySpawn.type) {
                case "kamikaze":
                    enemies.add(new KamikazeEnemy(enemyConfigDatabase.get(enemySpawn.type), enemySpawn.position.x, enemySpawn.position.y));
                    break;
                case "shooter":
                    enemies.add(new ShooterEnemy(enemyConfigDatabase.get(enemySpawn.type), enemySpawn.position.x, enemySpawn.position.y));
                    break;
                case "slasher":
                    enemies.add(new SlasherEnemy(enemyConfigDatabase.get(enemySpawn.type), enemySpawn.position.x, enemySpawn.position.y));
                    break;
                default: break;
            }
        }
    }

    public void stopStage(WorldState state) {
        this.state = state;
        // Lose, win screen etc.
    }

    private boolean isWavesLeft() {
        return currentWave < levelConfig.waves.size();
    }


    /********************* WORLD OPERATIONS *********************/
    public void reset() {
        player.reset();
        player.setPosition(100f, 100f);
        enemies.clear();
        forceFields.clear();

        Bullet bullet;
        for (int i = bullets.size(); --i >= 0;) {
            bullet = bullets.get(i);
            bullets.remove(i);
            bulletPool.free(bullet);
        }

        levelTimer = levelConfig.timeLimit;
        currentWave = 0;
    }

    public void dispose() {
        // Potential manually loaded textures
    }
}
