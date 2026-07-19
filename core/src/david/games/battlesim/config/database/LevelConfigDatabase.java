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
        // Wave 1 - Large shooter with 3 normal placed in front of him
        spawnConfigs.add(new EnemySpawnConfig("shooter_large", new Vector2(400f, 440f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(300f, 340f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 340f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(500f, 340f)));

        waves.add(new LevelWaveConfig(30f, -1f, spawnConfigs));

        // Wave 2 - Large slasher with 2 normal placed far on the sides
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher_large", new Vector2(400f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(700f, 400f)));
        waves.add(new LevelWaveConfig(20f, -1f, spawnConfigs));

        // Wave 3 - Large kamikaze with 2 normal placed close on the sides
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze_large", new Vector2(450f, 400f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(350f, 400f)));
        waves.add(new LevelWaveConfig(10f, -1f, spawnConfigs));

        LevelConfig config = new LevelConfig(33f, new Vector2(400f, 100f), waves);

        levelConfigs.put("level1", config);

        /* ************** Level 2 ************** */
        // Wave 1 - 4 shooters on the left side of screen
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(20f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(20f, 200f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(20f, 350f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(20f, 450f)));

        waves.add(new LevelWaveConfig(25f, -1f, spawnConfigs));

        // Wave 2 - 4 shooters on the right side of screen
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(760f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(760f, 200f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(760f, 350f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(760f, 450f)));

        waves.add(new LevelWaveConfig(23f, -1f, spawnConfigs));

        // Wave 3 - 2 kamikaze from left and right upper corners
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(700f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(100f, 500f)));

        waves.add(new LevelWaveConfig(10f, -1f, spawnConfigs));

        config = new LevelConfig(28f, new Vector2(400f, 300f), waves);

        levelConfigs.put("level2", config);

        /* ************** Level 3 ************** */
        // Wave 1 - circle of kamikazes on the edges
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        // Top row
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(100f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(700f, 500f)));
        // Middle row
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(100f, 300f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(700f, 300f)));
        // Bottom row
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(100f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(700f, 100f)));

        waves.add(new LevelWaveConfig(47f, -1f, spawnConfigs));

        // Wave 2 - shooter middle up
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(370f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(430f, 500f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 3 - slasher right middle
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(700f, 270f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(700f, 330f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 4 - shooter middle down
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 70f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 130f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 5 - slasher left middle
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 270f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 330f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 6 - all of the previous combined
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(700f, 300f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 300f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 7 - Large slasher, large shooter, large kamikaze
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter_large", new Vector2(200f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher_large", new Vector2(400f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze_large", new Vector2(600f, 500f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        config = new LevelConfig(50f, new Vector2(400f, 100f), waves);
        levelConfigs.put("level3", config);

        /* ************** Level 4 ************** */
        // Wave 1 - a sucker spawns in each corner and a row of shooters at the top
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(200f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(300f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(500f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(600f, 500f)));

        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(100f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(700f, 50f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(100f, 50f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(700f, 500f)));

        waves.add(new LevelWaveConfig(30f, -1f, spawnConfigs));

        // Wave 2 - one sucker in the middle
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(400f, 300f)));

        waves.add(new LevelWaveConfig(-1f, 4f, spawnConfigs));

        config = new LevelConfig(33f, new Vector2(400f, 300f), waves);

        levelConfigs.put("level4", config);

        /* ************** Level 5 ************** */
        // Wave 1 - a row of 5 healers at the top followed by a row of 5 suckers just below them
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(60f, 520f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(210f, 520f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(360f, 520f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(500f, 520f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(660f, 520f)));


        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(100f, 450f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(250f, 450f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(400f, 450f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(550f, 450f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(700f, 450f)));


        waves.add(new LevelWaveConfig(37f, -1f, spawnConfigs));

        // Wave 2 - A kamikaze top middle
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 500f)));

        waves.add(new LevelWaveConfig(-1f, 8f, spawnConfigs));

        // Wave 3 - A kamikaze top middle
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 500f)));

        waves.add(new LevelWaveConfig(-1f, 6f, spawnConfigs));

        // Wave 4 - A kamikaze top middle
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(400f, 500f)));

        waves.add(new LevelWaveConfig(-1f, 4f, spawnConfigs));

        config = new LevelConfig(40f, new Vector2(400f, 300f), waves);

        levelConfigs.put("level5", config);

        /* ************** Level 6 ************** */
        // Wave 1 - A large slasher encircled by 3 healers, top row
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        // Top row
        spawnConfigs.add(new EnemySpawnConfig("healer_special", new Vector2(350f, 550f)));
        spawnConfigs.add(new EnemySpawnConfig("healer_special", new Vector2(400f, 550f)));
        spawnConfigs.add(new EnemySpawnConfig("healer_special", new Vector2(450f, 550f)));

        spawnConfigs.add(new EnemySpawnConfig("slasher_large", new Vector2(400f, 500f)));

        waves.add(new LevelWaveConfig(55f, -1f, spawnConfigs));

        // Wave 2 - Keep spawning healers every 1.5 seconds, alternating top and bottom
        for (int i = 1; i < 28; i++) {
            spawnConfigs = new ArrayList<>();

            float y = (i % 2 == 0) ? 500f : 100f;
            spawnConfigs.add(new EnemySpawnConfig("healer_special", new Vector2(400f, y)));

            if (i == 9 || i == 22) {
                spawnConfigs.add(new EnemySpawnConfig("healer_special", new Vector2(400f, 330f)));
                spawnConfigs.add(new EnemySpawnConfig("kamikaze_large", new Vector2(400f, 300f)));
            }

            waves.add(new LevelWaveConfig(55f - 1.75f*i, -1f, spawnConfigs));
        }

        config = new LevelConfig(58f, new Vector2(400f, 300f), waves);

        levelConfigs.put("level6", config);

        /* ************** Level 7 ************** */
        // Wave 1
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 300f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 300f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 500f)));

        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(100f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(100f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(700f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(700f, 500f)));

        waves.add(new LevelWaveConfig(85f, -1f, spawnConfigs));

        // Wave 2
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(100f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(700f, 50f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(100f, 50f)));
        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(700f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(100f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(700f, 50f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(100f, 50f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(700f, 500f)));

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 500f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 3
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 500f)));

        waves.add(new LevelWaveConfig(35f, -1f, spawnConfigs));

        // Wave 4
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(100f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 100f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(700f, 500f)));

        waves.add(new LevelWaveConfig(15f, -1f, spawnConfigs));

        config = new LevelConfig(88f, new Vector2(400f, 300f), waves);

        levelConfigs.put("level7", config);

        /* ************** Level 0 / Tutorial ************** */
        // Wave 1
        waves = new ArrayList<>();
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("slasher", new Vector2(400f, 450f)));

        waves.add(new LevelWaveConfig(300f, -1f, spawnConfigs));

        // Wave 2
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 450f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 3
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(500f, 500f)));
        spawnConfigs.add(new EnemySpawnConfig("kamikaze", new Vector2(100f, 100f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 4
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(400f, 450f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 5
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(380f, 450f)));
        spawnConfigs.add(new EnemySpawnConfig("sucker", new Vector2(420, 450f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        // Wave 6
        spawnConfigs = new ArrayList<>();

        spawnConfigs.add(new EnemySpawnConfig("healer", new Vector2(380f, 450f)));

        waves.add(new LevelWaveConfig(-1f, 0f, spawnConfigs));

        config = new LevelConfig(312f, new Vector2(400f, 300f), waves);

        levelConfigs.put("level0", config);
    }

    public LevelConfig get(String type) {
        return levelConfigs.get(type);
    }
}
