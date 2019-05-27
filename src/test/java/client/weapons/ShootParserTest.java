package client.weapons;

import client.CliInput;
import client.InputAbstract;
import org.junit.jupiter.api.Test;
import server.controller.playeraction.ShootValidator;
import server.model.gameboard.GameBoard;
import server.model.map.Room;
import server.model.player.ConcretePlayer;
import server.model.player.Figure;
import server.model.player.PlayerAbstract;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShootParserTest {

    @Test
    void getWeaponInput() {
        GameBoard gameBoard = new GameBoard(1,8);
        InputAbstract inputAbstract = new CliInput();
        List<String> playersNamesList = new ArrayList<>();

        List<String> roomNameList = new ArrayList<>();
        for(Room room : gameBoard.getMap().getRooms()){
            roomNameList.add(room.getColor().toString());
        }

        inputAbstract.setRoomsNames(roomNameList);


        PlayerAbstract player1 = new ConcretePlayer("pippo", gameBoard, Figure.DESTRUCTOR);
        PlayerAbstract player2 = new ConcretePlayer("pluto", gameBoard, Figure.BANSHEE);
        PlayerAbstract player3 = new ConcretePlayer("topolino", gameBoard, Figure.DOZER);
        PlayerAbstract player4 = new ConcretePlayer("paperino", gameBoard, Figure.SPROG);
        PlayerAbstract player5 = new ConcretePlayer("minnie", gameBoard, Figure.VIOLET);

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

        player1.spawn(gameBoard.getMap().getSquare(0,2));
        player2.spawn(gameBoard.getMap().getSquare(1,0));
        player3.spawn(gameBoard.getMap().getSquare(1,0));
        player4.spawn(gameBoard.getMap().getSquare(1,0));
        player5.spawn(gameBoard.getMap().getSquare(1,0));

        ShootParser shootParser = new ShootParser();
        ShootPack shootPack = shootParser.getWeaponInput(gameBoard.getWeapon("Zx-2"), inputAbstract);

        ShootValidator shootValidator = new ShootValidator();
        boolean bool = shootValidator.validate(shootPack, gameBoard, player1);

        assertTrue(bool);


    }
}