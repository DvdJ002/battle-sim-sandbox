package david.games.battlesim.config.database;

public class LevelTriggerConfig {
    public float timeLeft;
    public float enemiesLeft;

    public LevelTriggerConfig(float timeLeft, float enemiesLeft) {
        this.timeLeft = timeLeft;
        this.enemiesLeft = enemiesLeft;
    }
}
