package com.team;

import java.util.BitSet;

public class UserOutput {
    private final String number;
    private final boolean isSpecial;
    private final NumberType numberType;
    private final boolean sign;
    private final BitSet exponent;
    private final BitSet mantissa;

    public UserOutput(String number, boolean isSpecial, NumberType numberType, boolean sign, BitSet exponent, BitSet mantissa) {
        this.number = number;
        this.isSpecial = isSpecial;
        this.numberType = numberType;
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

    public boolean isSpecial() {
        return isSpecial;
    }

    public NumberType getNumberType() {
        return numberType;
    }

    public String getNumber() {
        return number;
    }
}
