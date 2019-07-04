package server.controller.playeraction;

import client.info.Info;
import client.weapons.MicroEffect;
import constants.Color;
import server.model.map.Room;
import server.model.map.SquareAbstract;
import server.model.player.PlayerAbstract;


import java.io.Serializable;
import java.util.List;

/**
 * A {@link client.weapons.MicroPack} that is converted on the server with the right references to the objects on the server
 * @author Matteo Pacciani
 */
public class MicroInfo implements Serializable, Info {
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

    /**
     * Actuates all the effects of the related micro effect. Deals damage and marks to all the players in the list,
     * moves all the selected players in the specified square if {@link MicroEffect#isMoveFlag()} is true
     * @param shootInfo the {@link ShootInfo} this MicroInfo is part of
     */
    public void actuate(ShootInfo shootInfo){
        Color color = shootInfo.getAttacker().getColor();
        int damage = shootInfo.getWeapon()
                .getMicroEffect(macroNumber, microNumber).getDamage();
        for (PlayerAbstract playerAbstract : playersList) {
            if(damage > 0) {
                playerAbstract.addDamage(damage, color);
                if(playerAbstract.getJustDamagedBy() == null)
                    playerAbstract.getPlayerBoard().marks2Damage(shootInfo.getAttacker().getColor());
                playerAbstract.setJustDamagedBy(shootInfo.getAttacker());

            }
            playerAbstract.addMarks(shootInfo.getWeapon()
                    .getMicroEffect(macroNumber, microNumber).getMarks(), color);

            if (shootInfo.getWeapon()
                    .getMicroEffect(macroNumber, microNumber).isMoveFlag())
                playerAbstract.getGameCharacter().move(this.square);
        }
        //not checking if getMicroEffect==null cause it has already been validated
    }

    /**
     * Just moves all the players in the selected square. This is used by special weapons, because if the validation fails,
     * old positions will be restored. This is made to avoid restoring damage and marks too.
     * @param shootInfo the {@link ShootInfo} this MicroInfo is part of
     */
    public void fakeActuate(ShootInfo shootInfo) {
        for (PlayerAbstract playerAbstract : playersList) {
            if (shootInfo.getWeapon()
                    .getMicroEffect(macroNumber, microNumber).isMoveFlag())
                playerAbstract.getGameCharacter().move(this.square);
        }
        //not checking if getMicroEffect==null cause it has already been validated
    }
}
