package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final Registry registry;
    private static final String REGISTRATION_ROOM_NAME = "gameproxy";


    public Server() throws RemoteException{
        this.registry = LocateRegistry.createRegistry(1099);
    }


    public Registry getRegistry(){
        return this.registry;
    }

    public String getName(){  //RETURNS the name of the remote object
        return REGISTRATION_ROOM_NAME;
    }

    public void startRMI(){ //creates a new thread with the RMI connection
        ExecutorService executorRMI = Executors.newCachedThreadPool();
        executorRMI.submit(new ServerRMI(this));
    }


    public static void main(String[] args) throws RemoteException{
        Server server = new Server();
        server.startRMI();
    }
}
