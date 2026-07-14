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
                9f,
                1000f,
                10f,
                6.7f,
                9f,
                2.3f,
                110f,
                20f
        );
    }

    public PlayerConfig get() {
        return playerConfig;
    }
}
