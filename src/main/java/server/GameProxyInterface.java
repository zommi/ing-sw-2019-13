package server;

import client.ReceiverInterface;
import server.controller.playeraction.Action;

import java.rmi.Remote;

public interface GameProxyInterface extends Remote {

    public boolean makeAction(int clientID, Action action);

    public GameProxyInterface register(ReceiverInterface client);


    public boolean sendPlayerName(String name);

    public boolean sendMap(int numMap);
}
