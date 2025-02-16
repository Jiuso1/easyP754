package com.team;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.BitSet;

public class UserOutput {
    private final boolean isSpecial;
    private final NumberType numberType;
    private final int excess;
    private final int excessMinusOne;
    private final BigDecimal number;
    private final BigDecimal decimalY;
    private final int y;
    private final int integerExponent;
    private final int twoRaisedToY;
    private final BigDecimal x;
    private final ArrayList<BigDecimal> operand;
    private final ArrayList<BigDecimal> result;
    private final boolean sign;
    private final BitSet exponent;
    private final BitSet mantissa;

    public UserOutput(boolean isSpecial, NumberType numberType, int excess, int excessMinusOne, BigDecimal number, BigDecimal decimalY, int y, int integerExponent, int twoRaisedToY, BigDecimal x, ArrayList<BigDecimal> operand, ArrayList<BigDecimal> result, boolean sign, BitSet exponent, BitSet mantissa) {
        this.isSpecial = isSpecial;
        this.numberType = numberType;
        this.excess = excess;
        this.excessMinusOne = excessMinusOne;
        this.number = number;
        this.decimalY = decimalY;
        this.y = y;
        this.integerExponent = integerExponent;
        this.twoRaisedToY = twoRaisedToY;
        this.x = x;
        this.operand = operand;
        this.result = result;
        this.sign = sign;
        this.exponent = exponent;
        this.mantissa = mantissa;
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public NumberType getNumberType() {
        return numberType;
    }

    public int getExcess() {
        return excess;
    }

    public int getExcessMinusOne() {
        return excessMinusOne;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public BigDecimal getDecimalY() {
        return decimalY;
    }

    public int getY() {
        return y;
    }

    public int getIntegerExponent() {
        return integerExponent;
    }

    public int getTwoRaisedToY() {
        return twoRaisedToY;
    }

    public BigDecimal getX() {
        return x;
    }

    public ArrayList<BigDecimal> getOperand() {
        return operand;
    }

    public ArrayList<BigDecimal> getResult() {
        return result;
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
