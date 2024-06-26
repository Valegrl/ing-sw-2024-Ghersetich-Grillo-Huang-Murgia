package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Pagination;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The RulesController class is responsible for managing the rules view in the GUI.
 * It extends the FXMLController class and overrides several of its methods.
 * This class is responsible for displaying the rules of the game to the user.
 * It contains methods to initialize the view, switch the rule pages and go back to the main menu.
 * The class also contains a constructor that initializes an array of Image objects with the images of the rules.
 */
public class RulesController extends FXMLController {

    @FXML
    private ImageView imageView;

    @FXML
    private Pagination pagination;

    @FXML
    private VBox vBox;

    private final Image[] images = new Image[6];

    /**
     * This method is used to initialize the ImageView dimensions in the GUI.
     * It sets the ImageView's width and height properties to match the VBox's width and height properties.
     */
    public void initialize() {
        imageView.fitWidthProperty().bind(vBox.widthProperty());
        imageView.fitHeightProperty().bind(vBox.heightProperty());
    }

    /**
     * The constructor for the RulesController class.
     * It initializes an array of Image objects with the images of the rules.
     * The images are loaded from the specified paths.
     */
    public RulesController() {
        super();
        images[0] = new Image("/it/polimi/ingsw/images/rules/rules1.png");
        images[1] = new Image("/it/polimi/ingsw/images/rules/rules2.png");
        images[2] = new Image("/it/polimi/ingsw/images/rules/rules3.png");
        images[3] = new Image("/it/polimi/ingsw/images/rules/rules4.png");
        images[4] = new Image("/it/polimi/ingsw/images/rules/rules5.png");
        images[5] = new Image("/it/polimi/ingsw/images/rules/rules6.png");
    }

    /**
     * This method is used to initialize the RulesController with the given View and Stage.
     * It sets the view, stage, and controller properties of this class.
     * It also sets the initial image of the imageView and the page factory of the pagination.
     * An event listener is added to the currentPageIndexProperty of the pagination to update the image of the imageView when the page changes.
     *
     * @param view  The View object to be associated with this controller.
     * @param stage The Stage object to be associated with this controller.
     */
    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        imageView.setImage(images[0]);
        pagination.setPageFactory(this::createPage);
        pagination.currentPageIndexProperty().addListener((obs, oldVal, newVal) ->
                imageView.setImage(images[newVal.intValue()]));
    }

    /**
     * This method is used to create a new page for the Pagination control.
     * It currently returns a new VBox object for each page.
     *
     * @param pageIndex The index of the page to be created.
     * @return a new VBox object.
     */
    private Node createPage(int pageIndex) {
        return new VBox();
    }

    /**
     * This method is used to handle the response from the server.
     * Currently, it is not used in this controller.
     *
     * @param feedback The feedback from the server.
     * @param message  The message from the server.
     * @param eventID  The ID of the event.
     */
    @Override
    public void handleResponse(Feedback feedback, String message, String eventID) {
        // Not used
    }

    /**
     * This method is used to check if the user is in the menu.
     * In this controller, it always returns true.
     *
     * @return true since the user is in the menu when this controller is active.
     */
    @Override
    public boolean inMenu() {
        return true;
    }

    /**
     * This method is used to handle the action of going back to the main menu.
     * It tries to switch the screen to the main menu.
     * If there is an IOException when switching the screen, it throws a RuntimeException.
     */
    @FXML
    public void goBackMain() {
        try {
            switchScreen("MainMenu");
        } catch (IOException exception) {
            throw new RuntimeException("FXML Exception: failed to load MainMenu", exception);
        }
    }
}

