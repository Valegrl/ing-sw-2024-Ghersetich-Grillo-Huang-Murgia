package it.polimi.ingsw.network;

import it.polimi.ingsw.eventUtils.event.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Server extends Remote {
    void direct(Event event, Client client) throws RemoteException;
}
