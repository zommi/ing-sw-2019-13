package server.controller.playeraction;

import constants.Color;
import constants.Directions;
import server.model.cards.Weapon;
import server.model.cards.WeaponCard;
import server.model.map.Room;
import server.model.map.Square;
import server.model.player.PlayerAbstract;
import server.model.cards.*;

import java.util.ArrayList;
import java.util.List;

public class ShootInfo {

    private PlayerAbstract player;
    private Weapon weapon;
    private List<ArrayList<Character>> targets;
    private List<Integer> extra;
    private List<Square> yourSquares;
    private List<Square> targetSquares;
    private List<Square> chosenSquares;
    private List<Room> chosenRooms;

    public  ShootInfo(PlayerAbstract player, Weapon weapon, List<ArrayList<Character>> targets,
                     List<Integer> extra, List<Square> yourSquares, List<Square> targetSquares, List<Square> chosenSquares, List<Room> chosenRooms) {
        this.player = player;
        this.weapon = weapon;
        this.targets = targets;
        this.extra = extra;
        this.yourSquares = yourSquares;
        this.targetSquares = targetSquares;
        this.chosenSquares = chosenSquares;
        this.chosenRooms = chosenRooms;
    }

    public PlayerAbstract getPlayer() {
        return player;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public List<ArrayList<Character>> getTargets() {
        return targets;
    }

    public List<Integer> getExtra() {
        return extra;
    }

    public List<Square> getChosenSquares() {
        return chosenSquares;
    }

    public List<Square> getTargetSquares() {
        return targetSquares;
    }

    public List<Square> getYourSquares() {
        return yourSquares;
    }

    public List<Room> getChosenRooms() {
        return chosenRooms;
    }
}
