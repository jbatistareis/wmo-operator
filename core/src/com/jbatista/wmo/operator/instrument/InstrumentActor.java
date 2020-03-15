package com.jbatista.wmo.operator.instrument;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jbatista.wmo.AudioFormat;
import com.jbatista.wmo.KeyboardNote;
import com.jbatista.wmo.preset.InstrumentPreset;
import com.jbatista.wmo.synthesis.Instrument;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InstrumentActor extends Table {

    private final Instrument instrument = new Instrument(AudioFormat._44100Hz_16bit);
    private final InputProcessor keyboardInputProcessor;
    private final KeyboardPanel keyboardPanel;
    private final List<OscillatorPanel> oscillatorPanels = new ArrayList<>();
    private final AudioDevice audioDevice = Gdx.audio.newAudioDevice(44100, false);

    private final KeyboardNote[] notes = new KeyboardNote[61];

    private final float[] buffer = new float[44100];
    private float[] frame = new float[2];

    private int i;
    private int currentOctave;
    private int keyOffset = 48;

    private final List<InstrumentPreset> presets = new ArrayList<>();

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public InstrumentActor(Skin skin) {
        instrument.setGain(0.02);

        bottom();
        setFillParent(true);

        final KeyboardNote[] allNotes = KeyboardNote.values();
        for (int i = keyOffset; i <= (keyOffset + 60); i++) {
            notes[i - keyOffset] = allNotes[i];
        }

        keyboardInputProcessor = new KeyboardInputProcessor(this);
        keyboardPanel = new KeyboardPanel(this, skin);
        keyboardPanel.changeOctaveLabel(2);
        keyboardPanel.setMovable(false);
        keyboardPanel.pack();

        for (int i = 0; i < 6; i++) {
            final OscillatorPanel oscillatorPanel = new OscillatorPanel(i + 1, instrument.getAlgorithm().getOscillator(i), skin);
            oscillatorPanel.setMovable(false);
            oscillatorPanel.pack();

            if ((i != 2) && (i != 5)) {
                add(oscillatorPanel).expand();
            } else {
                add(oscillatorPanel).expand().row();
            }

            oscillatorPanels.add(oscillatorPanel);
        }

        add(keyboardPanel).colspan(3).expandX().row();

        executorService.submit(() -> {
            while (true) {
                for (i = 0; i < 256; i += 2) {
                    frame = instrument.getFloatFrame();
                    buffer[i] = frame[0];
                    buffer[i + 1] = frame[1];
                }

                audioDevice.writeSamples(buffer, 0, 256);
            }
        });
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
        keyboardPanel.changeOctaveLabel(currentOctave - 2);
    }

    void shiftOctaveRight(int currentOctave) {
        this.currentOctave = currentOctave;
        keyboardPanel.changeOctaveLabel(currentOctave - 2);
    }

    void pressKey(int index) {
        instrument.pressKey(index + keyOffset, notes[index].getFrequency());
        keyboardPanel.pressKey(index);
    }

    void releaseKey(int index) {
        instrument.releaseKey(index + keyOffset, notes[index].getFrequency());
        keyboardPanel.releaseKey(index);
    }

    void changeInstrument(int id) {
        System.out.println(presets.get(id).getName());
        instrument.loadInstrumentPreset(presets.get(id));
        oscillatorPanels.forEach(panel -> panel.reload());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    protected void finalize() throws Throwable {
        audioDevice.dispose();
        executorService.shutdownNow();
    }

}
