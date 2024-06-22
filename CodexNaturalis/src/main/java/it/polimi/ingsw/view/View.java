package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.controller.ViewController;

/**
 * Interface for the game's view.
 */
public interface View {

    /**
     * Starts the view.
     */
    void run();

    /**
     * Sets the FXML controller for the view.
     * @param controller The controller for the view.
     */
    void setFXMLController(FXMLController controller);

    /**
     * Returns whether the view is in game.
     * @return true if the view is in game, false otherwise.
     */
    boolean inGame();

    /**
     * Returns whether the view is in the lobby.
     * @return true if the view is in the lobby, false otherwise.
     */
    boolean inLobby();

    /**
     * Returns whether the view is in the menu.
     * @return true if the view is in the menu, false otherwise.
     */
    boolean inMenu();

    /**
     * Returns whether the view has chat open.
     * @return true if the view has chat open, false otherwise.
     */
    boolean inChat();

    /**
     * Prints a message in the cli terminal.
     * @param message The message to print.
     */
    void printMessage(String message);

    /**
     * Prints a string in the cli terminal.
     * @param message The message to print.
     */
    void print(String message);

    /**
     * Reads a string from the cli input.
     * @return The string read from the input.
     */
    String getInput();

    /**
     * Reads an integer from the cli input.
     * @return The integer read from the input, in a string format.
     */
    String getIntFromInput();

    /**
     * Returns the view's controller.
     * @return The view's controller.
     */
    ViewController getController();

    /**
     * Retrieves the view's username.
     * @return The view's username.
     */
    String getUsername();

    /**
     * Retrieves the view's current state.
     * @return The view's current state.
     */
    ViewState getState();

    /**
     * Returns whether the input read is stopped.
     * @return true if the input read is stopped, false otherwise.
     */
    boolean isInputReadStopped();

    /**
     * Sets the view's state.
     * @param state The state to set.
     */
    void setState(ViewState state);

    /**
     * Sets the view's username.
     * @param username The username to set.
     */
    void setUsername(String username);

    /**
     * Stops the input read.
     * @param stopInputRead true if the input read is stopped, false otherwise.
     */
    void stopInputRead(boolean stopInputRead);

    /**
     * Method to handle a response event from the server.
     * @param eventID The event ID.
     * @param feedback The server feedback on the event.
     * @param message The server message.
     */
    void handleResponse(String eventID, Feedback feedback, String message);

    /**
     * Method to handle a disconnection from the server.
     */
    void serverDisconnected();

    /**
     * Method to clear the input.
     */
    void clearInput();

    /**
     * Method to reset the UI after a server disconnection.
     */
    void resetUI();
}
