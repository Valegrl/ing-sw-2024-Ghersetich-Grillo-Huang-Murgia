package it.polimi.ingsw.network.clientSide;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.network.Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*Singleton*/
public class ClientManager extends UnicastRemoteObject implements Client {
    private ClientManager() throws RemoteException {
    }

    @Override
    public void report(Event event) throws RemoteException {

    }

    void managed(Event event){

    }
}
