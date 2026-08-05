package david.games.battlesim.config.database;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import david.games.battlesim.config.GameConfig;

public class InfiniteLevelDatabase {
    private int creditIncrement, largeEnemyUnlocked;
    private float enemySpawnPadding, timeIncrement;
    private final Map<String, Integer> enemyCredits;

    public InfiniteLevelDatabase() {
        creditIncrement = 3;
        timeIncrement = 1.75f; // Add that much seconds every new wave
        largeEnemyUnlocked = 11; // Wave at which large enemies begin to be spawned
        enemySpawnPadding = 90f;

        enemyCredits = new HashMap<>();
        enemyCredits.put("shooter", 5);
        enemyCredits.put("shooter_large", 13);

        enemyCredits.put("slasher", 8);
        enemyCredits.put("slasher_large", 16);

        enemyCredits.put("sucker", 9);

        enemyCredits.put("healer", 7);
        enemyCredits.put("healer_special", 15);

        enemyCredits.put("kamikaze", 12);
        enemyCredits.put("kamikaze_large", 20);
    }
    public LevelWaveConfig generateWave(int waveNumber, Vector2 playerPosition) {
        System.out.println("Infinite: Generating level for wave " + waveNumber + " with " + (waveNumber * creditIncrement) + " credits");

        ArrayList<EnemySpawnConfig> spawnConfigs = new ArrayList<>();
        ArrayList<String> availableSpawns = new ArrayList<>();
        int minEnemyCredit = Collections.min(enemyCredits.values());

        boolean spawnLargeEnemies = (waveNumber >= largeEnemyUnlocked);
        int remainingCredits = (++waveNumber) * creditIncrement;
        while (remainingCredits >= minEnemyCredit) {
            // Find all available enemies, filter large and healers if necessary
            for (String key: enemyCredits.keySet()) {
                if ((!spawnLargeEnemies && key.contains("large")) || (!containsNonHealerEnemies(spawnConfigs) && key.contains("healer"))) {
                    continue;
                }

                Integer neededCredits = enemyCredits.get(key);
                if (neededCredits <= remainingCredits) {
                    availableSpawns.add(key);
                }
            }

            // Subtract credits of the chosen enemy from remaining credits
            String chosenEnemy = availableSpawns.get(MathUtils.random(availableSpawns.size() - 1));
            Vector2 position = getEnemyPosition(playerPosition);
            remainingCredits -= enemyCredits.get(chosenEnemy);

            spawnConfigs.add(new EnemySpawnConfig(chosenEnemy, position));
            availableSpawns.clear();

            System.out.println("Added enemy type " + chosenEnemy + " at " + position + ". Remaining credits: " + remainingCredits);
        }

        return new LevelWaveConfig(waveNumber * timeIncrement, -1f, spawnConfigs);
    }

    public LevelWaveConfig prepareNextWave() {
        return new LevelWaveConfig(-1f, 0f, new ArrayList<EnemySpawnConfig>());
    }

    private Vector2 getEnemyPosition(Vector2 playerPosition) {
        // x - random with padding
        float x = MathUtils.random(enemySpawnPadding, GameConfig.WIDTH - enemySpawnPadding);
        // y - if player is above, random in lower half with padding and vice versa
        float y = playerPosition.y > GameConfig.HEIGHT/2f
                ? MathUtils.random(enemySpawnPadding, GameConfig.HEIGHT/2f - enemySpawnPadding)
                : MathUtils.random(GameConfig.HEIGHT/2f + enemySpawnPadding, GameConfig.HEIGHT - enemySpawnPadding);

        return new Vector2(x, y);
    }

    private boolean containsNonHealerEnemies(ArrayList<EnemySpawnConfig> spawnConfigs) {
        for (EnemySpawnConfig spawnConfig : spawnConfigs) {
            if (!spawnConfig.type.contains("healer")) { return true; }
        }

        return false;
    }
}
