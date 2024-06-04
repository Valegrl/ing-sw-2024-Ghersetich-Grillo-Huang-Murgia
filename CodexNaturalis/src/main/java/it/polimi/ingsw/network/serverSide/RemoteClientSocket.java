package it.polimi.ingsw.network.serverSide;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.eventUtils.EventTypeAdapter;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.model.deck.factory.EvaluatorTypeAdapter;
import it.polimi.ingsw.model.evaluator.Evaluator;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.Coordinate;
import it.polimi.ingsw.utils.CoordinateTypeAdapter;
import it.polimi.ingsw.utils.LocalTimeTypeAdapter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.net.Socket;
import java.time.LocalTime;

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
     * The {@link Socket} used to connect to the {@link it.polimi.ingsw.network.clientSide.RemoteServerSocket RemoteServerSocket}.
     */
    private final Socket socket;

    private final Gson gson;

    /**
     * Creates a new {@code RemoteClientSocket} with the given {@code Socket} and {@code Server}.
     *
     * @param socket the {@code Socket} used to connect to the {@code RemoteServerSocket}.
     * @throws RemoteException if any I/O error occurs.
     */
    public RemoteClientSocket(Socket socket) throws RemoteException {
        this.socket = socket;
        this.server = ServerManager.getInstance();
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException();
        }
        this.gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                .registerTypeAdapter(Coordinate.class, new CoordinateTypeAdapter())
                .registerTypeAdapter(Event.class, new EventTypeAdapter())
                .registerTypeAdapter(Evaluator.class, new EvaluatorTypeAdapter())
                .create();
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
            if (!socket.isClosed()) {
                String jsonString = gson.toJson(event);

                String id = event.getID();//FIXME just for testing
                if (!(id.equals("PONG") || id.equals("PING")))
                    System.out.println(socket.getPort() + " OUT: "+ id);

                outputStream.writeUTF(jsonString);
                outputStream.flush();
            }
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Receives an event from the {@code RemoteServerSocket} through the {@code ObjectInputStream}.
     * The message is deserialized into an {@code Event} and forwarded to the {@code ServerManager}.
     */
    public void readStream(){
        try {
            String jsonString = inputStream.readUTF();
            Event event;
            event = gson.fromJson(jsonString, Event.class);
            String id = event.getID(); // FIXME just for testing
            if (!(id.equals("PONG") || id.equals("PING")))
                System.out.println(socket.getPort() + " IN: "+ id);

            server.direct(event, this);
        } catch (IOException e) {
            if (!socket.isConnected())
                System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Closes the {@link  Socket} and the {@code ObjectInputStream} and {@code ObjectOutputStream}.
     */
    @Override
    public void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Cannot close socket to client " + e.getMessage());
        }
    }
}
