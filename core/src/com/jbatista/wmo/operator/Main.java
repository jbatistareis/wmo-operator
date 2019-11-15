package com.jbatista.wmo.operator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jbatista.wmo.AudioFormat;
import com.jbatista.wmo.KeyboardNote;
import com.jbatista.wmo.synthesis.Instrument;
import com.kotcrab.vis.ui.VisUI;

public class Main extends ApplicationAdapter {
    private Camera camera;
    private Stage stage;

    private Instrument instrument = new Instrument(AudioFormat._44100Hz_16bit);

    private InstrumentActor instrumentActor;
    private KeyboardProcessor keyboardProcessor;

    @Override
    public void create() {
        VisUI.load();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new FitViewport(450, 800, camera));

        instrumentActor = new InstrumentActor(instrument);
        keyboardProcessor = new KeyboardProcessor(instrument, 12, KeyboardNote.C_4);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(keyboardProcessor);
        Gdx.input.setInputProcessor(multiplexer);

        stage.addActor(instrumentActor);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        VisUI.dispose();
        stage.dispose();
    }

}
