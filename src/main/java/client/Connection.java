package client;

import java.io.Serializable;
import java.rmi.RemoteException;

 public interface Connection extends Serializable {

     int getCurrentID();

     int getGrenadeID();

     String getCurrentCharacterName();

     int getStartGame();

     int getClientID();

     boolean getError();

     void send(Info action);

     void sendAsynchronous(Info action);

     void configure(String name);

     GameModel getGameModel();

     void setClientID(int clientID);
     void setMapNum(int mapNum);
     void setInitialSkulls(int initialSkulls);
     void setStartGame(int startGame);
     void setGrenadeID(int grenadeID);
     void setCurrentID(int currentID);
     void setCurrentCharacter(String currentCharacter);

}
