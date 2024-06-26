package it.polimi.ingsw.network.serverSide;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.event.internal.PingEvent;
import it.polimi.ingsw.eventUtils.event.internal.PongEvent;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.utils.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class used to manage the received events server-side, implements {@link Server} interface for RMI compatibility.
 * It is a singleton class that forwards {@link Event Events} to the {@link VirtualView} of the client.
 */
public class ServerManager extends UnicastRemoteObject implements Server {
    /**
     * The singleton instance of the class.
     */
    private static ServerManager instance;

    /**
     * The queue used to store the received {@link Event Events}.
     */
    private final Queue<Pair<Event, Client>> requestsQueue = new LinkedList<>();

    /**
     * The map used to store the {@link VirtualView VirtualViews} of the clients.
     */
    private final Map<Client, VirtualView> virtualViews;

    /**
     * The {@link ExecutorService} used to manage the {@link Event Events}.
     */
    private final ExecutorService executor;

    /**
     * The map used to store the ping mechanism {@link Timer} for each client.
     */
    private final Map<Client, Timer> clientTimers = new HashMap<>();

    /**
     * Returns the singleton instance of the class.
     * @return The singleton instance of the class.
     * @throws RemoteException If any I/O error occurs.
     */
    public synchronized static ServerManager getInstance() throws RemoteException {
        if( instance == null ) {
            instance = new ServerManager();
        }
        return instance;
    }

    /**
     * Creates a new {@code ServerManager}.
     * @throws RemoteException If any I/O error occurs.
     */
    private ServerManager() throws RemoteException {
        this.virtualViews = new HashMap<>();
        this.executor = Executors.newCachedThreadPool();

        new Thread(() -> {
            while (true) {
                Pair<Event, Client> requestPair;
                synchronized (requestsQueue) {
                    while (requestsQueue.isEmpty()) {
                        try {
                            requestsQueue.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    requestPair = requestsQueue.poll();
                }
                if (requestPair.key().getID().equals(EventID.PONG.getID())) {
                    synchronized (clientTimers) {
                        clientTimers.get(requestPair.value()).cancel();
                    }
                } else if (requestPair.key().getID().equals(EventID.PING.getID())) {
                    sendPong(requestPair.value());
                } else
                    manage(requestPair.key(), requestPair.value());
            }
        }).start();

        new Thread(() -> {
            List<Client> clients;

            while (true) {
                synchronized (clientTimers) {
                    clients = new ArrayList<>(clientTimers.keySet());
                }
                for (Client client: clients) {
                    try {
                        synchronized (clientTimers) {
                            clientTimers.replace(client, new Timer());
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    clientDisconnected(client);
                                }
                            };
                            clientTimers.get(client).schedule(task, JsonConfig.getInstance().getPingTimeoutMs());
                        }

                        // sending PingEvent
                        client.report(new PingEvent());

                    } catch (RemoteException ignored) {}
                }
                try {
                    int timeout = JsonConfig.getInstance().getPingTimeoutMs() * 2;
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    @Override
    public void direct(Event event, Client client) throws RemoteException {
        synchronized (requestsQueue) {
            requestsQueue.add(new Pair<>(event, client));
            requestsQueue.notifyAll();
        }
    }

    /**
     * Manages the received {@link Event}.
     * @param event The {@link Event} to manage.
     * @param client The {@link Client} that sent the {@link Event}.
     */
    private void manage(Event event, Client client) {
        synchronized (virtualViews) {
           VirtualView virtualView = virtualViews.get(client);
           virtualView.handle(event);
        }
    }

    @Override
    public void join(Client client) throws RemoteException {
        VirtualView vv = new VirtualView((event) -> {
            try {
                client.report(event);
            } catch (RemoteException e) {
                System.err.println("The event cannot be sent to the client." + e.getMessage());
            }
        });

        synchronized (virtualViews) {
            virtualViews.put(client, vv);
        }

        clientTimers.put(client, new Timer());
    }

    /**
     * Sends a {@link PongEvent} to the client.
     * @param client The client to send the {@link PongEvent} to.
     */
    private void sendPong(Client client) {
        try {
            client.report(new PongEvent());
        } catch (RemoteException e) {
            System.err.println("Cannot send pong to client.");
        }
    }

    /**
     * Handles the disconnection of a client.
     * @param client The client that disconnected.
     */
    private void clientDisconnected(Client client) {
        Event disconnectedEvent = new ClientDisconnectedEvent();
        manage(disconnectedEvent, client);
        try {
            client.closeConnection();
        } catch (RemoteException ignored){} // ignored because rmi will always throw an exception when the client is offline

        leave(client);
    }

    /**
     * Removes the client from the server.
     * @param client The client to remove.
     */
    private void leave(Client client) {
        synchronized (virtualViews) {
            virtualViews.remove(client);
            clientTimers.remove(client);
        }
    }

    /**
     * Retrieves the {@link ExecutorService} used to manage the {@link Event Events}.
     * @return The {@link ExecutorService} used to manage the {@link Event Events}.
     */
    public ExecutorService getExecutor() {
        return executor;
    }
}
