package com.jbatista.wmo.operator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jbatista.wmo.DspUtil;
import com.jbatista.wmo.synthesis.Instrument;
import com.jbatista.wmo.synthesis.Oscillator;

public class InstrumentActor extends Actor {

    private final Instrument instrument;
    private final AudioDevice audioDevice = Gdx.audio.newAudioDevice(44100, false);

    private final short[] buffer = new short[44100];
    private short[] frame = new short[2];

    public InstrumentActor(Instrument instrument) {
        this.instrument = instrument;

        final Oscillator oscillator1 = new Oscillator();
        instrument.getAlgorithm().getOscillators().add(oscillator1);
    }

    @Override
    public void act(float delta) {
        for (int i = 0; i < 2048; i += 2) {
            frame = instrument.getShortFrame();

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
