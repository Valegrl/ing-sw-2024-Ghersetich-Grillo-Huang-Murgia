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
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * This class is responsible for controlling the lobby in the GUI.
 * It extends the FXMLController class and overrides some of its methods.
 * It handles the lobby-related events and updates the lobby view accordingly.
 */
public class LobbyController extends FXMLController {

    /**
     * A list of StackPane objects representing the possible players in the lobby.
     */
    private List<StackPane> playerStackPanes;

    /**
     * A list of Text objects representing the usernames of the players in the lobby.
     */
    private List<Text> usernames;

    /**
     * A list of Button objects representing the buttons associated with each player in the lobby.
     */
    private List<Button> playerButtons;

    /**
     * A list of RadioButton objects representing the radio buttons for chat options in the lobby.
     */
    private List<RadioButton> radioButtons;

    /**
     * A list of Text objects representing the ready status of each player in the lobby.
     */
    private List<Text> readyStatuses;

    @FXML
    public AnchorPane mainAnchor;

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

    @FXML
    public VBox vBox;

    @FXML
    private VBox vBox0;

    @FXML
    private VBox vBox1;

    @FXML
    private VBox vBox2;

    @FXML
    private VBox vBox3;

    @FXML
    private VBox vboxChat;

    @FXML
    private ImageView imageView0;

    @FXML
    private ImageView imageView1;

    @FXML
    private ImageView imageView2;

    @FXML
    private ImageView imageView3;

    /**
     * Default constructor for the LobbyController class.
     */
    public LobbyController(){
        super();
    }


    public void initialize() {
        double maxPercentageVBox = 0.12;
        double relativePercentage = 0.75;

        List<ImageView> imageViews = Arrays.asList(imageView0, imageView1, imageView2, imageView3);
        List<VBox> vBoxes = Arrays.asList(vBox0, vBox1, vBox2, vBox3);

        for (int i = 0; i < imageViews.size(); i++) {
            ImageView imageView = imageViews.get(i);
            VBox vBox = vBoxes.get(i);

            imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() ->
                            vBox.getWidth() * relativePercentage, vBox.widthProperty()
            ));

