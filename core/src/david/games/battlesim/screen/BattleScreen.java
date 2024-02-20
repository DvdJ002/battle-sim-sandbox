package david.games.battlesim.screen;

import static com.badlogic.gdx.math.Intersector.overlaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;

import david.games.battlesim.BattleGame;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.elements.Bullet;
import david.games.battlesim.elements.Enemy;
import david.games.battlesim.elements.KamikazeEnemy;
import david.games.battlesim.elements.Player;
import david.games.battlesim.elements.ShooterEnemy;
import david.games.battlesim.elements.SlasherEnemy;

public class BattleScreen extends ScreenAdapter {

    private final BattleGame game;
    private final AssetManager assetManager;
    private SpriteBatch batch;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Bullet> bullets;
    private Pool<Bullet> bulletPool;

    private Viewport viewport;
    private OrthographicCamera camera;
    Vector3 mousePosition;

    private Texture gameBackground;

    public BattleScreen(BattleGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT, camera);
        batch = game.getBatch();

        mousePosition = new Vector3(0,0,0);

        gameBackground = assetManager.get(AssetPaths.GAME_BACKGROUND, Texture.class);

        player = new Player(100, 100);
        enemies = new ArrayList<>();
        enemies.add(new SlasherEnemy(500f, 500f));
        // enemies.add(new KamikazeEnemy(400f, 400f));
        enemies.add(new ShooterEnemy(250f, 254f));

        bullets = new ArrayList<>();
        bulletPool = Pools.get(Bullet.class, 15);
        bulletPool.fill(5);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        handleInput();
        update(delta);

        batch.begin();
        draw();
        batch.end();
    }

    private void update(float delta){
        updateEnemies(delta);
        updateBullets(delta);
    }

    public void draw(){
        batch.draw(gameBackground, 0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);
        drawEnemies();
        drawBullets();
        player.draw(batch, mousePosition.x, mousePosition.y);
    }

    private void handleInput() {
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(mousePosition);

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.movePlayer(Gdx.graphics.getDeltaTime(), "left");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.movePlayer(Gdx.graphics.getDeltaTime(), "right");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.movePlayer(Gdx.graphics.getDeltaTime(), "up");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.movePlayer(Gdx.graphics.getDeltaTime(), "down");
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            player.phase();
        }
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            player.shootBullet(bulletPool, bullets);
        }
    }

    private void drawEnemies(){
        for (Enemy enemy: enemies){
            enemy.draw(batch);
        }
    }
    private void drawBullets(){
        for (Bullet bullet: bullets){
            bullet.draw(batch);
        }
    }

    private void updateEnemies(float delta){
        for (Iterator<Enemy> it_e = enemies.iterator(); it_e.hasNext();) {
            Enemy enemy = it_e.next();
            enemy.update(delta, player.getPositionVector());
            if (overlaps(player.hitbox, enemy.hitbox)) { player.takeHit("collision"); }
            if (enemy instanceof ShooterEnemy && !((ShooterEnemy) enemy).reloading){
                ((ShooterEnemy) enemy).shootBullet(player.getPositionVector(), bulletPool, bullets);
            }
            for (Iterator<Bullet> it_b = bullets.iterator(); it_b.hasNext();) {
                Bullet bullet = it_b.next();
                // Check if bullet hit the enemy or the player
                if (bullet.isAlive && bullet.fromPlayer && overlaps(bullet.hitbox, enemy.hitbox)) {
                    enemy.takeHit("bullet");
                    bullet.isAlive = false;
                } else if (bullet.isAlive && !bullet.fromPlayer && overlaps(bullet.hitbox, player.hitbox)) {
                    player.takeHit("bullet");
                    bullet.isAlive = false;
                }
            }
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
                bullet.update(delta);
            }
        }
    }

    @Override
    public void hide() {
        dispose();
    }
    @Override
    public void dispose() {
        gameBackground.dispose();
    }
}
