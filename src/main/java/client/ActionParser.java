package client;

import client.cli.UpdaterCLI;
import client.powerups.PowerUpParser;
import client.weapons.ShootParser;
import client.weapons.Weapon;
import server.model.cards.PowerUp;
import server.model.cards.PowerUpCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ActionParser{

    private GameModel gameModel;
    private InputAbstract input;

    public ActionParser(Updater updater){
        if(updater instanceof UpdaterCLI) {
            input = new CliInput();
        } else{
            input = new GuiInput();
        }
    }

    public void addGameModel(GameModel gameModel, String name){
        this.gameModel = gameModel;
        List<String> pNames = new ArrayList<>();
        for(String string : gameModel.getPlayersNames())
            if(!string.equals(name))
                pNames.add(string);
        setPlayersNames(pNames);
        //TODO initialize rooms
    }

    public InputAbstract getInput(){
        return input;
    }

    public Info createMoveEvent(int coordinatex, int coordinatey) {
        return new MoveInfo(coordinatex, coordinatey);
    }

    public Info createShootEvent(Weapon weapon){
        ShootParser shootParser = new ShootParser(gameModel);
        return shootParser.getWeaponInput(weapon,input);
    }

    public Weapon weaponFromString(String name){
        for(int i = 0; i < gameModel.getPlayerHand().getWeaponHand().size(); i++){
            if(name.equalsIgnoreCase(gameModel.getPlayerHand().getWeaponHand().get(i).getName())){
                return gameModel.getPlayerHand().getWeaponHand().get(i).getWeapon();
            }
        }
        System.out.println("ERROR: THE WEAPON DOES NOT EXIST OR YOU DON'T HAVE IT");
        System.out.println("Insert a new one: ");
        Scanner myObj = new Scanner(System.in);
        String read = myObj.nextLine();
        weaponFromString(read);
        return null;
    }

    public void setPlayersNames(List<String> names){
        this.input.setPlayersNames(names);
    }

    public Info createCollectEvent(int row, int col, int collectDecision) {
        return new CollectInfo(row, col, collectDecision);
    }

    public Info createPowerUpEvent(String powerUpChosen) {
        PowerUp powerUp = this.powerUpFromString(powerUpChosen);
        PowerUpParser powerUpParser = new PowerUpParser(gameModel);
        return powerUpParser.getPowerUpInput(powerUp,input);
    }


    public PowerUp powerUpFromString(String name){
        for(int i = 0; i < gameModel.getPlayerHand().getPowerupHand().size(); i++){
            if(name.equalsIgnoreCase(gameModel.getPlayerHand().getPowerupHand().get(i).getName())){
                return gameModel.getPlayerHand().getPowerupHand().get(i).getPowerUp();
            }
        }
        System.out.println("ERROR: THE POWERUP DOES NOT EXIST OR YOU DON'T HAVE IT");
        System.out.println("Insert a new one: ");
        Scanner myObj = new Scanner(System.in);
        String read = myObj.nextLine();
        powerUpFromString(read);
        return null;
    }

    public Info createSpawEvent(PowerUpCard powerupCard){
        return new SpawnInfo(powerupCard);
    }
}

