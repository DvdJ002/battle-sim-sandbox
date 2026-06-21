package david.games.battlesim.elements;

import java.util.ArrayList;

import david.games.battlesim.elements.actors.Enemy;
import david.games.battlesim.elements.actors.Player;
import david.games.battlesim.elements.spawners.BulletSpawner;
import david.games.battlesim.elements.spawners.EnemySpawner;
import david.games.battlesim.elements.spawners.ForceFieldSpawner;

public class GameContext {
    public Player player;
    public ArrayList<Enemy> enemies;
    public BulletSpawner bulletSpawner;
    public EnemySpawner enemySpawner;
    public ForceFieldSpawner forceFieldSpawner;

}
