package com.jbatista.wmo.operator.instrument;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jbatista.wmo.KeyboardNote;
import com.jbatista.wmo.TransitionCurve;
import com.jbatista.wmo.preset.InstrumentPreset;
import com.jbatista.wmo.preset.OscillatorPreset;
import com.jbatista.wmo.synthesis.Instrument;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

class OscillatorPanel extends VisWindow {

    private final int id;
    private final Instrument instrument;
    private final VisTable mainTable = new VisTable();

    // frequency
    private static final DecimalFormat FREQ_FORMAT = new DecimalFormat("#,###,##0.0000");
    private final VisTable tabFreq = new VisTable();
    private final VisLabel lblFrequency = new VisLabel("0 Hz");
    private final VisLabel lblDetune = new VisLabel("Detune");
    private final VisLabel lblCoarse = new VisLabel("Coarse");
    private final VisLabel lblFine = new VisLabel("Fine");
    private final VisSlider sldFreqDetune = new VisSlider(-7, 7, 1, false);
    private final VisSlider sldfreqCoarse = new VisSlider(0, 31, 1, false);
    private final VisSlider sldfreqFine = new VisSlider(0, 99, 1, false);
    private final VisCheckBox chkFreqRatioFixed = new VisCheckBox("Fixed");

    // EG
    private final VisTable tabEgLevels = new VisTable();
    private final VisTable tabEgRates = new VisTable();
    private final VisLabel lblEgLevels = new VisLabel("EG Levels");
    private final VisLabel lblEgRates = new VisLabel("EG Rates");
    private final VisLabel lblEgALevel = new VisLabel("A");
    private final VisLabel lblEgDLevel = new VisLabel("D");
    private final VisLabel lblEgSLevel = new VisLabel("S");
    private final VisLabel lblEgRLevel = new VisLabel("R");
    private final VisSlider sldEgALevel = new VisSlider(0, 99, 1, false);
    private final VisSlider sldEgDLevel = new VisSlider(0, 99, 1, false);
    private final VisSlider sldEgSLevel = new VisSlider(0, 99, 1, false);
    private final VisSlider sldEgRLevel = new VisSlider(0, 99, 1, false);
    private final IntSpinnerModel spnModelEgARate = new IntSpinnerModel(0, 0, 99, 1);
    private final IntSpinnerModel spnModelEgDRate = new IntSpinnerModel(0, 0, 99, 1);
    private final IntSpinnerModel spnModelEgSRate = new IntSpinnerModel(0, 0, 99, 1);
    private final IntSpinnerModel spnModelEgRRate = new IntSpinnerModel(0, 0, 99, 1);
    private final Spinner spnEgARate = new Spinner("A", spnModelEgARate);
    private final Spinner spnEgDRate = new Spinner("D", spnModelEgDRate);
    private final Spinner spnEgSRate = new Spinner("S", spnModelEgSRate);
    private final Spinner spnEgRRate = new Spinner("R", spnModelEgRRate);

    // breakpoint
    private String[] arrBrkNotes = new String[100];
    private static final List<TransitionCurve> TRANSITION_CURVES = Arrays.asList(TransitionCurve.values());
    private final VisTable tabBrk = new VisTable();
    private final VisLabel lblBrk = new VisLabel("Breakpoint");
    private final VisSelectBox<KeyboardNote> lstBrkNotes = new VisSelectBox<>();
    private final IntSpinnerModel spnModelBrkLeftDepth = new IntSpinnerModel(0, 0, 99, 1);
    private final IntSpinnerModel spnModelBrkRightDepth = new IntSpinnerModel(0, 0, 99, 1);
    private final Spinner spnBrkLeftDepth = new Spinner("L", spnModelBrkLeftDepth);
    private final Spinner spnBrkRightDepth = new Spinner("R", spnModelBrkRightDepth);
    private final VisSelectBox<BreakpointCurve> lstBrkLeftCurve = new VisSelectBox<>();
    private final VisSelectBox<BreakpointCurve> lstBrkRightCurve = new VisSelectBox<>();

