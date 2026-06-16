package david.games.battlesim.config.database;

import com.badlogic.gdx.math.Vector2;

public class EnemySpawnConfig {
    public String type;
    public Vector2 position;

    public EnemySpawnConfig(String type, Vector2 position) {
        this.type = type;
        this.position = position;
    }
}
