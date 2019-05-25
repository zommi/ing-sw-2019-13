package client;

import view.ServerAnswer;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Connection extends Serializable {

    public void addPlayerCharacter(String name);

    public boolean CharacterChoice(String name);

    public int getCurrentID();

    public String getCurrentCharacter();

    public int getStartGame();

    public int getInitialSkulls();

    public int getClientID();

    public boolean getError();

    public void send(Info action);

    public String getMap();

    public void configure();

    public GameModel getGameModel();

    public void add(String playerName, int map, int initialSkulls) throws RemoteException;
}
