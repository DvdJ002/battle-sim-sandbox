package david.games.battlesim.config.database;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import david.games.battlesim.config.GameConfig;

public final class LevelConfigDatabase {
    private Map<String, LevelConfig> levelConfigs = new HashMap<>();

    public LevelConfigDatabase() {
        /* ************** Level 1 ************** */
        ArrayList<EnemySpawnConfig> spawnConfigs1 = new ArrayList<>();
        Map<ArrayList<EnemySpawnConfig>, LevelTriggerConfig> waves1 = new HashMap<>();

        spawnConfigs1.add(new EnemySpawnConfig("shooter", new Vector2(400f, 400f)));
        spawnConfigs1.add(new EnemySpawnConfig("shooter", new Vector2(450f, 400f)));
        spawnConfigs1.add(new EnemySpawnConfig("shooter", new Vector2(350f, 400f)));

        waves1.put(spawnConfigs1, new LevelTriggerConfig(0f, 0f));

        LevelConfig config = new LevelConfig(90f, new Vector2(100f, 100f), waves1);

        levelConfigs.put("level1", config);

        /* ************** Level 2 ************** */
        ArrayList<EnemySpawnConfig> spawnConfigs2 = new ArrayList<>();
        Map<ArrayList<EnemySpawnConfig>, LevelTriggerConfig> waves2 = new HashMap<>();

        spawnConfigs2.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 400f)));
        spawnConfigs2.add(new EnemySpawnConfig("slasher", new Vector2(450f, 400f)));
        spawnConfigs2.add(new EnemySpawnConfig("shooter", new Vector2(350f, 400f)));

        waves2.put(spawnConfigs2, new LevelTriggerConfig(45f, 0f));

        config = new LevelConfig(90f, new Vector2(100f, 100f), waves2);

        levelConfigs.put("level2", config);

        /* ************** Level 3 ************** */
        ArrayList<EnemySpawnConfig> spawnConfigs3 = new ArrayList<>();
        Map<ArrayList<EnemySpawnConfig>, LevelTriggerConfig> waves3 = new HashMap<>();

        spawnConfigs3.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 400f)));
        spawnConfigs3.add(new EnemySpawnConfig("kamikaze", new Vector2(450f, 400f)));
        spawnConfigs3.add(new EnemySpawnConfig("kamikaze", new Vector2(350f, 400f)));
        spawnConfigs3.add(new EnemySpawnConfig("kamikaze", new Vector2(450f, 300f)));
        waves3.put(spawnConfigs3, new LevelTriggerConfig(0f, 0f));

        spawnConfigs3.clear();

        ArrayList<EnemySpawnConfig> spawnConfigs3_2 = new ArrayList<>();

        spawnConfigs3.add(new EnemySpawnConfig("slasher", new Vector2(400f, 400f)));
        spawnConfigs3.add(new EnemySpawnConfig("slasher", new Vector2(450f, 400f)));
        waves3.put(spawnConfigs3_2, new LevelTriggerConfig(45f, 0f));

        config = new LevelConfig(90f, new Vector2(100f, 100f), waves3);

        levelConfigs.put("level3", config);
    }

    public LevelConfig get(String type) {
        return levelConfigs.get(type);
    }

}
