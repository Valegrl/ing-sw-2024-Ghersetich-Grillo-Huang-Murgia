package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.AvailableLobbiesEvent;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AvailableLobbiesController extends FXMLController {

    @FXML
    private BorderPane availableLobbiesMenuFX;

    @FXML
    private TableView<LobbyState> lobbiesTable;

    @FXML
    private Label errorShowLobbies;

    @FXML
    private TableColumn<LobbyState, String> lobbyName;

    @FXML
    private TableColumn<LobbyState, Integer> numPlayers;

    @FXML
    private TableColumn<LobbyState, Integer> numRequired;


    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        errorShowLobbies.setText("");
    }

    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        if (feedback == Feedback.SUCCESS) {
            //showResponseMessage(message, 0);
        } else {
            errorShowLobbies.setText("Failed to get available lobbies from server: " + message);
            //showResponseMessage("Failed to get available lobbies from server: " + message, 2000);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Menu.fxml"));
            Parent root = loader.load();
            MenuController nextController = loader.getController();

            Scene scene = stage.getScene();
            scene.setRoot(root);
            transition(nextController);
        }
        catch (IOException exception){
            errorShowLobbies.setText("Error occurred, can't go back to menu.");
        }
    }

    @FXML
    public void refreshLobbies(){
        errorShowLobbies.setText("");
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
            errorShowLobbies.setText("No lobbies are available at the moment.");
        }
    }
}
