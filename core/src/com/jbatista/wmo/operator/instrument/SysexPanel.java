package com.jbatista.wmo.operator.instrument;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.jbatista.wmo.preset.InstrumentPreset;
import com.jbatista.wmo.util.Dx7Sysex;
import com.jbatista.wmo.util.WmoFile;
import com.kotcrab.vis.ui.util.dialog.Dialogs;
import com.kotcrab.vis.ui.util.dialog.InputDialogAdapter;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class SysexPanel extends VisWindow {

    private final InstrumentActor instrumentActor;
    private String tempInstrumentName;

    private final Table tabMain = new Table();
    private final VisTextButton btnLoadSysex = new VisTextButton("Load sysex");
    private final VisTextButton btnLoadWmo = new VisTextButton("Load wmo");
    private final VisTextButton btnSave = new VisTextButton("Save");
    private final HorizontalGroup hboxUpper = new HorizontalGroup();
    private final HorizontalGroup hboxLower = new HorizontalGroup();
    private final VisSelectBox<String> lstPresetNames = new VisSelectBox<>();
    private final FileChooser openSysexDialog = new FileChooser(FileChooser.Mode.OPEN);
    private final FileChooser openWmoDialog = new FileChooser(FileChooser.Mode.OPEN);
    private final FileChooser saveSysexDialog = new FileChooser(FileChooser.Mode.SAVE);
    private final List<InstrumentPreset> presetList = new ArrayList<>();

    private final FileTypeFilter fileTypeFilterSysex = new FileTypeFilter(false);
    private final FileTypeFilter fileTypeFilterWmo = new FileTypeFilter(false);

    SysexPanel(InstrumentActor instrumentActor) {
        super("Presets");
        setMovable(false);
        this.instrumentActor = instrumentActor;
        add(tabMain);

        tabMain.pad(5);

        hboxUpper.space(5);
        hboxUpper.addActor(lstPresetNames);
        hboxUpper.addActor(btnSave);

        hboxLower.space(5);
        hboxLower.addActor(btnLoadSysex);
        hboxLower.addActor(btnLoadWmo);

        tabMain.add(hboxUpper).center().padBottom(5).row();
        tabMain.add(hboxLower).center();

        presetList.add(new InstrumentPreset());
        instrumentActor.changeInstrument(presetList.get(0));
        setPresetsNames();

        lstPresetNames.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                instrumentActor.changeInstrument(presetList.get(lstPresetNames.getSelectedIndex()));
            }
        });

        fileTypeFilterSysex.addRule("System exclusive dump", "syx");
        openSysexDialog.setFileTypeFilter(fileTypeFilterSysex);
        openSysexDialog.setSelectionMode(FileChooser.SelectionMode.FILES);
        openSysexDialog.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                loadSysex(files.first().file());
            }
        });

        fileTypeFilterWmo.addRule("Exported WMO instruments", "wmo");
        openWmoDialog.setFileTypeFilter(fileTypeFilterWmo);
        openWmoDialog.setSelectionMode(FileChooser.SelectionMode.FILES);
        openWmoDialog.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                loadWmo(files.first().file());
            }
        });

        saveSysexDialog.setListener(new FileChooserAdapter() {
            @Override
            public void selected(Array<FileHandle> files) {
                saveWmo(presetList.get(lstPresetNames.getSelectedIndex()), files.first().file());
            }
        });

        btnLoadSysex.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getStage().addActor(openSysexDialog.fadeIn());
            }
        });

        btnLoadWmo.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getStage().addActor(openWmoDialog.fadeIn());
            }
        });

        btnSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                savePrompt();
            }
        });
    }

    private void setPresetsNames() {
        final String[] names = new String[presetList.size()];
        for (int i = 0; i < presetList.size(); i++) {
            names[i] = presetList.get(i).getName();
        }

        lstPresetNames.setItems(names);
    }

    private void loadSysex(File file) {
        try {
            presetList.clear();
            presetList.addAll(Dx7Sysex.readInstruments(file));
            instrumentActor.changeInstrument(presetList.get(0));
            setPresetsNames();
        } catch (Exception e) {
            Dialogs.showErrorDialog(getStage(), e.getMessage(), e);
        }
    }

    private void loadWmo(File file) {
        try {
            presetList.clear();
            presetList.addAll(WmoFile.loadWmoInstruments(file));
            instrumentActor.changeInstrument(presetList.get(0));
            setPresetsNames();
        } catch (Exception e) {
            Dialogs.showErrorDialog(getStage(), e.getMessage(), e);
        }
    }

    private void saveWmo(InstrumentPreset instrumentPreset, File file) {
        try {
            WmoFile.saveSingleWmoInstrument(instrumentPreset, file);
        } catch (Exception e) {
            Dialogs.showErrorDialog(getStage(), e.getMessage(), e);
        }
    }

    private void savePrompt() {
        Dialogs.showInputDialog(getStage(), "Enter the instrument name", "Name: ", new InputDialogAdapter() {
            @Override
            public void finished(String input) {
                tempInstrumentName = ((input.length() <= 10) ? input : input.substring(0, 10));
                saveSysexDialog.setDefaultFileName(tempInstrumentName + ".wmo");
                getStage().addActor(saveSysexDialog.fadeIn());
            }
        });
    }

}
