package com.vidivox.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;


/**
 * @author Matthew Canham, Jayden Cooke
 * A class to generate dialog boxes, which are lacking in JavaFX.
 * Mainly used to notify the user of errors.
 */
public class WarningDialogue extends Dialogue{

    /**
     * Handles normal errors.
     * @param warningText - The text to present to the user.
     */
    public WarningDialogue(String warningText) {
        this(warningText, "Warning!");
    }

    /**
     * Warning dialogue box with a customisable title and text input field.
     * @param warningText - The words to present to the user
     * @param titleText - The title of the dialog box
     */
    public WarningDialogue(String warningText, String titleText) {
        super(warningText,titleText);
    }

    protected Scene setUpLayout(String warningText){
        Label warningLabel = new Label(warningText);
        VBox layout = new VBox(10);
        layout.getChildren().add(warningLabel);
        Button closeButton = new Button("OK");
        //Close action is slightly different when there is no text to add.
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                warningStage.close();
            }
        });
        layout.getChildren().add(closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20,20,20,20));
        Scene scene = new Scene(layout);
        return scene;
    }
}
