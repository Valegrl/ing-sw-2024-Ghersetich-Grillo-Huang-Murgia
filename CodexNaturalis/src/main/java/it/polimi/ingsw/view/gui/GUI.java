package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.main.MainClient;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.controller.ViewController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that represents the Graphical User Interface of the application.
 */
public class GUI extends Application implements View {

    /**
     * The main stage of the application where all scenes are displayed.
     */
    private Stage stage;

    /**
     * The {@link ExecutorService} used to manage the handling of responses from server.
     */
    private ExecutorService executor;

    /**
     * The username of the user who is using this instance of GUI.
     */
    private String username;

    /**
     * The {@link ViewController} this GUI is associated with which is used for handling view events.
     */
    private ViewController controller;

    /**
     * The current {@link FXMLController} for managing the current FXML scene.
     * I.E if the user is in the main menu, this parameter is going to be
     * {@link it.polimi.ingsw.view.gui.controller.MainMenuController}
     */
    private FXMLController FXMLController;

    /**
     * GUI class extends Application and implements View.
     * It represents the graphical user interface of the application.
     * Initializes the Scanner, PrintStream, ViewController, and ExecutorService.
     */
    public GUI() {
        this.controller = new ViewController(this);
        this.executor = Executors.newCachedThreadPool();
    }


    /**
     * Method to launch the JavaFX application.
     * It's called by the {@link MainClient} in the main method.
     */
    public void run(){
        launch();
    }

    /**
     * Method to start the JavaFX application.
     * It's only used in the initialization of the GUI.
     * Loads the fonts, sets up the FXMLLoader, sets up the scene and the stage.
     * @param stage The main stage of the application
     * @throws IOException If there is an error loading the FXML file
     * @throws NullPointerException If the FXML file is not found
     */
    @Override
    public void start(Stage stage) throws IOException, NullPointerException{
        /*This only has UPPERCASE letters*/
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/font/BebasNeue-Regular.ttf"), 14);
        /*This has both UPPERCASE and LOWERCASE letters, used for forms*/
        Font.loadFont(getClass().getResourceAsStream("/it/polimi/ingsw/font/BebasNeuePro-Regular.ttf"), 14);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/MainMenu.fxml"));
        Parent root = loader.load();

        setFXMLController(loader.getController());
        Scene scene = new Scene(root);

        stage.setResizable(true);
        stage.setFullScreen(true);
        stage.setScene(scene);
        stage.setMinHeight(720);
        stage.setMinWidth(1280);

        stage.getIcons().add(new Image("it/polimi/ingsw/images/icons/gameIcon.png"));

        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });

        this.stage = stage;

        FXMLController.run(this, stage);
        stage.show();
    }

    /**
     * Method to set the {@link FXMLController} of this GUI.
     * @param nextController The new FXMLController
     */
    public void setFXMLController(FXMLController nextController){
        this.FXMLController= nextController;
    }

    /**
     * Tells if the GUI is in game.
     * @return A boolean depending on whether the current menu is in game or not.
     */
    @Override
    public boolean inGame() {
        return FXMLController.inGame();
    }

    /**
     * Tells if the GUI is in a lobby phase.
     * @return A boolean depending on whether the current menu is in lobby or not.
     */
    @Override
    public boolean inLobby() {
        return FXMLController.inLobby();
    }

    /**
     * Tells if the GUI is in a menu and is outside a game and lobby.
     * @return A boolean depending on whether the current menu is in menu or not.
     */
    @Override
    public boolean inMenu() {
        return FXMLController.inMenu();
    }

    /**
     * Tells if the GUI is in a menu that has the chat.
     * @return A boolean depending on whether the current menu is in chat or not.
     */
    @Override
    public boolean inChat() {
        return FXMLController.inChat();
    }

    /**
     * Return the current GUI's FXMLController.
     * @return {@link FXMLController}
     */
    public FXMLController getFXMLController(){
        return this.FXMLController;
    }

    /**
     * Return the current stage of the GUI, used for switching between scenes.
     * @return {@link Stage}
     */
    public Stage getStage(){
        return this.stage;
    }

    @Override
    public void printMessage(String message){}

    @Override
    public void print(String message) {}

    /**
     * Returns the instance of {@link ViewController} this GUI is associated with.
     * @return {@link ViewController}
     */
    @Override
    public ViewController getController() {
        return controller;
    }

    @Override
    public String getInput(){
        return "";
    }

    @Override
    public String getIntFromInput() {
        return "";
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public ViewState getState() {
        return null;
    }

    @Override
    public boolean isInputReadStopped() {
        return false;
    }

    @Override
    public void setState(ViewState state) {}

    /**
     * Sets the username for this GUI
     * @param username the username to be set for this GUI.
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Handles the response from the server.
     * This method is called when a response is received from the server.
     * It uses an ExecutorService to run the handling of the response in a separate thread.
     * The handling of the response is delegated to the current FXMLController.
     *
     * @param eventID The ID of the event that the response is associated with.
     * @param feedback The feedback received from the server.
     * @param message The message received from the server.
     */
    @Override
    public void handleResponse(String eventID, Feedback feedback, String message) {
        executor.execute(() -> FXMLController.handleResponse(feedback, message, eventID));
    }

    @Override
    public void stopInputRead(boolean stopInputRead) {}

    /**
     * Handles server disconnection by loading the server disconnection menu.
     * This method could be called in any menu.
     */
    public void serverDisconnected(){
        Platform.runLater(() -> {
            try {
                FXMLController.switchScreen("ServerDisconnectionMenu");
            }
            catch (IOException e){
                throw new RuntimeException("FXML Exception: failed to load ServerDisconnectionMenu", e);
            }
        });
    }

    /**
     * Resets the user interface to its initial state.
     * This method is called when the user interface needs to be reset, for example, after a disconnection from the server.
     * It creates a new ViewController and ExecutorService, loads the main menu FXML file, sets the FXMLController to the controller of the loaded FXML file,
     * creates a new Scene with the loaded FXML file, sets the scene of the stage to the new scene, sets the stage to full screen, and runs the FXMLController.
     *
     * @throws RuntimeException If there is an IOException when loading the FXML file.
     */
    @Override
    public void resetUI() {
        Platform.runLater(() -> {
            try {
                this.controller = new ViewController(this);
                this.executor = Executors.newCachedThreadPool();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/it/polimi/ingsw/fxml/MainMenu.fxml"));
                Parent root = loader.load();
                setFXMLController(loader.getController());
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setFullScreen(true);
                FXMLController.run(this, stage);
            } catch (IOException e) {
                throw new RuntimeException("FXML Exception: Failed to load main menu", e);
            }
        });
    }

    @Override
    public void clearInput() {}
}
