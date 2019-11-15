package com.jbatista.wmo.operator;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.jbatista.wmo.KeyboardNote;
import com.jbatista.wmo.synthesis.Instrument;
import com.jbatista.wmo.synthesis.Key;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class KeyboardProcessor implements InputProcessor {

    private final Instrument instrument;
    private final List<Key> keys = new LinkedList<>();

    public KeyboardProcessor(Instrument instrument, int amountOfKeys, KeyboardNote startingNote) {
        this.instrument = instrument;
        final KeyboardNote[] notes = KeyboardNote.values();
        final int index = Arrays.asList(notes).indexOf(startingNote);

        for (int i = index; i <= (amountOfKeys + index); i++) {
            keys.add(this.instrument.buildKey(notes[i].getFrequency()));
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.Z:
                keys.get(0).press();
                break;
            case Input.Keys.S:
                keys.get(1).press();
                break;
            case Input.Keys.X:
                keys.get(2).press();
                break;
            case Input.Keys.D:
                keys.get(3).press();
                break;
            case Input.Keys.C:
                keys.get(4).press();
                break;
            case Input.Keys.V:
                keys.get(5).press();
                break;
            case Input.Keys.G:
                keys.get(6).press();
                break;
            case Input.Keys.B:
                keys.get(7).press();
                break;
            case Input.Keys.H:
                keys.get(8).press();
                break;
            case Input.Keys.N:
                keys.get(9).press();
                break;
            case Input.Keys.J:
                keys.get(10).press();
                break;
            case Input.Keys.M:
                keys.get(11).press();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.Z:
                keys.get(0).release();
                break;
            case Input.Keys.S:
                keys.get(1).release();
                break;
            case Input.Keys.X:
                keys.get(2).release();
                break;
            case Input.Keys.D:
                keys.get(3).release();
                break;
            case Input.Keys.C:
                keys.get(4).release();
                break;
            case Input.Keys.V:
                keys.get(5).release();
                break;
            case Input.Keys.G:
                keys.get(6).release();
                break;
            case Input.Keys.B:
                keys.get(7).release();
                break;
            case Input.Keys.H:
                keys.get(8).release();
                break;
            case Input.Keys.N:
                keys.get(9).release();
                break;
            case Input.Keys.J:
                keys.get(10).release();
                break;
            case Input.Keys.M:
                keys.get(11).release();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
