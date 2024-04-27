package it.polimi.ingsw.network.serverSide;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/*Singleton*/
public class ServerManager extends UnicastRemoteObject implements Server {
    private ServerManager() throws RemoteException {
    }

    @Override
    public void direct(Event event, Client client) throws RemoteException {

    }

    void manage(Event event){

    }
}
