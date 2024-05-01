package it.polimi.ingsw.controller;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.fromView.game.*;
import it.polimi.ingsw.eventUtils.event.fromView.lobby.*;
import it.polimi.ingsw.eventUtils.event.fromView.menu.*;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.GameListener;

public class VirtualView {
    private GameListener gameListener;
    private Controller controller;
    private GameController gameController;

    public VirtualView(GameListener gl) {
        this.gameListener = gl;
        this.controller = Controller.getInstance();
        //TODO: Single thread for the incoming event queue.
    }

    //TODO: Implement queue manager methods.
    // REMEMBER: Manage one event at a time and wait for its return.
    // REMEMBER: Call my public method from @ServerManager to add the event to my queue.

    public void evaluateEvent(Event event){
        //TODO: Implement a logger to save unexpected events
        //TODO: ignore event
    }

    public void evaluateEvent(ChooseSetupEvent event){}

    public void evaluateEvent(DrawCardEvent event){}

    public void evaluateEvent(PlaceCardEvent event){}

    public void evaluateEvent(QuitGameEvent event){}

    public void evaluateEvent(KickFromLobbyEvent event){}

    public void evaluateEvent(PlayerReadyEvent event){}

    public void evaluateEvent(PlayerUnreadyEvent event){}

    public void evaluateEvent(QuitLobbyEvent event){}

    public void evaluateEvent(AvailableLobbiesEvent event){}

    public void evaluateEvent(CreateLobbyEvent event){}

    public void evaluateEvent(DeleteAccountEvent event){}

    public void evaluateEvent(GetMyOfflineGamesEvent event){}

    public void evaluateEvent(JoinLobbyEvent event){}

    public void evaluateEvent(LoginEvent event){}

    public void evaluateEvent(LogoutEvent event){}

    public void evaluateEvent(ReconnectToGameEvent event){}

    public void evaluateEvent(RegisterEvent event){}

    public void evaluateEvent(ClientDisconnectedEvent event){}
}
