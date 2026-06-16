package david.games.battlesim.world;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import java.util.ArrayList;
import java.util.Iterator;

import static david.games.battlesim.BattleGame.assetManager;

import david.games.battlesim.BattleGame;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.config.database.EnemyConfigDatabase;

import david.games.battlesim.config.database.LevelConfig;
import david.games.battlesim.config.database.PlayerConfigDatabase;
import david.games.battlesim.elements.GameContext;
import david.games.battlesim.elements.actors.Bullet;
import david.games.battlesim.elements.actors.Enemy;
import david.games.battlesim.elements.actors.KamikazeEnemy;
import david.games.battlesim.elements.actors.Player;
import david.games.battlesim.elements.actors.ShooterEnemy;
import david.games.battlesim.elements.actors.SlasherEnemy;
import david.games.battlesim.elements.spawners.BulletSpawner;
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
    EnemyConfigDatabase enemyConfigDatabase;

    public BattleWorld(BattleGame game) {
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

        /// Bullets
        context.bulletSpawner = new BulletSpawner() {
            @Override
            public void spawn(float x, float y, float angle, float speed, float damage, boolean fromPlayer) {
                Bullet bullet = bulletPool.obtain();
                bullet.initFromPool(x, y, angle, speed, damage, fromPlayer);
                bullets.add(bullet);
            }
        };

        // Bullets (they are pooled)
        bullets = new ArrayList<>();
        bulletPool = Pools.get(Bullet.class, 15);
        bulletPool.fill(5);
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

        updatePlayer(delta, inputState);
        updateBullets(delta);
        updateEnemies(delta);
    }
    private void updatePlayer(float delta, InputState inputState){
        if (player.health <= 0f) {
            state = WorldState.LOST;
            return;
        }
        player.inputDirection = inputState.direction;

        if (inputState.shootBulletPressed) {
            player.shootBullet(context.bulletSpawner);
        }
        if (inputState.phasePressed) {
            player.phase();
        }
        player.setShieldState(inputState.shieldActive);

        player.update(delta);
    }
    private void updateEnemies(float delta){
        for (Iterator<Enemy> it_e = enemies.iterator(); it_e.hasNext();) {
            Enemy enemy = it_e.next();
            enemy.update(delta, context);
            if (!enemy.isAlive) { it_e.remove(); }
        }
    }
    private void updateBullets(float delta){
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
    }

    /********************* DRAW *********************/
    public void draw(SpriteBatch batch){
        batch.draw(gameBackground, 0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);
        drawPlayer(batch);
        drawEnemies(batch);
        drawBullets(batch);
    }
    private void drawPlayer(SpriteBatch batch){
        player.draw(batch, mousePosition.x, mousePosition.y);
    }
    private void drawEnemies(SpriteBatch batch){
        for (Enemy enemy: enemies){
            enemy.draw(batch);
        }
    }
    private void drawBullets(SpriteBatch batch){
        for (Bullet bullet: bullets){
            bullet.draw(batch);
        }
    }

    /********************* WORLD OPERATIONS *********************/
    public void reset() {
        player.reset();
        player.setPosition(100f, 100f);
        enemies.clear();

        Bullet bullet;
        for (int i = bullets.size(); --i >= 0;) {
            bullet = bullets.get(i);
            bullets.remove(i);
            bulletPool.free(bullet);
        }
    }

    public void startLevel(LevelConfig levelConfig) {
        // Get stage config as input param and spawn set player position, spawn enemies, start timer etc.
        state = WorldState.RUNNING;
        System.out.println("Loading stage. Time limit: " + levelConfig.timeLimit + ", player pos: (" +  levelConfig.playerStart.x + ", " + levelConfig.playerStart.y + ")");
    }

    public void dispose() {
        // Potential manually loaded textures
    }
}
