package com.team;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MainApplication extends Application {
    public void start(Stage primaryStage) {
        Button button = new Button("Calculate");//Create a button with specified text.
        TextField textField = new TextField();//Create a TextField.
        HBox root = new HBox();//Create a horizontal box layout.
        root.getChildren().addAll(textField, button);//Add textfield and button as children of root.
        Scene scene = new Scene(root, 500, 300);//Create a scene specifying the root and the size.
        primaryStage.setScene(scene);//Add scene to the stage.
        primaryStage.show();//Make the stage visible.
    }

    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }
}
