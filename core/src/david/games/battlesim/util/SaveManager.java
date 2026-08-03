package david.games.battlesim.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import com.badlogic.gdx.utils.Json;

public class SaveManager
{
    private static final String SAVE_FILE = "save.json";
    FileHandle file = Gdx.files.local(SAVE_FILE);
    public SaveData progress;
    public SaveManager() {
        progress = new SaveData();
        initSave();
    }

    public void setLevelReached(int levelReached) {
        progress.levelReached = levelReached;

    }
    public void setInfiniteBest(int best) {
        progress.infiniteHighScore = best;
    }

    // Saves the data into the file and updates the progress
    public void save() {
        Json json = new Json();
        String data = json.toJson(progress);
        file.writeString(data, false);

        progress = new Json().fromJson(SaveData.class, file.readString());
    }

    public void initSave() {
        if (!file.exists()) {
            progress.infiniteHighScore = 0;
            progress.levelReached = 1;
            save();
        }

        progress = new Json().fromJson(SaveData.class, file.readString());
    }

    public SaveData getSaveData() {
        return progress;
    }
}
