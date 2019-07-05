package client;

import client.info.Info;

import java.io.Serializable;

/**
 * Interface used to setup client-server connection on client side,
 * it sets up the right class based on client input
 */
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

     /**
      * Main method used to communicate with the server during a player's own turn.
      * @param action Info the player wants to send the server.
      */
     void send(Info action);

     /**
      * Method used for asynchronous communication with server, it is mainly used
      * to send actions that must be done outside a player's turn.
      * @param action Info to be sent to the client.
      */
     void sendAsynchronous(Info action);

     /**
      * Method that sets up the connection between client and server.
      * It's purpose is to add the name of the client to the list of names present on
      * the server.
      * @param name Name of the player chosen by the client.
      */
     void configure(String name);

     /**
      * Method used during the setup phase of the game.
      * @return An istance of the lightweight model.
      */
     GameModel getGameModel();


     void setStartGame(int startGame);
     void setCurrentID(int currentID);
     void setCurrentCharacter(String currentCharacter);

}
