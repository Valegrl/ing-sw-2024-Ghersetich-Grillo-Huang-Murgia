package it.polimi.ingsw.network;

import it.polimi.ingsw.eventUtils.event.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Server interface defines the methods that a class must implement to act as a server in a network communication.
 * This interface extends the Remote interface to allow for remote method invocation.
 *
 * <p>Classes implementing this interface can receive events from a client.</p>
 */
public interface Server extends Remote {
    /**
     * Receives an event from the specified client.
     * @param event The event to be received.
     * @param client The client from which the event is received.
     * @throws RemoteException If a remote communication error occurs.
     */
    void direct(Event event, Client client) throws RemoteException;
}
