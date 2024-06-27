package it.polimi.ingsw.network;

import it.polimi.ingsw.eventUtils.event.Event;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The Client interface defines the methods that a class must implement to act as a client in a network communication.
 * This interface extends the Remote interface to allow for remote method invocation.
 *
 * <p>Classes implementing this interface can receive events from a server.</p>
 */
public interface Client extends Remote {
    /**
     * Receives an event from the server.
     * @param event The event to be received.
     * @throws RemoteException If a remote communication error occurs.
     */
    void report(Event event) throws RemoteException;

    /**
     * Method to close the connection between the server and the client.
     * @throws RemoteException If a remote communication error occurs.
     */
    default void closeConnection() throws RemoteException {}
}
