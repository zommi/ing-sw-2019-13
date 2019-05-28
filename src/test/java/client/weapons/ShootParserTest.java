package client.weapons;

import client.CliInput;
import client.GameModel;
import client.InputAbstract;
import org.junit.jupiter.api.Test;
import server.controller.playeraction.ShootValidator;
import server.model.gameboard.GameBoard;
import server.model.map.Room;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ShootParserTest {

    @Test
    void getWeaponInput(){

//        ByteArrayInputStream in = new ByteArrayInputStream(
//                ("n" + System.lineSeparator() + "0" + System.lineSeparator() + "y" + System.lineSeparator() +
//                        "0" + System.lineSeparator() + "2" + System.lineSeparator() + "y" + System.lineSeparator()).getBytes());

        //ByteArrayInputStream in = new ByteArrayInputStream(("n\n0\ny\n0\n2\ny\n").getBytes());
//        FileInputStream in = new FileInputStream(new File("." + File.separatorChar + "src" +
//                File.separatorChar + "main" + File.separatorChar + "resources" + "/weaponTest.txt"));
//        System.setIn(in);
//        Scanner sc = new Scanner(System.in);
//        while(sc.hasNextLine()){
//            System.out.println(sc.nextLine());
//        }

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

        player1.spawn(gameBoard.getMap().getSquare(1,3));
        player2.spawn(gameBoard.getMap().getSquare(1,2));
        player3.spawn(gameBoard.getMap().getSquare(1,2));
        player4.spawn(gameBoard.getMap().getSquare(1,1));
        player5.spawn(gameBoard.getMap().getSquare(1,1));


        /*ShootParser shootParser = new ShootParser(new GameModel());
        ShootPack shootPack = shootParser.getWeaponInput(gameBoard.getWeapon("Grenade Launcher"), inputAbstract);

        ShootValidator shootValidator = new ShootValidator();
        boolean bool = shootValidator.validate(shootPack, gameBoard, player1);

        assertTrue(bool);*/

        //System.setIn(System.in);
    }
}
