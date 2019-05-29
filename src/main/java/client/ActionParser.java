package client;

import client.weapons.ShootParser;
import client.weapons.Weapon;
import server.controller.playeraction.Action;
import server.model.cards.WeaponCard;

import java.util.List;
import java.util.Scanner;

public class ActionParser{

    private GameModel gameModel;
    private List<WeaponCard> weaponList;
    private InputAbstract input;

    public ActionParser(){
        input = new CliInput();
    }

    public void addGameModel(GameModel gameModel){
        this.gameModel = gameModel;
        weaponList = gameModel.getWeaponList().getList();
    }

    public InputAbstract getInput(){
        return input;
    }

    public Info createMoveEvent(int coordinatex, int coordinatey) {
        Info moveInfo = new MoveInfo(coordinatex, coordinatey);
        return null;
    }

    public Info createShootEvent(String weaponChosen){
        Weapon weapon = this.weaponFromString(weaponChosen);
        ShootParser shootParser = new ShootParser(gameModel);
        Info shootInfo = shootParser.getWeaponInput(weapon,input);
        return shootInfo;
    }

    public Weapon weaponFromString(String name){
        for(int i = 0; i < weaponList.size(); i++){
            if(name.equalsIgnoreCase(weaponList.get(i).getName())){
                return weaponList.get(i).getWeapon();
            }
        }
        System.out.println("ERROR: THE WEAPON DOES NOT EXIST");
        System.out.println("Insert a new one: ");
        Scanner myObj = new Scanner(System.in);
        String read = myObj.nextLine();
        weaponFromString(read);
        return null;
    }


    public static Info createCollectEvent(int collectDecision) {
        return null;
    }

    public static Info createPowerUpEvent(String PowerUp) {
        return null;
    }
}

