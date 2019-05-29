package client;

import client.CliInput;
import client.GameModel;
import client.InputAbstract;
import client.weapons.ShootPack;
import client.weapons.ShootParser;
import server.controller.playeraction.ShootValidator;
import server.model.gameboard.GameBoard;
import server.model.map.Room;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TestAcaso {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("./src/main/resources/weaponTest.txt");
        InputStream inputStream = new FileInputStream(file);
        System.setIn(inputStream);

        GameBoard gameBoard = new GameBoard(1,8);
        InputAbstract inputAbstract = new CliInput();
        List<String> playersNamesList = new ArrayList<>();

        List<String> roomNameList = new ArrayList<>();
        for(Room room : gameBoard.getMap().getRooms()){
            roomNameList.add(room.getColor().toString());
        }

        inputAbstract.setRoomsNames(roomNameList);


        PlayerAbstract player1 = new ConcretePlayer("player1");
        PlayerAbstract player2 = new ConcretePlayer("player2");
        PlayerAbstract player3 = new ConcretePlayer("player3");
        PlayerAbstract player4 = new ConcretePlayer("player4");
        PlayerAbstract player5 = new ConcretePlayer("player5");
        player1.setPlayerCharacter(Figure.DESTRUCTOR);
        player2.setPlayerCharacter(Figure.BANSHEE);
        player3.setPlayerCharacter(Figure.DOZER);
        player4.setPlayerCharacter(Figure.SPROG);
        player5.setPlayerCharacter(Figure.VIOLET);

        playersNamesList.add(player2.getName());
        playersNamesList.add(player3.getName());
        playersNamesList.add(player4.getName());
        playersNamesList.add(player5.getName());
        inputAbstract.setPlayersNames(playersNamesList);

        gameBoard.addGameCharacter(player1.getGameCharacter());
        gameBoard.addGameCharacter(player2.getGameCharacter());
        gameBoard.addGameCharacter(player3.getGameCharacter());
        gameBoard.addGameCharacter(player4.getGameCharacter());
        gameBoard.addGameCharacter(player5.getGameCharacter());

        player1.spawn(gameBoard.getMap().getSquare(2,1));
        player2.spawn(gameBoard.getMap().getSquare(2,1));
        player3.spawn(gameBoard.getMap().getSquare(2,1));
        player4.spawn(gameBoard.getMap().getSquare(2,1));
        player5.spawn(gameBoard.getMap().getSquare(2,2));


        ShootParser shootParser = new ShootParser(new GameModel());
        ShootPack shootPack = shootParser.getWeaponInput(gameBoard.getWeapon("sledgehammer"), inputAbstract);

        ShootValidator shootValidator = new ShootValidator();
        boolean bool = shootValidator.validate(shootPack, gameBoard, player1);
        if(bool)
            System.out.println("adsasdfasvkjsrnvlsev");
    }
}
