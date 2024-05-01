package it.polimi.ingsw.network.clientSide;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.internal.PingEvent;
import it.polimi.ingsw.eventUtils.event.internal.PongEvent;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.controller.ViewController;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 *ClientManager is a singleton class that manages the requests from the view and the responses from the server on the client side.
 * It implements the Client interface and extends UnicastRemoteObject for remote method invocation (RMI).
 * It has a queue of events to be sent to the server (requestsQueue) and a queue of responses received from the server (responsesQueue).
 * It contains a listener which handles the event from the queue.
 */
public class ClientManager extends UnicastRemoteObject implements Client {

    /**
     * The ViewController with which ClientManager is associated
     */
    private ViewController viewController;

    /**
     * The server, which the client is connected to.
     */
    private Server server;

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
     * The timer used to manage the connection with the server.
     */
    private Timer timer = new Timer();

    /**
     * A boolean value that indicates if the connection is open.
     */
    private boolean connectionOpen;

    private final Object timerLock = new Object();

    /**
     * Initializes the connection to the server via RMI.
     *
     * @param registryAddress The address of the RMI registry.
     * @throws RemoteException If a communication-related exception occurs.
     */
    public void initRMI(String registryAddress) throws RemoteException {
        try {
            this.server = (Server) Naming.lookup("rmi://"+registryAddress+"/CodexNaturalisServer51"); // TODO config?
            this.server.join(this);
            connectionOpen = true;
            startPing();
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
     * @throws RemoteException If a communication-related exception occurs.
     */
    public void initSocket(String ipAddress, int portNumber) throws RemoteException {
        try {
            server = new RemoteServerSocket(ipAddress, portNumber);
            connectionOpen = true;
            startPing();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        new Thread(() -> {
            RemoteServerSocket serverSocket = (RemoteServerSocket) server;
            while(!serverSocket.getSocket().isClosed()) {
                try {
                    serverSocket.readStream();
                } catch (RemoteException e) {
                    System.err.println(e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Private constructor for the singleton pattern, creates two threads for managing the requests and responses queues.
     * The first thread waits if the requests queue is empty, otherwise it polls the event from the queue and calls the listener to handle it.
     * The second thread waits if the responses queue is empty, otherwise it polls the event from the queue and manages the response.
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
                            server.direct(request, ClientManager.this);
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
                        if (response.getID().equals("PING")) {
                            sendPong();
                        } else if (response.getID().equals("PONG")) {
                            synchronized (timerLock) {
                                timer.cancel();
                            }
                        } else {
                            try {
                                viewController.evaluateEvent(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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
    public synchronized static ClientManager getInstance() throws RemoteException {
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
            responsesQueue.notify();
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
            requestsQueue.notify();
        }
    }

    /**
     * Sets the associated ViewController.
     *
     * @param vc The ViewController to be set.
     */
    public void setViewController(ViewController vc){
        this.viewController = vc;
    }

    private void startPing() {
        new Thread(() -> {
            while (connectionOpen) {
                try {
                    synchronized (timerLock) {
                        timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println("No response from server.");
                                serverCrashed();
                            }
                        };
                        timer.schedule(task, 2000); // TODO config file?
                    }

                    // sending PingEvent
                    System.out.println("Sending ping to server.");
                    server.direct(new PingEvent(), this);
                } catch (RemoteException e) {
                    System.err.println("Cannot send ping to server.");
                }
                try {
                    Thread.sleep(2000 * 2);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    private void sendPong() {
        System.out.println("Client received ping, sending pong...");
        try {
            server.direct(new PongEvent(), ClientManager.this);
        } catch (RemoteException e) {
            System.err.println("Cannot send pong to server.");
        }
    }

    private void serverCrashed() {
        connectionOpen = false;
        System.err.println("Server crashed.");
        try {
            server.closeConnection();
        } catch (RemoteException ignored){} // ignored because rmi will always throw an exception when the server is offline
        System.out.println("Server socket closed.");
        viewController.serverCrashed();
    }

}
