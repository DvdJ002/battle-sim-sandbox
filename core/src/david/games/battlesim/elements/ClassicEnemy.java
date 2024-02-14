package david.games.battlesim.elements;

import static david.games.battlesim.BattleGame.assetManager;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;

public class ClassicEnemy {
    public Rectangle hitbox;
    EnemyType type;
    Texture texture;

    float width, height, speed;

    public ClassicEnemy(EnemyType type, float x, float y, float speed) {
        switch(type){
            case SLASHER:
                texture = assetManager.get(AssetPaths.SLASHER, Texture.class);
                hitbox = new Rectangle();
                hitbox.x = x;
                hitbox.y = y;
                hitbox.width = GameConfig.WIDTH/14;
                hitbox.height = GameConfig.WIDTH/14;
                this.speed = speed;
                this.type = type;
                break;
            case SHOOTER:
                texture = assetManager.get(AssetPaths.SHOOTER, Texture.class);
                break;
        }
    }

    public void draw(SpriteBatch batch){
        batch.draw(
                texture, hitbox.x, hitbox.y, hitbox.width, hitbox.height,
                0, 0, texture.getWidth(), texture.getHeight(), false, false
        );
    }

    public void update(){
        // Check for health and stuff
        // Movement
        hitbox.x++;
        hitbox.y++;
    }
}
