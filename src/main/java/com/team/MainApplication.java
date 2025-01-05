package com.team;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApplication extends Application {
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        HBox hBox = new HBox();//Create a horizontal box layout.
        Button button = new Button("Calculate");//Create a button with specified text.
        TextField textField = new TextField();//Create a TextField.
        TextArea textArea = new TextArea("CO\u2082\n" +
                                        "NO\u2082\n");
        hBox.setAlignment(Pos.TOP_CENTER);//The horizontal box is aligned with the top center.
        hBox.getChildren().addAll(textField, button);//Add textfield and button as children of hBox.
        root.getChildren().addAll(hBox, textArea);//Add hBox and textArea as children of root.
        Scene scene = new Scene(root, 1000, 600);//Create a scene specifying the hBox and the size.
        primaryStage.setScene(scene);//Add scene to the stage.
        primaryStage.show();//Make the stage visible.
    }

    public static void main(String[] args) {
        Application.launch(MainApplication.class, args);
    }
}
