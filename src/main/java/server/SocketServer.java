package server;

import client.ReceiverInterface;
import server.model.game.Game;

import java.net.*;
import java.io.*;

public class SocketServer implements ServerInterface{
    private ServerSocket serverSocket;
    private int port;
    private Game game;


    public SocketServer(int port){
        this.port = port;
    }

    @Override
    public void run(){
        try {
            System.out.println("SERVER RUNNING");
            serverSocket = new ServerSocket(port);
            while(true){ //NOSONAR
                new SocketClientHandler(serverSocket.accept(),this.game).start();
            }
        } catch (IOException e) {
            System.out.println("ERROR DURING SOCKET INITIALIZATION");
            e.printStackTrace();
        }
    }



    @Override
    public int addClient(ReceiverInterface client) {
        return 0;
    }

    public void start(){
        SocketServer server = new SocketServer(1337);
        server.run();
    }
}
