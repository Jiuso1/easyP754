package com.team;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.BitSet;

import static com.team.NumberType.DENORMALIZED;
import static com.team.NumberType.NORMALIZED;

public class Calculator {
    private UserInput userInput;
    private final MathContext mathContext;

    public Calculator() {
        userInput = null;
        mathContext = new MathContext(100);//100 digit precision.
    }

    public Calculator(UserInput userInput) {
        this.userInput = userInput;
        mathContext = new MathContext(100);//100 digit precision.
    }

    public UserOutput calculateUserOutput() {
        UserOutput userOutput = null;
        int excess = 0;
        BigDecimal smallestNormalizedNumber = null;
        BigDecimal number = null;
        PrecisionMode precisionMode = userInput.getPrecisionMode();
        boolean isSpecial = userInput.isSpecial();

        if (isSpecial) {
            userOutput = calculateSpecialCase();
        } else {
            switch (precisionMode) {
                case SIMPLE: {
                    excess = 127;
                    break;
                }
                case DOUBLE: {
                    excess = 1022;
                    break;
                }
            }

            number = userInput.getNumber();
            smallestNormalizedNumber = BigDecimalMath.pow(BigDecimal.valueOf(2), BigDecimal.valueOf(1 - excess), mathContext);//BigDecimal.valueOf(Math.pow(2, 1 - excess));

            if (number.compareTo(smallestNormalizedNumber) >= 0) {//If the user input number is equal or bigger than the smallest normalized number:
                userOutput = calculateNormalizedCase();//The number is in normalized area.
            } else {//If the user input is smaller than the smallest normalized number:
                userOutput = calculateDenormalizedCase();//The number is in denormalized area.
            }
        }

        return userOutput;
    }

    public UserOutput calculateSpecialCase() {
        UserOutput userOutput = null;
        PrecisionMode precisionMode = userInput.getPrecisionMode();
        String text = userInput.getText();
        boolean sign = false;
        BitSet exponent = null;
        BitSet mantissa = null;
        int numberOfExponentBits = 0;
        int numberOfMantissaBits = 0;
        boolean match = false;//Values true if text has matched a special case.

        switch (precisionMode) {
            case SIMPLE: {
                numberOfExponentBits = 8;
                numberOfMantissaBits = 23;
                break;
            }
            case DOUBLE: {
                numberOfExponentBits = 11;
                numberOfMantissaBits = 52;
                break;
            }
        }

        switch (text) {
            case "+0": {
                exponent = new BitSet(numberOfExponentBits);//BitSet constructor specifies the number of bits.
                mantissa = new BitSet(numberOfMantissaBits);
                for (int i = 0; i < numberOfExponentBits; i++) {//All exponent bits are set to 0:
                    exponent.set(i, false);
                }
                for (int i = 0; i < numberOfMantissaBits; i++) {//All mantissa bits are set to 0:
                    mantissa.set(i, false);
                }
                break;
            }
            case "-0": {
                sign = true;//As the text has - symbol, sign values true or 1.
                exponent = new BitSet(numberOfExponentBits);//BitSet constructor specifies the number of bits.
                mantissa = new BitSet(numberOfMantissaBits);
                for (int i = 0; i < numberOfExponentBits; i++) {//All exponent bits are set to 0:
                    exponent.set(i, false);
                }
                for (int i = 0; i < numberOfMantissaBits; i++) {//All mantissa bits are set to 0:
                    mantissa.set(i, false);
                }
                break;
            }
            case "+ꝏ": {
                exponent = new BitSet(numberOfExponentBits);//BitSet constructor specifies the number of bits.
                mantissa = new BitSet(numberOfMantissaBits);
                for (int i = 0; i < numberOfExponentBits; i++) {//All exponent bits are set to 1:
                    exponent.set(i, true);
                }
                for (int i = 0; i < numberOfMantissaBits; i++) {//All mantissa bits are set to 0:
                    mantissa.set(i, false);
                }
                break;
            }
            case "-ꝏ": {
                sign = true;//As the text has - symbol, sign values true or 1.
                exponent = new BitSet(numberOfExponentBits);//BitSet constructor specifies the number of bits.
                mantissa = new BitSet(numberOfMantissaBits);
                for (int i = 0; i < numberOfExponentBits; i++) {//All exponent bits are set to 1:
                    exponent.set(i, true);
                }
                for (int i = 0; i < numberOfMantissaBits; i++) {//All mantissa bits are set to 0:
                    mantissa.set(i, false);
                }
                break;
            }
        }

        match = (exponent != null) && (mantissa != null);//If exponent and mantissa have been calculated, text has matched a special case.

        if (match) {
            userOutput = new UserOutput(precisionMode, true, null, null, null, sign, exponent, mantissa);
        }

        return userOutput;
    }

    public UserOutput calculateNormalizedCase() {
        UserOutput userOutput = null;
        BigDecimal number = userInput.getNumber();
        boolean sign = false;
        BitSet exponent = null;
        BitSet mantissa = null;
        int excess = 0;
        NumberType numberType = NORMALIZED;//The number representation is in normalized area.
        int numberOfExponentBits = 0;
        int numberOfMantissaBits = 0;
        PrecisionMode precisionMode = userInput.getPrecisionMode();

        switch (precisionMode) {
            case SIMPLE: {
                numberOfExponentBits = 8;
                numberOfMantissaBits = 23;
                excess = 127;
                break;
            }
            case DOUBLE: {
                numberOfExponentBits = 11;
                numberOfMantissaBits = 52;
                excess = 1022;
                break;
            }
        }

        exponent = new BitSet(numberOfExponentBits);//BitSet constructor specifies the number of bits.
        mantissa = new BitSet(numberOfMantissaBits);
        for (int i = 0; i < numberOfExponentBits; i++) {//All exponent bits are set to 0:
            exponent.set(i, false);
        }
        for (int i = 0; i < numberOfMantissaBits; i++) {//All mantissa bits are set to 0:
            mantissa.set(i, false);
        }

        return userOutput;
    }

    public UserOutput calculateDenormalizedCase() {
        UserOutput userOutput = null;
        NumberType numberType = DENORMALIZED;//The number representation is in denormalized area.
        return userOutput;
    }

    public UserInput getUserInput() {
        return userInput;
    }

    public void setUserInput(UserInput userInput) {
        this.userInput = userInput;
    }
}