    OscillatorPanel(int id, Instrument instrument) {
        super("Oscillator " + (id + 1));
        setMovable(false);
        this.id = id;
        this.instrument = instrument;
        add(mainTable);

        // frequency
        mainTable.add(tabFreq).expand().colspan(5).row();
        tabFreq.pad(5);

        sldFreqDetune.setValue(0);
        tabFreq.add(chkFreqRatioFixed).left();
        tabFreq.add(lblFrequency).expand().center();
        tabFreq.add(lblCoarse).expand().padRight(5).right();
        tabFreq.add(sldfreqCoarse).expand().padRight(5).row();
        tabFreq.add(lblDetune).expand().padRight(5).right();
        tabFreq.add(sldFreqDetune).expand().padBottom(5);
        tabFreq.add(lblFine).expand().padRight(5).right();
        tabFreq.add(sldfreqFine).expand();

        mainTable.addSeparator().colspan(5);
        setFrequencyControls();
        setFrequencyBindings();

        // EG
        mainTable.add(tabEgLevels).expand();
        mainTable.addSeparator(true);
        mainTable.add(tabEgRates).expand();
        mainTable.addSeparator(true);

        tabEgLevels.pad(5);
        tabEgLevels.add(lblEgLevels).colspan(2).center().row();
        tabEgLevels.add(lblEgALevel).padRight(5).center();
        tabEgLevels.add(sldEgALevel).padBottom(5).row();
        tabEgLevels.add(lblEgDLevel).padRight(5).center();
        tabEgLevels.add(sldEgDLevel).padBottom(5).row();
        tabEgLevels.add(lblEgSLevel).padRight(5).center();
        tabEgLevels.add(sldEgSLevel).padBottom(5).row();
        tabEgLevels.add(lblEgRLevel).padRight(5).center();
        tabEgLevels.add(sldEgRLevel).row();

        tabEgRates.pad(5);
        tabEgRates.add(lblEgRates).colspan(1).padBottom(5).center().row();
        tabEgRates.add(spnEgARate).padBottom(5).row();
        tabEgRates.add(spnEgDRate).padBottom(5).row();
        tabEgRates.add(spnEgSRate).padBottom(5).row();
        tabEgRates.add(spnEgRRate).row();

        setEgControls();
        setEgBindings();

        // breakpont
        mainTable.add(tabBrk);
        for (int i = 21; i < 121; i++) {
            arrBrkNotes[i - 21] = KeyboardNote.values()[i].toString();
        }

        lstBrkNotes.setItems(KeyboardNote.values());
        lstBrkLeftCurve.setItems(BreakpointCurve.values());
        lstBrkRightCurve.setItems(BreakpointCurve.values());

        tabBrk.pad(5);
        tabBrk.add(lblBrk).expand().colspan(2).center().row();
        tabBrk.add(lstBrkNotes).expand().padBottom(5).colspan(2).center().row();
        tabBrk.add(spnBrkLeftDepth).expand().padBottom(5).padRight(5).center();
        tabBrk.add(spnBrkRightDepth).expand().padBottom(5).center().row();
        tabBrk.add(lstBrkLeftCurve).expand().padRight(5).center();
        tabBrk.add(lstBrkRightCurve).expand().center().row();

        setBreakpointControls();
        setBreakpointBindings();
    }

    void reload() {
        setFrequencyControls();
        setFrequencyLabel();
        setEgControls();
        setBreakpointControls();
    }

    private InstrumentPreset instrumentPreset() {
        return instrument.getPreset();
    }

    private OscillatorPreset oscillatorPreset() {
        return instrument.getPreset().getOscillatorPresets()[id];
    }

    private void setFrequencyControls() {
        setFrequencyLabel();

        chkFreqRatioFixed.setChecked(oscillatorPreset().isFixedFrequency());
        sldFreqDetune.setValue(oscillatorPreset().getFrequencyDetune());
        sldfreqCoarse.setValue((float) oscillatorPreset().getFrequencyRatio());
        sldfreqFine.setValue(oscillatorPreset().getFrequencyFine());
    }

