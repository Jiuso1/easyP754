package com.team;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Calculation {
    private final int excess;
    private final int excessMinusOne;
    private final BigDecimal decimalY;
    private final int y;
    private final int integerExponent;
    private final BigDecimal twoRaisedToY;
    private final BigDecimal x;
    private final ArrayList<BigDecimal> operand;
    private final ArrayList<BigDecimal> result;

    public Calculation(int excess, int excessMinusOne, BigDecimal decimalY, int y, int integerExponent, BigDecimal twoRaisedToY, BigDecimal x, ArrayList<BigDecimal> operand, ArrayList<BigDecimal> result) {
        this.excess = excess;
        this.excessMinusOne = excessMinusOne;
        this.decimalY = decimalY;
        this.y = y;
        this.integerExponent = integerExponent;
        this.twoRaisedToY = twoRaisedToY;
        this.x = x;
        this.operand = operand;
        this.result = result;
    }

    public int getExcess() {
        return excess;
    }

    public int getExcessMinusOne() {
        return excessMinusOne;
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

    public BigDecimal getTwoRaisedToY() {
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
}
