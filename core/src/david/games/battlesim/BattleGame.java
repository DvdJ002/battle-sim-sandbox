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
import david.games.battlesim.util.SaveData;
import david.games.battlesim.util.SaveManager;

public class BattleGame extends Game {
	SpriteBatch batch;
	ShapeRenderer sr;
	public static AssetManager assetManager;
	private Screen currentScreen;
	public SaveManager saveManager;

	@Override
	public void create () {
		batch = new SpriteBatch();
		sr = new ShapeRenderer();
		saveManager = new SaveManager();

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
		assetManager.load(AssetDescriptors.PLAYER_DAMAGE_SOUND);
		assetManager.load(AssetDescriptors.GLOBAL_SHOOT_SOUND);
		assetManager.load(AssetDescriptors.PLAYER_ULTIMATE_SOUND);
		assetManager.load(AssetDescriptors.HEALER_BEAM_SOUND);
		assetManager.load(AssetDescriptors.KAMIKAZE_EXPLOSION_SOUND);

		assetManager.load(AssetPaths.SLASHER, Texture.class);
		assetManager.load(AssetPaths.SLASHER_ATTACK, Texture.class);
		assetManager.load(AssetPaths.KAMIKAZE, Texture.class);
		assetManager.load(AssetPaths.SHOOTER, Texture.class);
		assetManager.load(AssetPaths.SUCKER, Texture.class);
		assetManager.load(AssetPaths.HEALER, Texture.class);
		assetManager.load(AssetPaths.HEALER_BEAM, Texture.class);
		assetManager.load(AssetPaths.PLAYER, Texture.class);
		assetManager.load(AssetPaths.PLAYER_SHIELD, Texture.class);
		assetManager.load(AssetPaths.BULLET_BLUE, Texture.class);
		assetManager.load(AssetPaths.FORCE_FIELD_BLUE, Texture.class);
		assetManager.load(AssetPaths.FORCE_FIELD_DARK, Texture.class);

		assetManager.load(AssetPaths.MENU_BACKGROUND, Texture.class);
		assetManager.load(AssetPaths.GAME_BACKGROUND, Texture.class);
		assetManager.load(AssetPaths.TITLE, Texture.class);
		assetManager.load(AssetPaths.STAGE_SELECT, Texture.class);
		assetManager.load(AssetPaths.EFFECT_ROOTED, Texture.class);
		assetManager.load(AssetPaths.EFFECT_SLOWED, Texture.class);
		assetManager.load(AssetPaths.EFFECT_ICED, Texture.class);
		assetManager.load(AssetPaths.EFFECT_POISONED, Texture.class);
		assetManager.load(AssetPaths.EFFECT_UNKNOWN, Texture.class);
		assetManager.load(AssetPaths.EFFECT_INVINCIBLE, Texture.class);
		assetManager.load(AssetPaths.EFFECT_DISARMED, Texture.class);

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

	public void saveProgress(int levelReached) {
		saveManager.save(levelReached);
	}

	public boolean isLevelReached(int levelCode) {
		SaveData data = saveManager.getSaveData();
		return data.levelReached >= levelCode;
	}

	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}
}
