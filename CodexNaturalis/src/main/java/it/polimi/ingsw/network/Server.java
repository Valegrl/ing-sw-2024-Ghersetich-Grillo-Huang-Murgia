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

    /**
     * This method allows a client to join the server. It is used to establish a connection between the client and the server.
     * Once the client has joined, it can start sending events to the server.
     *
     * @param client The client that wants to join the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    default void join(Client client) throws RemoteException {}

    /**
     * Method to close the connection between the client and the server.
     * @throws RemoteException If a remote communication error occurs.
     */
    default void closeConnection() throws RemoteException {}
}
