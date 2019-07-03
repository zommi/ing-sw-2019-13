package client.weapons;

import client.SquareInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The pack that is created inside the client and sent to the server.
 * It will be converted on the server during the validation of the action.
 * It contains players, squares and rooms wrt the micro effect it refers to
 * @author Matteo Pacciani
 */
public class MicroPack implements Serializable {
    /**
     * Index of the corresponding macro effect
     */
    private int macroNumber;

    /**
     * Index of the corresponding micro effect
     */
    private int microNumber;

    /**
     * A list of all the player names that have been chosen by the attacker
     */
    private List<String> playersList;

    /**
     * The square in which all the selected players will be moved
     */
    private SquareInfo square;

    /**
     * A list of the selected rooms
     */
    private List<String> roomsList;

    /**
     * A list of the selected no move squares
     */
    private List<SquareInfo> noMoveSquaresList;

    public MicroPack(int macroNumber, int microNumber){
        this.macroNumber = macroNumber;
        this.microNumber = microNumber;
        playersList = new ArrayList<>();
        roomsList = new ArrayList<>();
        noMoveSquaresList = new ArrayList<>();
    }

    public int getMicroNumber() {
        return microNumber;
    }

    public int getMacroNumber() {
        return macroNumber;
    }

    public void setSquare(SquareInfo square) {
        this.square = square;
    }

    public void setPlayersList(List<String> playersList) {
        this.playersList = playersList;
    }

    public void setNoMoveSquaresList(List<SquareInfo> noMoveSquaresList) {
        this.noMoveSquaresList = noMoveSquaresList;
    }

    public void setRoomsList(List<String> roomsList) {
        this.roomsList = roomsList;
    }

    public List<String> getPlayersList() {
        return playersList;
    }

    public SquareInfo getSquare() {
        return square;
    }

    public List<String> getRoomsList() {
        return roomsList;
    }

    public List<SquareInfo> getNoMoveSquaresList() {
        return noMoveSquaresList;
    }
}
