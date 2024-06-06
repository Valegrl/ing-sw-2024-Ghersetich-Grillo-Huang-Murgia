package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.controller.ViewController;
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

}
