package com.team;

import java.util.ArrayList;
import java.util.BitSet;

import static com.team.PrecisionMode.DOUBLE;
import static com.team.PrecisionMode.SIMPLE;

public class Calculator {
    private UserInput userInput;

    public Calculator() {
        userInput = null;
    }

    public Calculator(UserInput userInput) {
        this.userInput = userInput;
    }

    public UserOutput calculateUserOutput() {
        UserOutput userOutput = null;
        if (userInput.isSpecial()) {
            userOutput = calculateSpecialCase();
        } else {
            userOutput = calculateUsualCase();
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

        if (userInput.getPrecisionMode() == SIMPLE) {
            numberOfExponentBits = 8;
            numberOfMantissaBits = 23;
        } else if (userInput.getPrecisionMode() == DOUBLE) {
            numberOfExponentBits = 11;
            numberOfMantissaBits = 52;
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

    public UserOutput calculateUsualCase() {
        UserOutput userOutput = null;

        System.out.println("Calculating usual case...");

        return userOutput;
    }

    public UserOutput calculateNormalizedNumber() {
        UserOutput userOutput = null;
        return userOutput;
    }

    public UserOutput calculateDenormalizedNumber() {
        UserOutput userOutput = null;
        return userOutput;
    }

    public UserInput getUserInput() {
        return userInput;
    }

    public void setUserInput(UserInput userInput) {
        this.userInput = userInput;
    }
}
