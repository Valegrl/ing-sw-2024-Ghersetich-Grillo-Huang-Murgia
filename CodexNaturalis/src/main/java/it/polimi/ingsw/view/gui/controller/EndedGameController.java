package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class EndedGameController extends FXMLController {

    @FXML
    private Label congratulationsLabel;

    @FXML
    private Label gameEndedLabel;

    @FXML
    private AnchorPane leaderboardAnchor1;

    @FXML
    private AnchorPane leaderboardAnchor2;

    @FXML
    private AnchorPane leaderboardAnchor3;

    @FXML
    private AnchorPane leaderboardAnchor4;

    @FXML
    private Label leaderboardLabel;

    @FXML
    private VBox leaderboardVbox;

    @FXML
    private Label gameName;

    @FXML
    private AnchorPane mainAnchor;

    @FXML
    private Button newgameButton;

    @FXML
    private RadioButton playAreaButton1;

    @FXML
    private RadioButton playAreaButton2;

    @FXML
    private RadioButton playAreaButton3;

    @FXML
    private RadioButton playAreaButton4;

    @FXML
    private Label playerText1;

    @FXML
    private Label playerText2;

    @FXML
    private Label playerText3;

    @FXML
    private Label playerText4;

    @FXML
    private Label points1;

    @FXML
    private Label points2;

    @FXML
    private Label points3;

    @FXML
    private Label points4;

    @FXML
    private Button quitButton;

    private List<RadioButton> radioButtons;

    private List<AnchorPane> playerAnchorPanes;

    public EndedGameController(){
        super();
    }

    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        playerAnchorPanes = Arrays.asList(leaderboardAnchor1, leaderboardAnchor2, leaderboardAnchor3, leaderboardAnchor4);
        radioButtons = Arrays.asList(playAreaButton1, playAreaButton2, playAreaButton3, playAreaButton4);
        setGameName();

    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {

    }

    private void setGameName() {
        this.gameName.setText("GAME:" + controller.getLobbyId());
    }
    private void setCongratulationsMessage(String winner) {
        this.congratulationsLabel.setText("CONGRATULATIONS " + winner + ", YOU WON!");
    }


    @FXML
    public void goMainMenu(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/MainMenu.fxml"));
            Parent root = loader.load();
            MainMenuController nextController = loader.getController();

            Scene scene = stage.getScene();
            scene.setRoot(root);

            transition(nextController);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }

    @FXML
    public void goLobbies(ActionEvent e){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/EnterLobbiesMenu.fxml"));
            Parent root = loader.load();
            EnterLobbiesController nextController = loader.getController();

            Scene scene = stage.getScene();
            scene.setRoot(root);

            transition(nextController);
        }
        catch (IOException exception){
            exception.printStackTrace();
        }
    }


    @Override
    public boolean inGame(){
        return true;
    }
}
