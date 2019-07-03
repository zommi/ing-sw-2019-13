package server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer implements Runnable{
    private int port;
    private ExecutorService executorService;
    private Server server;


    public SocketServer(int port, Server server){
        this.port = port;
        this.server = server;
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("SOCKET SERVER RUNNING");

            while(true){    //NOSONAR
                SocketClientHandler socketClientHandler = new SocketClientHandler(serverSocket.accept(), server);
                executorService.submit(socketClientHandler);
            }
        } catch (IOException e) {
            System.out.println("ERROR DURING SOCKET INITIALIZATION");
            e.printStackTrace();
        }
    }
}
