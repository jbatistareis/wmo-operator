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
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.kotcrab.vis.ui.widget.file.FileChooser;
import com.kotcrab.vis.ui.widget.file.FileChooserAdapter;
import com.kotcrab.vis.ui.widget.file.FileTypeFilter;

import java.io.File;

class SysexPanel extends VisWindow {

    private final InstrumentActor instrumentActor;
    private String tempInstrumentName;

    private final Table tabMain = new Table();
    private final VisTextButton btnLoadSysex = new VisTextButton("Load sysex");
    private final VisTextButton btnLoadWmo = new VisTextButton("Load wmo");
    private final VisTextButton btnSave = new VisTextButton("Save");
    private final HorizontalGroup hboxUpper = new HorizontalGroup();
    private final HorizontalGroup hboxLower = new HorizontalGroup();
    private final VisSelectBox<InstrumentPreset> lstPresetNames = new VisSelectBox<>();
    private final FileChooser openSysexDialog = new FileChooser(FileChooser.Mode.OPEN);
    private final FileChooser openWmoDialog = new FileChooser(FileChooser.Mode.OPEN);
    private final FileChooser saveSysexDialog = new FileChooser(FileChooser.Mode.SAVE);

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

        lstPresetNames.setItems(new InstrumentPreset());
        lstPresetNames.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                instrumentActor.changeInstrument(lstPresetNames.getSelected());
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
                saveWmo(instrumentActor.getCurrentPreset(), files.first().file());
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
                getStage().addActor(saveSysexDialog.fadeIn());
            }
        });
    }

    private void loadSysex(File file) {
        try {
            lstPresetNames.setItems(Dx7Sysex.extractPresets(file).toArray(new InstrumentPreset[]{}));
        } catch (Exception e) {
            Dialogs.showErrorDialog(getStage(), e.getMessage(), e);
        }
    }

    private void loadWmo(File file) {
        try {
            lstPresetNames.setItems(WmoFile.loadWmoInstruments(file).toArray(new InstrumentPreset[]{}));
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

}
