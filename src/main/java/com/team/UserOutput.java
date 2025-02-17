package com.team;

import java.math.BigDecimal;
import java.util.BitSet;

public class UserOutput {
    private final PrecisionMode precisionMode;
    private final boolean isSpecial;
    private final NumberType numberType;
    private final BigDecimal number;
    private final Calculation calculation;
    private final boolean sign;
    private final BitSet exponent;
    private final BitSet mantissa;

    public UserOutput(PrecisionMode precisionMode, boolean isSpecial, NumberType numberType, BigDecimal number, Calculation calculation, boolean sign, BitSet exponent, BitSet mantissa) {
        this.precisionMode = precisionMode;
        this.isSpecial = isSpecial;
        this.numberType = numberType;
        this.number = number;
        this.calculation = calculation;
        this.sign = sign;
        this.exponent = exponent;
        this.mantissa = mantissa;
    }

    public PrecisionMode getPrecisionMode() {
        return precisionMode;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public NumberType getNumberType() {
        return numberType;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public Calculation getCalculation() {
        return calculation;
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
