package com.jbatista.wmo.operator.instrument;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jbatista.wmo.AudioFormat;
import com.jbatista.wmo.KeyboardNote;
import com.jbatista.wmo.synthesis.Instrument;
import com.jbatista.wmo.synthesis.Key;
import com.jbatista.wmo.synthesis.Oscillator;

import java.util.LinkedList;
import java.util.List;

public class InstrumentActor extends Table {

    private final Instrument instrument = new Instrument(AudioFormat._44100Hz_16bit);
    private final InputProcessor keyboardInputProcessor;
    private final KeyboardPanel keyboardPanel;
    private final AudioDevice audioDevice = Gdx.audio.newAudioDevice(44100, false);

    private final KeyboardNote[] notes = new KeyboardNote[61];
    private final List<Key> keys = new LinkedList<>();

    private final float[] buffer = new float[44100];
    private float[] frame = new float[2];

    private int i;
    private int currentOctave;

    public InstrumentActor() {
        instrument.getAlgorithm().getOscillators().add(new Oscillator());

        bottom();
        setFillParent(true);

        final KeyboardNote[] allNotes = KeyboardNote.values();
        for (int i = 24; i <= 84; i++) {
            notes[i - 24] = allNotes[i];
            keys.add(this.instrument.buildKey(allNotes[i].getFrequency()));
        }

        keyboardInputProcessor = new KeyboardInputProcessor(this);
        keyboardPanel = new KeyboardPanel(this);

        keyboardPanel.pack();

        keyboardPanel.changeOctaveLabel(2);

        add(keyboardPanel).expandX().row();
    }

    KeyboardNote[] getNotes() {
        return notes;
    }

    int getCurrentOctave() {
        return currentOctave;
    }

    public InputProcessor getKeyboardInputProcessor() {
        return keyboardInputProcessor;
    }

    void shiftOctaveLeft(int currentOctave) {
        this.currentOctave = currentOctave;
        keys.forEach(key -> key.release());
        keyboardPanel.changeOctaveLabel(currentOctave - 2);
    }

    void shiftOctaveRight(int currentOctave) {
        this.currentOctave = currentOctave;
        keys.forEach(key -> key.release());
        keyboardPanel.changeOctaveLabel(currentOctave - 2);
    }

    void pressKey(int index) {
        instrument.pressKey(notes[index].getFrequency());
        keyboardPanel.pressKey(index);
    }

    void releaseKey(int index) {
        instrument.releaseKey(notes[index].getFrequency());
        keyboardPanel.releaseKey(index);
    }

    @Override
    public void act(float delta) {
        playAudio();

        super.act(delta);
    }

    private void playAudio() {
        for (i = 0; i < 2048; i += 2) {
            frame = instrument.getFloatFrame();

            buffer[i] = frame[0];
            buffer[i + 1] = frame[1];
        }

        audioDevice.writeSamples(buffer, 0, 2048);
    }

    @Override
    protected void finalize() throws Throwable {
        audioDevice.dispose();
    }

}
