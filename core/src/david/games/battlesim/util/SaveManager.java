package david.games.battlesim.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class SaveManager
{
    private final File saveFile = new File("data.json");
    private int currentLevelReached;
    public SaveManager() {
        initSaveFile();
        currentLevelReached = 6;
    }

    public void initSaveFile() {
        if (!saveFile.exists()) {
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(int levelReached) {
        // serialize JSON
        try (Writer writer = new FileWriter(saveFile)) {
            //gson.toJson(levelReached, writer);
            // currentLevelReached = from json
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {

    }

    public boolean isLevelReached(int level) {
        return currentLevelReached >= level;
    }
}
