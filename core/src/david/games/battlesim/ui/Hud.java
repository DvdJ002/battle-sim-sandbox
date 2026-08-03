package david.games.battlesim.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;

import static david.games.battlesim.BattleGame.assetManager;

import java.util.ArrayList;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.elements.actors.Boss;
import david.games.battlesim.elements.actors.Enemy;
import david.games.battlesim.elements.actors.ForceField;
import david.games.battlesim.elements.actors.Bullet;
import david.games.battlesim.elements.data.BossState;
import david.games.battlesim.elements.data.StatusEffect;
import david.games.battlesim.world.BattleWorld;

public class Hud {
    Texture textureIced, textureRooted, textureSlowed, textureUnknown, textureInvincible, textureDisarmed;
    private float padding = 15f;
    public Hud() {
        textureIced = assetManager.get(AssetPaths.EFFECT_ICED, Texture.class);
        textureRooted = assetManager.get(AssetPaths.EFFECT_ROOTED, Texture.class);
        textureSlowed = assetManager.get(AssetPaths.EFFECT_SLOWED, Texture.class);
        textureUnknown = assetManager.get(AssetPaths.EFFECT_UNKNOWN, Texture.class);
        textureInvincible = assetManager.get(AssetPaths.EFFECT_INVINCIBLE, Texture.class);
        textureDisarmed = assetManager.get(AssetPaths.EFFECT_DISARMED, Texture.class);
    }

    // Draw HUD based on worldState, worldState must NOT be modified!
    public void drawBars(ShapeRenderer sr, BattleWorld worldState) {
        /**************** ENEMIES *****************/
        // Don't draw health of the boss
        for (int i = (worldState.bossFight) ? 1 : 0; i < worldState.enemies.size(); i++) {
            Enemy enemy = worldState.enemies.get(i);
            Vector2 position = enemy.getPosition();

            float x = position.x;
            float y = position.y + enemy.hitbox.height * 1.1f;
            float barHeight = 8f;

            // Full bar
            sr.setColor(Color.DARK_GRAY);
            sr.rect(x, y, enemy.hitbox.width, barHeight);
            // Fill
            sr.setColor(Color.RED);
            sr.rect(x, y, enemy.getHealthPercentage() * enemy.hitbox.width, barHeight);
        }

        /**************** PLAYER *****************/
        // Player HP
        float barWidth = 220f;
        float barHeight = 18f;

        float x = padding;
        float y = GameConfig.HEIGHT - padding - barHeight;
        float healthPercentage = worldState.player.health / worldState.player.config.maxHealth;

        // Full HP bar
        sr.setColor(Color.DARK_GRAY);
        sr.rect(x, y, barWidth, barHeight);
        // Fill HP
        sr.setColor(worldState.player.invincible ? Color.PINK : Color.GREEN);
        sr.rect(x, y, barWidth * healthPercentage, barHeight);

        // Player shield
        y -= (padding + barHeight) - 5f;
        float shieldHealthPercentage = worldState.player.shieldHealth / worldState.player.config.maxHealth;
        // Inverse, it's rising (charging)
        float shieldRechargePercentage = 1 - (worldState.player.shieldRechargeTimer / worldState.player.config.shieldRechargeDuration);

        // Full shield bar
        sr.setColor(Color.DARK_GRAY);
        sr.rect(x, y, barWidth, barHeight);
        // Fill shield health, if empty fill shield recharge
        if (shieldHealthPercentage > 0f) {
            sr.setColor(17/255f, 242/255f, 230/255f, 1f);
            sr.rect(x, y, barWidth * shieldHealthPercentage, barHeight);
        }
        else {
            sr.setColor(17/255f, 242/255f, 230/255f, 0.3f);
            sr.rect(x, y, barWidth * shieldRechargePercentage, barHeight);
        }


        y -= (padding + barHeight);
        float rectSize = 25f;

        // Player's ultimate ability
        sr.setColor(Color.DARK_GRAY);
        sr.rect(x, y, rectSize, rectSize);

        float forceFieldCooldown = worldState.player.forceFieldCooldownTimer <= 0f ? rectSize : rectSize - rectSize * (worldState.player.forceFieldCooldownTimer / worldState.player.config.forceFieldCooldown);
        sr.setColor(Color.GOLD);
        sr.rect(x, y, rectSize, forceFieldCooldown);


        // Player phase cooldown
        barWidth = 100f;
        barHeight = 8f;
        y += rectSize/3f;

        float dashRechargePercentage = worldState.player.phaseCooldownTimer / worldState.player.config.phaseCooldown;
        sr.setColor(Color.BLUE);
        sr.rect(x + rectSize + padding, y  + padding/2, barWidth * dashRechargePercentage, barHeight);

        /**************** BOSS *****************/
        if (worldState.bossFight && !worldState.enemies.isEmpty()) {
            Boss bossEnemy = (Boss) worldState.enemies.get(0);

            x = 150f;
            y = padding;
            barHeight = 14f;
            barWidth = 500f;
            float transparency = bossEnemy.state == BossState.IDLE ? 1f : 0.6f;

            // Full bar
            sr.setColor(59/255f, 58/255f, 58/255f, transparency);
            sr.rect(x, y, barWidth, barHeight);

            // Fill
            sr.setColor(1f, 0f, 0f, transparency);
            sr.rect(x, y, bossEnemy.getHealthPercentage() * barWidth, barHeight);
        }
    }

