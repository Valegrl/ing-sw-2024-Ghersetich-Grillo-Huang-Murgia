package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.view.gui.controller.MenuController;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

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
     * Notifies all waiting threads of a response.
     */
    public void notifyResponse() {
        synchronized (viewLock) {
            viewLock.notifyAll();
        }
    }

    /**
     * The {@code switchScreen} method is used to switch the current screen to a new one.
     * It loads the FXML file of the new screen, gets the controller of the new screen, and sets the root of the current scene to the root of the new screen.
     * It then transitions to the new controller.
     *
     * @param FXML The name of the FXML file of the new screen.
     * @throws IOException If an input or output exception occurred.
     */
    public void switchScreen(String FXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/" + FXML + ".fxml"));
        Parent root = loader.load();
        FXMLController nextController = loader.getController();

        Scene scene = stage.getScene();
        scene.setRoot(root);
        transition(nextController);
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
        stackPane.setMaxWidth(116);
        stackPane.setMinWidth(116);
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

    public int calculateGridLength(Map<Coordinate, ImmPlayableCard> playedCards){
        int maxX, minX;
        maxX = 0;
        minX = 0;
        for(Coordinate coordinate : playedCards.keySet()){
            int currX = coordinate.getX();
            if(currX > maxX){
                maxX = currX;
            }
            else if(currX < minX){
                minX = currX;
            }
        }
        return (maxX + 1) - (minX - 1);
    }

    public int calculateGridHeight(Map<Coordinate, ImmPlayableCard> playedCards){
        int maxY, minY;
        maxY = 0;
        minY = 0;
        for(Coordinate coordinate : playedCards.keySet()){
            int currY = coordinate.getY();
            if(currY > maxY){
                maxY = currY;
            }
            else if(currY < minY){
                minY = currY;
            }
        }
        return (maxY + 1) - (minY - 1);
    }

    public int calculateMinX(Map<Coordinate, ImmPlayableCard> playedCards){
        int minX;
        minX = 0;
        for(Coordinate coordinate : playedCards.keySet()){
            int currX = coordinate.getX();
            if(currX < minX){
                minX = currX;
            }
        }
        return minX;
    }

    public int calculateMaxY(Map<Coordinate, ImmPlayableCard> playedCards){
        int maxY;
        maxY = 0;
        for(Coordinate coordinate : playedCards.keySet()){
            int currY = coordinate.getY();
            if(currY > maxY){
                maxY = currY;
            }
        }
        return maxY;
    }


}
