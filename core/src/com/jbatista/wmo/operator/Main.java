package com.jbatista.wmo.operator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.jbatista.wmo.operator.instrument.InstrumentActor;
import com.kotcrab.vis.ui.VisUI;

public class Main extends ApplicationAdapter {
    private Skin skin;

    private Camera camera;
    private Stage stage;

    private InstrumentActor instrumentActor;

    @Override
    public void create() {
        VisUI.load();
        skin = new Skin(Gdx.files.internal("visui/uiskin.json"));

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new FitViewport(1280, 720, camera));

        instrumentActor = new InstrumentActor(skin);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(instrumentActor.getKeyboardInputProcessor());
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
