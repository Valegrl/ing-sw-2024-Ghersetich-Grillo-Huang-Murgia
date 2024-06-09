package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChosenTokenSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetChatMessagesEvent;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TokenSetupController extends FXMLController {

    @FXML
    private Label lobbyName;

    @FXML
    private TextArea chatArea;

    @FXML
    private TextField chatInput;


    @FXML
    private RadioButton generalRadioButton;

    @FXML
    private RadioButton radioButton1;

    @FXML
    private RadioButton radioButton2;

    @FXML
    private RadioButton radioButton3;

    @FXML
    private ImageView red;

    @FXML
    private ImageView blue;

    @FXML
    private ImageView green;

    @FXML
    private ImageView yellow;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button quitButton;

    @FXML
    private Button sendButton;


    private List<RadioButton> radioButtons;

    private List<ImageView> imageViewTokens;

    private Map<String, ImageView> visibleTokens;


    public TokenSetupController(){
        super();
    }

    @Override
    public void run(View view, Stage stage){
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        setLobbyName(controller.getLobbyId());
        radioButtons = Arrays.asList(radioButton1, radioButton2, radioButton3);

        visibleTokens = new LinkedHashMap<>() {{
            put("red", red);
            put("blue", blue);
            put("green", green);
            put("yellow", yellow);
        }};

        startCountdown(30);
        update();
        addChoiceSelection();

        controller.newViewEvent(new GetChatMessagesEvent());
        waitForResponse();

        chatInput.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                submitMessage();
            }
        });

    }

    /**
     * This method starts a backwards countdown from the given number of seconds.
     * If the player does not choose a token within the countdown, a random token will be assigned to the player.
     */
    public void startCountdown(int seconds) {
        progressBar.setProgress(1.0);

        Timeline timeline = new Timeline();

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(seconds), new KeyValue(progressBar.progressProperty(), 0));

        timeline.getKeyFrames().add(keyFrame);
        timeline.playFromStart();
    }


    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        //numTokens = controller.getAvailableTokens().size();
        switch (EventID.getByID(eventID)) {
            case CHOOSE_TOKEN_SETUP:
                if (feedback == Feedback.SUCCESS) {
                    System.out.println(message);
                } else {
                    System.out.println(message);
                }
                break;

            case UPDATE_GAME_PLAYERS:
                //TODO
                break;

            case UPDATE_LOCAL_MODEL:
                //TODO
                break;


            case GET_CHAT_MESSAGES:
                String getChatFormattedMessages = message.replace("[1m", " ").replace("[0m","");
                chatArea.appendText(getChatFormattedMessages + "\n");
                break;

            case CHAT_GM, CHAT_PM:
                if (feedback.equals(Feedback.SUCCESS)) {
                    String formattedMessage = message.replace("[1m", " ").replace("[0m","");
                    chatArea.appendText(formattedMessage + "\n");
                } else {
                    System.out.println("Message unable to send!");
                }
                break;

            case EventID.QUIT_GAME:
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

        }
        notifyResponse();
    }



    /**
     * Method to set the name of the lobby so that the player can see the lobby's name.
     * @param lobbyName the name of the lobby
     */
    private void setLobbyName(String lobbyName) {
        this.lobbyName.setText("LOBBY: " + lobbyName);
    }

    private void update(){
        updateTokenOptions();
        updateChatOptions();
        //TODO use this method to update the tokens as well ?
    }

    private void addChoiceSelection(){
        red.setOnMouseClicked(event -> {
            controller.newViewEvent(new ChosenTokenSetupEvent(Token.RED));
            System.out.println("Chosen red token");
        });

        blue.setOnMouseClicked(event -> {
            controller.newViewEvent(new ChosenTokenSetupEvent(Token.BLUE));
            System.out.println("Chosen blue token");
        });

        green.setOnMouseClicked(event -> {
            controller.newViewEvent(new ChosenTokenSetupEvent(Token.GREEN));
            System.out.println("Chosen green token");
        });

        yellow.setOnMouseClicked(event -> {
            controller.newViewEvent(new ChosenTokenSetupEvent(Token.YELLOW));
            System.out.println("Chosen yellow token");
        });
    }


    private void updateTokenOptions(){
        for (ImageView imageView : visibleTokens.values()) {
            imageView.setVisible(false);
        }
        for (Token token : controller.getAvailableTokens()) {
            String color = token.getColor();
            ImageView imageView = visibleTokens.get(color);
            if (imageView != null) {
                imageView.setVisible(true);
            }
        }
    }

    /**
     * This method is used to update the chat options in the game setup view.
     * It iterates over the players' status and updates the radio buttons accordingly.
     */
    private void updateChatOptions(){
        int i = 0;
        generalRadioButton.setSelected(true);
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
     * This method is used to quit the game.
     * It sends a {@link QuitGameEvent} to the server and waits for a response.
     */
    @FXML
    public void quitGame(){
        Event event = new QuitGameEvent();
        controller.newViewEvent(event);
        waitForResponse();
    }

    /**
     * This method is used to submit a chat message.
     * It sends a {@link ChatGMEvent} or {@link ChatPMEvent} to the server based on the selected radio button.
     */
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

    /**
     * This method is used to submit a private message if the selected radio button for chat is a specific user.
     */
    private void sendPrivateMessage(String username, String message){
        if(controller.getPlayersStatus().containsKey(username)) {
            controller.newViewEvent(new ChatPMEvent(new PrivateChatMessage(username, message)));
        }
        else{
            System.out.println("Error: player not in match");
        }
    }

    /**
     * This method is used to submit a private message if the selected radio button for chat is general.
     */
    private void sendPublicMessage(String message){
        controller.newViewEvent(new ChatGMEvent(new ChatMessage(message)));
    }

    /**
     * This method is used to check if the player is in the game.
     * It returns true as the player is in the game during the game setup phase.
     */
    @Override
    public boolean inGame(){
        return true;
    }

    /**
     * This method is used to check if the player is in the chat.
     * It returns true as the player is in the chat during the game setup phase.
     */
    @Override
    public boolean inChat(){
        return true;
    }
}