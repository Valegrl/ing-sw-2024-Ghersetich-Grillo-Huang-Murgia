package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.ChatGMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.ChatPMEvent;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.game.ChosenTokenSetupEvent;
import it.polimi.ingsw.eventUtils.event.fromView.game.QuitGameEvent;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.local.GetChatMessagesEvent;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.ChatMessage;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.utils.Pair;
import it.polimi.ingsw.utils.PrivateChatMessage;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the controller for the Token Setup phase of the game.
 * It handles the user interactions during the token selection and game setup phase.
 * It extends the FXMLController class and overrides its methods to provide the specific functionality needed for this phase.
 */
public class TokenSetupController extends FXMLController {

    @FXML
    private VBox vboxChat;

    @FXML
    private VBox vBox;

    @FXML
    private Text lobbyName;

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
    private Label chooseLabel;

    private List<RadioButton> radioButtons;

    private Map<String, ImageView> visibleTokens;

    /**
     * This boolean is used if the player has chosen a token, they can't choose a token again.
     */
    boolean tokenChosen;

    /**
     * This method is used to initialize the TokenSetupController.
     * It sets the width and height properties of the ImageView objects for the tokens and the VBox for the chat.
     * The width and height of the ImageView objects are set to be a certain percentage of the width and height of the VBox.
     * The maximum width and height of the VBox for the chat are also set to be a certain percentage of the width and height of the VBox.
     */
    public void initialize() {
        double relativePercentage = 0.15;

        List<ImageView> imageViews = Arrays.asList(red, blue, green, yellow);

        for (ImageView imageView : imageViews) {
            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() ->
                    vBox.getWidth() * relativePercentage, vBox.widthProperty()
            ));

