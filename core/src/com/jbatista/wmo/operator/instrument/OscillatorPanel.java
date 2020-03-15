package com.jbatista.wmo.operator.instrument;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jbatista.wmo.KeyboardNote;
import com.jbatista.wmo.TransitionCurve;
import com.jbatista.wmo.synthesis.Oscillator;
import com.kotcrab.vis.ui.widget.*;
import com.kotcrab.vis.ui.widget.spinner.IntSpinnerModel;
import com.kotcrab.vis.ui.widget.spinner.Spinner;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

public class OscillatorPanel extends Window {

    private final Oscillator oscillator;
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

    OscillatorPanel(int id, Oscillator oscillator, Skin skin) {
        super("Oscillator " + id, skin);
        this.oscillator = oscillator;
        add(mainTable);

        // frequency
        mainTable.add(tabFreq).expand().colspan(5).row();
        tabFreq.pad(5);

        sldFreqDetune.setValue(0);
        tabFreq.add(chkFreqRatioFixed).left();
        tabFreq.add(lblFrequency).expand().padRight(5).center();
        tabFreq.add(lblCoarse).expand().padRight(5).right();
        tabFreq.add(sldfreqCoarse).expand().padRight(5).expand().row();
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

    public void reload() {
        setFrequencyControls();
        setFrequencyLabel();
        setEgControls();
    }

    private void setFrequencyControls() {
        setFrequencyLabel();
        chkFreqRatioFixed.setChecked(oscillator.isFixedFrequency());
        sldFreqDetune.setValue(oscillator.getFrequencyDetune());
        sldfreqCoarse.setValue((float) oscillator.getFrequencyRatio());
        sldfreqFine.setValue(oscillator.getFrequencyFine());
    }

    private void setFrequencyLabel() {
        if (oscillator.isFixedFrequency()) {
            lblFrequency.setText(FREQ_FORMAT.format(oscillator.getEffectiveFrequency()) + " Hz");
        } else {
            lblFrequency.setText(String.valueOf(FREQ_FORMAT.format(oscillator.getEffectiveFrequency())));
        }
    }

    private void setEgControls() {
        sldEgALevel.setValue(oscillator.getEnvelopeGenerator().getAttackLevel());
        sldEgDLevel.setValue(oscillator.getEnvelopeGenerator().getDecayLevel());
        sldEgSLevel.setValue(oscillator.getEnvelopeGenerator().getSustainLevel());
        sldEgRLevel.setValue(oscillator.getEnvelopeGenerator().getReleaseLevel());
        spnModelEgARate.setValue(oscillator.getEnvelopeGenerator().getAttackSpeed());
        spnModelEgDRate.setValue(oscillator.getEnvelopeGenerator().getDecaySpeed());
        spnModelEgSRate.setValue(oscillator.getEnvelopeGenerator().getSustainSpeed());
        spnModelEgRRate.setValue(oscillator.getEnvelopeGenerator().getReleaseSpeed());
    }

    private void setBreakpointControls() {
        lstBrkNotes.setSelected(oscillator.getBreakpoint().getNote());
        spnModelBrkLeftDepth.setValue(oscillator.getBreakpoint().getLeftDepth());
        spnModelBrkRightDepth.setValue(oscillator.getBreakpoint().getRightDepth());
        lstBrkLeftCurve.setSelected(BreakpointCurve.values()[TRANSITION_CURVES.indexOf(oscillator.getBreakpoint().getLeftCurve())]);
        lstBrkRightCurve.setSelected(BreakpointCurve.values()[TRANSITION_CURVES.indexOf(oscillator.getBreakpoint().getRightCurve())]);
    }

    private void setFrequencyBindings() {
        chkFreqRatioFixed.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.setFixedFrequency(chkFreqRatioFixed.isChecked());
                setFrequencyLabel();
            }
        });

        sldFreqDetune.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.setFrequencyDetune((int) sldFreqDetune.getValue());
                setFrequencyLabel();
            }
        });

        sldfreqFine.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.setFrequencyFine((int) sldfreqFine.getValue());
                setFrequencyLabel();
            }
        });

        sldfreqCoarse.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.setFrequencyRatio(sldfreqCoarse.getValue());
                setFrequencyLabel();
            }
        });
    }

    private void setEgBindings() {
        sldEgALevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getEnvelopeGenerator().setAttackLevel((int) sldEgALevel.getValue());
            }
        });

        sldEgDLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getEnvelopeGenerator().setDecayLevel((int) sldEgDLevel.getValue());
            }
        });

        sldEgSLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getEnvelopeGenerator().setSustainLevel((int) sldEgSLevel.getValue());
            }
        });

        sldEgRLevel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getEnvelopeGenerator().setReleaseLevel((int) sldEgRLevel.getValue());
            }
        });

        spnEgARate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getEnvelopeGenerator().setAttackSpeed(spnModelEgARate.getValue());
            }
        });

        spnEgDRate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getEnvelopeGenerator().setDecaySpeed(spnModelEgDRate.getValue());
            }
        });

        spnEgSRate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getEnvelopeGenerator().setSustainSpeed(spnModelEgSRate.getValue());
            }
        });

        spnEgRRate.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getEnvelopeGenerator().setReleaseSpeed(spnModelEgRRate.getValue());
            }
        });

    }

    private void setBreakpointBindings() {
        lstBrkNotes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getBreakpoint().setNote(lstBrkNotes.getSelected());
            }
        });

        spnBrkLeftDepth.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getBreakpoint().setLeftDepth(spnModelBrkLeftDepth.getValue());
            }
        });

        spnBrkRightDepth.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getBreakpoint().setRightDepth(spnModelBrkRightDepth.getValue());
            }
        });

        lstBrkLeftCurve.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getBreakpoint().setLeftCurve(lstBrkLeftCurve.getSelected().getCurve());
            }
        });

        lstBrkRightCurve.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                oscillator.getBreakpoint().setRightCurve(lstBrkRightCurve.getSelected().getCurve());
            }
        });
    }

}
