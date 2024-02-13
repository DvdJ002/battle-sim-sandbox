package david.games.battlesim;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import david.games.battlesim.assets.AssetDescriptors;
import david.games.battlesim.screen.MenuScreen;

public class BattleGame extends Game {
	SpriteBatch batch;
	public static AssetManager assetManager;
	private Screen currentScreen;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
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
		assetManager.finishLoading();
	}

	public AssetManager getAssetManager(){
		return assetManager;
	}
	public SpriteBatch getBatch() {
		return batch;
	}

	@Override
	public void dispose () {
		batch.dispose();
		assetManager.dispose();
	}
}
