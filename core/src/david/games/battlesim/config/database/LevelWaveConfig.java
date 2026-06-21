package david.games.battlesim.config.database;

import java.util.ArrayList;

public class LevelWaveConfig {
    public float timeLeft;
    public float enemiesLeft;
    public ArrayList<EnemySpawnConfig> spawns;

    public LevelWaveConfig(float timeLeft, float enemiesLeft, ArrayList<EnemySpawnConfig> spawns) {
        this.timeLeft = timeLeft;
        this.enemiesLeft = enemiesLeft;
        this.spawns = spawns;
    }
}
