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
    private SaveData progress;
    public SaveManager() {
        progress = new SaveData();
        initSave();
    }

    // Saves the data into the file and updates the progress
    public void save(int levelReached) {
        Json json = new Json();
        progress.levelReached = levelReached;
        String data = json.toJson(progress);
        file.writeString(data, false);

        progress = new Json().fromJson(SaveData.class, file.readString());
    }

    public void initSave() {
        if (!file.exists()) {
            save(1);
        }

        progress = new Json().fromJson(SaveData.class, file.readString());
    }

    public SaveData getSaveData() {
        return progress;
    }
}
