package com.team;

import java.util.BitSet;

public class UserOutput {
    private final boolean sign;
    private final BitSet exponent;
    private final BitSet mantissa;

    public UserOutput(boolean sign, BitSet exponent, BitSet mantissa) {
        this.sign = sign;
        this.exponent = exponent;
        this.mantissa = mantissa;
    }

    public boolean getSign() {
        return sign;
    }

    public BitSet getExponent() {
        return exponent;
    }

    public BitSet getMantissa() {
        return mantissa;
    }
}
