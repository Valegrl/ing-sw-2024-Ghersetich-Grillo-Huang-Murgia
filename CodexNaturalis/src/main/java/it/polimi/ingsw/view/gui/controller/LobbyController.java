package it.polimi.ingsw.view.gui.controller;


import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.KickFromLobbyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerReadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.PlayerUnreadyEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.QuitLobbyEvent;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


//TODO USE TOGGLE BUTTONS FOR THE READY?
public class LobbyController extends FXMLController {

    private List<StackPane> playerStackPanes;

    private List<Text> usernames;

    private List<Button> playerButtons;

    private List<RadioButton> radioButtons;

    private List<Text> readyStatuses;

    @FXML
    private TextField chatInput;

    @FXML
    private TextArea chatArea;

    @FXML
    private Text lobbyName;

    @FXML
    private StackPane playerStackPane0;

    @FXML
    private StackPane playerStackPane1;

    @FXML
    private StackPane playerStackPane2;

    @FXML
    private StackPane playerStackPane3;

    @FXML
    private Text username0;

    @FXML
    private Text username1;

    @FXML
    private Text username2;

    @FXML
    private Text username3;

    @FXML
    private Text readyStatus0;

    @FXML
    private Text readyStatus1;

    @FXML
    private Text readyStatus2;

    @FXML
    private Text readyStatus3;

    @FXML
    private Button playerButton0;

    @FXML
    private Button playerButton1;

    @FXML
    private Button playerButton2;

    @FXML
    private Button playerButton3;

    @FXML
    private RadioButton generalRadioButton;

    @FXML
    private RadioButton radioButton1;

    @FXML
    private RadioButton radioButton2;

    @FXML
    private RadioButton radioButton3;

    @FXML
    private Button readyButton;


    public LobbyController(){
        super();
    }

    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        playerStackPanes = Arrays.asList(playerStackPane0, playerStackPane1, playerStackPane2, playerStackPane3);
        usernames = Arrays.asList(username0, username1, username2, username3);
        readyStatuses = Arrays.asList(readyStatus0, readyStatus1, readyStatus2, readyStatus3);
        playerButtons = Arrays.asList(playerButton0, playerButton1, playerButton2, playerButton3);
        radioButtons = Arrays.asList(radioButton1, radioButton2, radioButton3);

        setLobbyName(controller.getLobbyId());
        chatArea.appendText("Welcome to the lobby: " + controller.getLobbyId() + "\n");

        /*
        controller.newViewEvent(new GetChatMessagesEvent());
        waitForResponse();
         */

        chatInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                submitMessage();
            }
        });

        updateLobby();
    }


    @FXML
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.PLAYER_READY, PLAYER_UNREADY:
                Platform.runLater(this::updateLobby);
                break;
            case EventID.UPDATE_LOBBY_PLAYERS:
                Platform.runLater(this::updateLobby);
                break;
            case EventID.QUIT_LOBBY:
                if(feedback == Feedback.SUCCESS){
                    Platform.runLater(() -> {
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
                    });
                }
                else{
                    System.out.println("Something bad happened, you can't quit!");
                }
                break;
            case EventID.KICKED_PLAYER_FROM_LOBBY:
                Platform.runLater(() -> {
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
                });
                break;
            case EventID.GET_CHAT_MESSAGES:
                Platform.runLater(() ->{
                    chatArea.appendText(message);
                });
                break;
            case EventID.CHAT_GM, CHAT_PM:
                if (feedback.equals(Feedback.SUCCESS)) {
                    chatArea.appendText(message + "\n");
                } else {
                    System.out.println("Message unable to send!");
                }
                break;
        }
        notifyResponse();;
    }

    private void updateLobby(){
        if (controller.isLobbyLeader()) {
            lobbyLeaderUpdate();
        }
        else if (!controller.isLobbyLeader()){
            lobbyUserUpdate();
        }
    }

    private void lobbyLeaderUpdate(){
        int i = 0;
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.WHITE);
        highlight.setRadius(10.0);
        highlight.setOffsetX(0);
        highlight.setOffsetY(0);

        for(String user : controller.getPlayersStatus().keySet()){
            if(controller.getUsername().equals(user)){

                if(controller.getPlayersStatus().get(user)){
                    readyStatuses.get(i).setText("Ready");
                }
                else{
                    readyStatuses.get(i).setText("Unready");
                }

                playerStackPanes.get(i).setVisible(true);
                playerStackPanes.get(i).setEffect(highlight);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(true);
                playerButtons.get(i).setText("Quit");
                playerButtons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        quitLobby();
                    }
                });
            }
            else{

                if(controller.getPlayersStatus().get(user)){
                    readyStatuses.get(i).setText("Ready");
                }
                else{
                    readyStatuses.get(i).setText("Unready");
                }


                playerStackPanes.get(i).setVisible(true);
                playerStackPanes.get(i).setEffect(null);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(true);
                playerButtons.get(i).setText("Kick");
                playerButtons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        kickPlayer(user);
                    }
                });
            }
            i++;
        }
        while(i < playerStackPanes.size()){
            playerStackPanes.get(i).setVisible(false);
            playerStackPanes.get(i).setEffect(null);
            playerButtons.get(i).setVisible(false);
            usernames.get(i).setText("");
            readyStatuses.get(i).setText("");
            i++;
        }
        updateChatOptions();
    }

    private void lobbyUserUpdate(){
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.WHITE);
        highlight.setRadius(10.0);
        highlight.setOffsetX(0);
        highlight.setOffsetY(0);

        int i = 0;
        for(String user : controller.getPlayersStatus().keySet()){
            if(controller.getUsername().equals(user)){

                if(controller.getPlayersStatus().get(user)){
                    readyStatuses.get(i).setText("Ready");
                }
                else{
                    readyStatuses.get(i).setText("Unready");
                }


                playerStackPanes.get(i).setVisible(true);
                playerStackPanes.get(i).setEffect(highlight);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(true);
                playerButtons.get(i).setText("Quit");
                playerButtons.get(i).setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        quitLobby();
                    }
                });
            }
            else{

                if(controller.getPlayersStatus().get(user)){
                    readyStatuses.get(i).setText("Ready");
                }
                else{
                    readyStatuses.get(i).setText("Unready");
                }

                playerStackPanes.get(i).setVisible(true);
                playerStackPanes.get(i).setEffect(null);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(false);
            }
            i++;
        }
        while( i < playerStackPanes.size()){
            playerStackPanes.get(i).setVisible(false);
            playerStackPanes.get(i).setEffect(null);
            usernames.get(i).setText("");
            playerButtons.get(i).setVisible(false);
            readyStatuses.get(i).setText("");
            i++;
        }

        updateChatOptions();
    }

    private void updateChatOptions(){
        int i = 0;
        generalRadioButton.setSelected(true);
        //TODO make it so the general button doesn't get selected everytime someone joins or leaves is more complex

        for(String user : controller.getPlayersStatus().keySet()){
            if(!controller.getUsername().equals(user)){
                radioButtons.get(i).setVisible(true);
                radioButtons.get(i).setText(user);
                i++;
            }
        }
        while(i < radioButtons.size()){
            radioButtons.get(i).setVisible(false);
            radioButtons.get(i).setText("");
            i++;
        }
    }

    /**
     * Method to set the name of the lobby
     *
     * @param lobbyName the name of the lobby
     */
    public void setLobbyName(String lobbyName) {
        this.lobbyName.setText("LOBBY: " + lobbyName);
    }


    private void quitLobby() {
        Event event = new QuitLobbyEvent();
        controller.newViewEvent(event);
        waitForResponse();
    }

    private void kickPlayer(String userToKick) {
        if (controller.getPlayersStatus().size() > 1) {
            Event event = new KickFromLobbyEvent(userToKick);
            controller.newViewEvent(event);
            waitForResponse();
        }
        else{
            System.out.println("Nobody here but us chickens");
        }
    }

    @FXML
    public void submitMessage(){
        String message = chatInput.getText();

        if(message.isEmpty()){

        }
        else {
            if (generalRadioButton.isSelected()) {
                sendPublicMessage(message);
            } else {
                for (RadioButton button : radioButtons) {
                    if (button.isSelected()) {
                        String username = button.getText();
                        sendPrivateMessage(username, message);
                    }
                }
            }
        }
        chatInput.clear();
    }

    private void sendPrivateMessage(String username, String message){
        if(controller.getPlayersStatus().containsKey(username)) {
            controller.newViewEvent(new ChatPMEvent(new PrivateChatMessage(username, message)));
        }
        else{
            System.out.println("Error: player not in match");
        }
    }

    private void sendPublicMessage(String message){
        controller.newViewEvent(new ChatGMEvent(new ChatMessage(message)));
    }

    @FXML
    public void toggleReadyStatus(){
        Event event;
        if(controller.getPlayersStatus().get(view.getUsername())){
            readyButton.setText("Ready");
            event = new PlayerUnreadyEvent();
        }
        else{
            readyButton.setText("Unready");
            event = new PlayerReadyEvent();
        }
        controller.newViewEvent(event);
        waitForResponse();
    }


    @Override
    public boolean inLobby(){
        return true;
    }

    @Override
    public boolean inChat(){
        return true;
    }
}

