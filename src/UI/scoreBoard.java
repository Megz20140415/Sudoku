package UI;

import static UI.gamePlay.*;
import static UI.global.*;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 *
 * @author Muhammad
 */
public class scoreBoard {

    GridPane scorePageContainer;
    private GridPane saveDataLayout;
    
    private TextField nameTextField;
    
    private Button backToMenuButton;
    
    private Label headline;
    static Label timeLabel;
    private Label inputLabel;
    private Label boardHeadline;
    static Label scoreBoardArray[] = new Label[5];
    


    public GridPane initialize() {
        //Score page layout
        scorePageContainer = new GridPane();
        scorePageContainer.getStyleClass().add("score-page");
        scorePageContainer.setAlignment(Pos.CENTER);

        //Page Headline
        Label headline = new Label("Time");
        headline.getStyleClass().add("score-text");
        headline.getStyleClass().add("page-headline");
        headline.setPadding(new Insets(0, 0, 5, 0));
        centerObject(headline);
        scorePageContainer.setConstraints(headline, 0, 0);

        //Game time
        timeLabel = new Label("1:00");
        timeLabel.getStyleClass().add("score-text");
        timeLabel.getStyleClass().add("game-score-time");
        timeLabel.setPadding(new Insets(5, 0, 30, 0));
        centerObject(timeLabel);
        scorePageContainer.setConstraints(timeLabel, 0, 1);

        //Left panel layout
        saveDataLayout = new GridPane();
        centerObject(saveDataLayout);
        scorePageContainer.setConstraints(saveDataLayout, 0, 2);

        //Textfield placeholder
        inputLabel = new Label("Enter you name:");
        inputLabel.getStyleClass().add("input-label");
        inputLabel.getStyleClass().add("score-text");
        saveDataLayout.setConstraints(inputLabel, 0, 0);

        //Textfield
        nameTextField = new TextField();
        nameTextField.getStyleClass().add("name-text-field");
        saveDataLayout.setConstraints(nameTextField, 1, 0);

        nameTextField.setOnKeyPressed((KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                fade(saveDataLayout, 750, 0, FADE_OUT);
                nameTextField.setDisable(true);
                saveDataLayout.getChildren().remove(inputLabel);
                saveDataLayout.setAlignment(Pos.CENTER);
                nameTextField.setAlignment(Pos.CENTER);
                timeLabel.setPadding(new Insets(5, 0, 15, 0));
                fade(saveDataLayout, 750, 0, FADE_IN);
               
                try {
                    database.addDashboard(nameTextField.getText(), gameTime.getTime(), levelLabel.getText());
                } catch (SQLException ex) {
                    Logger.getLogger(scoreBoard.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        saveDataLayout.getChildren().addAll(inputLabel, nameTextField);

        boardHeadline = new Label("Time Score Board");
        boardHeadline.getStyleClass().add("score-text");
        boardHeadline.getStyleClass().add("page-headline");
        boardHeadline.setPadding(new Insets(50, 0, 15, 0));
        scorePageContainer.setConstraints(boardHeadline, 0, 3);
        centerObject(boardHeadline);

        scorePageContainer.getChildren().addAll(headline, timeLabel, saveDataLayout, boardHeadline);

        for (int counter = 0; counter < 5; counter++) {
            scoreBoardArray[counter] = new Label();
            scoreBoardArray[counter].getStyleClass().add("score-line");
            scoreBoardArray[counter].getStyleClass().add("score-text");
            scoreBoardArray[counter].setTextAlignment(TextAlignment.LEFT);
            centerObject(scoreBoardArray[counter]);

            if (counter < 4) {
                scoreBoardArray[counter].setPadding(new Insets(5, 0, 5, 0));
            } else {
                scoreBoardArray[counter].setPadding(new Insets(5, 0, 30, 0));
            }

            scorePageContainer.setConstraints(scoreBoardArray[counter], 0, 4 + counter + 1);
            scorePageContainer.getChildren().add(scoreBoardArray[counter]);
        }

        backToMenuButton = new Button("BACK TO MAIN MENU");
        backToMenuButton.getStyleClass().add("button-white");
        backToMenuButton.setPadding(new Insets(10, 20, 10, 20));
        scorePageContainer.setConstraints(backToMenuButton, 0, 10);
        centerObject(backToMenuButton);
        scorePageContainer.getChildren().add(backToMenuButton);

        backToMenuButton.setOnAction(e -> {
            sudokuOperation(CLEAR_SUDOKU);
            switchPanes(screenContainer, scorePageContainer, mainMenuContainer);
            Timeline resetUITimeline = new Timeline();
            KeyFrame startReset = new KeyFrame(Duration.millis(300), event -> resetUI());
            resetUITimeline.getKeyFrames().add(startReset);
            resetUITimeline.play();
        });

        return scorePageContainer;
    }

    private void resetUI() {
        saveDataLayout.setConstraints(inputLabel, 0, 0);
        nameTextField.setDisable(false);
        timeLabel.setPadding(new Insets(5, 0, 30, 0));
        nameTextField.setAlignment(Pos.CENTER_LEFT);
    }

    private void centerObject(Object node) {
        scorePageContainer.setHalignment((Node) node, HPos.CENTER);
    }
}
