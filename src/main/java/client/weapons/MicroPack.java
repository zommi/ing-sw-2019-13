package client.weapons;

import client.SquareInfo;

import java.util.ArrayList;
import java.util.List;

public class MicroPack {
    private int macroNumber;
    private int microNumber;
    private List<String> playersList;
    private SquareInfo square;
    private List<String> roomsList;
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
}
