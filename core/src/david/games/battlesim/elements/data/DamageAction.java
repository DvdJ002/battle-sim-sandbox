package david.games.battlesim.elements.data;

import com.badlogic.gdx.math.Vector2;

public class DamageAction {
    public float amount;
    public StatusEffect type;
    public float intensity;
    public float duration;
    public final Vector2 sourcePosition = new Vector2();
}

