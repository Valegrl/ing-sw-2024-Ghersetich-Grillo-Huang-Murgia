package it.polimi.ingsw.network.clientSide;

import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.eventUtils.listener.ViewListener;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;
import it.polimi.ingsw.view.View;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

//TODO Code review

/*Singleton*/
public class ClientManager extends UnicastRemoteObject implements Client {

    //TODO implement a filter which controls if an event's id is in local only and doesn't need to send to server
    private  View view;

    private Server server;

    private ViewListener listener;

    private ClientManager clientManager;

    private final Queue<Event> requests = new LinkedList<>();

    private final Queue<Event> responds = new LinkedList<>();

    private ClientManager(View view, Server server) throws RemoteException {

        //TODO code review
        new Thread(){
            @Override
            public void run() {
                while(true){
                    if(isRequestsEmpty()){
                        continue;
                    }
                    Event request = pollFromRequests();
                    try {
                        listener.handle(request);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run(){
                while(true){
                    if(isRespondsEmpty()){
                        continue;
                    }
                    Event respond = pollFromResponds();
                    try{
                        listener.handle(respond);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }

    public ClientManager getInstance(View view, Server server) throws RemoteException {
        if(clientManager == null)
            this.clientManager = new ClientManager(view, server);
        return this.clientManager;
    }

    @Override
    public synchronized void report(Event event) throws RemoteException {
        responds.add(event);
    }

    public synchronized void  handleEvent(Event event) throws RemoteException {
        requests.add(event);
    }

    public void managed(Event event){
        //TODO implementation
    }

    public synchronized Event pollFromRequests(){
        return requests.poll();
    }

    public synchronized Event pollFromResponds(){
        return responds.poll();
    }

    public void addViewListener(ViewListener listener){
        this.listener = listener;
    }

    private synchronized boolean isRequestsEmpty(){
        if(requests.isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }
    private synchronized boolean isRespondsEmpty(){
        if(responds.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

}