    private void setFrequencyLabel() {
        if (oscillatorPreset().isFixedFrequency()) {
            // lblFrequency.setText(FREQ_FORMAT.format(preset().getEffectiveFrequency()) + " Hz" + getDetuneLabel());
        } else {
            // lblFrequency.setText(FREQ_FORMAT.format(preset().getEffectiveFrequency()) + getDetuneLabel());
        }
    }

    private String getDetuneLabel() {
        return (oscillatorPreset().getFrequencyDetune() == 0)
                ? ""
                : (oscillatorPreset().getFrequencyDetune() < 0)
                ? " " + oscillatorPreset().getFrequencyDetune()
                : " +" + oscillatorPreset().getFrequencyDetune();
    }

    private void setEgControls() {
        sldEgALevel.setValue(oscillatorPreset().getAttackLevel());
        sldEgDLevel.setValue(oscillatorPreset().getDecayLevel());
        sldEgSLevel.setValue(oscillatorPreset().getSustainLevel());
        sldEgRLevel.setValue(oscillatorPreset().getReleaseLevel());

        spnModelEgARate.setValue(oscillatorPreset().getAttackSpeed());
        spnModelEgDRate.setValue(oscillatorPreset().getDecaySpeed());
        spnModelEgSRate.setValue(oscillatorPreset().getSustainSpeed());
        spnModelEgRRate.setValue(oscillatorPreset().getReleaseSpeed());
    }

    private void setBreakpointControls() {
        lstBrkNotes.setSelected(oscillatorPreset().getBreakpointNote());

        spnModelBrkLeftDepth.setValue(oscillatorPreset().getBreakpointLeftDepth());
        spnModelBrkRightDepth.setValue(oscillatorPreset().getBreakpointRightDepth());

        lstBrkLeftCurve.setSelected(BreakpointCurve.values()[TRANSITION_CURVES.indexOf(oscillatorPreset().getBreakpointLeftCurve())]);
        lstBrkRightCurve.setSelected(BreakpointCurve.values()[TRANSITION_CURVES.indexOf(oscillatorPreset().getBreakpointRightCurve())]);
    }

    private void setFrequencyBindings() {
        chkFreqRatioFixed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setFixedFrequency(chkFreqRatioFixed.isChecked());
                setFrequencyLabel();
            }
        });

        sldFreqDetune.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setFrequencyDetune((int) sldFreqDetune.getValue());
                setFrequencyLabel();
            }
        });

        sldfreqFine.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setFrequencyFine((int) sldfreqFine.getValue());
                setFrequencyLabel();
            }
        });

        sldfreqCoarse.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setFrequencyRatio(sldfreqCoarse.getValue());
                setFrequencyLabel();
            }
        });
    }

    private void setEgBindings() {
        // level
        sldEgALevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setAttackLevel((int) sldEgALevel.getValue());
            }
        });

        sldEgDLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setDecayLevel((int) sldEgDLevel.getValue());
            }
        });

        sldEgSLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setSustainLevel((int) sldEgSLevel.getValue());
            }
        });

        sldEgRLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setReleaseLevel((int) sldEgRLevel.getValue());
            }
        });

        // rate
        spnEgARate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setAttackSpeed(spnModelEgARate.getValue());
            }
        });

        spnEgDRate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setDecaySpeed(spnModelEgDRate.getValue());
            }
        });

        spnEgSRate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setSustainSpeed(spnModelEgSRate.getValue());
            }
        });

        spnEgRRate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setReleaseSpeed(spnModelEgRRate.getValue());
            }
        });
    }

    private void setBreakpointBindings() {
        lstBrkNotes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setBreakpointNote(lstBrkNotes.getSelected());
            }
        });

        spnBrkLeftDepth.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setBreakpointLeftDepth(spnModelBrkLeftDepth.getValue());
            }
        });

        spnBrkRightDepth.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setBreakpointRightDepth(spnModelBrkRightDepth.getValue());
            }
        });

        lstBrkLeftCurve.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setBreakpointLeftCurve(lstBrkLeftCurve.getSelected().getCurve());
            }
        });

        lstBrkRightCurve.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillatorPreset().setBreakpointRightCurve(lstBrkRightCurve.getSelected().getCurve());
            }
        });
    }

}
