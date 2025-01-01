package com.team;

import java.util.BitSet;

import static com.team.PrecisionMode.DOUBLE;
import static com.team.PrecisionMode.SIMPLE;

public class Main {
    public static void main(String[] args) {
        UserInput userInput = new UserInput("-ꝏ", SIMPLE);
        Calculator calculator = new Calculator(userInput);
        UserOutput userOutput = calculator.calculateUserOutput();
        boolean sign = false;
        BitSet exponent = null;
        BitSet mantissa = null;

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
            for (int i = 0; i < 8; i++) {
                System.out.print(exponent.get(i) ? "1" : "0");
            }
            System.out.println();
            System.out.print("M: ");
            for (int i = 0; i < 23; i++) {
                System.out.print(mantissa.get(i) ? "1" : "0");
            }
            System.out.println();
        }
    }
}