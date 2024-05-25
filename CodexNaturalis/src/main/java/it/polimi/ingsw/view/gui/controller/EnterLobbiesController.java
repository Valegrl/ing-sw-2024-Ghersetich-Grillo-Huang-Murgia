package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.AvailableLobbiesEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.CreateLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.JoinLobbyEvent;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class EnterLobbiesController extends FXMLController {
    //TODO add event listener if i click on a row i'll join the lobby

    @FXML
    private BorderPane availableLobbiesMenuFX;

    @FXML
    private TableView<LobbyState> lobbiesTable;

    @FXML
    private Label errorLobbies;

    @FXML
    private TableColumn<LobbyState, String> lobbyName;

    @FXML
    private TableColumn<LobbyState, Integer> numPlayers;

    @FXML
    private TableColumn<LobbyState, Integer> numRequired;

    @FXML
    private TextField lobbyNameField;

    @FXML
    private Slider requiredNumSlider;


    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        errorLobbies.setText("");
        lobbiesTable.setPlaceholder(new Label(""));

        lobbyNameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitCreateLobby(new ActionEvent());
            }
        });

        lobbiesTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2 && (!lobbiesTable.getSelectionModel().isEmpty()))
            {
                LobbyState selectedLobby = lobbiesTable.getSelectionModel().getSelectedItem();
                String lobbyID = selectedLobby.getId();
                joinLobby(lobbyID);
            }
        });

        refreshLobbies();

    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.AVAILABLE_LOBBIES:
                if(feedback == Feedback.SUCCESS) {
                    //showResponseMessage(message, 0);
                } else {
                    errorLobbies.setText("Failed to get available lobbies from server: " + message);
                    //showResponseMessage("Failed to get available lobbies from server: " + message, 2000);
                }
                break;
            case EventID.CREATE_LOBBY:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/LobbyMenu.fxml"));
                            Parent root = loader.load();
                            LobbyController nextController = loader.getController();

                            Scene scene = stage.getScene();
                            scene.setRoot(root);
                            transition(nextController);
                        } catch (IOException exception) {
                            errorLobbies.setText("Failed to join lobby.");
                        }
                    });
                    showResponseMessage("Lobby created:" + message, 0);
                } else {
                    errorLobbies.setText("Lobby creation failed: " + message);
                }
                break;
            case  EventID.JOIN_LOBBY:
                if (feedback == Feedback.SUCCESS){
                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/LobbyMenu.fxml"));
                            Parent root = loader.load();
                            LobbyController nextController = loader.getController();

                            Scene scene = stage.getScene();
                            scene.setRoot(root);
                            transition(nextController);
                        } catch (IOException exception) {
                            errorLobbies.setText("IOException: Failed to join lobby.");
                        }
                    });
                }
                else{
                    Platform.runLater(() -> {
                        errorLobbies.setText("Failed to join lobby: " + message);
                        refreshLobbies();
                    });
                }
                break;
        }
        notifyResponse();
    }

    @Override
    public boolean inMenu() {
        return true;
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
            errorLobbies.setText("Error occurred, can't go back to menu.");
        }
    }

    @FXML
    public void refreshLobbies(){
        errorLobbies.setText("");
        lobbiesTable.getItems().clear();

        Event event = new AvailableLobbiesEvent();
        controller.newViewEvent(event);
        waitForResponse();

        if(!controller.getLobbies().isEmpty()){
            lobbyName.setCellValueFactory(new PropertyValueFactory<>("id"));
            numPlayers.setCellValueFactory(new PropertyValueFactory<>("onlinePlayers"));
            numRequired.setCellValueFactory(new PropertyValueFactory<>("requiredPlayers"));

            List<LobbyState> lobbies = controller.getLobbies();
            lobbiesTable.getItems().addAll(lobbies);
        }
        else{
            //showResponseMessage("No lobbies available.", 1000);
            errorLobbies.setText("No lobbies are available at the moment.");
        }
    }

    @FXML
    private void submitCreateLobby(ActionEvent e) {
        String lobbyName = lobbyNameField.getText();
        int requiredNumber = (int) requiredNumSlider.getValue();
        lobbyNameField.clear();

        if(lobbyName.isEmpty()){
          errorLobbies.setText("Lobby name can't be left empty!");
        }
        else {
            Event event = new CreateLobbyEvent(lobbyName, requiredNumber);
            controller.newViewEvent(event);
            waitForResponse();
        }
    }

    private void joinLobby(String lobbyID){
        Event event = new JoinLobbyEvent(lobbyID);
        controller.newViewEvent(event);
        waitForResponse();
    }
}
