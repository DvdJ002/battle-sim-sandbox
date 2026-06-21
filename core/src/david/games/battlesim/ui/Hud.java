package david.games.battlesim.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import static david.games.battlesim.BattleGame.assetManager;

import java.util.ArrayList;

import david.games.battlesim.assets.AssetPaths;
import david.games.battlesim.config.GameConfig;
import david.games.battlesim.elements.actors.Enemy;
import david.games.battlesim.elements.damage.StatusEffect;
import david.games.battlesim.world.BattleWorld;

public class Hud {
    Texture textureIced, textureKnockback, textureSucked, textureSlowed, textureUnknown, textureInvincible;
    private float padding = 15f;
    public Hud() {
        textureIced = assetManager.get(AssetPaths.EFFECT_ICED, Texture.class);
        textureKnockback = assetManager.get(AssetPaths.EFFECT_KNOCKBACK, Texture.class);
        textureSucked = assetManager.get(AssetPaths.EFFECT_SUCKED, Texture.class);
        textureSlowed = assetManager.get(AssetPaths.EFFECT_SLOWED, Texture.class);
        textureUnknown = assetManager.get(AssetPaths.EFFECT_UNKNOWN, Texture.class);
        textureInvincible = assetManager.get(AssetPaths.EFFECT_INVINCIBLE, Texture.class);
    }

    // Draw HUD based on worldState, worldState must NOT be modified!
    public void drawBars(ShapeRenderer sr, BattleWorld worldState) {
        /**************** ENEMIES *****************/
        for (Enemy enemy: worldState.enemies){
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

    public void drawText(SpriteBatch batch, BitmapFont font, BattleWorld worldState) {
        float x =  GameConfig.WIDTH - padding - 130f;
        float y =  GameConfig.HEIGHT - padding;
        float time = worldState.levelTimer;
        if (time <= 3f) {
            font.setColor(Color.RED);
        }
        font.draw(batch, String.format("Left: %.1f", time) , x, y);
        font.draw(batch, String.format("Left: %.1f", time) , x + 1f, y);


        // Stage hasn't started yet
        if (worldState.currentWave < 1) {
            return;
        }

        y -= 2*padding;
        x += 3*padding;
        font.setColor(Color.BLACK);
        font.draw(batch, (worldState.currentWave) + "/" + worldState.levelConfig.waves.size(), x, y);
        font.draw(batch, (worldState.currentWave) + "/" + worldState.levelConfig.waves.size(), x + 1f, y);
    }


    private Texture getEffectTexture(StatusEffect type) {
        switch (type) {
            case ICED: return textureIced;
            case SLOWED: return textureSlowed;
            case KNOCKBACK: return textureKnockback;
            case SUCKED: return textureSucked;
            case INVINCIBLE: return textureInvincible;
            default: return textureUnknown;
        }
    }

}
