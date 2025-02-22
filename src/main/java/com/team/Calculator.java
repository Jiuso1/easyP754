package com.team;

import ch.obermuhlner.math.big.BigDecimalMath;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.BitSet;

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
                    excess = 1023;
                    break;
                }
            }

            number = userInput.getNumber().abs();//number values the absolute value of the user input. Negative sign is ignored.
            smallestNormalizedNumber = BigDecimalMath.pow(BigDecimal.valueOf(2), BigDecimal.valueOf(1 - excess), mathContext);//The smallest positive normalized number is calculated.

            if (number.compareTo(smallestNormalizedNumber) >= 0) {//If number is equal or bigger than the smallest normalized number:
                userOutput = calculateNormalizedCase();//This number is in normalized area.
            } else {//If number is smaller than the smallest normalized number:
                userOutput = calculateDenormalizedCase();//This number is in denormalized area.
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

        match = (exponent != null);//If exponent is not null, match values true.

        if (match) {
            userOutput = new UserOutput(precisionMode, true, null, null, null, sign, exponent, mantissa);
        }

        return userOutput;
    }

    public UserOutput calculateNormalizedCase() {
        UserOutput userOutput = null;//Object that saves all output.
        BigDecimal number = userInput.getNumber();
        boolean sign = false;
        BitSet exponent = null;
        BitSet mantissa = null;
        int excess = 0;
        int numberOfExponentBits = 0;
        int numberOfMantissaBits = 0;
        PrecisionMode precisionMode = userInput.getPrecisionMode();
        Calculation calculation = null;//Object that saves all calculation data.
        //Calculation data:
        BigDecimal decimalY = null;
        int y = 0;
        int integerExponent = 0;
        BigDecimal twoRaisedToY = null;
        BigDecimal x = null;
        BigDecimal integerPart = null;
        BigDecimal currentOperand = null;
        int currentOperandIterator = 0;//Variable used to iterate all calculation operands.
        ArrayList<BigDecimal> operand = new ArrayList<>();//Saves all calculated operands.
        ArrayList<BigDecimal> result = new ArrayList<>();//Saves all calculated results.
        BigDecimal currentResult = null;
        BitSet flippedExponent = null;//Flipped exponent due to long[] to BitSet conversion. When this flippedExponent is flipped, the result is exponent.

        if (number.compareTo(BigDecimal.ZERO) < 0) {//If number is negative:
            sign = true;//Sign values 1.
            number = number.abs();//Number updates with the absolute value.
        }

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
                excess = 1023;
                break;
            }
        }

        //Memory is reserved to exponent and mantissa. Constructor specifies the number of bits:
        exponent = new BitSet(numberOfExponentBits);
        mantissa = new BitSet(numberOfMantissaBits);

        //Calculation data is calculated and saved in calculation object:
        decimalY = BigDecimalMath.log2(number, mathContext);//log_2(number) is assigned to decimalY.
        y = decimalY.setScale(0, RoundingMode.DOWN).intValue();//decimalY is rounded down and cast to int. Source: https://stackoverflow.com/questions/4134047/java-bigdecimal-round-to-the-nearest-whole-value
        integerExponent = y + excess;
        flippedExponent = BitSet.valueOf(new long[]{integerExponent});//int to BitSet conversion. Source: https://stackoverflow.com/questions/11820402/convert-a-byte-or-int-to-bitset
        //flippedExponent contains the reverse version of exponent BitSet.
        for (int i = 0; i < numberOfExponentBits; i++) {//For all exponent bits:
            exponent.set((numberOfExponentBits - 1) - i, flippedExponent.get(i));//exponent bits are set by turning flippedExponent around.
        }
        twoRaisedToY = BigDecimalMath.pow(BigDecimal.TWO, new BigDecimal(y), mathContext);//Calculates 2^y.
        x = number.divide(twoRaisedToY, mathContext);//Calculates number/twoRaisedToY.
        integerPart = x.setScale(0, RoundingMode.DOWN);//integerPart is extracted from x. Source: https://codingtechroom.com/question/extract-decimal-from-bigdecimal-java
        currentOperand = x.subtract(integerPart);//The first operand is x minus its integer part.

        do {//Do...
            currentResult = currentOperand.multiply(BigDecimal.TWO);//The result of each operating values its operand*2.
            if (currentResult.compareTo(BigDecimal.ONE) >= 0) {//If the current result is equal to or bigger than 1:
                mantissa.set(currentOperandIterator, true);//Mantissa bit values 1.
            } else {
                mantissa.set(currentOperandIterator, false);//Mantissa bit values 0.
            }
            //Both the operand and result of each operation are added to their list:
            operand.add(currentOperand);
            result.add(currentResult);
            integerPart = currentResult.setScale(0, RoundingMode.DOWN);//integerPart is extracted from currentOperand. Source: https://codingtechroom.com/question/extract-decimal-from-bigdecimal-java
            currentOperand = currentResult.subtract(integerPart);//The next operand is the previous result minus its integer part.
            currentOperandIterator++;//Iterator increments.
        } while ((currentResult.compareTo(BigDecimal.ONE) != 0) && (currentOperandIterator < numberOfMantissaBits));//...while result doesn't equal 1 or not all bits have been calculated.

        calculation = new Calculation(excess, excess - 1, decimalY, y, integerExponent, twoRaisedToY, x, operand, result);

        userOutput = new UserOutput(precisionMode, false, NORMALIZED, number, calculation, sign, exponent, mantissa);

        return userOutput;
    }

    public UserOutput calculateDenormalizedCase() {
        BigDecimal number = userInput.getNumber();
        PrecisionMode precisionMode = userInput.getPrecisionMode();
        int numberOfExponentBits = 0;
        int numberOfMantissaBits = 0;
        int excess = 0;
        BigDecimal x = null;
        BigDecimal twoRaisedToMinusExponentMinusOne = null;
        UserOutput userOutput = null;//Object that saves all output.
        Calculation calculation = null;//Object that saves all calculation data.

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
                excess = 1023;
                break;
            }
        }

        twoRaisedToMinusExponentMinusOne = BigDecimalMath.pow(BigDecimal.TWO, -(excess - 1), mathContext);
        x = number.divide(twoRaisedToMinusExponentMinusOne, mathContext);
        System.out.println(x);

        return userOutput;
    }

    public UserInput getUserInput() {
        return userInput;
    }

    public void setUserInput(UserInput userInput) {
        this.userInput = userInput;
    }
}
