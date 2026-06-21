package david.games.battlesim.config.database;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class LevelConfigDatabase {
    private Map<String, LevelConfig> levelConfigs = new HashMap<>();

    public LevelConfigDatabase() {
        ArrayList<LevelWaveConfig> waves = new ArrayList<>();
        ArrayList<EnemySpawnConfig> spawnConfigs = new ArrayList<>();

        /* ************** Level 1 ************** */
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(100f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(200f, 300f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(600f, 300f)));
        waves.add(new LevelWaveConfig(20f, -1f, spawnConfigs));

        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(450f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(350f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(450f, 300f)));
        waves.add(new LevelWaveConfig(10f, -1f, spawnConfigs));

        LevelConfig config = new LevelConfig(23f, new Vector2(400f, 100f), waves);

        levelConfigs.put("level1", config);

        /* ************** Level 2 ************** */
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(450f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(350f, 400f)));

        waves.add(new LevelWaveConfig(10f, -1f, spawnConfigs));

        config = new LevelConfig(13f, new Vector2(400f, 100f), waves);

        levelConfigs.put("level2", config);

        /* ************** Level 3 ************** */
        // Wave 1
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(450f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(350f, 400f)));
        waves.add(new LevelWaveConfig(25f, -1f, spawnConfigs));

        // Wave 2
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(400f, 400f)));

        waves.add(new LevelWaveConfig(-1f, 2f, spawnConfigs));

        // Wave 3
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(400f, 400f)));

        waves.add(new LevelWaveConfig(-1f, 2f, spawnConfigs));

        // Wave 4
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 400f)));

        waves.add(new LevelWaveConfig(-1f, 1f, spawnConfigs));

        // Wave 5
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(450f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(450f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(350f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(300f, 400f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        config = new LevelConfig(28f, new Vector2(400f, 100f), waves);
        levelConfigs.put("level3", config);

        /* ************** Level 4 ************** */
        // Wave 1
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(20f, 200f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(20f, 300f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(20f, 400f)));

        waves.add(new LevelWaveConfig(20f, -1f, spawnConfigs));

        // Wave 2
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(760f, 200f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(760f, 300f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(760f, 400f)));

        waves.add(new LevelWaveConfig(18f, -1f, spawnConfigs));

        config = new LevelConfig(20.5f, new Vector2(400f, 300f), waves);

        levelConfigs.put("level4", config);

        /* ************** Level 4 ************** */
        // Wave 1
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        waves.add(new LevelWaveConfig(100f, -1f, spawnConfigs));

        config = new LevelConfig(20.5f, new Vector2(400f, 300f), waves);

        levelConfigs.put("level5", config);
    }

    public LevelConfig get(String type) {
        return levelConfigs.get(type);
    }

}
