package it.polimi.ingsw.network.clientSide;

import it.polimi.ingsw.eventUtils.EventID;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.event.internal.PingEvent;
import it.polimi.ingsw.eventUtils.event.internal.PongEvent;
import it.polimi.ingsw.eventUtils.event.internal.ServerDisconnectedEvent;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.utils.JsonConfig;
import it.polimi.ingsw.view.controller.ViewController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

/**
 * ClientManager is a singleton class that manages the requests from the view and the responses from the server on the client side.
 * Implements {@link Client} interface for RMI compatibility.
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
     * The thread that manages the requests queue.
     */
    private final Thread requestsThread;

    /**
     * The queue of responses received from the server.
     */
    private final Queue<Event> responsesQueue = new LinkedList<>();

    /**
     * The thread that manages the responses queue.
     */
    private final Thread responsesThread;

    /**
     * The timer used to manage the connection with the server.
     */
    private Timer timer = new Timer();

    /**
     * A boolean value that indicates if the connection is open.
     */
    private boolean connectionOpen;

    /**
     * The lock used to synchronize the timers.
     */
    private final Object timerLock = new Object();

    /**
     * Initializes the connection to the server via RMI.
     *
     * @param registryAddress The address of the RMI registry.
     * @throws RemoteException If a communication-related exception occurs.
     */
    public void initRMI(String registryAddress) throws RemoteException {
        try {
            server = (Server) Naming.lookup("rmi://"+registryAddress+"/"+JsonConfig.getInstance().getRmiRegistryName());
            server.join(this);
            connectionOpen = true;
            startPing();
        } catch (MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
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
    public void initSocket(String ipAddress, int portNumber) throws IOException {
        server = new RemoteServerSocket(ipAddress, portNumber);
        connectionOpen = true;
        startPing();
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
        requestsThread = new Thread(() -> {
            while(true){
                Event request = null;
                synchronized(requestsQueue) {
                    while (requestsQueue.isEmpty()) {
                        try {
                            requestsQueue.wait();
                        } catch (InterruptedException ignored) {
                            return;
                        }
                    }
                    request = requestsQueue.poll();
                }
                try {
                    server.direct(request, ClientManager.this);
                } catch (RemoteException e) {
                    System.err.println("Cannot send event to server.");
                }
            }
        });
        requestsThread.start();

        responsesThread = new Thread(() -> {
            while(true) {
                Event response = null;
                synchronized (responsesQueue) {
                    while (responsesQueue.isEmpty()) {
                        try {
                            responsesQueue.wait();
                        }
                        catch(InterruptedException ignored){
                            return;
                        }
                    }
                    response = responsesQueue.poll();
                }
                if (response.getID().equals(EventID.PING.getID())) {
                    sendPong();
                } else if (response.getID().equals(EventID.PONG.getID())) {
                    synchronized (timerLock) {
                        timer.cancel();
                    }
                } else {
                    try {
                        viewController.externalEvent(response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        responsesThread.start();
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
            responsesQueue.notifyAll();
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
            requestsQueue.notifyAll();
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

    /**
     * Function that starts the ping mechanism.
     */
    private void startPing() {
        new Thread(() -> {
            while (connectionOpen) {
                try {
                    synchronized (timerLock) {
                        timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                System.out.println("\nNo response from server.");
                                serverDisconnected();
                            }
                        };
                        timer.schedule(task, JsonConfig.getInstance().getPingTimeoutMs());
                    }

                    // sending PingEvent
                    server.direct(new PingEvent(), this);
                } catch (RemoteException e) {
                    System.err.println("\nCannot send ping to server.");
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

    /**
     * Function that sends a PongEvent to the server to communicate its presence for server's ping mechanism.
     */
    private void sendPong() {
        try {
            server.direct(new PongEvent(), ClientManager.this);
        } catch (RemoteException e) {
            System.err.println("Cannot send pong to server.");
        }
    }

    /**
     * Function that manages the disconnection from the server.
     */
    private void serverDisconnected() {
        requestsThread.interrupt();
        responsesThread.interrupt();
        synchronized (timerLock) {
            timer.cancel();
            timer = new Timer();
        }
        try {
            connectionOpen = false;
            server.closeConnection();
            server = null;
        } catch (RemoteException ignored){} // ignored because rmi will always throw an exception when the server is offline
        viewController.externalEvent(new ServerDisconnectedEvent());
        instance = null;
    }

    /**
     * Static function that validates an IP address.
     *
     * @param ip The IP address to be validated.
     * @return True if the IP address is valid, false otherwise.
     */
    public static boolean validateAddress(String ip) {
        String pattern = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
        return ip.matches(pattern);
    }

}
