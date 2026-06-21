package david.games.battlesim.config.database;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class LevelConfig {
    public float timeLimit;
    public Vector2 playerStart;
    public ArrayList<LevelWaveConfig> waves;

    public LevelConfig(
            float timeLimit,
            Vector2 playerStart,
            ArrayList<LevelWaveConfig> waves
    ) {
        this.timeLimit = timeLimit;
        this.playerStart = playerStart;
        this.waves = waves;
    }
}
