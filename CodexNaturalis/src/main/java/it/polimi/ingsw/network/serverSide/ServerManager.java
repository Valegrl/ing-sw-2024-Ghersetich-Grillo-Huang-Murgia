package it.polimi.ingsw.network.serverSide;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.Pair;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*Singleton*/
public class ServerManager extends UnicastRemoteObject implements Server {

    private static ServerManager instance;

    private final Queue<Pair<Event, Client>> requestsQueue = new LinkedList<>();

    private final Map<Client, VirtualView> virtualViews;

    private final ExecutorService executor;

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
                manage(requestPair.key(), requestPair.value());
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
        synchronized (virtualViews) {
            virtualViews.put(client, new VirtualView((event) -> {
                try {
                    client.report(event);
                } catch (RemoteException e) {
                    System.err.println("The event cannot be sent to the client.");
                }
            }));
        }
    }

    public void leave(Client client) {
        synchronized (virtualViews) {
            virtualViews.remove(client);
        }
    }

    public ExecutorService getExecutor() {
        return executor;
    }
}
