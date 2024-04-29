package it.polimi.ingsw.network.clientSide;

import com.google.gson.Gson;
import it.polimi.ingsw.eventUtils.event.Event;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

public class RemoteServerSocket implements Server {
    private final Socket socket;
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private RemoteServerSocket(String ipAddress, int portNumber) throws IOException {
        try {
            this.socket= new Socket(ipAddress,portNumber);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void direct(Event event, Client client) throws RemoteException {
        //TODO Figure out how to use client parameter
        try {
            Gson gson = new Gson();
            String jsonString = gson.toJson(event);
            outputStream.writeObject(jsonString);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }
    }
    public void readStream() throws RemoteException{
        try {
            String jsonString = (String) inputStream.readObject();
            Gson gson = new Gson();
            Event event;
            event = gson.fromJson(jsonString, Event.class);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class non found: " + e.getMessage());
        }
    }
}
