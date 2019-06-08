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
            input = new CliInput();
        } else{
            input = new GuiInput(updater);
        }
    }

    public void addGameModel(GameModel gameModel, String name){
        this.gameModel = gameModel;
        input.setGameModel(gameModel);

        //initialize players names
        List<String> pNames = new ArrayList<>();
        for(String string : gameModel.getPlayersNames())
            if(!string.equalsIgnoreCase(name))
                pNames.add(string);
        setPlayersNames(pNames);

        //initialize rooms names
        List<String> rNames = new ArrayList<>();
        for(Room room : gameModel.getMap().getRooms())
            rNames.add(room.getColor().name());
        setRoomsNames(rNames);
    }

    public InputAbstract getInput(){
        return input;
    }

    public Info createMoveEvent(int coordinatex, int coordinatey) {
        return new MoveInfo(coordinatex, coordinatey);
    }

    public Info createShootEvent(Weapon weapon){
        ShootParser shootParser = new ShootParser(gameModel, input);
        return shootParser.getWeaponInput(weapon);
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
        input.setPlayersNames(names);
    }

    public void setRoomsNames(List<String> names){
        input.setRoomsNames(names);
    }

    public Info createCollectEvent(int row, int col, int collectDecision, WeaponCard weaponToDiscard, List<PowerUpCard> powerUpCards) {
        CollectInfo collectInfo = new CollectInfo(row, col, collectDecision, weaponToDiscard);
        collectInfo.setPowerUpCards(powerUpCards);
        return  collectInfo;
    }

    public Info createPowerUpEvent(PowerUp powerUp) {
        PowerUpParser powerUpParser = new PowerUpParser(gameModel);
        return powerUpParser.getPowerUpInput(powerUp,input);
    }

    public Info createSpawnEvent(PowerUpCard powerupCard){
        return new SpawnInfo(powerupCard);
    }
}

