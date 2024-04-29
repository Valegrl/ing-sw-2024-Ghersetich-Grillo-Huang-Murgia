package it.polimi.ingsw.network.serverSide;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.net.Socket;

public class RemoteClientSocket implements Client {

    private final Server server;

    private final ObjectOutputStream outputStream;

    private final ObjectInputStream inputStream;


    public RemoteClientSocket(Socket socket, Server server) throws RemoteException {
        this.server = server;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RemoteException();
        }
    }
    @Override
    public void report(Event event) throws RemoteException {
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(event);
            outputStream.writeObject(jsonString);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
    public void readStream(){
        try {
            String jsonString = (String) inputStream.readObject();
            Gson gson = new Gson();
            Event event;
            event = gson.fromJson(jsonString, Event.class);
            server.direct(event, this);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }

    }
}
