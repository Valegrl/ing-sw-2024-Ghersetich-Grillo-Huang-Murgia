package it.polimi.ingsw.network.serverSide;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.net.Socket;

/**
 * The {@code RemoteClientSocket} class is used as a server-side skeleton.
 * It forwards {@code Event}s to a {@code RemoteServerSocket}, that is a client-side stub.
 */
public class RemoteClientSocket implements Client {
    /**
     * The {@code Server} that this {@code RemoteClientSocket} is connected to.
     */
    private final Server server;

    /**
     * The {@code ObjectOutputStream} used to send {@code Event}s to the {@code RemoteServerSocket}.
     */
    private final ObjectOutputStream outputStream;
    /**
     * The {@code ObjectInputStream} used to receive {@code Event}s from the {@code RemoteServerSocket}.
     */
    private final ObjectInputStream inputStream;

    /**
     * Creates a new {@code RemoteClientSocket} with the given {@code Socket} and {@code Server}.
     *
     * @param socket the {@code Socket} used to connect to the {@code RemoteServerSocket}.
     * @throws RemoteException if any I/O error occurs.
     */
    public RemoteClientSocket(Socket socket) throws RemoteException {
        this.server = ServerManager.getInstance();
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException();
        }
    }

    /**
     * Forwards an {@code Event} to the {@code RemoteServerSocket}.
     * The {@code Event} is serialized into a JSON string and sent through the {@code ObjectOutputStream}.
     *
     * @param event the {@link Event} to forward.
     * @throws RemoteException if any I/O error occurs.
     */
    @Override
    public void report(Event event) throws RemoteException {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(event);
            outputStream.writeUTF(jsonString);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Receives an event from the {@code RemoteServerSocket} through the {@code ObjectInputStream}.
     * The message is deserialized into an {@code Event} and forwarded to the {@code ServerManager}.
     *
     * @throws RemoteException if any I/O error occurs.
     */
    public void readStream(){
        try {
            String jsonString = (String) inputStream.readUTF();
            Gson gson = new Gson();
            Event event;
            event = gson.fromJson(jsonString, Event.class);
            server.direct(event, this);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
}
