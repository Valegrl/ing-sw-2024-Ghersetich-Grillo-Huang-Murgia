package it.polimi.ingsw.network.clientSide;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;

import java.rmi.RemoteException;

public class RemoteServerSocket implements Server {
    @Override
    public void direct(Event event, Client client) throws RemoteException {

    }

    void readStream(){

    }
}
