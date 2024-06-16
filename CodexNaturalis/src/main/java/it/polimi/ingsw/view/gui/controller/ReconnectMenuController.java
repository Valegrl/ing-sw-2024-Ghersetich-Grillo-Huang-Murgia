package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.GetMyOfflineGamesEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.ReconnectToGameEvent;
import it.polimi.ingsw.model.GameStatus;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ReconnectMenuController extends FXMLController {

    @FXML
    private Label reconnectLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private TableView<LobbyState> lobbiesTable;

    @FXML
    private TableColumn<LobbyState, String> lobbyName;

    @FXML
    private TableColumn<LobbyState, Integer> numPlayers;

    @FXML
    private TableColumn<LobbyState, Integer> numRequired;

    public ReconnectMenuController(){
        super();
    }

    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        lobbiesTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && (!lobbiesTable.getSelectionModel().isEmpty()))
            {
                LobbyState selectedLobby = lobbiesTable.getSelectionModel().getSelectedItem();
                String lobbyID = selectedLobby.getId();
                reconnectToGame(lobbyID);
            }
        });
        refreshLobbies();
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)){
            case GET_MY_OFFLINE_GAMES :
                if(feedback == Feedback.SUCCESS){

                }
                else{
                    Platform.runLater(() -> errorLabel.setText("Failed to get offline games"));
                }
                break;
            case RECONNECT_TO_GAME:
                if(feedback == Feedback.SUCCESS){
                    if(controller.getGameStatus().equals(GameStatus.RUNNING)){
                        Platform.runLater(() ->{
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/InGamev2.fxml"));
                                Parent root = loader.load();
                                InGameController nextController = loader.getController();

                                Scene scene = stage.getScene();
                                scene.setRoot(root);
                                transition(nextController);
                            } catch (IOException exception) {
                                exception.printStackTrace();
                            }
                        });
                    }
                }
                else{
                    Platform.runLater(() -> errorLabel.setText(message));
                }
                break;
        }
        notifyResponse();
    }

    @FXML
    public void goBack(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/Menu.fxml"));
            Parent root = loader.load();
            MenuController nextController = loader.getController();

            Scene scene = stage.getScene();
            scene.setRoot(root);
            transition(nextController);
        }
        catch (IOException exception){
            errorLabel.setText("Error occurred, can't go back to menu.");
        }
    }

    private void refreshLobbies() {
        errorLabel.setText("");
        lobbiesTable.getItems().clear();

        Event event = new GetMyOfflineGamesEvent();
        controller.newViewEvent(event);
        waitForResponse();

        if(!controller.getOfflineGames().isEmpty()){
            lobbyName.setCellValueFactory(new PropertyValueFactory<>("id"));
            numPlayers.setCellValueFactory(new PropertyValueFactory<>("onlinePlayers"));
            numRequired.setCellValueFactory(new PropertyValueFactory<>("requiredPlayers"));

            List<LobbyState> offlineGames = controller.getOfflineGames();
            lobbiesTable.getItems().addAll(offlineGames);
        }
        else{
            errorLabel.setText("There's no game to reconnect to");
        }
    }

    private void reconnectToGame(String lobbyID){
        Event event = new ReconnectToGameEvent(lobbyID);
        controller.newViewEvent(event);
        waitForResponse();
    }

    @Override
    public boolean inMenu(){
        return true;
    }
}


