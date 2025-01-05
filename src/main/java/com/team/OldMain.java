package com.team;

import java.util.BitSet;

import static com.team.PrecisionMode.SIMPLE;

public class OldMain {
    public static void main(String[] args) {
        UserInput userInput = new UserInput("+3.14", SIMPLE);
        Calculator calculator = new Calculator(userInput);
        UserOutput userOutput = calculator.calculateUserOutput();
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

        if (userOutput == null) {
            System.out.println("userOutput is null");
        } else {
            sign = userOutput.getSign();
            exponent = userOutput.getExponent();
            mantissa = userOutput.getMantissa();
            System.out.println("Result:");
            System.out.print("S: ");
            System.out.println(sign ? "1" : "0");
            //Don't use BitSet.length(), use your own variables. Source: https://stackoverflow.com/questions/40786466/how-get-real-length-of-bitset
            System.out.print("E: ");
            for (int i = 0; i < numberOfExponentBits; i++) {
                System.out.print(exponent.get(i) ? "1" : "0");
            }
            System.out.println();
            System.out.print("M: ");
            for (int i = 0; i < numberOfMantissaBits; i++) {
                System.out.print(mantissa.get(i) ? "1" : "0");
            }
            System.out.println();
        }
    }
}