    // Draw HUD based on worldState, worldState must NOT be modified!
    public void drawIcons(SpriteBatch batch, BattleWorld worldState) {
        float barWidth = 220f;
        float spacing = 0f;

        float x = padding + barWidth + 50f;
        float y = GameConfig.HEIGHT - padding;
        float size = GameConfig.EFFECT_ICON_SIZE;

        ArrayList<StatusEffect> effects = worldState.player.getActiveEffects();
        for (StatusEffect effect : effects) {
            batch.draw(
                    getEffectTexture(effect), x - size + spacing, y - size, size, size,
                    size, size, 1, 1, 0f, 0, 0,
                    getEffectTexture(effect).getWidth(), getEffectTexture(effect).getHeight(), false, false
            );
            spacing += 55f;
        }
    }

    public void drawText(SpriteBatch batch, BitmapFont font, BattleWorld worldState, int levelCode) {
        float x =  GameConfig.WIDTH - padding - 140f;
        float y =  GameConfig.HEIGHT - padding;

        float time = worldState.levelTimer;
        if (time <= 3f) {
            font.setColor(Color.RED);
        }

        String timeText = String.format("Left: %.1f", time);
        font.draw(batch, timeText, x, y);
        font.draw(batch, timeText, x + 1f, y);

        // Stage hasn't started yet
        if (worldState.currentWave < 1) {
            drawLevelTitle(batch, font, levelCode);
            return;
        }

        y -= 2*padding;
        x += 3*padding;
        font.setColor(Color.BLACK);
        font.draw(batch, (worldState.currentWave) + "/" + worldState.levelConfig.waves.size(), x, y);
        font.draw(batch, (worldState.currentWave) + "/" + worldState.levelConfig.waves.size(), x + 1f, y);
    }

    public void drawBossBeatenText(SpriteBatch batch, BitmapFont font) {
        float x =  GameConfig.WIDTH - GameConfig.WIDTH/4f*3;
        float y =  GameConfig.HEIGHT - GameConfig.HEIGHT/7f;
        float linePadding = 33f;

        font.draw(batch, "Congratulations, you have beaten the final boss", x - 135f, y);
        font.getData().setScale(0.87f);
        font.draw(batch, "Try the newly unlocked bonus level to fight a special enemy", x - 150f, y - linePadding*1.2f);
        font.getData().setScale(1f);
        font.draw(batch, "Esc - Leave fight", x + 95f, y - linePadding*3f);
    }

