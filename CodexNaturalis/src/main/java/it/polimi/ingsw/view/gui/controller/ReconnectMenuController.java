package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.GetMyOfflineGamesEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.ReconnectToGameEvent;
import it.polimi.ingsw.utils.LobbyState;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

/**
 * The ReconnectMenuController class extends the FXMLController class and is responsible for handling the user interface and user interactions for the reconnect menu.
 * This includes handling button clicks, displaying error messages, and managing the table of lobbies the user disconnected from.
 * The class also communicates with the GameController to send and receive events.
 */
public class ReconnectMenuController extends FXMLController {

    @FXML
    private Button backButton;

    @FXML
    private Button quitButton;

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

    /**
     * The constructor for the ReconnectMenuController class.
     * Calls the superclass constructor.
     */
    public ReconnectMenuController(){
        super();
    }

    /**
     * The run method is called to initialize the view and stage for the controller.
     * It also sets up a mouse click event for the lobbies table.
     *
     * @param view the view to be set up
     * @param stage the stage to be set up
     */
    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        lobbiesTable.setPlaceholder(new Label(""));

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

    /**
     * The handleResponse method is called to handle the response from the server.
     * It updates the user interface based on the feedback received.
     * To get the lobbies the user disconnected from, this FXMLController has to handle {@link GetMyOfflineGamesEvent}.
     * If the user experiences a disconnection during the operation of the {@link GameSetupController},
     * they will be able to reconnect at the stage of the{@link TokenSetupController}
     * If the user experiences a disconnection during the operation of the {@link TokenSetupController},
     * they will be able to reconnect at the stage of the{@link InGameController}.
     *
     * @param feedback the feedback received from the server
     * @param message the message received from the server
     * @param eventID the ID of the event that triggered the response
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)){
            case GET_MY_OFFLINE_GAMES :
                if(feedback == Feedback.FAILURE){
                    Platform.runLater(() -> errorLabel.setText("Failed to get offline games"));
                }
                break;
            case RECONNECT_TO_GAME:
                if(feedback == Feedback.SUCCESS){
                    if(!controller.isInSetup()) {
                        Platform.runLater(() -> {
                            try {
                                switchScreen("InGamev2");
                            } catch (IOException exception) {
                                throw new RuntimeException(exception);
                            }
                        });
                    }
                    else if(controller.isInSetup()){
                        Platform.runLater(() -> {
                            reconnectLabel.setText("Wait, we are connecting you to the game.");
                            backButton.setVisible(false);
                            backButton.setManaged(false);
                            quitButton.setVisible(true);
                            quitButton.setManaged(true);
                        });
                    }
                }
                else{
                    Platform.runLater(() -> errorLabel.setText(message));
                }
                break;
            case CHOOSE_TOKEN_SETUP:
                if(!controller.isInTokenSetup()) {
                    Platform.runLater(() -> {
                        try {
                            switchScreen("TokenSetup");
                        } catch (IOException exception) {
                            throw new RuntimeException(exception);
                        }
                    });
                }
                break;
            case UPDATE_LOCAL_MODEL:
                Platform.runLater(() -> {
                    try {
                        switchScreen("InGamev2");
                    } catch (IOException exception) {
                        throw new RuntimeException(exception);
                    }
                });
                break;
            case QUIT_GAME:
                if (feedback == Feedback.SUCCESS){
                    Platform.runLater(() -> {
                        try {
                            switchScreen("Menu");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                else {
                    Platform.runLater(() -> {
                        errorLabel.setVisible(true);
                        errorLabel.setText("Can't quit from the reconnection");
                    });
                }
                break;
        }
        notifyResponse();
    }

    /**
     * The goBack method is called when the back button is clicked.
     * It loads the menu view and transitions to the {@link MenuController}.
     */
    @FXML
    public void goBack() throws IOException{
        try {
            switchScreen("Menu");
        }
        catch (IOException exception){
            throw new IOException("Error loading the FXML: trying to load Menu");
        }
    }

    /**
     * The quit method is called when the quit button is clicked.
     * It creates a new {@link QuitGameEvent} and sends it to the controller.
     * After sending the event, it waits for a response from the server.
     */
    @FXML
    public void quit(){
        QuitGameEvent event = new QuitGameEvent();
        controller.newViewEvent(event);
        waitForResponse();
    }

    /**
     * The refreshLobbies method is called to refresh the list of available lobbies.
     * It clears the lobbies table and sends a {@link GetMyOfflineGamesEvent} to the server.
     */
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

    /**
     * The reconnectToGame method is called to reconnect to a game.
     * It sends a {@link ReconnectToGameEvent} to the server with the ID of the selected game.
     *
     * @param lobbyID the ID of the lobby to reconnect to
     */
    private void reconnectToGame(String lobbyID){
        Event event = new ReconnectToGameEvent(lobbyID);
        controller.newViewEvent(event);
        waitForResponse();
    }

    /**
     * The inMenu method is used to check if the player is in the menu.
     * It returns true as the player is in the menu during the reconnect phase.
     *
     * @return true
     */
    @Override
    public boolean inMenu(){
        return true;
    }

    /**
     * The inGame method is used to check if the player is in the game.
     * It returns true as the player is in the game during the reconnect phase.
     *
     * @return true
     */
    @Override
    public boolean inGame(){
        return true;
    }
}


