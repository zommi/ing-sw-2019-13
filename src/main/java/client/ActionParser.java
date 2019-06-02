package client;

import client.powerups.PowerUpParser;
import client.weapons.ShootParser;
import client.weapons.Weapon;
import server.model.cards.PowerUp;
import server.model.cards.PowerUpCard;

import java.util.Scanner;

public class ActionParser{

    private GameModel gameModel;
    private InputAbstract input;

    public ActionParser(){
        input = new CliInput();
    }

    public void addGameModel(GameModel gameModel){
        this.gameModel = gameModel;
    }

    public InputAbstract getInput(){
        return input;
    }

    public Info createMoveEvent(int coordinatex, int coordinatey) {
        return new MoveInfo(coordinatex, coordinatey);
    }

    public Info createShootEvent(String weaponChosen){
        Weapon weapon = this.weaponFromString(weaponChosen);
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


    public Info createCollectEvent(int x, int y, int collectDecision) {
        return new CollectInfo(x, y, collectDecision);
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

