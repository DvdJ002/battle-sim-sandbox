package david.games.battlesim.config.database;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import david.games.battlesim.config.EnemySteeringState;
import david.games.battlesim.config.GameConfig;

public final class EnemyConfigDatabase {
    private Map<String, EnemyConfig> configs = new HashMap<>();
    public EnemyConfigDatabase() {
        /* ------------------------------------------------------------ */
        /* ----------------------- BASE ENEMIES ----------------------- */
        /* ------------------------------------------------------------ */

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
                2f, 20f, 100f, GameConfig.DEFAULT_ENEMY_SIZE, 2f, 300f, 600f, 400f, 1000f, 6.7f
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
                0f, 1f, 250f, GameConfig.DEFAULT_ENEMY_SIZE*1.4f, 100f, 20f, 12f, 45f
        );
        setDefaultAiConfig(sucker);
        sucker.steeringState.maxLinearAcceleration = sucker.chaseSpeed;
        sucker.steeringState.maxAngularAcceleration = sucker.chaseSpeed;
        sucker.steeringState.maxLinearSpeed = sucker.chaseSpeed;
        sucker.steeringState.maxAngularSpeed = sucker.chaseSpeed;

        configs.put("sucker", sucker);

        /* ************** Healer enemy ************** */
        EnemyConfig.HealerConfig healer = new EnemyConfig.HealerConfig(
                0f, 1f, 40f, GameConfig.DEFAULT_ENEMY_SIZE, 200f, 0.5f, 120f, 240f, 7f, 4f
        );
        setDefaultAiConfig(healer);
        healer.steeringState.maxLinearAcceleration = healer.roamSpeed;
        healer.steeringState.maxAngularAcceleration = healer.roamSpeed;
        healer.steeringState.maxLinearSpeed = healer.roamSpeed;
        healer.steeringState.maxAngularSpeed = healer.roamSpeed;

        configs.put("healer", healer);

        /* ************** Summoner enemy ************** */
        EnemyConfig.SummonerConfig summoner = new EnemyConfig.SummonerConfig(
                1.1f, 0f, 300f, GameConfig.DEFAULT_ENEMY_SIZE*1.6f, 3, 0.75f, 1.5f, 1f, "kamikaze_small", "slasher_small"
        );
        setDefaultAiConfig(summoner);
        summoner.steeringState.maxLinearAcceleration = 125f;
        summoner.steeringState.maxAngularAcceleration = 125f;
        summoner.steeringState.maxLinearSpeed = 125f;
        summoner.steeringState.maxAngularSpeed = 125f;

        configs.put("summoner", summoner);


        /* --------------------------------------------------------------- */
        /* ----------------------- SPECIAL ENEMIES ----------------------- */
        /* --------------------------------------------------------------- */

        /* ************** Large shooter enemy ************** */
        EnemyConfig.ShooterConfig shooterLarge = new EnemyConfig.ShooterConfig(
                2f, 35f, 250f, GameConfig.DEFAULT_ENEMY_SIZE * 1.4f, 2f, 300f, 500f, 300f, 1300f, 11f
        );
        setDefaultAiConfig(shooterLarge);

        configs.put("shooter_large", shooterLarge);

        /* ************** Large slasher enemy ************** */
        EnemyConfig.SlasherConfig slasherLarge = new EnemyConfig.SlasherConfig(
                4f, 20f, 240f, GameConfig.DEFAULT_ENEMY_SIZE * 1.4f, 1.8f, 400f, 230f, 1f, 280f
        );
        setDefaultAiConfig(slasherLarge);

        configs.put("slasher_large", slasherLarge);

        /* ************** Small slasher enemy ************** */
        EnemyConfig.SlasherConfig slasherSmall = new EnemyConfig.SlasherConfig(
                0.8f, 0f, 50f, GameConfig.SMALL_ENEMY_SIZE, 1f, 300f, 150f, 0.5f, 110f
        );
        setDefaultAiConfig(slasherSmall);

        configs.put("slasher_small", slasherSmall);

        /* ************** Large kamikaze enemy ************** */
        EnemyConfig.KamikazeConfig kamikazeLarge = new EnemyConfig.KamikazeConfig(
                2f, 190f, 180f, GameConfig.DEFAULT_ENEMY_SIZE * 1.4f, 150f, 25f
        );
        setDefaultAiConfig(kamikazeLarge);
        kamikazeLarge.steeringState.maxLinearAcceleration = 550.0f;
        kamikazeLarge.steeringState.maxAngularAcceleration = 550.0f;

