package david.games.battlesim.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

import david.games.battlesim.elements.Player;

public class MyInputProcessor implements InputProcessor {

    private Player player;

    public MyInputProcessor(Player player) {
        this.player = player;
    }

    public boolean touchDown (int x, int y, int pointer, int button) {
        if (button == Input.Buttons.RIGHT){ player.activateShield(); }
        return true;
    }

    public boolean touchUp (int x, int y, int pointer, int button) {
        if (button == Input.Buttons.RIGHT){ player.deactivateShield(); }
        return true;
    }

    public boolean keyDown (int keycode) {
        if (keycode == Input.Keys.SPACE) { player.phase(); }
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged (int x, int y, int pointer) {
        return false;
    }

    public boolean mouseMoved (int x, int y) {
        return false;
    }

    public boolean scrolled (float amountX, float amountY) {
        return false;
    }

    public boolean keyUp (int keycode) {
        return false;
    }

    public boolean keyTyped (char character) {
        return false;
    }
}