            imageView.fitHeightProperty().bind(Bindings.createDoubleBinding(() ->
                    vBox.getHeight() * relativePercentage, vBox.heightProperty()
            ));
        }

        double vboxChatPercentage = 0.32;

        vboxChat.maxWidthProperty().bind(Bindings.createDoubleBinding(() ->
                vBox.getWidth() * vboxChatPercentage, vBox.widthProperty()
        ));

        vboxChat.maxHeightProperty().bind(Bindings.createDoubleBinding(() ->
                vBox.getHeight() * (1 - vboxChatPercentage), vBox.heightProperty()
        ));
    }

    /**
     * This is the constructor for the TokenSetupController class.
     * It calls the superclass constructor.
     */
    public TokenSetupController() {
        super();
    }

    /**
     * Initializes the controller and sets up the user interface for the token setup phase.
     * This method is called when the token setup view is loaded.
     *
     * @param view  The view instance associated with this controller.
     * @param stage The stage on which the token setup scene is shown.
     */
    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();
        controller.setInSetup(new Pair<>(true, true));

        setLobbyName(controller.getLobbyId());
        radioButtons = Arrays.asList(radioButton1, radioButton2, radioButton3);

        visibleTokens = new LinkedHashMap<>() {{
            put("red", red);
            put("blue", blue);
            put("green", green);
            put("yellow", yellow);
        }};

        startCountdown(JsonConfig.getInstance().getSetupTokensTimerMs() / 1000);
        update();
        addChoiceSelection();
        addHoverEffect();
        controller.newViewEvent(new GetChatMessagesEvent());
        chatInput.setOnAction(actionEvent -> submitMessage());
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

    /**
     * This method handles the response from the server based on the event ID and feedback received.
     * It updates the user interface and game state accordingly, it handles most importantly the following events:
     * ChooseTokenSetupEvent is used to let the users in game that someone has chosen their token.
     * ChosenTokenSetupEvent is used to let the user know their token was chosen successfully.
     * UpdateGamePlayersEvent is used to let the users know the status regarding the game, such as a user quitting or being disconnected.
     * UpdateLocalModel is used to update the token setup and move to the next screen which is the in game screen.
     *
     * @param feedback The feedback received from the server. It can be SUCCESS or FAILURE.
     * @param message  The message received from the server. It can be an informational message or an error message.
     * @param eventID  The ID of the event that triggered the response. It is used to determine the appropriate action to take.
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case CHOOSE_TOKEN_SETUP:
                Platform.runLater(() -> {
                    chatArea.appendText("\n" + message + "\n");
                    update();
                });
                break;

            case CHOSEN_TOKEN_SETUP:
                if (feedback == Feedback.SUCCESS) {
                    tokenChosen = true;
                    Platform.runLater(() -> {
                        for (ImageView imageView : visibleTokens.values()) {
                            imageView.setVisible(false);
                        }
                        quitButton.setVisible(false);
                        chooseLabel.setText("TOKEN CHOSEN");
                        chatArea.appendText("\n" + message + "\n");
                    });
                }
                break;

            case UPDATE_GAME_PLAYERS:
                Platform.runLater(() -> chatArea.appendText("\n" + message + "\n"));
                break;

            case UPDATE_LOCAL_MODEL:
                Platform.runLater(() -> {
                    try {
                        switchScreen("InGame");
                    } catch (IOException exception) {
                        throw new RuntimeException("FXML Exception: failed to load InGame", exception);
                    }
                });
                break;

            case GET_CHAT_MESSAGES:
                String getChatFormattedMessages = message.replace("[1m", " ").replace("[0m", "");
                Platform.runLater(() -> chatArea.appendText(getChatFormattedMessages + "\n"));
                break;

            case CHAT_GM, CHAT_PM:
                String formattedMessage = message.replace("[1m", " ").replace("[0m", "");
                if (feedback.equals(Feedback.SUCCESS)) {
                    Platform.runLater(() -> chatArea.appendText(formattedMessage + "\n"));
                } else {
                    Platform.runLater(() -> chatArea.appendText(message));
                }
                break;

            case EventID.QUIT_GAME:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            switchScreen("EnterLobbiesMenu");
                        } catch (IOException exception) {
                            throw new RuntimeException("FXML Exception: failed to load EnterLobbiesMenu", exception);
                        }
                    });
                } else {
                    Platform.runLater(() -> chatArea.appendText(message));
                }
                break;
        }
    }

    /**
     * Method to set the name of the lobby so that the player can see the lobby's name.
     *
     * @param lobbyName the name of the lobby
     */
    private void setLobbyName(String lobbyName) {
        this.lobbyName.setText("GAME: " + lobbyName);
    }

    /**
     * Updates the token options and chat options in the game setup view.
     * This method is called whenever there is a need to refresh the UI, such as after receiving a response from the server.
     * It calls the {@code updateTokenOptions} method to update the available tokens for selection and the {@code updateChatOptions} method to update the available chat options.
     */
    private void update() {
        updateTokenOptions();
        updateChatOptions();
    }

    /**
     * Adds a hover effect to the token images in the game setup view.
     * This method is called during the initialization of the view.
     * The method creates a DropShadow effect and applies it to the token images when the mouse cursor enters the image.
     * When the mouse cursor exits the image, the effect is removed.
     * This gives a visual feedback to the user that the token image is selectable.
     */
    private void addHoverEffect() {
        DropShadow highlight = new DropShadow();
        highlight.setColor(Color.WHITE);
        highlight.setRadius(10.0);
        highlight.setOffsetX(0);
        highlight.setOffsetY(0);
        for (ImageView imageView : visibleTokens.values()) {
            imageView.setOnMouseEntered(event -> imageView.setEffect(highlight));

            imageView.setOnMouseExited(event -> imageView.setEffect(null));
        }
    }

    /**
     * Adds a mouse click event to each token image in the game setup view.
     * This method is called during the initialization of the view.
     * When a token image is clicked, a new ChosenTokenSetupEvent is created with the corresponding token color and sent to the controller.
     * The controller then handles the event and updates the game state accordingly.
     */
    private void addChoiceSelection() {
        red.setOnMouseClicked(event -> controller.newViewEvent(new ChosenTokenSetupEvent(Token.RED)));
        blue.setOnMouseClicked(event -> controller.newViewEvent(new ChosenTokenSetupEvent(Token.BLUE)));
        green.setOnMouseClicked(event -> controller.newViewEvent(new ChosenTokenSetupEvent(Token.GREEN)));
        yellow.setOnMouseClicked(event -> controller.newViewEvent(new ChosenTokenSetupEvent(Token.YELLOW)));
    }

    /**
     * Updates the visibility of token images in the game setup view.
     * This method is called during the UI update process.
     * If a token has not been chosen yet, it performs the following operations:
     * - Sets all token images to be invisible.
     * - Iterates over the available tokens from the controller.
     * - For each available token, it retrieves the corresponding image view from the visibleTokens map using the token color as the key.
     * - Sets the image view of the available token to be visible.
     * If a token has been chosen, it does not perform any operations.
     */
    private void updateTokenOptions() {
        if (!tokenChosen) {
            for (ImageView imageView : visibleTokens.values()) {
                imageView.setVisible(false);
            }
            for (Token token : controller.getAvailableTokens()) {
                String color = token.getColor();
                ImageView imageView = visibleTokens.get(color);
                imageView.setVisible(true);
            }
        }
    }

    /**
     * This method is used to update the chat options in the game setup view.
     * It iterates over the players' status and updates the radio buttons accordingly.
     */
    private void updateChatOptions() {
        int i = 0;
        generalRadioButton.setSelected(true);
        for (String user : controller.getPlayersStatus().keySet()) {
            if (!controller.getUsername().equals(user)) {
                radioButtons.get(i).setVisible(true);
                radioButtons.get(i).setText(user);
                i++;
            }
        }
        while (i < radioButtons.size()) {
            radioButtons.get(i).setVisible(false);
            radioButtons.get(i).setText("");
            i++;
        }
    }

    /**
     * This method is used to quit the game.
     * It sends a {@link QuitGameEvent} to the server.
     */
    @FXML
    public void quitGame() {
        Event event = new QuitGameEvent();
        controller.newViewEvent(event);
    }

    /**
     * This method is used to submit a chat message.
     * It sends a {@link ChatGMEvent} or {@link ChatPMEvent} to the server based on the selected radio button.
     */
    @FXML
    public void submitMessage() {
        String message = chatInput.getText();

        if (message.isEmpty()) {
            return;
        } else {
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
    private void sendPrivateMessage(String username, String message) {
        if (controller.getPlayersStatus().containsKey(username)) {
            controller.newViewEvent(new ChatPMEvent(new PrivateChatMessage(username, message)));
        } else {
            System.out.println("Error: player not in match");
        }
    }

    /**
     * This method is used to submit a private message if the selected radio button for chat is general.
     */
    private void sendPublicMessage(String message) {
        controller.newViewEvent(new ChatGMEvent(new ChatMessage(message)));
    }

    /**
     * This method is used to check if the player is in the game.
     * It returns true as the player is in the game during the game setup phase.
     */
    @Override
    public boolean inGame() {
        return true;
    }

    /**
     * This method is used to check if the player is in the chat.
     * It returns true as the player is in the chat during the game setup phase.
     */
    @Override
    public boolean inChat() {
        return true;
    }
}