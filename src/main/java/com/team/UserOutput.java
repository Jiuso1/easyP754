package com.team;

import java.util.BitSet;

public class UserOutput {
    private final boolean sign;
    private final BitSet exponent;
    private final BitSet mantissa;
    private final boolean isSpecial;
    private final NumberType numberType;

    public UserOutput(boolean sign, BitSet exponent, BitSet mantissa, boolean isSpecial, NumberType numberType) {
        this.sign = sign;
        this.exponent = exponent;
        this.mantissa = mantissa;
        this.isSpecial = isSpecial;
        this.numberType = numberType;
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

    public boolean isSpecial() {
        return isSpecial;
    }

    public NumberType getNumberType() {
        return numberType;
    }
}
