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

    public Info createMoveEvent(int coordinatex, int coordinatey) {
        return new MoveInfo(coordinatex, coordinatey);
    }

    public Info createShootEvent(WeaponCard weaponCard){
        ShootParser shootParser = new ShootParser(gameModel, input);
        return shootParser.getWeaponInput(weaponCard);
    }

    public Info createCollectEvent(int row, int col, int collectDecision, WeaponCard weaponToDiscard, List<PowerUpCard> powerUpCards) {
        CollectInfo collectInfo = new CollectInfo(row, col, collectDecision, weaponToDiscard);
        collectInfo.setPowerUpCards(powerUpCards);
        return  collectInfo;
    }

    public Info createPowerUpEvent(PowerUpCard powerUpCard) {
        PowerUpParser powerUpParser = new PowerUpParser(input);
        return powerUpParser.getPowerUpInput(powerUpCard);
    }

    public Info createSpawnEvent(PowerUpCard powerupCard){
        return new SpawnInfo(powerupCard);
    }

    public Info createReloadEvent(List<WeaponCard> weaponCards, List<PowerUpCard> powerUpCards){
        return new ReloadInfo(weaponCards, powerUpCards);
    }
}

