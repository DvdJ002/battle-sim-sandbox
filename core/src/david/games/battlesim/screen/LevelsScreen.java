package david.games.battlesim.screen;

import static david.games.battlesim.BattleGame.assetManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import david.games.battlesim.BattleGame;
import david.games.battlesim.assets.AssetDescriptors;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;

public class LevelsScreen extends ScreenAdapter {
    private final BattleGame game;
    private final AssetManager assetManager;

    private Viewport viewport;
    private Stage stage;

    private Skin skin;

    public LevelsScreen(BattleGame game) {
        this.game = game;
        assetManager = game.getAssetManager();
    }

    @Override
    public void show() {
        viewport = new FitViewport(GameConfig.WIDTH, GameConfig.HEIGHT);
        stage = new Stage(viewport, game.getBatch());

        skin = assetManager.get(AssetDescriptors.UI_SKIN);

        stage.addActor(createUi());
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0f, 0f);

        stage.act(delta);
        stage.draw();
    }
    @Override
    public void hide() {
        dispose();
    }
    @Override
    public void dispose() {
        stage.dispose();
    }

    private Actor createUi() {
        Table table = new Table();
        table.defaults().pad(20);

        Table buttonTable = new Table();
        buttonTable.defaults().padLeft(1).padRight(1);
        buttonTable.center();
        buttonTable.top();

        buttonTable.setBackground(new TextureRegionDrawable(
                new TextureRegion(assetManager.get(AssetPaths.MENU_BACKGROUND, Texture.class))
        ));

        Image titleImage = new Image(new TextureRegionDrawable(
                new TextureRegion(assetManager.get(AssetPaths.STAGE_SELECT, Texture.class))
        ));
        buttonTable.add(titleImage).padTop(10).padBottom(100).colspan(4).row();

        // Level buttons
        for (int i = 1; i < 10; i++){
            final int level = i;

            final TextButton button = new TextButton("Level " + level, skin);
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (button.isDisabled()) {
                        return;
                    }

                    game.setScreen(new BattleScreen(game, level));
                }
            });

            if (i % 4 == 0) {
                buttonTable.add(button).padBottom(10).growX().row();
            } else {
                buttonTable.add(button).padBottom(10).growX();
            }

            if (!game.isLevelReached(i)) {
                button.setDisabled(true);
            }
        }

        // Boss button
        final TextButton bossButton = new TextButton("Boss level", skin);
        bossButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (bossButton.isDisabled()) {
                    return;
                }

                game.setScreen(new BattleScreen(game, 10));
            }
        });

        // Span two columns and center it
        buttonTable.add(bossButton).colspan(2).padBottom(10).growX();
        if (!game.isLevelReached(10)) {
            bossButton.setDisabled(true);
        }
        buttonTable.add(); // empty column
        buttonTable.row();


        TextButton menuButton = new TextButton("Menu" , skin);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        buttonTable.add(menuButton).padTop(140).padBottom(50).colspan(4);

        table.add(buttonTable).expand().fill();
        table.setFillParent(true);

        return table;
    }
}
