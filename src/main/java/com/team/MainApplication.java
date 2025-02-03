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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.BitSet;

public class MainApplication extends Application {
    public void start(Stage primaryStage) {
        VBox root = new VBox();//Create a vertical box layout.
        HBox hBox = new HBox();//Create a horizontal box layout.
        TextField inputTextField = new TextField();//Create an input TextField.
        TextArea outputTextArea = new TextArea();//Create an output TextArea.
        ObservableList<PrecisionMode> items = FXCollections.observableArrayList();//Create a collection of PrecisionMode items. Source: https://acodigo.blogspot.com/2015/04/javafx-uso-de-combobox.html
        items.addAll(PrecisionMode.SIMPLE, PrecisionMode.DOUBLE);//Both modes are added as items.
        ComboBox<PrecisionMode> precisionModeComboBox = new ComboBox<>(items);//Create a precision mode ComboBox with given items.
        precisionModeComboBox.setValue(items.getFirst());//Default mode is set to Simple precision (first item).
        Button button = new Button("Calculate");//Create a button with specified text.
        button.setOnAction(a -> generateOutputTextArea(inputTextField, precisionModeComboBox, outputTextArea));
        Font font = new Font("Arial", 20);//Create a font for the output style.
        outputTextArea.setEditable(false);//output isn't editable.
        outputTextArea.setFont(font);//output has Arial 20 font.
        outputTextArea.setPrefHeight(600);
        hBox.setAlignment(Pos.TOP_CENTER);//The horizontal box is aligned with the top center.
        hBox.getChildren().addAll(inputTextField, precisionModeComboBox, button);//Add input and button as children of hBox.
        root.getChildren().addAll(hBox, outputTextArea);//Add hBox and output as children of root.
        Scene scene = new Scene(root, 1000, 600);//Create a scene specifying root and window size.
        primaryStage.setScene(scene);//Add scene to the stage.
        primaryStage.show();//Make the stage visible.
    }

    public void generateOutputTextArea(TextField inputTextField, ComboBox<PrecisionMode> precisionModeComboBox, TextArea outputTextArea) {
        UserInput userInput = new UserInput(inputTextField.getText(), precisionModeComboBox.getValue());
        Calculator calculator = new Calculator(userInput);
        UserOutput userOutput = calculator.calculateUserOutput();
        boolean sign = false;
        BitSet exponent = null;
        BitSet mantissa = null;
        boolean isSpecial = false;
        int numberOfExponentBits = 0;
        int numberOfMantissaBits = 0;
        String outputString = "";

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
            outputString = "An error occurred: userOutput is null.\n";
        } else {
            sign = userOutput.getSign();
            exponent = userOutput.getExponent();
            mantissa = userOutput.getMantissa();
            isSpecial = userOutput.isSpecial();
            if (isSpecial) {
                outputString = "The given input is a special case. Read your lecture notes in order to understand this result :-).\n";
            } else {
                outputString = "The input isn't a special case. We proceed with calculations.";
            }
            outputString += "Result:\n";
            outputString += "S: ";
            outputString += sign ? "1" : "0";
            outputString += "\n";
            //Don't use BitSet.length(), use your own variables. Source: https://stackoverflow.com/questions/40786466/how-get-real-length-of-bitset
            outputString += "E: ";
            for (int i = 0; i < numberOfExponentBits; i++) {
                outputString += exponent.get(i) ? "1" : "0";
            }
            outputString += "\n";
            outputString += "M: ";
            for (int i = 0; i < numberOfMantissaBits; i++) {
                outputString += mantissa.get(i) ? "1" : "0";
            }
            outputString += "\n";
        }

        outputTextArea.setText(outputString);
    }

    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }
}
