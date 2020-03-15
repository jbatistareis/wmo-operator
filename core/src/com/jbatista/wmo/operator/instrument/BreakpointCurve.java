package com.jbatista.wmo.operator.instrument;

import com.jbatista.wmo.TransitionCurve;

enum BreakpointCurve {

    MINUS_LINEAR("-LIN", TransitionCurve.LINEAR_DECREASE),
    MINUS_EXPONENTIAL("-EXP", TransitionCurve.EXP_DECREASE),
    PLUS_EXPONENTIAL("+EXP", TransitionCurve.EXP_INCREASE),
    PLUS_LINEAR("+LIN", TransitionCurve.LINEAR_INCREASE);

    private String name;
    private TransitionCurve curve;

    BreakpointCurve(String name, TransitionCurve curve) {
        this.name = name;
        this.curve = curve;
    }

    @Override
    public String toString() {
        return name;
    }

    public TransitionCurve getCurve() {
        return curve;
    }

}
