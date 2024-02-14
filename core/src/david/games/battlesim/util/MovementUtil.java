package david.games.battlesim.util;

import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.math.Rectangle;

public final class MovementUtil {

    // Returns angle from hitbox to destination point specified by (desX, desY)
    public static float findAngleBetweenPoints(float srcX, float srcY, float desX, float desY){
        float angle = (float) Math.atan2((desY - srcY), (desX - srcX));
        return (((float) Math.toDegrees(angle) + 360) % 360) - 90;
    }

    private MovementUtil() {
        throw new java.lang.UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
