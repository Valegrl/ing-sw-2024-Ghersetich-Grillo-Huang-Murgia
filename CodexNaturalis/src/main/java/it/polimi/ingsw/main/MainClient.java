package it.polimi.ingsw.main;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.tui.TUI;

/*start client*/
public class MainClient {

    private final View view;

    public MainClient(View view) {
        this.view = view;
    }

    public static void main(String[] args) {
        // TODO test hardcoded UI choice
        MainClient client = new MainClient(new TUI());
    }
}
