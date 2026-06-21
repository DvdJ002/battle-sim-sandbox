package david.games.battlesim.util;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import david.games.battlesim.elements.damage.DamageAction;
import david.games.battlesim.elements.damage.StatusEffect;

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
    public static Vector2 angleToCirclePoints(float centerX, float centerY, float radius, float angle){
        float angleRadians = MathUtils.degreesToRadians * (angle + 90);
        float x = centerX + radius * MathUtils.cos(angleRadians);
        float y = centerY + radius * MathUtils.sin(angleRadians);
        return new Vector2(x, y);
    }

    public static DamageAction getDamageAction(StatusEffect type, float amount, float intensity, float duration) {
        DamageAction damageAction = new DamageAction();
        damageAction.type = type;
        damageAction.amount = amount;
        damageAction.intensity = intensity;
        damageAction.duration = duration;

        return damageAction;
    }

    private GameUtil() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
