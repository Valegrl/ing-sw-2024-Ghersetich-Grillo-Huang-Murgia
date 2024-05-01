package it.polimi.ingsw.main;

import it.polimi.ingsw.controller.VirtualView;
import it.polimi.ingsw.network.serverSide.RemoteClientSocket;
import it.polimi.ingsw.network.serverSide.ServerManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/*start server*/
public class MainServer {
    public static void main(String[] args) {
        MainServer server = new MainServer();

        Thread socketThread = new Thread(
                () -> {
                    int port = 1098;
                    System.out.println("Starting Socket server on port " + port + "...");
                    try {
                        server.startSocket(port); // TODO port, config file?
                    } catch (RemoteException e) {
                        System.err.println("Cannot start socket server, socket protocol will ne disabled");
                    }
                });
        socketThread.start();

        Thread rmiThread = new Thread(
                () -> {
                    int port = 1099;
                    System.out.println("Starting RMI server on port " + port + "...");
                    try {
                        server.startRMI(port); // TODO port, config file?
                        System.out.println("Server rmi ready on port: " + port);
                    } catch (RemoteException e) {
                        System.err.println("Cannot start RMI server, RMI protocol will ne disabled");
                    }
                });
        rmiThread.start();
    }

    private void startSocket(int port) throws RemoteException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server socket ready on port: " + port);

            while(true) {
                System.out.println("Waiting for a new socket...");
                Socket socket = serverSocket.accept();
                System.out.println("New socket accepted");
                ServerManager.getInstance().getExecutor().execute(() -> {
                    RemoteClientSocket client = null;
                    try {
                        client = new RemoteClientSocket(socket);
                        ServerManager.getInstance().join(client);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    while(!socket.isClosed()) {
                        client.readStream();
                    }
                });
            }

        } catch (IOException e) {
            throw new RemoteException("Cannot start socket server", e);
        }
    }

    private void startRMI(int port) throws RemoteException {
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind("CodexNaturalisServer51", ServerManager.getInstance()); // TODO config?
    }

}
