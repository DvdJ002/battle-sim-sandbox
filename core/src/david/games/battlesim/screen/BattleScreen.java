package david.games.battlesim.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static david.games.battlesim.BattleGame.assetManager;
import static david.games.battlesim.BattleGame.saveManager;

import david.games.battlesim.BattleGame;
import david.games.battlesim.assets.AssetDescriptors;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.config.database.LevelConfigDatabase;
import david.games.battlesim.ui.Hud;
import david.games.battlesim.util.InputState;
import david.games.battlesim.world.BattleWorld;

public class BattleScreen extends ScreenAdapter {
    private final BattleGame game;
    private SpriteBatch batch;
    private ShapeRenderer sr;
    private BitmapFont font;
    Vector3 mousePosition;
    InputState inputState;

    private OrthographicCamera camera;
    private Viewport viewport;
    private BattleWorld world;

    OrthographicCamera hudCamera;
    private Viewport hudViewport;
    private Hud hud;
    private Sound winSound, loseSound;

    private int currentLevelCode;
    private boolean isDebugActive, isTutorialMode, isInfiniteMode;

    public BattleScreen(BattleGame game, int levelCode) {
        this.game = game;
        this.currentLevelCode = levelCode;
        this.isDebugActive = false;
        this.isTutorialMode = (levelCode == 0);
        this.isInfiniteMode = (levelCode == 100);
    }

    @Override
    public void show() {
        batch = game.getBatch();
        sr = game.getShapeRenderer();
        font = assetManager.get(AssetDescriptors.UI_FONT);
        font.setColor(Color.BLACK);
        inputState = new InputState();
        mousePosition = new Vector3(0,0,0);

        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT, camera);

        world = new BattleWorld();
        world.setTutorial(isTutorialMode);
        world.setInfinite(isInfiniteMode);

        LevelConfigDatabase levelConfigDatabase = new LevelConfigDatabase();

        hudCamera = new OrthographicCamera();
        hudViewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT, hudCamera);
        hud = new Hud();

        winSound = assetManager.get(AssetDescriptors.GAME_WIN_SOUND);
        loseSound = assetManager.get(AssetDescriptors.GAME_LOSE_SOUND_1);

        world.startLevel(levelConfigDatabase.get("level" + this.currentLevelCode));
    }

    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        camera.update();
        hudCamera.update();
        batch.setProjectionMatrix(camera.combined);

        readInput();
        world.update(delta, inputState);
        handleWorldState();

        // Game elements drawing, plus UI icons and text
        batch.begin();
        world.draw(batch);
        batch.setProjectionMatrix(hudCamera.combined);
        hud.drawIcons(batch, world);

        if (isInfiniteMode) {
            hud.drawInfiniteText(batch, font, world.levelTimer, world.currentWave, saveManager.progress.infiniteHighScore);
        }
        else if (world.bossFight) {
            if (world.enemies.isEmpty()) { hud.drawBossBeatenText(batch, font); }
        }
        else if (world.tutorialMode) {
            hud.drawTutorialText(batch, font, world.currentWave);
        }
        else {
            hud.drawText(batch, font, world, currentLevelCode);
        }

        batch.end();

        // Shape rendering (hud)
        sr.setProjectionMatrix(hudCamera.combined);
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        hud.drawBars(sr, world);
        sr.end();

        if (isDebugActive) {
            sr.begin(ShapeRenderer.ShapeType.Line);
            hud.drawDebugOverlay(sr, world); 
            sr.end();
        }
    }

    public void readInput() {
        // Go back to levels screen
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            endScreen();
        }

        // Player input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) inputState.direction.y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) inputState.direction.y -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) inputState.direction.x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) inputState.direction.x += 1;

        inputState.phasePressed = Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        inputState.shieldActive = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
        inputState.shootBulletPressed = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        inputState.forceFieldPressed = Gdx.input.isKeyJustPressed(Input.Keys.Q);
        
        // General input
        inputState.resetGamePressed = Gdx.input.isKeyJustPressed(Input.Keys.R);
        inputState.debugSpawnEnemy = Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE);

        if (GameConfig.DEBUG_MODE) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.L)) {
                isDebugActive = true;
            }
            else if (Gdx.input.isKeyJustPressed(Input.Keys.O)) {
                isDebugActive = false;
            }
        }

        mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(mousePosition);
        inputState.mousePosition = mousePosition;
    }

    private void handleWorldState() {
        switch (world.state) {
            case WON:
                saveLevelProgress(currentLevelCode + 1);
                winSound.play(GameConfig.VOLUME_LOUD);
                endScreen();
                return;
            case LOST:
            case STOPPED:
            case EXIT_REQUESTED:
                if (isInfiniteMode) {
                    game.saveInfiniteBest(world.currentWave);
                }
                loseSound.play(GameConfig.VOLUME_DEFAULT);
                endScreen();
                return;
            case RUNNING: break;
        }
    }

    public void saveLevelProgress(int levelCode) {
        // If the level is unlocked anyways don't do anything
        if (!game.isLevelReached(levelCode)) {
            game.saveLevelProgress(levelCode);
        }
    }

    public void endScreen() {
        if (isTutorialMode || isInfiniteMode) { game.setScreen(new MenuScreen(game)); }
        else { game.setScreen(new LevelsScreen(game)); }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }
    @Override
    public void hide() {
        dispose();
    }
    @Override
    public void dispose() {
        super.dispose();
        world.dispose();
    }
}
