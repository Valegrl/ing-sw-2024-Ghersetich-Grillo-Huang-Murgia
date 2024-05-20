package it.polimi.ingsw.view;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.controller.ViewController;
import javafx.stage.Stage;


public abstract class FXMLController {

    protected Stage stage;

    protected View view;

    protected ViewController controller;

    protected final Object viewLock = new Object();

    public FXMLController() {
    }

    public void transition(FXMLController nextController) {
        view.setFXMLController(nextController);
        this.controller = view.getController();
        nextController.run(view, stage);
    }

    abstract public void run(View view, Stage stage);

    abstract public void handleResponse(Feedback feedback, String message, String eventID);

    public boolean inMenu() {return false;};

    public boolean inLobby() {return false;};

    public boolean inGame() {return false;};

    public boolean inChat() {return false;};

    protected void waitForResponse() {
        synchronized (viewLock) {
            try {
                viewLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void showResponseMessage(String message, int sleepTime) {
        //TODO don't print, use the texts in the FXML
        view.printMessage(message);
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException ignored) {}
    }

    public void notifyResponse() {
        synchronized (viewLock) {
            viewLock.notifyAll();
        }
    }

}
