package server.controller.playeraction;

import constants.Color;
import constants.Directions;
import server.model.cards.Weapon;
import server.model.cards.WeaponCard;
import server.model.map.Room;
import server.model.map.Square;
import server.model.map.SquareAbstract;
import server.model.player.GameCharacter;
import server.model.player.PlayerAbstract;
import server.model.cards.*;

import java.util.ArrayList;
import java.util.List;

public class ShootInfo {

    private PlayerAbstract player;
    private Weapon weapon;
    private List<ArrayList<GameCharacter>> targets;
    private List<Integer> extra;
    private List<SquareAbstract> yourSquares;
    private List<SquareAbstract> targetSquares;
    private SquareAbstract chosenSquare;
    private Room chosenRoom;

    public  ShootInfo(PlayerAbstract player, Weapon weapon, List<ArrayList<GameCharacter>> targets,
                     List<Integer> extra, List<SquareAbstract> yourSquares,
                      List<SquareAbstract> targetSquares, Square chosenSquare, Room chosenRoom) {
        this.player = player;
        this.weapon = weapon;
        this.targets = targets;
        this.extra = extra;
        this.yourSquares = yourSquares;
        this.targetSquares = targetSquares;
        this.chosenSquare = chosenSquare;
        this.chosenRoom = chosenRoom;
    }

    public PlayerAbstract getPlayer() {
        return player;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public List<ArrayList<GameCharacter>> getTargets() {
        return targets;
    }

    public List<Integer> getExtra() {
        return extra;
    }

    public SquareAbstract getChosenSquare() {
        return chosenSquare;
    }

    public List<SquareAbstract> getTargetSquares() {
        return targetSquares;
    }

    public List<SquareAbstract> getYourSquares() {
        return yourSquares;
    }

    public Room getChosenRoom() {
        return chosenRoom;
    }
}
