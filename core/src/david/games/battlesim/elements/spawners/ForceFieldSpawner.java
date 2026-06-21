package david.games.battlesim.elements.spawners;

public interface ForceFieldSpawner {
    void spawn(float x, float y, float maxDamage, float duration, float size, boolean fromPlayer, boolean waning);
}
