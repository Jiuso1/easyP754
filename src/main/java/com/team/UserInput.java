package com.team;

import java.math.BigDecimal;

public class UserInput {
    private final String text;
    private final PrecisionMode precisionMode;
    private BigDecimal number;
    private final boolean isSpecial;

    public UserInput(String text, PrecisionMode precisionMode) {
        this.text = text;
        this.precisionMode = precisionMode;

        try {
            this.number = new BigDecimal(text);//We try to get a BigDecimal from the text.
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
