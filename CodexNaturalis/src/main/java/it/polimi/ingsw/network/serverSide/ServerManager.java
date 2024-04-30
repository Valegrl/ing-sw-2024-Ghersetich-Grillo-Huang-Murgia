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

/*Singleton*/
public class ServerManager extends UnicastRemoteObject implements Server {

    private static ServerManager instance;

    private final Queue<Pair<Event, Client>> requestsQueue = new LinkedList<>();

    private final Map<Client, VirtualView> virtualViews;

    public static ServerManager getInstance() throws RemoteException {
        if( instance == null ) {
            instance = new ServerManager();
        }
        return instance;
    }

    private ServerManager() throws RemoteException {
         this.virtualViews = new HashMap<>();

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
        VirtualView virtualView = virtualViews.get(client);
        Event response = null;

        event.receiveEvent(virtualView);

        try {
            client.report(response);
        } catch (RemoteException e) {
            // System.err.println("Cannot send message to " + virtualView.getUsername() + "'s client"); TODO VirtualView username
            System.err.println("Error: " + e.getMessage());
        }

    }

    public void join(Client client) {
        VirtualView virtualView = new VirtualView();
        virtualViews.put(client, virtualView);
    }

    public void addVirtualView(Client client, VirtualView virtualView) {
        virtualViews.put(client, virtualView);
    }

    public void removeVirtualView(Client client) {
        virtualViews.remove(client);
    }
}
