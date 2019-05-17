package server.controller.playeraction;

import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MicroInfo {
    private int microNumber;
    private List<PlayerAbstract> playersList;
    private SquareAbstract square;
    private List<Room> roomsList;
    private List<SquareAbstract> noMoveSquaresList;

    public MicroInfo(int microNumber, List<PlayerAbstract> playersList, SquareAbstract square, List<Room> roomsList, List<SquareAbstract> noMoveSquaresList) {
        this.microNumber = microNumber;
        this.playersList = playersList;
        this.square = square;
        this.roomsList = roomsList;
        this.noMoveSquaresList = noMoveSquaresList;
    }

    public int getMicroNumber() {
        return microNumber;
    }

    public List<PlayerAbstract> getPlayersList() {
        return playersList;
    }

    public List<SquareAbstract> getNoMoveSquaresList() {
        return noMoveSquaresList;
    }

    public List<Room> getRoomsList() {
        return roomsList;
    }

    public SquareAbstract getSquare() {
        return square;
    }

    public PlayerAbstract getFirstPlayer(){
        return playersList.get(0);
    }

    public void actuate(){

    }

}
