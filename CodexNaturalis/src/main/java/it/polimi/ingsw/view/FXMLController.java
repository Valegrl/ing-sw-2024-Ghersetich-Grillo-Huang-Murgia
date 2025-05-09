package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.model.card.CardType;
import it.polimi.ingsw.model.card.Item;
import it.polimi.ingsw.model.player.Token;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Map;

/**
 * Abstract class FXMLController.
 * This class provides a structure for controllers used in the FXML views of the GUI.
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
     * This is the width that will be used when displaying a card in InGame and EndedGame.
     */
    private final int wCardRes = 300;

    /**
     * This is the height that will be used when displaying a card in InGame and EndedGame.
     */
    private final int hCardRes = 150;

    /**
     * This is the height that will be used when displaying a token in InGame and EndedGame.
     */
    private final int tokenW = 100;

    /**
     * Default constructor for FXMLController. FXMLLoader only accepts parameter-less constructors.
     */
    public FXMLController() {
    }

    /**
     * Transitions to the next controller for the GUI.
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
     * Abstract method to handle responses from the server.
     * @param feedback The feedback from the view
     * @param message The message associated with the feedback
     * @param eventID The ID of the event
     */
    abstract public void handleResponse(Feedback feedback, String message, String eventID);

    /**
     * Checks if the application is currently in the menu state.
     * @return false as this is an abstract implementation. The actual implementation should be in the subclass.
     */
    public boolean inMenu() {return false;}

    /**
     * Checks if the application is currently in the lobby state.
     * @return false as this is an abstract implementation. The actual implementation should be in the subclass.
     */
    public boolean inLobby() {return false;}

    /**
     * Checks if the application is currently in the game state.
     * @return false as this is an abstract implementation. The actual implementation should be in the subclass.
     */
    public boolean inGame() {return false;}

    /**
     * Checks if the application is currently in the chat state.
     * @return false as this is an abstract implementation. The actual implementation should be in the subclass.
     */
    public boolean inChat() {return false;}

    /**
     * The {@code switchScreen} method is used to switch the current screen to a new one.
     * It loads the FXML file of the new screen, gets the controller of the new screen, and sets the root of the current scene to the root of the new screen.
     * It then transitions to the new controller for the GUI.
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
            image = switch (item) {
                case FUNGI ->
                        new Image("it/polimi/ingsw/images/cards/playable/gold/back/FB.png", getWCardRes(), getHCardRes(), true, true);
                case ANIMAL ->
                        new Image("it/polimi/ingsw/images/cards/playable/gold/back/AB.png", getWCardRes(), getHCardRes(), true, true);
                case INSECT ->
                        new Image("it/polimi/ingsw/images/cards/playable/gold/back/IB.png", getWCardRes(), getHCardRes(), true, true);
                case PLANT ->
                        new Image("it/polimi/ingsw/images/cards/playable/gold/back/PB.png", getWCardRes(), getHCardRes(), true, true);
                default -> image;
            };
        }
        else{
            image = switch (item) {
                case FUNGI ->
                        new Image("it/polimi/ingsw/images/cards/playable/resource/back/FB.png", getWCardRes(), getHCardRes(), true, true);
                case ANIMAL ->
                        new Image("it/polimi/ingsw/images/cards/playable/resource/back/AB.png", getWCardRes(), getHCardRes(), true, true);
                case INSECT ->
                        new Image("it/polimi/ingsw/images/cards/playable/resource/back/IB.png", getWCardRes(), getHCardRes(), true, true);
                case PLANT ->
                        new Image("it/polimi/ingsw/images/cards/playable/resource/back/PB.png", getWCardRes(), getHCardRes(), true, true);
                default -> image;
            };
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
        Image image;
        if(type == CardType.GOLD){
            image = new Image("it/polimi/ingsw/images/cards/playable/gold/front/" + ID + ".png", getWCardRes(), getHCardRes(), true, true);
        }
        else{
            image = new Image("it/polimi/ingsw/images/cards/playable/resource/front/" + ID + ".png", getWCardRes(), getHCardRes(), true, true);
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
            case RED -> new Image("/it/polimi/ingsw/images/icons/RedToken.png", tokenW, 0, true, true);
            case BLUE -> new Image("/it/polimi/ingsw/images/icons/BlueToken.png", tokenW, 0, true, true);
            case GREEN -> new Image("/it/polimi/ingsw/images/icons/GreenToken.png", tokenW, 0, true, true);
            case YELLOW -> new Image("/it/polimi/ingsw/images/icons/YellowToken.png", tokenW, 0, true, true);
        };
    }

    /**
     * This method returns an Image object of the black token.
     *
     */
    public Image getBlackToken() {
        return new Image("/it/polimi/ingsw/images/icons/BlackToken.png", tokenW, 0, true, true);
    }

    /**
     * This method creates a StackPane with an ImageView as its child.
     * The ImageView is set with the provided Image.
     * The ImageView is then added to the StackPane's children.
     *
     * @param image The Image to be set on the ImageView.
     * @return The created StackPane with the ImageView as its child.
     */
    public StackPane stackPaneBuilder(Image image){
        //Border border = new Border(new BorderStroke(javafx.scene.paint.Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(10)));

        double relativePercentageW = 0.07;
        double relativeStackW = 0.77;
        double relativeStackH = 0.58;

        ImageView imageView = new ImageView();
        imageView.fitWidthProperty().bind(Bindings.createDoubleBinding(() ->
                stage.getWidth() * relativePercentageW, stage.widthProperty()
        ));
        imageView.setImage(image);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        StackPane stackPane = new StackPane();

        DoubleBinding imageViewWidthBinding = Bindings.createDoubleBinding(
                () -> imageView.getLayoutBounds().getWidth(),
                imageView.layoutBoundsProperty()
        );
        stackPane.maxWidthProperty().bind(imageViewWidthBinding.multiply(relativeStackW));
        stackPane.minWidthProperty().bind(imageViewWidthBinding.multiply(relativeStackW));

        DoubleBinding imageViewHeightBinding = Bindings.createDoubleBinding(
                () -> imageView.getLayoutBounds().getHeight(),
                imageView.layoutBoundsProperty()
        );
        stackPane.maxHeightProperty().bind(imageViewHeightBinding.multiply(relativeStackH));
        stackPane.minHeightProperty().bind(imageViewHeightBinding.multiply(relativeStackH));

        stackPane.getChildren().add(imageView);

        return stackPane;
    }

    /**
     * Calculates the length of the grid based on the coordinates of the played cards.
     * The length is determined by finding the maximum and minimum X coordinates among the played cards,
     * and then subtracting the minimum from the maximum and adding 2.
     *
     * @param playedCards A map of the coordinates and the corresponding played cards.
     * @return The length of the grid.
     */
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

    /**
     * Calculates the height of the grid based on the coordinates of the played cards.
     * The height is determined by finding the maximum and minimum Y coordinates among the played cards,
     * and then subtracting the minimum from the maximum and adding 2.
     *
     * @param playedCards A map of the coordinates and the corresponding played cards.
     * @return The height of the grid.
     */
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

    /**
     * Calculates the minimum X coordinate among the played cards.
     * The minimum X coordinate is determined by iterating over the coordinates of the played cards
     * and updating the minimum X coordinate whenever a smaller one is found.
     *
     * @param playedCards A map of the coordinates and the corresponding played cards.
     * @return The minimum X coordinate among the played cards.
     */
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

    /**
     * Calculates the maximum Y coordinate among the played cards.
     * The maximum Y coordinate is determined by iterating over the coordinates of the played cards
     * and updating the maximum Y coordinate whenever a larger one is found.
     *
     * @param playedCards A map of the coordinates and the corresponding played cards.
     * @return The maximum Y coordinate among the played cards.
     */
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

    /**
     * @return The width resolution that will be used when displaying a card in the GUI.
     */
    public int getWCardRes() {
        return wCardRes;
    }

    /**
     * @return The height resolution that will be used when displaying a card in the GUI.
     */
    public int getHCardRes() {
        return hCardRes;
    }
}
