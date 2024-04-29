package it.polimi.ingsw.network.clientSide;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.listener.ViewListener;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

//TODO Code review

/*Singleton*/
public class ClientManager extends UnicastRemoteObject implements Client {

    //TODO implement a filter which controls if an event's id is in local only and doesn't need to send to server
    private  View view;

    private Server server;

    private ViewListener listener;

    private static ClientManager clientManager;

    private final Queue<Event> requestsQueue = new LinkedList<>();

    private final Queue<Event> respondsQueue = new LinkedList<>();

    private boolean running = true;

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

    public void initSocket(String ipAddress, int portNumber, View view) throws RemoteException {
        //TODO implement Socket
        this.view = view;
        try{
            Socket socket = new Socket(ipAddress, portNumber);
            ObjectInputStream objectIn = new ObjectInputStream(socket.getInputStream());
            Server server = (Server) objectIn.readObject();
        }
        catch(IOException | ClassNotFoundException e){
            System.err.println("Client socket exception:");
            e.printStackTrace();
        }
    }

    private ClientManager() throws RemoteException {

        //TODO code review
        new Thread(){
            @Override
            public void run() {
                while(running){
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
                while(running) {
                    synchronized (respondsQueue) {
                        while (respondsQueue.isEmpty()) {
                            try {
                                respondsQueue.wait();
                            }
                            catch(InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                        Event respond = respondsQueue.poll();
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

    public static ClientManager getInstance() throws RemoteException {
        if(clientManager == null)
            clientManager = new ClientManager();
        return clientManager;
    }

    @Override
    public void report(Event event) throws RemoteException {
        synchronized (respondsQueue) {
            respondsQueue.add(event);
            notifyAll();
        }
    }

    public void  handleEvent(Event event) throws RemoteException {
        synchronized (requestsQueue) {
            requestsQueue.add(event);
            notifyAll();
        }
    }

    public void addViewListener(ViewListener listener){
        this.listener = listener;
    }

    public void managed(Event event){
        //TODO implementation
    }

    private void stopThreads(){
        running = false;
    }

}
