package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.eventUtils.event.fromView.Feedback;
import it.polimi.ingsw.view.FXMLController;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewState;
import it.polimi.ingsw.view.controller.ViewController;
import it.polimi.ingsw.view.gui.controller.MainMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GUI extends Application implements View {

    private Stage stage;

    private final Scanner in;

    private final PrintStream out;

    private final ExecutorService executor;

    private String username;

    private final ViewController controller;

    private FXMLController FXMLController;

    public GUI() {
        this.in = new Scanner(System.in);
        this.out = new PrintStream(System.out, true);
        this.controller = new ViewController(this);
        this.executor = Executors.newCachedThreadPool();
    }

    public void run(){
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException, NullPointerException{
        /*This only has UPPERCASE letters*/
        Font.loadFont(getClass().getResourceAsStream("/font/BebasNeue-Regular.ttf"), 14);
        /*This has both UPPERCASE and LOWERCASE letters, used for forms*/
        Font.loadFont(getClass().getResourceAsStream("/font/BebasNeuePro-Regular.ttf"), 14);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main/MainMenu.fxml"));
        Parent root = loader.load();

        setFXMLController(loader.getController());
        Scene scene = new Scene(root);

        stage.setResizable(false);
        stage.setFullScreen(true);
        stage.setScene(scene);
        this.stage = stage;

        FXMLController.run(this, stage);
        stage.show();
    }

    public void setFXMLController(FXMLController nextController){
        this.FXMLController= nextController;
    }

    @Override
    public boolean inGame() {
        return FXMLController.inGame();
    }

    @Override
    public boolean inLobby() {
        return FXMLController.inLobby();
    }

    @Override
    public boolean inMenu() {
        return FXMLController.inMenu();
    }

    @Override
    public boolean inChat() {
        return FXMLController.inChat();
    }

    public FXMLController getFXMLController(){
        return this.FXMLController;
    }

    public Stage getStage(){
        return this.stage;
    }

    @Override
    public void printMessage(String message){
        out.println(message);
    }

    @Override
    public void print(String message) {}

    @Override
    public ViewController getController() {
        return controller;
    }

    @Override
    public String getInput(){
        return in.nextLine();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public ViewState getState() {
        //return state;
        return null;
    }

    @Override
    public void setState(ViewState state) {
        // this.state = state;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void handleResponse(String eventID, Feedback feedback, String message) {
        executor.execute(() -> FXMLController.handleResponse(feedback, message, eventID));
    }

    @Override
    public void stopInputRead(boolean stopInputRead) {
    }

    public void serverDisconnected(){}

    @Override
    public void clearInput() {
    }

}