    public void drawDebugOverlay(ShapeRenderer sr, BattleWorld worldState) {
        // Draw enemy hitboxes
        sr.setColor(Color.RED);
        Gdx.gl.glLineWidth(3f);

        for (Enemy enemy : worldState.enemies) {
            Rectangle hitbox = enemy.hitbox;
            sr.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

        // Other actor hitboxes
        sr.setColor(Color.BLACK);

        for (ForceField field : worldState.forceFields) {
            sr.circle(field.hitbox.x, field.hitbox.y, field.hitbox.radius);
        }
        for (Bullet bullet : worldState.bullets) {
            sr.circle(bullet.hitbox.x, bullet.hitbox.y, bullet.hitbox.radius);
        }

        // Draw player circle
        sr.setColor(Color.GREEN);

        Vector2 playerPos = worldState.player.position;
        sr.circle(playerPos.x, playerPos.y, worldState.player.hitbox.radius);
    }

    public void drawTutorialText(SpriteBatch batch, BitmapFont font, int wave) {
        float x =  GameConfig.WIDTH - GameConfig.WIDTH/4f*3;
        float y =  GameConfig.HEIGHT - GameConfig.HEIGHT/7f;
        float linePadding = 33f;
        switch (wave) {
            case 0:
                font.draw(batch, "Welcome to the game", x + 50f, y);
                font.getData().setScale(0.87f);
                font.draw(batch, "Shoot: LMB | Move: WASD | Dash: Space | Shield: RMB | Ult: Q ", x - 193f, y - linePadding*1.2f);
                font.getData().setScale(1f);
                font.draw(batch, "Shortly you will be shown every enemy type", x - 80f, y - linePadding*3f);
                break;
            case 1:
                font.draw(batch, "This is an aggressive enemy called slasher", x - 75f, y);
                font.draw(batch, "It will dash at you when close and slow you.", x - 75f, y - linePadding);
                break;
            case 2:
                font.draw(batch, "This is a careful enemy called shooter", x - 60f, y);
                font.draw(batch, "It will shoot at you, and evade if you get too close", x - 110f, y - linePadding);
                break;
            case 3:
                font.draw(batch, "This is an exploding enemy called kamikaze", x - 75f, y);
                font.draw(batch, "You probably just got exploded, if not congrats", x - 100f, y - linePadding);
                break;
            case 4:
                font.draw(batch, "This is a slow, tanky, AOE enemy called sucker", x - 90f, y);
                font.draw(batch, "It will suck you into a force field if you get too close", x - 135f, y - linePadding);
                break;
            case 5:
                font.draw(batch, "Finally, a healing enemy called healer", x - 60f, y);
                font.draw(batch, "It locks onto an enemy to heal it. ", x - 50f, y - linePadding);
                font.draw(batch, "If you kill the enemy it is actively healing, it will also die", x - 180f, y - 2*linePadding);
                break;
            case 6:
                font.draw(batch, "Tutorial over. The game has 8 levels + final boss", x - 150f, y);
                font.draw(batch, "Don't forget to use your ult (Q), and dash when needed", x - 180f, y - 2*linePadding);
                font.draw(batch, "You'll also fight special versions of these enemies", x - 178f, y - 3*linePadding);
                font.draw(batch, "New waves are based either on time or enemies killed", x - 178f, y - 4*linePadding);
                font.draw(batch, "R - reset | Esc - Exit level", x + 50f, y - 5*linePadding);
                break;
        }
    }

    public void drawInfiniteText(SpriteBatch batch, BitmapFont font, int currentWave, int best) {
        float x =  GameConfig.WIDTH - padding - 110f;
        float y =  GameConfig.HEIGHT - padding;

        font.draw(batch, "Best: " + best, x - 30f, y);

        y -= 2*padding;
        x -= 2*padding;
        font.setColor(Color.BLACK);
        font.draw(batch, "Wave: " + currentWave, x, y);
    }

    public void drawLevelTitle(SpriteBatch batch, BitmapFont font, int levelCode) {
        // Large text
        font.getData().setScale(1.3f);
        float x =  GameConfig.WIDTH - GameConfig.WIDTH/4f*3;
        float y =  GameConfig.HEIGHT - GameConfig.HEIGHT/7f;
        switch (levelCode) {
            case 1:
                font.draw(batch, "Level 1: Entourage", x + 50f, y);
                break;
            case 2:
                font.draw(batch, "Level 2: War on two fronts", x - 30f, y);
                break;
            case 3:
                font.draw(batch, "Level 3: Spontaneous explosion", x - 85f, y);
                break;
            case 4:
                font.draw(batch, "Level 4: Vacuum cleaned", x - 25f, y);
                break;
            case 5:
                font.draw(batch, "Level 5: Front line", x + 40f, y);
                break;
            case 6:
                font.draw(batch, "Level 6: The Chosen One", x - 35f, y);
                break;
            case 7:
                font.draw(batch, "Level 7: Hornet's Nest", x, y);
                break;
            case 8:
                font.draw(batch, "Level 8: Well-rounded attack", x - 55f, y);
                break;
            default:
                break;
        }
        // Restore
        font.getData().setScale(1f);
    }

    private Texture getEffectTexture(StatusEffect type) {
        switch (type) {
            case ICED: return textureIced;
            case SLOWED: return textureSlowed;
            case ROOTED: return textureRooted;
            case INVINCIBLE: return textureInvincible;
            case DISARMED: return textureDisarmed;
            default: return textureUnknown;
        }
    }

}
