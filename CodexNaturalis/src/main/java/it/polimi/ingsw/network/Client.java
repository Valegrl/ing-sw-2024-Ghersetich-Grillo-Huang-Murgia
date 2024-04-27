package it.polimi.ingsw.network;

import it.polimi.ingsw.eventUtils.event.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void report(Event event) throws RemoteException;
}
