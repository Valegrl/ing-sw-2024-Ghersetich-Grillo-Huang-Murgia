package it.polimi.ingsw.network.clientSide;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.listener.ViewListener;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 *ClientManager is a singleton class that manages the requests from the view and the responses from the server on the client side.
 * It implements the Client interface and extends UnicastRemoteObject for remote method invocation (RMI).
 * It has a queue of events to be sent to the server (requestsQueue) and a queue of responses received from the server (responsesQueue).
 * It contains a listener which handles the event from the queue.
 */
public class ClientManager extends UnicastRemoteObject implements Client {

    //TODO implement a filter which controls if an event's id is in local only and doesn't need to send to server
    /**
     * The view with which ClientManager is associated
     */
    private View view;

    /**
     * The server, which the client is connected to.
     */
    private Server server;

    /**
     * The listener for view events.
     */
    private ViewListener listener;

    /**
     * The singleton instance of ClientManager.
     */
    private static ClientManager instance;

    /**
     * The queue of events to be sent to the server.
     */
    private final Queue<Event> requestsQueue = new LinkedList<>();

    /**
     * The queue of responses received from the server.
     */
    private final Queue<Event> responsesQueue = new LinkedList<>();

    /**
     * Initializes the connection to the server via RMI.
     *
     * @param registryAddress The address of the RMI registry.
     * @param view The view to be associated with this ClientManager.
     * @throws RemoteException If a communication-related exception occurs.
     */
    public void initRMI(String registryAddress, View view) throws RemoteException {
        //TODO implement RMI
        try {
            this.view = view;
            Registry registry = LocateRegistry.getRegistry(registryAddress);
            this.server = (Server) registry.lookup("rmi://"+registryAddress+"/ServerInterface");
        }
        catch(Exception e){
            System.err.println("Client RMI exception:");
            e.printStackTrace();
        }
    }

    /**
     * Initializes the connection to the server via a socket and creates a thread.
     *The thread remains active reading events coming from {@link RemoteServerSocket}.
     *
     * @param ipAddress The IP address of the server.
     * @param portNumber The port number of the server.
     * @param view The view to be associated with this ClientManager.
     * @throws RemoteException If a communication-related exception occurs.
     */
    public void initSocket(String ipAddress, int portNumber, View view) throws RemoteException {
        //TODO create thread in which ClientManager readStream() from the remoteServerSocket
        RemoteServerSocket remoteServerSocket = null;
        try {
            remoteServerSocket = new RemoteServerSocket(ipAddress, portNumber);
        } catch (IOException e) {
            System.err.println("Client Socket exception");
            e.printStackTrace();
        }
        RemoteServerSocket finalRemoteServerSocket = remoteServerSocket;

        if (finalRemoteServerSocket != null) {
            Thread socketThread = new Thread(
                    () -> {
                        try {
                            finalRemoteServerSocket.readStream();
                        } catch (RemoteException e) {
                            System.err.println("Cannot read stream from the RemoteServerSocket");
                        }
                    });
            socketThread.start();
        }
        else {
            throw new RemoteException("Error creating remoteServerSocket");
        }
    }

    /**
     * Private constructor for the singleton pattern, creates two threads for managing the requests and responses queues.
     * The first thread waits if the requests queue is empty, otherwise it polls the event from the queue and calls the listener to handle it.
     * The second thread waits if the responses queue is empty, otherwise it polls the event...
     *
     * @throws RemoteException If a communication-related exception occurs.
     */
    private ClientManager() throws RemoteException {
        //TODO code review
        new Thread(){
            @Override
            public void run() {
                while(true){
                    synchronized(requestsQueue) {
                        while (requestsQueue.isEmpty()) {
                            try {
                                requestsQueue.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Event request = requestsQueue.poll();
                        try {
                            listener.handle(request);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                while(true) {
                    synchronized (responsesQueue) {
                        while (responsesQueue.isEmpty()) {
                            try {
                                responsesQueue.wait();
                            }
                            catch(InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        Event response = responsesQueue.poll();
                        try {
                            /*Something*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();

    }

    /**
     * Returns the singleton instance of ClientManager, if the instance is null creates a new instance.
     *
     * @return The singleton instance of ClientManager.
     * @throws RemoteException If a communication-related exception occurs.
     */
    public static ClientManager getInstance() throws RemoteException {
        if(instance == null)
            instance = new ClientManager();
        return instance;
    }

    /**
     * Adds an event to the responses queue.
     *
     * @param event The event to be reported.
     * @throws RemoteException If a communication-related exception occurs.
     */
    @Override
    public void report(Event event) throws RemoteException {
        synchronized (responsesQueue) {
            responsesQueue.add(event);
            notifyAll();
        }
    }

    /**
     * Adds an event to the requests queue.
     *
     * @param event The event to be handled.
     * @throws RemoteException If a communication-related exception occurs.
     */
    public void  handleEvent(Event event) throws RemoteException {
        synchronized (requestsQueue) {
            requestsQueue.add(event);
            notifyAll();
        }
    }

    /**
     * Adds a listener for view events.
     *
     * @param listener The listener to be added.
     */
    public void addViewListener(ViewListener listener){
        this.listener = listener;
    }

    public void managed(Event event){
        //TODO implementation
    }


}
