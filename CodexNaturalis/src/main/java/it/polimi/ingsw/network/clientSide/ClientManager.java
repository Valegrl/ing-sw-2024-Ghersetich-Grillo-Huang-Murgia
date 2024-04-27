package it.polimi.ingsw.network.clientSide;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.listener.ViewListener;
import it.polimi.ingsw.network.Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/*Singleton*/
public class ClientManager extends UnicastRemoteObject implements Client {

    private final static ClientManager clientManager;

    static{
        try{
            clientManager = new ClientManager();
        }
        catch(Exception e){
            throw new RuntimeException("Exception occurred during clientManager creation");
        }
    }

    private RemoteServerSocket serverSocket;

    private List<Event> requests;

    private List<Event> responds;

    private ClientManager() throws RemoteException {
        try{
            serverSocket = new RemoteServerSocket();
        }
        catch(Exception e){
            System.out.println("Error linking with client's socket");
        }
    }

    @Override
    public void report(Event event) throws RemoteException {

    }

    void managed(Event event){

    }

    public static ClientManager getInstance(){
        return clientManager;
    }

    public RemoteServerSocket getServerSocket(){
        return this.serverSocket;
    }

    public void setServerSocket(RemoteServerSocket RSS){
        this.serverSocket = RSS;
    }
}
