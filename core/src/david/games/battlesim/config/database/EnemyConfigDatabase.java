package david.games.battlesim.config.database;

import java.util.HashMap;
import java.util.Map;

import david.games.battlesim.config.EnemySteeringState;
import david.games.battlesim.config.GameConfig;

public final class EnemyConfigDatabase {
    private Map<String, EnemyConfig> configs = new HashMap<>();
    public EnemyConfigDatabase() {
        /* ************** Kamikaze enemy ************** */
        EnemyConfig.KamikazeConfig kamikaze = new EnemyConfig.KamikazeConfig(
            2f, 75f, 80f, GameConfig.DEFAULT_ENEMY_SIZE, 120f, 25f
        );
        setDefaultAiConfig(kamikaze);
        kamikaze.steeringState.maxLinearAcceleration = 600.0f;
        kamikaze.steeringState.maxAngularAcceleration = 600.0f;

        configs.put("kamikaze", kamikaze);

        /* ************** Shooter enemy ************** */
        EnemyConfig.ShooterConfig shooter = new EnemyConfig.ShooterConfig(
                2f, 20f, 100f, GameConfig.DEFAULT_ENEMY_SIZE, 2f, 300f, 600f, 400f, 1000f
        );
        setDefaultAiConfig(shooter);

        configs.put("shooter", shooter);

        /* ************** Slasher enemy ************** */
        EnemyConfig.SlasherConfig slasher = new EnemyConfig.SlasherConfig(
                2f, 20f, 120f, GameConfig.DEFAULT_ENEMY_SIZE, 1f, 600f, 230f, 1f, 220f
        );
        setDefaultAiConfig(slasher);

        configs.put("slasher", slasher);

        /* ************** Sucker enemy ************** */
        EnemyConfig.SuckerConfig sucker = new EnemyConfig.SuckerConfig(
                0f, 1f, 250f, GameConfig.DEFAULT_ENEMY_SIZE*1.4f, 100f, 25f, 12f, 35f
        );
        setDefaultAiConfig(sucker);
        sucker.steeringState.maxLinearAcceleration = sucker.chaseSpeed;
        sucker.steeringState.maxAngularAcceleration = sucker.chaseSpeed;
        sucker.steeringState.maxLinearSpeed = sucker.chaseSpeed;
        sucker.steeringState.maxAngularSpeed = sucker.chaseSpeed;

        configs.put("sucker", sucker);
    }

    private void setDefaultAiConfig(EnemyConfig instance) {
        EnemySteeringState steeringState = new EnemySteeringState();

        steeringState.maxLinearSpeed = 1500.0f;
        steeringState.maxLinearAcceleration = 200.0f;
        steeringState.maxAngularSpeed = 1500.0f;
        steeringState.maxAngularAcceleration = 200.0f;
        steeringState.orientation = 5f; steeringState.angularVelocity = 5f; steeringState.zeroLinearSpeedThreshold = 5f;

        instance.steeringState = steeringState;
    }

    public EnemyConfig get(String type) {
        return configs.get(type);
    }
}
