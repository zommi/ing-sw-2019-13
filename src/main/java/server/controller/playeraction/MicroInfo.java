package server.controller.playeraction;

import client.ShootInfo;
import constants.Color;
import exceptions.NoSuchEffectException;
import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;

import java.util.List;

public class MicroInfo {
    private int macroNumber;
    private int microNumber;
    private List<PlayerAbstract> playersList;
    private SquareAbstract square;
    private List<Room> roomsList;
    private List<SquareAbstract> noMoveSquaresList;

    public MicroInfo(int macroNumber, int microNumber, List<PlayerAbstract> playersList, SquareAbstract square, List<Room> roomsList, List<SquareAbstract> noMoveSquaresList) {
        this.macroNumber = macroNumber;
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

    public Room getFirstRoom(){
        return roomsList.get(0);
    }

    public SquareAbstract getFirstNMS(){
        return noMoveSquaresList.get(0);
    }

    public void setSquare(SquareAbstract square){
        this.square = square;
    }

    public void actuate(ShootInfo shootInfo){
        Color color = shootInfo.getAttacker().getColor();
        try {
            for (PlayerAbstract playerAbstract : playersList) {
                playerAbstract.addDamage(shootInfo.getWeapon()
                        .getMicroEffect(macroNumber, microNumber).getDamage(), color);
                playerAbstract.addMarks(shootInfo.getWeapon()
                        .getMicroEffect(macroNumber, microNumber).getMarks(), color);
                if (shootInfo.getWeapon()
                        .getMicroEffect(macroNumber, microNumber).isMoveFlag())
                    playerAbstract.getGameCharacter().move(this.square);
            }
        } catch(NoSuchEffectException e){
            //this should never happen
        }

    }

}
