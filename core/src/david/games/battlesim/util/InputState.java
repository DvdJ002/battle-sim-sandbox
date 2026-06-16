package david.games.battlesim.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class InputState {
        // Player
        public Vector2 direction;
        public boolean shieldActive;
        public boolean phasePressed;
        public boolean shootBulletPressed;

        // GENERAL
        public Vector3 mousePosition;
        public boolean resetGamePressed;
        public boolean debugSpawnEnemy;

        public InputState() {
                direction = new Vector2();
                mousePosition = new Vector3();
        }

        @Override
        public String toString() {
                return "InputState{" +
                        "direction=" + direction +
                        ", shieldActive=" + shieldActive +
                        ", phasePressed=" + phasePressed +
                        ", shootBulletPressed=" + shootBulletPressed +
                        ", mousePosition=" + mousePosition +
                        ", resetGamePressed=" + resetGamePressed +
                        ", debugSpawnEnemy=" + debugSpawnEnemy +
                        '}';
        }
}
