package david.games.battlesim.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import david.games.battlesim.config.database.EnemySpawnConfig;
import david.games.battlesim.config.database.LevelConfig;
import david.games.battlesim.config.database.LevelWaveConfig;
import david.games.battlesim.elements.data.DamageAction;
import david.games.battlesim.elements.data.StatusEffect;

public final class GameUtil {

    // Returns angle from (srcX, srcY) to destination point (desX, desY)
    public static float findAngleBetweenPoints(float srcX, float srcY, float desX, float desY){
        float angle = (float) Math.atan2((desY - srcY), (desX - srcX));
        return (((float) Math.toDegrees(angle) + 360) % 360);
    }

    // Returns a movement vector for the path from (srcX, srcY) -> (desX, desY)
    public static Vector2 findNearestPathToPoint(float srcX, float srcY, float desX, float desY) {
        Vector2 direction = new Vector2();
        direction.x = desX - srcX;
        direction.y = desY - srcY;
        return direction.nor();
    }

    // Returns true if the distance between the points is less or equal to distance parameter
    public static boolean isNear(float srcX, float srcY, float desX, float desY, float distance){
        return (Vector2.dst(srcX, srcY, desX, desY) <= distance);
    }

    // Returns a point on the circle based on the angle from the center (centerX, centerY)
    public static void angleToCirclePoints(float centerX, float centerY, float radius, float angle, Vector2 result){
        float angleRadians = MathUtils.degreesToRadians * (angle);
        result.set(centerX + radius * MathUtils.cos(angleRadians), centerY + radius * MathUtils.sin(angleRadians));
    }

    public static DamageAction getDamageAction(StatusEffect type, float amount, float intensity, float duration) {
        DamageAction damageAction = new DamageAction();
        damageAction.type = type;
        damageAction.amount = amount;
        damageAction.intensity = intensity;
        damageAction.duration = duration;

        return damageAction;
    }

    // TODO spawning logic
    public static LevelWaveConfig generateInfiniteWave(int credits) {
        ArrayList<EnemySpawnConfig> spawnConfigs = new ArrayList<>();

        // Enemy placements logic
        spawnConfigs.add(new EnemySpawnConfig("shooter_large", new Vector2(400f, 440f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(300f, 340f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(400f, 340f)));
        spawnConfigs.add(new EnemySpawnConfig("shooter", new Vector2(500f, 340f)));

        return new LevelWaveConfig(-1f, 0f, spawnConfigs);
    }

    private GameUtil() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
