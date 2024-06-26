package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.AvailableLobbiesEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.CreateLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.JoinLobbyEvent;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

/**
 * This class is responsible for controlling the GUI during the lobby selection phase.
 * It handles the display of available lobbies, creation of new lobbies, and joining existing lobbies.
 * It also provides options to refresh the list of available lobbies and to go back to the main menu.
 */
public class EnterLobbiesController extends FXMLController {

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

    /**
     * This method is the entry point for the controller when the GUI is run.
     * It sets up the view, stage, and controller, and initializes the error message and table placeholder.
     * It also sets up event handlers for the lobby name field and the lobbies table.
     * When the enter key is pressed in the lobby name field, it triggers the creation of a new lobby.
     * When a lobby in the lobbies table is double-clicked, it triggers the joining of the selected lobby.
     *
     * @param view  The view associated with this controller.
     * @param stage The stage on which the GUI is displayed.
     */
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
            if (event.getClickCount() == 2 && (!lobbiesTable.getSelectionModel().isEmpty())) {
                LobbyState selectedLobby = lobbiesTable.getSelectionModel().getSelectedItem();
                String lobbyID = selectedLobby.getId();
                joinLobby(lobbyID);
            }
        });

        refreshLobbies();
    }

    /**
     * This method handles the response from the server after a request has been made.
     * It checks the feedback and the event ID to determine the appropriate action.
     * If the feedback is successful, it will perform the corresponding action (e.g., print available lobbies, switch screen to "LobbyMenu").
     * If the feedback is not successful, it will display an error message on the GUI.
     *
     * @param feedback The feedback from the server indicating the success or failure of the request.
     * @param message  The message from the server providing additional information about the request.
     * @param eventID  The ID of the event that triggered the request.
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.AVAILABLE_LOBBIES:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(this::printAvailableLobbies);
                } else {
                    Platform.runLater(() -> errorLobbies.setText("Failed to get available lobbies from server: " + message));
                }
                break;
            case EventID.CREATE_LOBBY:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            switchScreen("LobbyMenu");
                        } catch (IOException exception) {
                            errorLobbies.setText("Lobby creation failed");
                            throw new RuntimeException("FXML Exception: failed to load LobbyMenu", exception);
                        }
                    });
                } else {
                    Platform.runLater(() -> errorLobbies.setText("Lobby creation failed: " + message));
                }
                break;
            case EventID.JOIN_LOBBY:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            switchScreen("LobbyMenu");
                        } catch (IOException exception) {
                            errorLobbies.setText("Failed to join lobby");
                            throw new RuntimeException("FXML Exception: failed to load LobbyMenu", exception);
                        }
                    });
                } else {
                    Platform.runLater(() -> {
                        errorLobbies.setText("Failed to join lobby: " + message);
                        PauseTransition pause = new PauseTransition(Duration.seconds(2));
                        pause.setOnFinished(event -> refreshLobbies());
                        pause.play();
                    });
                }
                break;
        }
    }

    /**
     * This method is used to check if the current controller is in the menu.
     * In this case, it always returns true as the EnterLobbiesController is part of the menu.
     *
     * @return true, indicating that the controller is in the menu.
     */
    @Override
    public boolean inMenu() {
        return true;
    }


    /**
     * Handles the action of going back to the menu. It loads a new FXML scene.
     *
     * @throws RuntimeException if there is an IOException when switching the screen.
     */
    @FXML
    public void goBack() {
        try {
            switchScreen("Menu");
        } catch (IOException exception) {
            errorLobbies.setText("Error occurred, can't go back to menu");
            throw new RuntimeException("FXML Exception: failed to load Menu", exception);
        }
    }

    /**
     * Refreshes the list of available lobbies.
     */
    @FXML
    public void refreshLobbies() {
        errorLobbies.setText("");
        lobbiesTable.getItems().clear();

        Event event = new AvailableLobbiesEvent();
        controller.newViewEvent(event);
    }

    /**
     * This method is used to display the available lobbies in the table view.
     * It first checks if there are any lobbies available from the controller.
     * If there are, it adds all the lobbies to the table view.
     * If there are no lobbies available, it sets the error message to indicate that no lobbies are available at the moment.
     */
    private void printAvailableLobbies() {
        if (!controller.getLobbies().isEmpty()) {
            lobbyName.setCellValueFactory(new PropertyValueFactory<>("id"));
            numPlayers.setCellValueFactory(new PropertyValueFactory<>("onlinePlayers"));
            numRequired.setCellValueFactory(new PropertyValueFactory<>("requiredPlayers"));

            List<LobbyState> lobbies = controller.getLobbies();
            lobbiesTable.getItems().addAll(lobbies);
        } else {
            //showResponseMessage("No lobbies available.", 1000);
            errorLobbies.setText("No lobbies are available at the moment.");
        }
    }

    /**
     * Submits a request to create a new lobby.
     *
     * @param e The action event associated with the button click.
     */
    @FXML
    private void submitCreateLobby(ActionEvent e) {
        String lobbyName = lobbyNameField.getText();
        int requiredNumber = (int) requiredNumSlider.getValue();
        lobbyNameField.clear();

        if (lobbyName.isEmpty()) {
            errorLobbies.setText("Lobby name can't be left empty!");
        } else if (lobbyName.length() > JsonConfig.getInstance().getMaxLobbyIdLength()) {
            errorLobbies.setText("Lobby name can't be longer than "+JsonConfig.getInstance().getMaxLobbyIdLength()+" characters!");
        } else {
            Event event = new CreateLobbyEvent(lobbyName, requiredNumber);
            controller.newViewEvent(event);

        }
    }

    /**
     * Joins a selected lobby.
     *
     * @param lobbyID The ID of the selected lobby.
     */
    private void joinLobby(String lobbyID) {
        Event event = new JoinLobbyEvent(lobbyID);
        controller.newViewEvent(event);
    }
}
