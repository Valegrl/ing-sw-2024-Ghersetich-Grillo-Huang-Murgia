package it.polimi.ingsw.network.clientSide;

import com.google.gson.Gson;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

/**
 * The {@code RemoteServerSocket} class is used as a client-side stub.
 * It forwards {@code Event}s to a {@code RemoteClientSocket}, that is a server-side skeleton.
 */
public class RemoteServerSocket implements Server {
    /**
     * The {@code Socket} used to connect to the {@code RemoteClientSocket}.
     */
    private final Socket socket;

    /**
     * The {@code ObjectOutputStream} used to send {@code Event}s to the {@code RemoteClientSocket}.
     */
    private final ObjectOutputStream outputStream;

    /**
     * The {@code ObjectInputStream} used to receive {@code Event}s from the {@code RemoteClientSocket}.
     */
    private final ObjectInputStream inputStream;

    /**
     * Creates a new {@code RemoteServerSocket} with the given IP address and port number.
     *
     * @param ipAddress the IP address used to establish a connection.
     * @param portNumber the port number used to establish a connection.
     * @throws IOException if any I/O error occurs.
     */
    private RemoteServerSocket(String ipAddress, int portNumber) throws IOException {
        try {
            this.socket= new Socket(ipAddress,portNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Forwards an {@code Event} to the {@code RemoteClientSocket}.
     * The {@code Event} is serialized into a JSON string and sent through the {@code ObjectOutputStream}.
     *
     * @param event the {@link Event} to forward.
     * @param client the {@link Client} that is forwarding the {@link Event} to the {@code RemoteClientSocket}.
     * @throws RemoteException if any I/O error occurs.
     */
    @Override
    public void direct(Event event, Client client) throws RemoteException {
        //TODO Figure out how to use client parameter
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(event);
            outputStream.writeObject(jsonString);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Receives an event from the {@code RemoteClientSocket} through the {@code ObjectInputStream}.
     * The message is deserialized into an {@code Event} and forwarded to the {@code ClientManager}.
     *
     * @throws RemoteException if any I/O error occurs.
     */
    public void readStream() throws RemoteException{
        try {
            String jsonString = (String) inputStream.readObject();
            Gson gson = new Gson();
            Event event;
            event = gson.fromJson(jsonString, Event.class);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class non found: " + e.getMessage());
        }
    }
}
