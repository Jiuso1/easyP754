package com.team;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainApplication extends Application {
    public void start(Stage primaryStage) {
        VBox root = new VBox();//Create a vertical box layout.
        HBox hBox = new HBox();//Create a horizontal box layout.
        Button button = new Button("Calculate");//Create a button with specified text.
        TextField input = new TextField();//Create an input TextField.
        TextArea output = new TextArea();//Create an output TextArea.
        Font font = new Font("Arial", 16);//Create a font for the output style.
        output.setEditable(false);//output isn't editable.
        output.setFont(font);//output has Arial 20 font.
        output.setText("Hello JavaFX!\n" +
                "Multiline Text");//The output text is set.
        hBox.setAlignment(Pos.TOP_CENTER);//The horizontal box is aligned with the top center.
        hBox.getChildren().addAll(input, button);//Add input and button as children of hBox.
        root.getChildren().addAll(hBox, output);//Add hBox and output as children of root.
        Scene scene = new Scene(root, 1000, 600);//Create a scene specifying root and window size.
        primaryStage.setScene(scene);//Add scene to the stage.
        primaryStage.show();//Make the stage visible.
    }

    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }
}
