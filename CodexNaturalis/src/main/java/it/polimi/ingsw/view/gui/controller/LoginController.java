package it.polimi.ingsw.view.gui.controller;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.eventUtils.event.fromView.menu.LoginEvent;
import it.polimi.ingsw.eventUtils.event.fromView.menu.RegisterEvent;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The LoginController class extends the FXMLController class and is responsible for handling the user interface and user interactions for the login and registration process.
 * This includes handling button clicks, displaying error messages, and managing the text fields for username and password input.
 * The class also communicates with the GameController to send and receive events.
 */
public class LoginController extends FXMLController {

    @FXML
    private AnchorPane loginMenuFX;

    @FXML
    private AnchorPane loginSubmitMenuFX;

    @FXML
    private AnchorPane registerSubmitMenuFX;

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private TextField registerUsernameField;

    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private Label errorRegistration;

    @FXML
    private Label errorLogin;

    /**
     * The constructor for the LoginController class.
     * Calls the superclass constructor.
     */
    public LoginController() {
        super();
    }

    /**
     * The run method is called to initialize the view and stage for the controller.
     * It also sets up key press events for the password fields.
     *
     * @param view the view to be set up
     * @param stage the stage to be set up
     */
    @Override
    public void run(View view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.controller = view.getController();

        loginPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitLogin(new ActionEvent());
            }
        });

        registerPasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitRegister(new ActionEvent());
            }
        });
    }

    /**
     * The {@code submitRegister} method is called when the register button is clicked.
     * It checks if the username and password fields are not empty, then sends a {@code RegisterEvent} to the server.
     *
     * @param e the action event
     */
    @FXML
    public void submitRegister(ActionEvent e){
        String registerUsername = registerUsernameField.getText();
        String registerPassword = registerPasswordField.getText();

        if(registerUsername.isEmpty() || registerPassword.isEmpty()){
            errorRegistration.setText("Username and password fields can't be left empty!");
        }
        else{
            registerUsernameField.clear();
            registerPasswordField.clear();
            errorRegistration.setText("");
            register(registerUsername, registerPassword);
        }
    }

    /**
     * The {@code submitLogin} method is called when the login button is clicked.
     * It checks if the username and password fields are not empty, then sends a {@code LoginEvent} to the server.
     *
     * @param e the action event
     */
    @FXML
    public void submitLogin(ActionEvent e){
        String loginUsername = loginUsernameField.getText();
        String loginPassword = loginPasswordField.getText();

        if(loginUsername.isEmpty() || loginPassword.isEmpty()){
            errorLogin.setText("Username and password fields can't be left empty!");
        }
        else{
            loginUsernameField.clear();
            loginPasswordField.clear();
            errorLogin.setText("");
            login(loginUsername, loginPassword);
        }
    }

    /**
     * The {@code goLogin} method is called when the login button is clicked on the main menu.
     * It hides the main menu and shows the login form.
     *
     * @param e the action event
     */
    @FXML
    public void goLogin(ActionEvent e){

        loginMenuFX.setVisible(false);
        loginMenuFX.setManaged(false);
        loginSubmitMenuFX.setVisible(true);
        loginSubmitMenuFX.setManaged(true);
    }

    /**
     * The {@code goRegister} method is called when the register button is clicked on the main menu.
     * It hides the main menu and shows the registration form.
     *
     * @param e the action event
     */
    @FXML
    public void goRegister(ActionEvent e){

        loginMenuFX.setVisible(false);
        loginMenuFX.setManaged(false);
        registerSubmitMenuFX.setVisible(true);
        registerSubmitMenuFX.setManaged(true);
    }

    /**
     * The {@code goBack} method is called when the back button is clicked on the login or registration form.
     * It hides the form and shows the main menu.
     *
     * @param e the action event
     */
    @FXML
    public void goBack(ActionEvent e){

        loginUsernameField.clear();
        loginPasswordField.clear();
        registerUsernameField.clear();
        registerPasswordField.clear();
        errorRegistration.setText("");
        errorLogin.setText("");

        loginMenuFX.setVisible(true);
        loginMenuFX.setManaged(true);
        loginSubmitMenuFX.setVisible(false);
        loginSubmitMenuFX.setManaged(false);
        registerSubmitMenuFX.setVisible(false);
        registerSubmitMenuFX.setManaged(false);
    }

    /**
     * The handleResponse method is called to handle the response from the server.
     * It updates the user interface based on the feedback received.
     *
     * @param feedback the feedback received from the server
     * @param message the message received from the server
     * @param eventID the ID of the event that triggered the response
     */
    @Override
    @FXML
    public void handleResponse(Feedback feedback, String message, String eventID) {
        notifyResponse();
        switch (EventID.getByID(eventID)) {
            case LOGIN:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        try {
                            switchScreen("Menu");
                        }
                        catch (IOException exception){
                            errorLogin.setText("Login failed");
                            throw new RuntimeException("FXML Exception: failed to load Menu", exception);
                        }
                    });
                } else {
                     Platform.runLater(() -> errorLogin.setText("Error " + message));
                }
                break;
            case REGISTER:
                if (feedback == Feedback.SUCCESS) {
                    Platform.runLater(() -> {
                        registerSubmitMenuFX.setManaged(false);
                        registerSubmitMenuFX.setVisible(false);
                        loginSubmitMenuFX.setVisible(true);
                        loginSubmitMenuFX.setManaged(true);
                    });
                } else {
                    Platform.runLater(() -> errorRegistration.setText("Error " + message));
                }
                break;
        }
    }

    /**
     * The login method is used to send a LoginEvent to the server.
     *
     * @param loginUsername the username to log in with
     * @param loginPassword the password to log in with
     */
    private void login(String loginUsername, String loginPassword){
        Event event = new LoginEvent(loginUsername, loginPassword);
        controller.newViewEvent(event);
        waitForResponse();
    }

    /**
     * The register method is used to send a RegisterEvent to the server.
     *
     * @param registerUsername the username to register with
     * @param registerPassword the password to register with
     */
    private void register(String registerUsername, String registerPassword){
        Event event = new RegisterEvent(registerUsername, registerPassword);
        controller.newViewEvent(event);
        waitForResponse();
    }

    /**
     * The inMenu method is used to check if the player is in the menu.
     * It returns true as the player is in the menu during the login and registration process.
     *
     * @return true
     */
    @Override
    public boolean inMenu() {
        return true;
    }

}
