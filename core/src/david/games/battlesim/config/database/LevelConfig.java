package david.games.battlesim.config.database;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LevelConfig {
    public float timeLimit;
    public Vector2 playerStart;
    private Map<ArrayList<EnemySpawnConfig>, LevelTriggerConfig> waves;

    public LevelConfig(
            float timeLimit,
            Vector2 playerStart,
            Map<ArrayList<EnemySpawnConfig>, LevelTriggerConfig> waves
    ) {
        this.timeLimit = timeLimit;
        this.playerStart = playerStart;
        this.waves = waves;
    }
}
