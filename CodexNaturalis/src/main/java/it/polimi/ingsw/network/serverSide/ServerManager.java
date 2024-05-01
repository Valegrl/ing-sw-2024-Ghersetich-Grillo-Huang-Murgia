package it.polimi.ingsw.network.serverSide;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.internal.ClientDisconnectedEvent;
import it.polimi.ingsw.eventUtils.event.internal.PingEvent;
import it.polimi.ingsw.eventUtils.event.internal.PongEvent;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*Singleton*/
public class ServerManager extends UnicastRemoteObject implements Server {

    // TODO uncomment getUserName() methods when implemented in VirtualView

    private static ServerManager instance;

    private final Queue<Pair<Event, Client>> requestsQueue = new LinkedList<>();

    private final Map<Client, VirtualView> virtualViews;

    private final ExecutorService executor;

    private final Map<Client, Timer> clientTimers = new HashMap<>();

    public synchronized static ServerManager getInstance() throws RemoteException {
        if( instance == null ) {
            instance = new ServerManager();
        }
        return instance;
    }

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
                if (requestPair.key().getID().equals("PONG")) {
                    //System.out.println("Server received pong from client '" + virtualViews.get(requestPair.value()).getUsername() + "'.");
                    synchronized (clientTimers) {
                        clientTimers.get(requestPair.value()).cancel();
                    }
                } else if (requestPair.key().getID().equals("PING")) {
                    sendPong(requestPair.value());
                } else
                    manage(requestPair.key(), requestPair.value());
            }
        }).start();

        new Thread(() -> {
            List<Client> clients;
            VirtualView clientView;

            while (true) {
                synchronized (clientTimers) {
                    clients = new ArrayList<>(clientTimers.keySet());
                }
                for (Client client: clients) {
                    synchronized (virtualViews) {
                        clientView = virtualViews.get(client);
                    }
                    try {
                        synchronized (clientTimers) {
                            clientTimers.replace(client, new Timer());
                            VirtualView finalClientView = clientView;
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    //System.out.println("No response from client: '" + finalClientView.getUsername() + "'.");
                                    clientDisconnected(client);
                                }
                            };
                            clientTimers.get(client).schedule(task, 2000); // TODO config file?
                        }

                        // sending PingEvent
                        //System.out.println("Sending ping to '" + clientView.getUsername() + "' client.");
                        client.report(new PingEvent());

                    } catch (RemoteException e) {
                        //System.err.println("Cannot send ping to client '" + clientView.getUsername() + "'.");
                    }
                }
                try {
                    Thread.sleep(3000 * 2);
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
            requestsQueue.notify();
        }
    }

    private void manage(Event event, Client client) {
        synchronized (virtualViews) {
            VirtualView virtualView = virtualViews.get(client);
            event.receiveEvent(virtualView); // TODO change when implemented VirtualView queue
        }
    }

    @Override
    public void join(Client client) throws RemoteException {
        VirtualView vv = new VirtualView((event) -> {
            try {
                client.report(event);
            } catch (RemoteException e) {
                System.err.println("The event cannot be sent to the client.");
            }
        });

        synchronized (virtualViews) {
            virtualViews.put(client, vv);
        }

        clientTimers.put(client, new Timer());
    }

    private void sendPong(Client client) {
        System.out.println("Server received ping, sending pong...");
        try {
            client.report(new PongEvent());
        } catch (RemoteException e) {
            System.err.println("Cannot send pong to client.");
        }
    }

    private void clientDisconnected(Client client) {
        Event disconnectedEvent = new ClientDisconnectedEvent();
        disconnectedEvent.receiveEvent(virtualViews.get(client));
        try {
            client.closeConnection();
        } catch (RemoteException ignored){} // ignored because rmi will always throw an exception when the client is offline

        //System.out.println("Client '" + virtualViews.get(client).getUsername() + "' disconnected.");
        leave(client);
    }

    private void leave(Client client) {
        synchronized (virtualViews) {
            virtualViews.remove(client);
            clientTimers.remove(client);
        }
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}
