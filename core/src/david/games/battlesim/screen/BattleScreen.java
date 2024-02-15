package david.games.battlesim.screen;

import static com.badlogic.gdx.math.Intersector.overlaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import david.games.battlesim.BattleGame;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.elements.ClassicEnemy;
import david.games.battlesim.elements.EnemyType;
import david.games.battlesim.elements.Player;
import david.games.battlesim.util.MovementUtil;

public class BattleScreen extends ScreenAdapter {

    private final BattleGame game;
    private final AssetManager assetManager;
    private SpriteBatch batch;
    private Player player;
    private ArrayList<ClassicEnemy> enemies;


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
        enemies.add(new ClassicEnemy(EnemyType.SLASHER, 500f, 500f));
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
    }

    public void draw(){
        batch.draw(gameBackground, 0, 0, GameConfig.WIDTH, GameConfig.HEIGHT);
        drawEnemies();
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
    }

    private void drawEnemies(){
        for (ClassicEnemy enemy: enemies){
            enemy.draw(batch);
        }
    }

    private void updateEnemies(float delta){
        for (ClassicEnemy enemy: enemies){
            enemy.update(delta, player.getPositionVector());
            // Check for collision with player
            if (overlaps(player.hitbox, enemy.hitbox)) {
                // player.onCollision();
            }

            if (enemy.type == EnemyType.SLASHER &&
                MovementUtil.isNear(player.hitbox.x, player.hitbox.y, enemy.hitbox.x, enemy.hitbox.y, 200f)
            ){
                enemy.dash(400f, player.getPositionVector());
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
