package it.polimi.ingsw.network.clientSide;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.eventUtils.EventTypeAdapter;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.model.deck.factory.EvaluatorTypeAdapter;
import it.polimi.ingsw.model.evaluator.Evaluator;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.LocalTimeTypeAdapter;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCard;
import it.polimi.ingsw.viewModel.immutableCard.ImmPlayableCardTypeAdapter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.time.LocalTime;

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

    private final Client client;

    /**
     * Creates a new {@code RemoteServerSocket} with the given IP address and port number.
     *
     * @param ipAddress the IP address used to establish a connection.
     * @param portNumber the port number used to establish a connection.
     * @throws IOException if any I/O error occurs.
     */
    public RemoteServerSocket(String ipAddress, int portNumber) throws IOException {
        this.client = ClientManager.getInstance();
        try {
            this.socket = new Socket(ipAddress,portNumber);
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
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                    .registerTypeAdapter(Event.class, new EventTypeAdapter())
                    .registerTypeAdapter(Evaluator.class, new EvaluatorTypeAdapter())
                    .create();
            String jsonString = gson.toJson(event);
            outputStream.writeUTF(jsonString);
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
    public void readStream() throws RemoteException {
        try {
            if (!socket.isClosed()) {
                String jsonString = inputStream.readUTF();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalTime.class, new LocalTimeTypeAdapter())
                        .registerTypeAdapter(Event.class, new EventTypeAdapter())
                        .registerTypeAdapter(Evaluator.class, new EvaluatorTypeAdapter())
                        .registerTypeAdapter(ImmPlayableCard.class, new ImmPlayableCardTypeAdapter())
                        .create();
                Event event = gson.fromJson(jsonString, Event.class);
                client.report(event);
            }
        } catch (IOException e) {
            if (!socket.isConnected())
                System.err.println("I/O error: " + e.getMessage());
        }
    }

    /**
     * Closes the {@link  Socket} and the {@code ObjectInputStream} and {@code ObjectOutputStream}.
     */
    protected void closeSocket() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("Cannot close socket to client " + e.getMessage());
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
            System.err.println("Cannot close socket to server");
        }
    }

    protected Socket getSocket() {
        return socket;
    }
}