        configs.put("kamikaze_large", kamikazeLarge);

        /* ************** Small kamikaze enemy ************** */
        EnemyConfig.KamikazeConfig kamikazeSmall = new EnemyConfig.KamikazeConfig(
                2f, 30f, 40f, GameConfig.SMALL_ENEMY_SIZE, 70f, 12f
        );
        setDefaultAiConfig(kamikazeSmall);
        kamikazeSmall.steeringState.maxLinearAcceleration = 400.0f;
        kamikazeSmall.steeringState.maxAngularAcceleration = 400.0f;

        configs.put("kamikaze_small", kamikazeSmall);

        /* ************** Special healer enemy ************** */
        EnemyConfig.HealerConfig healerSpecial = new EnemyConfig.HealerConfig(
                0f, 1f, 40f, GameConfig.DEFAULT_ENEMY_SIZE, 500f, 7f, 120f, 300f, 10f, 4f
        );
        setDefaultAiConfig(healerSpecial);
        healerSpecial.steeringState.maxLinearAcceleration = healerSpecial.roamSpeed;
        healerSpecial.steeringState.maxAngularAcceleration = healerSpecial.roamSpeed;
        healerSpecial.steeringState.maxLinearSpeed = healerSpecial.roamSpeed;
        healerSpecial.steeringState.maxAngularSpeed = healerSpecial.roamSpeed;

        configs.put("healer_special", healerSpecial);

        /* --------------------------------------------------------------- */
        /* -------------------------- BOSS ENEMY ------------------------- */
        /* --------------------------------------------------------------- */
        EnemyConfig.BossConfig boss = new EnemyConfig.BossConfig(
                1.5f,
                1f,
                5650f,
                GameConfig.BOSS_SIZE,
                125f,
                1.8f,
                240f,
                850f,
                430f,
                0.7f,
                0.25f,
                1100f,
                20f,
                10f,
                5f,
                0.4f,
                1.5f,
                3.5f,
                160f,
                600f,
                1350f,
                4,
                1.2f,
                1f,
                50f,
                3f,
                3f,
                1100f,
                700f,
                10f,
                30f,
                135f,
                150f,
                4f,
                0.8f,
                95f,
                450f,
                30f,
                2.9f,
                0.75f,
                10f,
                1500f,
                900f,
                2f,
                new ArrayList<Integer>(),
                new ArrayList<Integer>(),
                new ArrayList<Integer>()
        );

        // 0 - NONE, 1 - DASH (Both), 2 - BULLETS (Long), 3 - SUMMON_OFFENSIVES (Long), 4 - SUMMON_HEALERS (Both),
        // 5 - SLAM (Close), 6 - EXPLOSION (Both), 7 - CANNONBALLS (Long), 8 - SUMMON_KAMIKAZES, 9 - PURSUIT
        boss.closeRangeAttackPool = Arrays.asList(1, 1, 4, 5, 5, 6);
        boss.longRangeAttackPool = Arrays.asList(1, 1, 2, 2, 3, 3, 3, 3, 4, 6, 7);
        //boss.enragedAttackPool = Arrays.asList(8, 9); // Plus final is hardcoded

        setDefaultAiConfig(boss);
        boss.steeringState.maxLinearSpeed = boss.baseSpeed;
        boss.steeringState.maxAngularSpeed = boss.baseSpeed;

        configs.put("boss", boss);
    }

    private void setDefaultAiConfig(EnemyConfig instance) {
        EnemySteeringState steeringState = new EnemySteeringState();

        steeringState.maxLinearSpeed = 1500.0f;
        steeringState.maxLinearAcceleration = GameConfig.DEFAULT_ACCEL;
        steeringState.maxAngularSpeed = 1500.0f;
        steeringState.maxAngularAcceleration = GameConfig.DEFAULT_ACCEL;
        steeringState.orientation = 5f; steeringState.angularVelocity = 5f; steeringState.zeroLinearSpeedThreshold = 5f;

        instance.steeringState = steeringState;
    }

    public EnemyConfig get(String type) {
        return configs.get(type);
    }
}
