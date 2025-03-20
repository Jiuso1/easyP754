package com.team;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;

public class UserInput {
    private final String text;
    private final PrecisionMode precisionMode;
    private BigDecimal number;
    private final boolean isSpecial;

    public UserInput(String text, PrecisionMode precisionMode) {
        MathContext mathContext = new MathContext(1000);//1000 digit precision.
        BigDecimal base = null;//Power base BigDecimal object.
        BigDecimal exponent = null;//Exponent base BigDecimal object.
        int exponentSymbolIndex = 0;//Index where '^' is stored in text.

        //Parameters are assigned to attributes:
        this.text = text;
        this.precisionMode = precisionMode;

        try {
            if (this.text.contains("^")) {//If text contains '^':
                exponentSymbolIndex = text.indexOf('^');//Index where '^' places is stored.
                //Given a^b, a is the previous number to '^' and b is the next number to '^':
                base = new BigDecimal(text.substring(0, exponentSymbolIndex));
                exponent = new BigDecimal(text.substring(exponentSymbolIndex + 1));
                number = BigDecimalMath.pow(base, exponent, mathContext);//We try to get a BigDecimal calculating a^b.
            } else {//If text doesn't contain '^':
                this.number = new BigDecimal(text);//We try to get a BigDecimal from the text using BigDecimal(String) constructor.
            }
            if (this.number.compareTo(BigDecimal.ZERO) == 0) {//If the number values 0:
                throw new ZeroException();//ZeroException is thrown.
            }
        } catch (NumberFormatException e) {//If the number can't be converted to BigDecimal:
            this.number = null;//number values null.
        } catch (ZeroException e) {//If the number is 0:
            this.number = null;//number values null.
        }

        this.isSpecial = (this.number == null);//isSpecial values true if number values null. Otherwise isSpecial values false.
    }

    public String getText() {
        return text;
    }

    public PrecisionMode getPrecisionMode() {
        return precisionMode;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public boolean isSpecial() {
        return isSpecial;
    }
}