package david.games.battlesim.elements.spawners;

public interface BulletSpawner {
    void spawn(float x, float y, float angle, float speed, float damage, boolean fromPlayer);
}
