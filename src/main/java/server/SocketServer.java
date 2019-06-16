package server;

import client.Connection;
import client.ReceiverInterface;
import server.model.game.Game;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer implements Runnable{
    private ServerSocket serverSocket;
    private int port;
    private Game game;
    private List<SocketClientHandler> clientsAdded;
    private ExecutorService executorService;
    private Server server;


    public SocketServer(int port, Server server){
        this.port = port;
        this.server = server;
        executorService = Executors.newCachedThreadPool();
        clientsAdded = new ArrayList<>();
    }

    @Override
    public void run(){
        try {
            System.out.println("SOCKET SERVER RUNNING");
            serverSocket = new ServerSocket(port);
            while(true){    //NOSONAR
                SocketClientHandler socketClientHandler = new SocketClientHandler(serverSocket.accept(), server);
                clientsAdded.add(socketClientHandler);
                executorService.submit((SocketClientHandler)socketClientHandler);
            }
        } catch (IOException e) {
            System.out.println("ERROR DURING SOCKET INITIALIZATION");
            e.printStackTrace();
        }
    }

    public List<SocketClientHandler> getClientsAdded() {
        return clientsAdded;
    }


    //public int addClient(ReceiverInterface client) {
    //    return 0;
    //}
}
