package com.jbatista.wmo.operator.instrument;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class KeyboardInputProcessor implements InputProcessor {

    private final InstrumentActor instrumentActor;

    private int octaveIndex = 2;
    private int keyIndexOffset = 24;

    KeyboardInputProcessor(InstrumentActor instrumentActor) {
        this.instrumentActor = instrumentActor;
    }

    void shiftOctaveLeft() {
        octaveIndex = Math.max(0, octaveIndex - 1);
        keyIndexOffset = octaveIndex * 12;

        instrumentActor.shiftOctaveLeft(octaveIndex + 2);
    }

    void shiftOctaveRight() {
        octaveIndex = Math.min(4, octaveIndex + 1);
        keyIndexOffset = octaveIndex * 12;

        instrumentActor.shiftOctaveRight(octaveIndex + 2);
    }

    void pressKey(int index) {
        instrumentActor.pressKey(index + keyIndexOffset);
    }

    void releaseKey(int index) {
        instrumentActor.releaseKey(index + keyIndexOffset);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.LEFT:
                shiftOctaveLeft();
                break;

            case Input.Keys.RIGHT:
                shiftOctaveRight();
                break;

            case Input.Keys.Q:
                if (octaveIndex > 0) {
                    pressKey(0 - 12);
                }
                break;

            case Input.Keys.NUM_2:
                if (octaveIndex > 0) {
                    pressKey(1 - 12);
                }
                break;

            case Input.Keys.W:
                if (octaveIndex > 0) {
                    pressKey(2 - 12);
                }
                break;

            case Input.Keys.NUM_3:
                if (octaveIndex > 0) {
                    pressKey(3 - 12);
                }
                break;

            case Input.Keys.E:
                if (octaveIndex > 0) {
                    pressKey(4 - 12);
                }
                break;

            case Input.Keys.R:
                if (octaveIndex > 0) {
                    pressKey(5 - 12);
                }
                break;

            case Input.Keys.NUM_5:
                if (octaveIndex > 0) {
                    pressKey(6 - 12);
                }
                break;

            case Input.Keys.T:
                if (octaveIndex > 0) {
                    pressKey(7 - 12);
                }
                break;

            case Input.Keys.NUM_6:
                if (octaveIndex > 0) {
                    pressKey(8 - 12);
                }
                break;

            case Input.Keys.Y:
                if (octaveIndex > 0) {
                    pressKey(9 - 12);
                }
                break;

            case Input.Keys.NUM_7:
                if (octaveIndex > 0) {
                    pressKey(10 - 12);
                }
                break;

            case Input.Keys.U:
                if (octaveIndex > 0) {
                    pressKey(11 - 12);
                }
                break;

            case Input.Keys.Z:
                pressKey(0);
                break;

            case Input.Keys.S:
                pressKey(1);
                break;

            case Input.Keys.X:
                pressKey(2);
                break;

            case Input.Keys.D:
                pressKey(3);
                break;

            case Input.Keys.C:
                pressKey(4);
                break;

            case Input.Keys.V:
                pressKey(5);
                break;

            case Input.Keys.G:
                pressKey(6);
                break;

            case Input.Keys.B:
                pressKey(7);
                break;

            case Input.Keys.H:
                pressKey(8);
                break;

            case Input.Keys.N:
                pressKey(9);
                break;

            case Input.Keys.J:
                pressKey(10);
                break;

            case Input.Keys.M:
                pressKey(11);
                break;

            case Input.Keys.COMMA:
                pressKey(12);
                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.Q:
                if (octaveIndex > 0) {
                    releaseKey(0 - 12);
                }
                break;

            case Input.Keys.NUM_2:
                if (octaveIndex > 0) {
                    releaseKey(1 - 12);
                }
                break;

            case Input.Keys.W:
                if (octaveIndex > 0) {
                    releaseKey(2 - 12);
                }
                break;

            case Input.Keys.NUM_3:
                if (octaveIndex > 0) {
                    releaseKey(3 - 12);
                }
                break;

            case Input.Keys.E:
                if (octaveIndex > 0) {
                    releaseKey(4 - 12);
                }
                break;

            case Input.Keys.R:
                if (octaveIndex > 0) {
                    releaseKey(5 - 12);
                }
                break;

            case Input.Keys.NUM_5:
                if (octaveIndex > 0) {
                    releaseKey(6 - 12);
                }
                break;

            case Input.Keys.T:
                if (octaveIndex > 0) {
                    releaseKey(7 - 12);
                }
                break;

            case Input.Keys.NUM_6:
                if (octaveIndex > 0) {
                    releaseKey(8 - 12);
                }
                break;

            case Input.Keys.Y:
                if (octaveIndex > 0) {
                    releaseKey(9 - 12);
                }
                break;

            case Input.Keys.NUM_7:
                if (octaveIndex > 0) {
                    releaseKey(10 - 12);
                }
                break;

            case Input.Keys.U:
                if (octaveIndex > 0) {
                    releaseKey(11 - 12);
                }
                break;

            case Input.Keys.Z:
                releaseKey(0);
                break;

            case Input.Keys.S:
                releaseKey(1);
                break;

            case Input.Keys.X:
                releaseKey(2);
                break;

            case Input.Keys.D:
                releaseKey(3);
                break;

            case Input.Keys.C:
                releaseKey(4);
                break;

            case Input.Keys.V:
                releaseKey(5);
                break;

            case Input.Keys.G:
                releaseKey(6);
                break;

            case Input.Keys.B:
                releaseKey(7);
                break;

            case Input.Keys.H:
                releaseKey(8);
                break;

            case Input.Keys.N:
                releaseKey(9);
                break;

            case Input.Keys.J:
                releaseKey(10);
                break;

            case Input.Keys.M:
                releaseKey(11);
                break;

            case Input.Keys.COMMA:
                releaseKey(12);
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
