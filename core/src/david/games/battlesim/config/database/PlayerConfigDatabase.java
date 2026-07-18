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
                13f,
                6.7f,
                3f,
                2.1f,
                105f,
                20f,
                0.13f,
                0.7f
        );
    }

    public PlayerConfig get() {
        return playerConfig;
    }
}
