package com.team;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApplication extends Application {
    public void start(Stage primaryStage) {
        // create a button with specified text
        Button button = new Button("Say 'Hello World'");
        // set a handler that is executed when the user activates the button
        // e.g. by clicking it or pressing enter while it's focused
        button.setOnAction(e -> {
            //Open information dialog that says hello
            Alert alert = new Alert(AlertType.INFORMATION, "Hello World!?");
            alert.showAndWait();
        });
        // the root of the scene shown in the main window
        StackPane root = new StackPane();
        // add button as child of the root
        root.getChildren().add(button);
        // create a scene specifying the root and the size
        Scene scene = new Scene(root, 500, 300);
        // add scene to the stage
        primaryStage.setScene(scene);
        // make the stage visible
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }
}
