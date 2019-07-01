package client;

import java.io.Serializable;
import java.rmi.RemoteException;

 public interface Connection extends Serializable {

     int getCurrentID();

     String getCurrentCharacterName();

     int getStartGame();

     int getClientID();

     boolean getError();

     void send(Info action);

     void sendAsynchronous(Info action);

     void configure(String name);

     GameModel getGameModel();

     void setStartGame(int startGame);
     void setCurrentID(int currentID);
     void setCurrentCharacter(String currentCharacter);

}
