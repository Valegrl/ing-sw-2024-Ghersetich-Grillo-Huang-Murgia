package it.polimi.ingsw.main;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.tui.TUI;

/*start client*/
public class MainClient {

    private final View view;

    public MainClient(View view) {
        this.view = view;
    }

    public static void main(String[] args) {
        MainClient client = new MainClient( (( args.length>0 ) && args[0].equals("-cli")) ? new TUI() : new GUI());
    }
}
