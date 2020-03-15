package com.jbatista.wmo.operator.instrument;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.jbatista.wmo.preset.InstrumentPreset;
import com.jbatista.wmo.util.Dx7Sysex;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class SysexPanel extends Window {

    private final InstrumentActor instrumentActor;
    private final Skin skin;

    private final Table tabMain = new Table();
    private final VisTextButton btnlLoad = new VisTextButton("Load file");
    private final VisSelectBox<String> lstPresetNames = new VisSelectBox<>();
    private final FileChooser fileChooser = new FileChooser(FileChooser.Mode.OPEN);
    private final FileTypeFilter fileTypeFilter = new FileTypeFilter(false);
    private final List<InstrumentPreset> presetList = new ArrayList<>();

    SysexPanel(InstrumentActor instrumentActor, Skin skin) {
        super("No file loaded", skin);
        this.instrumentActor = instrumentActor;
        this.skin = skin;
        add(tabMain);
        tabMain.pad(5);
        tabMain.add(btnlLoad).padRight(5);
        tabMain.add(lstPresetNames).row();

        lstPresetNames.setItems("**********");
        lstPresetNames.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                instrumentActor.changeInstrument(presetList.get(lstPresetNames.getSelectedIndex()));
            }
        });

        fileTypeFilter.addRule("System exclusive dump", "syx");

        fileChooser.setFileTypeFilter(fileTypeFilter);
        fileChooser.setSelectionMode(FileChooser.SelectionMode.FILES);
        fileChooser.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                loadFile(files.first().file());
            }
        });

        btnlLoad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getStage().addActor(fileChooser.fadeIn());
            }
        });
    }

    private void loadFile(File file) {
        try {
            presetList.clear();
            presetList.addAll(Dx7Sysex.readInstruments(file));
            instrumentActor.changeInstrument(presetList.get(0));
            getTitleLabel().setText(file.getName());

            final String[] names = new String[presetList.size()];
            for (int i = 0; i < presetList.size(); i++) {
                names[i] = presetList.get(i).getName();
            }

            lstPresetNames.setItems(names);
        } catch (Exception e) {
            final Dialog dialog = new Dialog("An error occurred", skin, "dialog");
            dialog.setMovable(false);
            dialog.setModal(true);
            dialog.text(e.getMessage());
            dialog.button("Ok", true);
            dialog.show(getStage());
        }
    }

}
