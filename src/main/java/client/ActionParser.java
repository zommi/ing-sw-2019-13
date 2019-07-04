package client;

import client.cli.UpdaterCLI;
import client.powerups.PowerUpParser;
import client.weapons.ShootParser;
import client.weapons.Weapon;
import server.model.cards.PowerUp;
import server.model.cards.PowerUpCard;
import server.model.cards.WeaponCard;
import server.model.map.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ActionParser{

    private GameModel gameModel;
    private InputAbstract input;

    public ActionParser(Updater updater){
        if(updater instanceof UpdaterCLI) {
            input = new CliInput(this);
        } else{
            input = new GuiInput(updater);
        }
    }

    public void addGameModel(GameModel gameModel){
        this.gameModel = gameModel;
        input.setGameModel(gameModel);
    }

    public InputAbstract getInput(){
        return input;
    }

    /**
     * The method creates a move action that the client can send to the server
     * @param coordinatex where the player wants to move
     * @param coordinatey where the player wants to move
     * @return the Info, which is the move action to send to the server
     */
    public Info createMoveEvent(int coordinatex, int coordinatey) {
        return new MoveInfo(coordinatex, coordinatey);
    }

    /**
     * The method creates a shoot action that the client can send to the server
     * @param weaponCard that the player wants to use to shoot to a target
     * @return the Info, which is the shoot action to send to the server
     */
    public Info createShootEvent(WeaponCard weaponCard){
        ShootParser shootParser = new ShootParser(gameModel, input);
        return shootParser.getWeaponInput(weaponCard);
    }

    /**
     * The method creates a collect action that the client can send to the server
     * @param row that the client chose to collect from
     * @param col that the client chose to collect from
     * @param collectDecision indicating whether the client wants to collect a weapon or an ammo
     * @param weaponToDiscard which is the weapon the client chooses to discard if he already has 3 weapons in his hands and wants to collect a forth one
     * @param powerUpCards the client wants to pay with
     * @return the Info, which is the collect action to send to the server
     */
    public Info createCollectEvent(int row, int col, int collectDecision, WeaponCard weaponToDiscard, List<PowerUpCard> powerUpCards) {
        CollectInfo collectInfo = new CollectInfo(row, col, collectDecision, weaponToDiscard);
        collectInfo.setPowerUpCards(powerUpCards);
        return  collectInfo;
    }

    /**
     * The method creates a powerup action that the client can send to the server
     * @param powerUpCard the client wants to use
     * @return the Info, which is the powerup action to send to the server
     */
    public Info createPowerUpEvent(PowerUpCard powerUpCard) {
        PowerUpParser powerUpParser = new PowerUpParser(input);
        return powerUpParser.getPowerUpInput(powerUpCard);
    }

    /**
     * The method creates a spawn action that the client can send to the server
     * @param powerupCard the client wants to discard
     * @return the Info, which is the spawn action to send to the server
     */
    public Info createSpawnEvent(PowerUpCard powerupCard){
        return new SpawnInfo(powerupCard);
    }

    /**
     * The method creates a reload action that the client can send to the server
     * @param weaponCards that the client wants to reload
     * @param powerUpCards that the client wants to pay with
     * @return the Info, which is the reload action to send to the server
     */
    public Info createReloadEvent(List<WeaponCard> weaponCards, List<PowerUpCard> powerUpCards){
        return new ReloadInfo(weaponCards, powerUpCards);
    }
}

