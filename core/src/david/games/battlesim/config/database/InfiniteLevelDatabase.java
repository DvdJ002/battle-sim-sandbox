package david.games.battlesim.config.database;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import david.games.battlesim.config.GameConfig;

public class InfiniteLevelDatabase {
    private int creditIncrement, suckersThreshold, healersThreshold;
    private float largeEnemyChance, enemySpawnPadding;
    private final Map<String, Integer> enemyCredits;

    public InfiniteLevelDatabase() {
        creditIncrement = 5;
        suckersThreshold = 5;
        healersThreshold = 10;
        largeEnemyChance = 0.25f;
        enemySpawnPadding = 90f;

        enemyCredits = new HashMap<>();
        enemyCredits.put("shooter", 5);
        enemyCredits.put("shooter_large", 9);

        enemyCredits.put("slasher", 8);
        enemyCredits.put("slasher_large", 11);

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

        int remainingCredits = (++waveNumber) * creditIncrement;
        while (remainingCredits >= minEnemyCredit) {
            // Find all available enemies
            for (String key: enemyCredits.keySet()) {
                Integer neededCredits = enemyCredits.get(key);
                if (neededCredits <= remainingCredits) {
                    availableSpawns.add(key);
                }
            }

            for (String key : enemyCredits.keySet()) {
                Integer neededCredits = enemyCredits.get(key);
                if (neededCredits < remainingCredits) {
                    availableSpawns.add(key);
                }
            }

            // Subtract credits of the chosen enemy from remaining credits
            String chosenEnemy = availableSpawns.get(MathUtils.random(availableSpawns.size() - 1));
            Vector2 position = getEnemyPosition(playerPosition, spawnConfigs);
            remainingCredits -= enemyCredits.get(chosenEnemy);

            spawnConfigs.add(new EnemySpawnConfig(chosenEnemy, position));

            System.out.println("Added enemy type " + chosenEnemy + " at " + position + ". Remaining credits: " + remainingCredits);
        }

        return new LevelWaveConfig(-1f, 0f, spawnConfigs);
    }

    public LevelWaveConfig prepareNextWave() {
        return new LevelWaveConfig(-1f, 0f, new ArrayList<EnemySpawnConfig>());
    }

    private Vector2 getEnemyPosition(Vector2 playerPosition, ArrayList<EnemySpawnConfig> spawnConfigs) {
        // x - random with padding
        float x = MathUtils.random(enemySpawnPadding, GameConfig.WIDTH - enemySpawnPadding);
        // y - if player is above, random in lower half with padding and vice versa
        float y = playerPosition.y > GameConfig.HEIGHT/2f
                ? MathUtils.random(enemySpawnPadding, GameConfig.HEIGHT/2f - enemySpawnPadding)
                : MathUtils.random(GameConfig.HEIGHT/2f + enemySpawnPadding, GameConfig.HEIGHT - enemySpawnPadding);

        return new Vector2(x, y);
    }
}
