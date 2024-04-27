package it.polimi.ingsw.network.serverSide;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.network.Client;

import java.rmi.RemoteException;

public class RemoteClientSocket implements Client {
    @Override
    public void report(Event event) throws RemoteException {

    }

    void readStream(){

    }
}
