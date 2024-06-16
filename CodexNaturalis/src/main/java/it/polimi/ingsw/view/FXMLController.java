package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.view.controller.ViewController;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Abstract class FXMLController.
 * This class provides a structure for controllers used in the FXML views.
 */
public abstract class FXMLController {

    /**
     * The stage in which the FXML is shown.
     */
    protected Stage stage;

    /**
     * The view associated with the controller.
     */
    protected View view;

    /**
     * The controller for handling events.
     */
    protected ViewController controller;

    /**
     * An object for thread synchronization
     */
    protected final Object viewLock = new Object();

    /**
     * Default constructor for FXMLController. FXMLLoader only accepts parameter-less constructors.
     */
    public FXMLController() {
    }

    /**
     * Transitions to the next controller.
     * @param nextController The next FXMLController
     */
    public void transition(FXMLController nextController) {
        view.setFXMLController(nextController);
        //TODO debug to see if it's needed
        //this.controller = view.getController(); not needed I think
        nextController.run(view, stage);
    }

    /**
     * Abstract method to run the controller. Usually used right after switching to a new scene.
     * @param view The view associated with this controller
     * @param stage The stage in which the FXML view is shown
     */
    abstract public void run(View view, Stage stage);

    /**
     * Abstract method to handle responses.
     * @param feedback The feedback from the view
     * @param message The message associated with the feedback
     * @param eventID The ID of the event
     */
    abstract public void handleResponse(Feedback feedback, String message, String eventID);

    public boolean inMenu() {return false;};

    public boolean inLobby() {return false;};

    public boolean inGame() {return false;};

    public boolean inChat() {return false;};

    /**
     * Waits for a response from the view.
     * This method blocks the thread until a response is received.
     */
    protected void waitForResponse() {
        synchronized (viewLock) {
            try {
                viewLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Shows a response message.
     * @param message The message to be shown
     * @param sleepTime The time to wait before showing the message
     */
    public void showResponseMessage(String message, int sleepTime) {
        //TODO don't print, use the texts in the FXML
        view.printMessage(message);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ignored) {}
    }

    /**
     * Notifies all waiting threads of a response.
     */
    public void notifyResponse() {
        synchronized (viewLock) {
            viewLock.notifyAll();
        }
    }

    /**
     * This method is used to get the back image of a card based on the item and card type.
     * It constructs the path of the image file based on the item and card type and creates an Image object.
     *
     * @param item The item of the card (FUNGI, ANIMAL, INSECT, PLANT).
     * @param type The type of the card (GOLD or RESOURCE).
     * @return An Image object representing the back of the card.
     */
    public Image getBackCardImage(Item item, CardType type){
        Image image = null;
        if(type == CardType.GOLD){
            switch (item){
                case FUNGI :
                    image = new Image("it/polimi/ingsw/images/cards/playable/gold/back/FB.png");
                    break;
                case ANIMAL:
                    image = new Image("it/polimi/ingsw/images/cards/playable/gold/back/AB.png");
                    break;
                case INSECT:
                    image = new Image("it/polimi/ingsw/images/cards/playable/gold/back/IB.png");
                    break;
                case PLANT:
                    image = new Image("it/polimi/ingsw/images/cards/playable/gold/back/PB.png");
                    break;
            }
        }
        else{
            switch (item){
                case FUNGI :
                    image = new Image("it/polimi/ingsw/images/cards/playable/resource/back/FB.png");
                    break;
                case ANIMAL:
                    image = new Image("it/polimi/ingsw/images/cards/playable/resource/back/AB.png");
                    break;
                case INSECT:
                    image = new Image("it/polimi/ingsw/images/cards/playable/resource/back/IB.png");
                    break;
                case PLANT:
                    image = new Image("it/polimi/ingsw/images/cards/playable/resource/back/PB.png");
                    break;
            }
        }
        return image;
    }

    /**
     * This method is used to get the front image of a card based on the ID and card type.
     * It constructs the path of the image file based on the ID and card type and creates an Image object.
     *
     * @param ID The ID of the card (RC01, RC02, GC01, GC02 etc...).
     * @param type The type of the card (GOLD or RESOURCE).
     * @return An Image object representing the front of the card.
     */
    public Image getFrontCardImage(String ID, CardType type){
        Image image = null;
        if(type == CardType.GOLD){
            image = new Image("it/polimi/ingsw/images/cards/playable/gold/front/" + ID + ".png");
        }
        else{
            image = new Image("it/polimi/ingsw/images/cards/playable/resource/front/" + ID + ".png");
        }
        return image;
    }

    /**
     * This method returns an Image object based on the provided Token.
     * It uses a switch statement to determine which image to return based on the Token.
     * If the Token is RED, it returns the image for the red token.
     * If the Token is BLUE, it returns the image for the blue token.
     * If the Token is GREEN, it returns the image for the green token.
     * If the Token is YELLOW, it returns the image for the yellow token.
     * If the Token is not one of the above, it returns null.
     *
     * @param token The Token to determine which image to return.
     * @return The Image object for the provided Token, or null if the Token is not one of the specified cases.
     */
    public Image getTokenImage(Token token){
        return switch (token) {
            case RED -> new Image("/it/polimi/ingsw/images/icons/RedToken.png");
            case BLUE -> new Image("/it/polimi/ingsw/images/icons/BlueToken.png");
            case GREEN -> new Image("/it/polimi/ingsw/images/icons/GreenToken.png");
            case YELLOW -> new Image("/it/polimi/ingsw/images/icons/YellowToken.png");
            default -> null;
        };
    }

    /**
     * This method creates a StackPane with an ImageView as its child.
     * The ImageView is set with the provided Image.
     * The StackPane is set with a fixed width of 113 and a fixed height of 60.
     * The ImageView is set with a fit height of 100 and a fit width of 150, and its pickOnBounds and preserveRatio properties are set to true.
     * The ImageView is then added to the StackPane's children.
     *
     * @param image The Image to be set on the ImageView.
     * @return The created StackPane with the ImageView as its child.
     */
    public StackPane stackPaneBuilder(Image image){
        //Border border = new Border(new BorderStroke(javafx.scene.paint.Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10)));

        StackPane stackPane = new StackPane();
        stackPane.setMaxWidth(113);
        stackPane.setMinWidth(113);
        stackPane.setMaxHeight(60);
        stackPane.setMinHeight(60);

        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(150);
        imageView.setImage(image);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        stackPane.getChildren().add(imageView);

        return stackPane;
    }

}
