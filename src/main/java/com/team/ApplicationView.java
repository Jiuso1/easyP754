package com.team;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ApplicationView extends Application {
    public void start(Stage primaryStage) {
        VBox root = new VBox();//Create a vertical box layout.
        HBox hBox = new HBox();//Create a horizontal box layout.
        TextField inputTextField = new TextField();//Create an input TextField.
        TextArea outputTextArea = new TextArea();//Create an output TextArea.
        ObservableList<PrecisionMode> items = FXCollections.observableArrayList();//Create a collection of PrecisionMode items. Source: https://acodigo.blogspot.com/2015/04/javafx-uso-de-combobox.html
        items.addAll(PrecisionMode.SIMPLE, PrecisionMode.DOUBLE, PrecisionMode.HALF, PrecisionMode.QUADRUPLE, PrecisionMode.OCTUPLE);//Modes are added as items.
        ComboBox<PrecisionMode> precisionModeComboBox = new ComboBox<>(items);//Create a precision mode ComboBox with given items.
        precisionModeComboBox.setValue(items.getFirst());//Default mode is set to Simple precision (first item).
        Button button = new Button("Calculate");//Create a button with specified text.
        button.setOnAction(a -> generateOutputTextArea(inputTextField, precisionModeComboBox, outputTextArea));//When button is clicked generateOutputTextArea is called.
        inputTextField.setOnKeyPressed(e -> {//When a key is pressed:
            if (e.getCode() == KeyCode.ENTER) {//If the key is ENTER:
                generateOutputTextArea(inputTextField, precisionModeComboBox, outputTextArea);//generateOutputTextArea is called.
            }
        });
        Font font = new Font("Source Sans Pro", 20);//Create a font for the output style.
        outputTextArea.setEditable(false);//output isn't editable.
        outputTextArea.setFont(font);//output has Arial 20 font.
        outputTextArea.setPrefHeight(600);//output has a height of 600 pixels.
        hBox.setAlignment(Pos.TOP_CENTER);//The horizontal box is aligned with the top center.
        hBox.getChildren().addAll(inputTextField, precisionModeComboBox, button);//Add input and button as children of hBox.
        root.getChildren().addAll(hBox, outputTextArea);//Add hBox and output as children of root.
        Scene scene = new Scene(root, 1000, 600);//Create a scene specifying root and window size.
        primaryStage.setScene(scene);//Add scene to the stage.
        primaryStage.setTitle("easyP754");//Set "easyP754" as window's title.
        primaryStage.show();//Make the stage visible.
    }

    public void generateOutputTextArea(TextField inputTextField, ComboBox<PrecisionMode> precisionModeComboBox, TextArea outputTextArea) {
        UserInput userInput = new UserInput(inputTextField.getText(), precisionModeComboBox.getValue());
        Calculator calculator = new Calculator(userInput);
        UserOutput userOutput = calculator.calculateUserOutput();
        String outputString = "";
        int numberOfExponentBits = 0;
        int numberOfMantissaBits = 0;
        int operandSize = 0;//Number of operands in operand ArrayList (which is equal to the number of results in result ArrayList).

        if (userOutput == null) {
            outputString = "userOutput is null.\n";
        } else {
            outputString = "userOutput is not null.\n";
            switch (userOutput.getPrecisionMode()) {
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
                case HALF: {
                    numberOfExponentBits = 5;
                    numberOfMantissaBits = 10;
                    break;
                }
                case QUADRUPLE: {
                    numberOfExponentBits = 15;
                    numberOfMantissaBits = 112;
                    break;
                }
                case OCTUPLE: {
                    numberOfExponentBits = 19;
                    numberOfMantissaBits = 236;
                    break;
                }
            }

            if (userOutput.isSpecial()) {
                outputString += "userOutput is special.\n";
            } else {
                outputString += "userOutput is not special.\n";
                outputString += "V(X) = (-1)^S • 1.M • 2^(E - EXCESS)\n";
                outputString += "V(X) = (-1)^0 • 1.0 • 2^(1 - " + userOutput.getCalculation().getExcess() + ")\n";
                outputString += "V(X) = 2^(-" + userOutput.getCalculation().getExcessMinusOne() + ")\n";
                outputString += "Using |" + userOutput.getNumber() + "|, changing sign bit if needed.\n";

                switch (userOutput.getNumberType()) {//Depending on numberType we print all calculations:
                    case NORMALIZED: {
                        outputString += userOutput.getNumber().abs() + " ≥ 2^(-" + userOutput.getCalculation().getExcessMinusOne() + ")\n";
                        outputString += "userOutput is normalized.\n";
                        outputString += "V(X) = " + userOutput.getNumber().abs() + " = 1.M • 2^(E - " + userOutput.getCalculation().getExcess() + ")\n";
                        outputString += "X = 1.M ; 1 ≤ X < 2\n";
                        outputString += "Y = E - " + userOutput.getCalculation().getExcess() + " ; Y ∈ ℤ\n";
                        outputString += "X = 1.0\n";
                        outputString += userOutput.getNumber().abs() + " = 1.0 • 2^Y'\n";
                        outputString += userOutput.getNumber().abs() + " = 2^Y'\n";
                        outputString += "log₂ " + userOutput.getNumber().abs() + " = Y'\n";
                        outputString += "Y' = " + userOutput.getCalculation().getDecimalY().toString().substring(0, 10) + "\n";
                        outputString += "Y = " + userOutput.getCalculation().getY() + "\n";
                        outputString += "Y = E - " + userOutput.getCalculation().getExcess() + "\n";
                        outputString += userOutput.getCalculation().getY() + " = E - " + userOutput.getCalculation().getExcess() + "\n";
                        outputString += "E = " + userOutput.getCalculation().getIntegerExponent() + "\n";
                        outputString += "V(X) = " + userOutput.getNumber().abs() + " = X • 2^" + userOutput.getCalculation().getY() + "\n";
                        outputString += "X = " + userOutput.getNumber().abs() + " ÷ " + userOutput.getCalculation().getTwoRaisedToY() + "\n";
                        outputString += "X = " + userOutput.getCalculation().getX() + "\n";
                        operandSize = userOutput.getCalculation().getOperand().size();
                        for (int i = 0; i < operandSize; i++) {
                            outputString += userOutput.getCalculation().getOperand().get(i) + " • 2 = " + userOutput.getCalculation().getResult().get(i) + "\n";
                        }
                        break;
                    }
                    case DENORMALIZED: {
                        outputString += userOutput.getNumber().abs() + " < 2^(-" + userOutput.getCalculation().getExcessMinusOne() + ")\n";
                        outputString += "userOutput is denormalized.\n";
                        outputString += "V(X) = " + userOutput.getNumber().abs() + " = 0.M • 2^(-E" + " + 1)\n";
                        outputString += "V(X) = " + userOutput.getNumber().abs() + " = 0.M • 2^(-" + userOutput.getCalculation().getExcess() + " + 1)\n";
                        outputString += "V(X) = " + userOutput.getNumber().abs() + " = 0.M • 2^(-" + userOutput.getCalculation().getExcessMinusOne() + ")\n";
                        outputString += "X = 0.M ; 0 ≤ X < 1\n";
                        outputString += "V(X) = " + userOutput.getNumber().abs() + " = X • 2^(-" + userOutput.getCalculation().getExcessMinusOne() + ")\n";
                        outputString += "X = " + userOutput.getNumber().abs() + " ÷ 2^(-" + userOutput.getCalculation().getExcessMinusOne() + ")\n";
                        outputString += "X = " + userOutput.getCalculation().getX() + "\n";
                        operandSize = userOutput.getCalculation().getOperand().size();
                        for (int i = 0; i < operandSize; i++) {
                            outputString += userOutput.getCalculation().getOperand().get(i) + " • 2 = " + userOutput.getCalculation().getResult().get(i) + "\n";
                        }
                        break;
                    }
                }
            }
            outputString += "Sign: " + (userOutput.getSign() ? "1" : "0") + "\n";
            outputString += "Exponent: ";

            //Don't use BitSet.length(), use your own variables. Source: https://stackoverflow.com/questions/40786466/how-get-real-length-of-bitset
            for (int i = 0; i < numberOfExponentBits; i++) {//All exponent bits are written:
                outputString += userOutput.getExponent().get(i) ? "1" : "0";
            }
            outputString += "\n";
            outputString += "Mantissa: ";
            for (int i = 0; i < numberOfMantissaBits; i++) {//All mantissa bits are written:
                outputString += userOutput.getMantissa().get(i) ? "1" : "0";
            }
            outputString += "\n";
        }
        outputTextArea.setText(outputString);
    }

    public static void main(String[] args) {
        Application.launch(ApplicationView.class, args);
    }
}
