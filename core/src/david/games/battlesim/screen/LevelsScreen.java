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
        buttonTable.add(titleImage).padTop(10).padBottom(100).colspan(5).row();

        // Magic number (changed later with GameManager)
        for (int i = 1; i < 9; i++){
            if (i % 4 == 0) {
                buttonTable.add(new TextButton("Stage " + i, skin)).padBottom(10).expandX().fill().row();
            } else {
                buttonTable.add(new TextButton("Stage " + i, skin)).padBottom(10).expandX().fill();
            }
            buttonTable.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new BattleScreen(game));
                }
            });
        }

        TextButton menuButton = new TextButton("Menu" , skin);
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game));
            }
        });
        buttonTable.add(menuButton).padTop(140).padBottom(50).colspan(5);

        table.add(buttonTable);
        table.setFillParent(true);

        return table;
    }
}
