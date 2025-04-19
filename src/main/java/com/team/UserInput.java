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
        String baseString = "";//Power base String object.
        String exponentString = "";//Power base String object.
        int exponentSymbolIndex = 0;//Index where '^' is stored in text.
        boolean sign = false;//Values true if number is negative.

        //Parameters are assigned to attributes:
        this.text = text;
        this.precisionMode = precisionMode;

        try {
            if (this.text.contains("^")) {//If text contains '^':
                exponentSymbolIndex = text.indexOf('^');//Index where '^' places is stored.
                //Given a^b, a is the previous number to '^' and b is the next number to '^':
                baseString = text.substring(0, exponentSymbolIndex);
                exponentString = text.substring(exponentSymbolIndex + 1);
                if (baseString.contains("-")) {//If baseString contains "-":
                    baseString = baseString.replaceAll("-", "");//baseString updates with minus character removed.
                    sign = true;//Sign values 1.
                }
                //BigDecimal objects are constructed from String objects:
                base = new BigDecimal(baseString);
                exponent = new BigDecimal(exponentString);
                number = BigDecimalMath.pow(base, exponent, mathContext);//We try to get a BigDecimal calculating a^b.
                if (sign == true) {//If sign values true:
                    number = number.negate();//number updates with the original value, negating the absolute value.
                }
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