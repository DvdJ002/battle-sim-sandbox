package david.games.battlesim.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetDescriptors {

    public static final AssetDescriptor<Skin> UI_SKIN = new AssetDescriptor<>(AssetPaths.UI_SKIN, Skin.class);
    public static final AssetDescriptor<BitmapFont> UI_FONT = new AssetDescriptor<>(AssetPaths.UI_FONT, BitmapFont.class);

    public static final AssetDescriptor<Sound> PLAYER_DAMAGE_SOUND = new AssetDescriptor<Sound>(AssetPaths.PLAYER_DAMAGE_SOUND, Sound.class);
    public static final AssetDescriptor<Sound> GLOBAL_SHOOT_SOUND = new AssetDescriptor<Sound>(AssetPaths.GLOBAL_SHOOT_SOUND, Sound.class);
    public static final AssetDescriptor<Sound> PLAYER_ULTIMATE_SOUND = new AssetDescriptor<Sound>(AssetPaths.PLAYER_ULTIMATE_SOUND, Sound.class);
    public static final AssetDescriptor<Sound> HEALER_BEAM_SOUND = new AssetDescriptor<Sound>(AssetPaths.HEALER_BEAM_SOUND, Sound.class);
    public static final AssetDescriptor<Sound> KAMIKAZE_EXPLOSION_SOUND = new AssetDescriptor<Sound>(AssetPaths.KAMIKAZE_EXPLOSION_SOUND, Sound.class);
    public static final AssetDescriptor<Sound> ENEMY_INVINCIBLE_SOUND = new AssetDescriptor<Sound>(AssetPaths.ENEMY_INVINCIBLE_SOUND, Sound.class);
    public static final AssetDescriptor<Sound> GAME_WIN_SOUND = new AssetDescriptor<Sound>(AssetPaths.GAME_WIN_SOUND, Sound.class);
    public static final AssetDescriptor<Sound> GAME_LOSE_SOUND_1 = new AssetDescriptor<Sound>(AssetPaths.GAME_LOSE_SOUND_1, Sound.class);
    public static final AssetDescriptor<Sound> GAME_LOSE_SOUND_2 = new AssetDescriptor<Sound>(AssetPaths.GAME_LOSE_SOUND_2, Sound.class);
    public static final AssetDescriptor<Sound> ENEMY_SPAWN_SOUND = new AssetDescriptor<Sound>(AssetPaths.ENEMY_SPAWN_SOUND, Sound.class);

}
