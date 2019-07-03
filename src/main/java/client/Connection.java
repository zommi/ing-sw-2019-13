package client;

import java.io.Serializable;
import java.rmi.RemoteException;

 public interface Connection extends Serializable {

     int getCurrentID();

     /**
      * This method asks to the server the name of the current character
      * @return the name of the current character
      */
     String getCurrentCharacterName();

     /**
      * This method asks to the server if the game has started
      * @return an int that indicates if the game has started
      */
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
