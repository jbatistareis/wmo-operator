package com.jbatista.wmo.operator.instrument;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;

import java.util.LinkedList;
import java.util.List;

class KeyboardPanel extends VisWindow {
    private final InstrumentActor instrumentActor;
    private final List<Image> keys = new LinkedList<>();

    private final Drawable blackIdle = new TextureRegionDrawable(new TextureRegion(new Texture("blackIdle.jpg")));
    private final Drawable whiteIdle = new TextureRegionDrawable(new TextureRegion(new Texture("whiteIdle.jpg")));
    private final Drawable blackPress = new TextureRegionDrawable(new TextureRegion(new Texture("blackPress.jpg")));
    private final Drawable whitePress = new TextureRegionDrawable(new TextureRegion(new Texture("whitePress.jpg")));

    private final List<VisTextButton> labelButtons = new LinkedList<>();

    private final String[] keyLabels = new String[]{
            "Q", "2", "W", "3", "E", "R", "5", "T", "6", "Y", "7", "U",
            "Z", "S", "X", "D", "C", "V", "G", "B", "H", "N", "J", "M", ","};

    private boolean clicked = false;

    KeyboardPanel(InstrumentActor instrumentActor) {
        super("");
        this.instrumentActor = instrumentActor;

        for (int i = 0; i < instrumentActor.getNotes().length; i++) {
            final VisTextButton button = new VisTextButton("");
            button.setDisabled(true);

            labelButtons.add(button);
            add(button).width(19).pad(0.5f);
        }

        row();

        for (int i = 0; i < instrumentActor.getNotes().length; i++) {
            final Image key;

            if (instrumentActor.getNotes()[i].toString().contains("#")) {
                key = new Image(blackIdle);
            } else {
                key = new Image(whiteIdle);
            }

            final int index = i;
            key.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    instrumentActor.pressKey(index);
                    clicked = true;

                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    instrumentActor.releaseKey(index);
                    clicked = false;
                }

                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    if (clicked) {
                        instrumentActor.pressKey(index);
                    }
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    instrumentActor.releaseKey(index);
                }
            });

            keys.add(key);
            add(key).pad(0.5f);
        }
    }

    void changeOctaveLabel(int index) {
        String title;
        if (index == 0) {
            title = "Octave 2";
        } else {
            title = "Octaves " + (index + 1) + "/" + (index + 2);
        }

        getTitleLabel().setText(title);

        labelButtons.forEach(button -> {
            button.setColor(Color.WHITE);
            button.setText("-");
        });

        VisTextButton button;
        for (int i = 12; i < 25; i++) {
            button = labelButtons.get(index * 12 + i - 12);
            button.setText(keyLabels[i]);
            button.setColor(Color.SLATE);

            if ((index > 0) && (i < 24)) {
                button = labelButtons.get(index * 12 + i - 24);
                button.setText(keyLabels[i - 12]);
                button.setColor(Color.TAN);
            }
        }
    }

    void pressKey(int index) {
        if (instrumentActor.getNotes()[index].toString().contains("#")) {
            keys.get(index).setDrawable(blackPress);
        } else {
            keys.get(index).setDrawable(whitePress);
        }
    }

    void releaseKey(int index) {
        if (instrumentActor.getNotes()[index].toString().contains("#")) {
            keys.get(index).setDrawable(blackIdle);
        } else {
            keys.get(index).setDrawable(whiteIdle);
        }
    }

}
