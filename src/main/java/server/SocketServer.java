package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketServer implements Runnable{
    private int port;
    private ExecutorService executorService;
    private Server server;


    SocketServer(int port, Server server){
        this.port = port;
        this.server = server;
        executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void run(){
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("SOCKET SERVER RUNNING");

            startAcceptingConnections(serverSocket);

        } catch (IOException e) {
            System.out.println("Error during ServerSocket initialization, closing program...");
            e.printStackTrace();
            System.exit(0);
        }
    }

    private void startAcceptingConnections(ServerSocket serverSocket){
            while (true) {    //NOSONAR
                try {
                    SocketClientHandler socketClientHandler = new SocketClientHandler(serverSocket.accept(), server);
                    executorService.submit(socketClientHandler);
                }catch(IOException e){
                    //
                }
            }
    }
}
