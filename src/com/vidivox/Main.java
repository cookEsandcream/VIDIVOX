package com.vidivox;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


/**
 * @author Jayden Cooke
 * Automatically generated by JavaFX SceneBuilder, only the background was changed.
 * Main method for VIDIVOX. Creates the stage using the FXML and displays it.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/MainWindow.fxml"));
        primaryStage.setTitle("VIDIVOX");
        primaryStage.setScene(new Scene(root));
        //Sets a black background for the areas not filled by the video. Doesn't appear to work with Ubuntu's graphics.
        primaryStage.getScene().fillProperty().setValue(Color.BLACK);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
