package david.games.battlesim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import david.games.battlesim.assets.AssetDescriptors;
import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.screen.MenuScreen;

public class BattleGame extends Game {
	SpriteBatch batch;
	ShapeRenderer sr;
	public static AssetManager assetManager;
	private Screen currentScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();

		loadAssetManager();

		setScreen(new MenuScreen(this));
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
	}

	@Override
	public Screen getScreen() {
		return currentScreen;
	}

	private void loadAssetManager(){
		assetManager = new AssetManager();
		assetManager.load(AssetDescriptors.UI_SKIN);
		assetManager.load(AssetDescriptors.UI_FONT);

		assetManager.load(AssetPaths.SLASHER, Texture.class);
		assetManager.load(AssetPaths.SLASHER_ATTACK, Texture.class);
		assetManager.load(AssetPaths.KAMIKAZE, Texture.class);
		assetManager.load(AssetPaths.SHOOTER, Texture.class);
		assetManager.load(AssetPaths.PLAYER, Texture.class);
		assetManager.load(AssetPaths.PLAYER_SHIELD, Texture.class);
		assetManager.load(AssetPaths.BULLET_BLUE, Texture.class);
		assetManager.load(AssetPaths.FORCE_FIELD_BLUE, Texture.class);
		assetManager.load(AssetPaths.FORCE_FIELD_DARK, Texture.class);

		assetManager.load(AssetPaths.MENU_BACKGROUND, Texture.class);
		assetManager.load(AssetPaths.GAME_BACKGROUND, Texture.class);
		assetManager.load(AssetPaths.TITLE, Texture.class);
		assetManager.load(AssetPaths.STAGE_SELECT, Texture.class);
		assetManager.load(AssetPaths.EFFECT_KNOCKBACK, Texture.class);
		assetManager.load(AssetPaths.EFFECT_SLOWED, Texture.class);
		assetManager.load(AssetPaths.EFFECT_ICED, Texture.class);
		assetManager.load(AssetPaths.EFFECT_SUCKED, Texture.class);
		assetManager.load(AssetPaths.EFFECT_POISONED, Texture.class);
		assetManager.load(AssetPaths.EFFECT_UNKNOWN, Texture.class);
		assetManager.load(AssetPaths.EFFECT_INVINCIBLE, Texture.class);

		assetManager.finishLoading();
	}

	public AssetManager getAssetManager(){
		return assetManager;
	}
	public SpriteBatch getBatch() {
		return batch;
	}
	public ShapeRenderer getShapeRenderer() {
		return sr;
	}

	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}
}
