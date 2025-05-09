package it.polimi.ingsw.main;

import it.polimi.ingsw.network.serverSide.RemoteClientSocket;
import it.polimi.ingsw.network.serverSide.ServerManager;
import it.polimi.ingsw.utils.JsonConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Class used to start the server.
 */
public class MainServer {
    public static void main(String[] args) {
        MainServer server = new MainServer();
        JsonConfig.loadConfig();

        if(args.length < 1) {
            System.out.println("Usage: java -jar CodexNaturalis.jar <server_ip> [-cli]");
            System.exit(1);
        }

        Thread socketThread = new Thread(
                () -> {
                    int port = JsonConfig.getInstance().getServerSocketPort();
                    System.out.println("Starting Socket server on port " + port + "...");
                    try {
                        server.startSocket(port);
                    } catch (RemoteException e) {
                        System.err.println("Cannot start socket server, socket protocol will ne disabled");
                    }
                });
        socketThread.start();

        Thread rmiThread = new Thread(
                () -> {
                    int port = JsonConfig.getInstance().getRmiRegistryPort();
                    System.out.println("Starting RMI server on port " + port + "...");
                    try {
                        server.startRMI(args[0].substring(1), port);
                        System.out.println("Server rmi ready on port: " + port);
                    } catch (RemoteException e) {
                        System.err.println("Cannot start RMI server, RMI protocol will ne disabled");
                    }
                });
        rmiThread.start();
    }

    /**
     * Starts a socket server on the given port.
     * @param port The port number used to establish a connection.
     * @throws RemoteException If any I/O error occurs.
     */
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

    /**
     * Starts an RMI server on the given port.
     * @param ip The IP address of the machine executing the mainServer.
     * @param port The port number used to establish a connection.
     * @throws RemoteException If any I/O error occurs.
     */
    private void startRMI(String ip, int port) throws RemoteException {
        System.setProperty("java.rmi.server.hostname", ip);
        Registry registry = LocateRegistry.createRegistry(port);
        registry.rebind(JsonConfig.getInstance().getRmiRegistryName(), ServerManager.getInstance());
    }
}
