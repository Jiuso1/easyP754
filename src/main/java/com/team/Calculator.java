package com.team;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.BitSet;

import ch.obermuhlner.math.big.BigDecimalMath;

import static com.team.PrecisionMode.DOUBLE;
import static com.team.PrecisionMode.SIMPLE;

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

        if (userInput.isSpecial()) {
            userOutput = calculateSpecialCase();
        } else {
            switch (userInput.getPrecisionMode()) {
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
        boolean sign = false;
        BitSet exponent = null;
        BitSet mantissa = null;
        int numberOfExponentBits = 0;
        int numberOfMantissaBits = 0;

        switch (userInput.getPrecisionMode()) {
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

        System.out.println("Calculating special case...");

        switch (userInput.getText()) {
            case "+0": {
                exponent = new BitSet(numberOfExponentBits);//BitSet constructor specifies the number of bits.
                mantissa = new BitSet(numberOfMantissaBits);
                for (int i = 0; i < numberOfExponentBits; i++) {//All exponent bits are set to 0:
                    exponent.set(i, false);
                }
                for (int i = 0; i < numberOfMantissaBits; i++) {//All mantissa bits are set to 0:
                    mantissa.set(i, false);
                }
                userOutput = new UserOutput(sign, exponent, mantissa);
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
                userOutput = new UserOutput(sign, exponent, mantissa);
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
                userOutput = new UserOutput(sign, exponent, mantissa);
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
                userOutput = new UserOutput(sign, exponent, mantissa);
                break;
            }
            default: {
                System.out.println("Invalid input");
            }
        }

        return userOutput;
    }

    public UserOutput calculateNormalizedCase() {
        UserOutput userOutput = null;
        BigDecimal number = userInput.getNumber();
        System.out.println("Calculating normalized case...");
        BigDecimal decimalY = BigDecimalMath.log2(number, mathContext);
        System.out.println("decimalY: " + decimalY);
        return userOutput;
    }

    public UserOutput calculateDenormalizedCase() {
        UserOutput userOutput = null;
        System.out.println("Calculating denormalized case...");
        return userOutput;
    }

    public UserInput getUserInput() {
        return userInput;
    }

    public void setUserInput(UserInput userInput) {
        this.userInput = userInput;
    }
}
