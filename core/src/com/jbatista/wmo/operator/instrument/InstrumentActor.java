package com.jbatista.wmo.operator.instrument;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.jbatista.wmo.KeyboardNote;
import com.jbatista.wmo.preset.InstrumentPreset;
import com.jbatista.wmo.synthesis.Instrument;
import com.kotcrab.vis.ui.widget.file.FileChooser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InstrumentActor extends Table {

    private final Instrument instrument = new Instrument(44100);
    private final InputProcessor keyboardInputProcessor;
    private final List<OscillatorPanel> oscillatorPanels = new ArrayList<>();
    private final SysexPanel sysexPanel;
    private final KeyboardPanel keyboardPanel;
    private final HorizontalGroup hboxGeneral = new HorizontalGroup();
    private final AudioDevice audioDevice = Gdx.audio.newAudioDevice(44100, false);

    private final KeyboardNote[] notes = new KeyboardNote[61];

    private final float[] buffer = new float[44100];
    private float[] frame = new float[2];

    private int i;
    private int currentOctave;
    private int keyOffset = 48;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public InstrumentActor() {
        FileChooser.setDefaultPrefsName("com.jbatista.wmo.operator");

        bottom();
        setFillParent(true);

        for (int i = 0; i < 6; i++) {
            final OscillatorPanel oscillatorPanel = new OscillatorPanel(i, instrument);
            oscillatorPanel.pack();

            if ((i != 2) && (i != 5)) {
                add(oscillatorPanel).expand();
            } else {
                add(oscillatorPanel).expand().row();
            }

            oscillatorPanels.add(oscillatorPanel);
        }

        sysexPanel = new SysexPanel(this);
        hboxGeneral.space(5);
        hboxGeneral.addActor(sysexPanel);

        final KeyboardNote[] allNotes = KeyboardNote.values();
        for (int i = keyOffset; i <= (keyOffset + 60); i++) {
            notes[i - keyOffset] = allNotes[i];
        }

        keyboardInputProcessor = new KeyboardInputProcessor(this);
        keyboardPanel = new KeyboardPanel(this);
        keyboardPanel.changeOctaveLabel(2);
        keyboardPanel.setMovable(false);
        keyboardPanel.pack();

        add(hboxGeneral).expand().colspan(3).row();
        add(keyboardPanel).expand().colspan(3).row();

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
        instrument.pressKey(index + keyOffset);
        keyboardPanel.pressKey(index);
    }

    void releaseKey(int index) {
        instrument.releaseKey(index + keyOffset);
        keyboardPanel.releaseKey(index);
    }

    void changeInstrument(InstrumentPreset instrumentPreset) {
        instrument.setPreset(new InstrumentPreset(instrumentPreset));
        oscillatorPanels.forEach(panel -> panel.reload());
    }

    InstrumentPreset getCurrentPreset() {
        return instrument.getPreset();
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
