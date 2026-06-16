package david.games.battlesim.config.database;

import david.games.battlesim.config.database.PlayerConfig;

public class PlayerConfigDatabase {
    private PlayerConfig playerConfig;
    public PlayerConfigDatabase() {
        playerConfig = new PlayerConfig(
                280f,
                0.5f,
                100f,
                100f,
                0.1f,
                1.25f,
                15f,
                1000f,
                10f
        );
    }

    public PlayerConfig get() {
        return playerConfig;
    }
}