            imageView.fitHeightProperty().bind(Bindings.createDoubleBinding(() ->
                            vBox.getHeight() * relativePercentage, vBox.heightProperty()
            ));
        }

        List<Text> usernames = Arrays.asList(username0, username1, username2, username3);

        for (int i = 0; i < usernames.size(); i++) {
            Text username = usernames.get(i);
            VBox vBox = vBoxes.get(i);

            username.wrappingWidthProperty().bind(Bindings.createDoubleBinding(() ->
                            vBox.getWidth() * relativePercentage, vBox.widthProperty()
            ));
        }

        double vboxChatPercentage = 0.32;

        vboxChat.prefWidthProperty().bind(Bindings.createDoubleBinding(() ->
                        vBox.getWidth() * vboxChatPercentage, vBox.widthProperty()
        ));

        vboxChat.prefHeightProperty().bind(Bindings.createDoubleBinding(() ->
                        vBox.getHeight() * (1-vboxChatPercentage), vBox.heightProperty()
        ));

        for (VBox vBox : vBoxes) {
            vBox.prefWidthProperty().bind(Bindings.createDoubleBinding(() ->
                            mainAnchor.getWidth() * maxPercentageVBox, mainAnchor.widthProperty()
            ));

            vBox.prefHeightProperty().bind(Bindings.createDoubleBinding(() ->
                            mainAnchor.getHeight() * maxPercentageVBox, mainAnchor.heightProperty()
            ));
        }
    }

    /**
     * This method is called when the lobby view is run.
     * It initializes the lobby view and sets up the necessary event handlers.
     *
     * @param view The view associated with this controller.
     * @param stage The stage on which the lobby view is displayed.
     */
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

        chatInput.setOnAction(actionEvent -> submitMessage());

        updateLobby();
    }

    /**
     * This method handles the response from the server.
     * It updates the lobby view based on the feedback and event ID received.
     * It also receives the messages player write in chat.
     *
     * @param feedback The feedback received from the server.
     * @param message The message received from the server.
     * @param eventID The event ID of the event that triggered the response.
     */
    @FXML
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        switch (EventID.getByID(eventID)) {
            case EventID.PLAYER_READY, PLAYER_UNREADY:
                Platform.runLater(this::updateLobby);
                break;
            case EventID.UPDATE_LOBBY_PLAYERS:
                chatArea.appendText("\n" + message + "\n");
                Platform.runLater(this::updateLobby);
                break;
            case EventID.QUIT_LOBBY:
                if(feedback == Feedback.SUCCESS){
                    Platform.runLater(() -> {
                        try {
                           switchScreen("EnterLobbiesMenu");
                        }
                        catch (IOException exception){
                            throw new RuntimeException("FXML Exception: failed to load enter EnterLobbiesMenu", exception);
                        }
                    });
                }
                else{
                    Platform.runLater(() -> chatArea.appendText("Something bad happened, you can't quit!"));
                }
                break;
            case EventID.KICKED_PLAYER_FROM_LOBBY:
                Platform.runLater(() -> {
                    try {
                        switchScreen("EnterLobbiesMenu");
                    }
                    catch (IOException exception){
                        throw new RuntimeException("FXML Exception: failed to load enter EnterLobbiesMenu", exception);
                    }
                });
                break;
            case EventID.GET_CHAT_MESSAGES:
                String getChatFormattedMessages = message.replace("[1m", " ").replace("[0m","");
                Platform.runLater(() ->chatArea.appendText(getChatFormattedMessages + "\n"));
                break;
            case EventID.CHAT_GM, CHAT_PM:
                if (feedback.equals(Feedback.SUCCESS)) {
                    String formattedMessage = message.replace("[1m", " ").replace("[0m","");
                    Platform.runLater(() ->chatArea.appendText(formattedMessage + "\n"));
                } else {
                    Platform.runLater(() ->chatArea.appendText("Message unable to send!\n"));
                }
                break;

            case EventID.UPDATE_GAME_PLAYERS:
                BackgroundGameSetupController nextController = new BackgroundGameSetupController();
                transition(nextController);
                break;
        }
        notifyResponse();
    }

    /**
     * This method is invoked in response to any updates received from the server pertaining to the lobby.
     * The updates may include events such as a player joining or leaving the lobby, a player being kicked from the lobby,
     * or a change in the ready status of a player. The method is responsible for processing these updates and
     * reflecting the changes in the lobby accordingly to the user's role.
     */
    private void updateLobby(){
        if (controller.isLobbyLeader()) {
            lobbyLeaderUpdate();
        }
        else if (!controller.isLobbyLeader()){
            lobbyUserUpdate();
        }
    }

    /**
     * This method is executed by the {@code updateLobby} method when the current user is identified as the lobby administrator.
     * Being the lobby administrator, the user possesses the authority to kick other users from the lobby.
     */
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
                    readyStatuses.get(i).setText("Not ready");
                }

                playerStackPanes.get(i).setVisible(true);
                playerStackPanes.get(i).setEffect(highlight);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(true);
                playerButtons.get(i).setText("Quit");
                playerButtons.get(i).setOnAction(actionEvent -> quitLobby());
            }
            else{

                if(controller.getPlayersStatus().get(user)){
                    readyStatuses.get(i).setText("Ready");
                }
                else{
                    readyStatuses.get(i).setText("Not ready");
                }


                playerStackPanes.get(i).setVisible(true);
                playerStackPanes.get(i).setEffect(null);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(true);
                playerButtons.get(i).setText("Kick");
                playerButtons.get(i).setOnAction(actionEvent -> kickPlayer(user));
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

    /**
     * This method is executed by the {@code updateLobby} method when the current user is identified as a lobby user.
     * Unlike the lobby administrator, the user does not possess the authority to kick other users from the lobby.
     */
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
                    readyStatuses.get(i).setText("Not ready");
                }


                playerStackPanes.get(i).setVisible(true);
                playerStackPanes.get(i).setEffect(highlight);
                usernames.get(i).setText(user);
                playerButtons.get(i).setVisible(true);
                playerButtons.get(i).setText("Quit");
                playerButtons.get(i).setOnAction(actionEvent -> quitLobby());
            }
            else{

                if(controller.getPlayersStatus().get(user)){
                    readyStatuses.get(i).setText("Ready");
                }
                else{
                    readyStatuses.get(i).setText("Not ready");
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

    /**
     * This method is executed by the {@code updateLobby} method. It updates the radio buttons for the chat.
     * Hiding the options for users who are no longer in the lobby and showing the ones for user who just joined.
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
     * Method to set the name of the lobby so that the player can see the lobby's name.
     *
     * @param lobbyName the name of the lobby
     */
    public void setLobbyName(String lobbyName) {
        this.lobbyName.setText("LOBBY: " + lobbyName);
    }


    /**
     * This method is used to quit the lobby.
     * It sends a {@link QuitLobbyEvent} to the server.
     */
    private void quitLobby() {
        Event event = new QuitLobbyEvent();
        controller.newViewEvent(event);
    }

    /**
     * This method is used to kick a player from the lobby.
     * It sends a {@link KickFromLobbyEvent} to the server.
     * This method can only be used by lobby admins.
     *
     * @param userToKick The username of the player to be kicked.
     */
    private void kickPlayer(String userToKick) {
        if (controller.getPlayersStatus().size() > 1) {
            Event event = new KickFromLobbyEvent(userToKick);
            controller.newViewEvent(event);
        }
        else{
            System.out.println("Nobody here but us chickens");
        }
    }

    /**
     * This method is used to submit a chat message.
     * It sends a {@link ChatGMEvent} or {@link ChatPMEvent} to the server based on the selected radio button.
     */
    @FXML
    public void submitMessage(){
        String message = chatInput.getText();

        if(message.isEmpty()){
            return;
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
     * This method is used to toggle the ready status of the player.
     * It sends a {@link PlayerReadyEvent} or {@link PlayerUnreadyEvent} to the server based on the current ready status.
     */
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
    }

    /**
     * This method states that the player is in a lobby.
     *
     */
    @Override
    public boolean inLobby(){
        return true;
    }

    /**
     * This method states that the player is in a chat.
     *
     */
    @Override
    public boolean inChat(){
        return true;
    }

}

