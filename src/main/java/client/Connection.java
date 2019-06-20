package client;

import server.model.map.GameMap;
import view.ServerAnswer;

import java.io.Serializable;
import java.rmi.RemoteException;

public interface Connection extends Serializable {

    public void addPlayerCharacter(String name);

    public boolean isCharacterChosen(String name);

    public String getCharacterName();

    public int getCurrentID();

    public int getGrenadeID();

    public String getCurrentCharacterName();

    public int getStartGame();

    public int getInitialSkulls();

    public int getClientID();

    public boolean getError();

    public void send(Info action);

    public void sendAsynchronous(Info action);

    public String getMapName();

    public void configure(String name);

    public GameModel getGameModel();

    public void add(String name, int map, int initialSkulls);

    void setClientID(int clientID);
    void setMapNum(int mapNum);
    void setInitialSkulls(int initialSkulls);
    void setStartGame(int startGame);
    void setGrenadeID(int grenadeID);
    void setCurrentID(int currentID);
    void setCurrentCharacter(String currentCharacter);

}
