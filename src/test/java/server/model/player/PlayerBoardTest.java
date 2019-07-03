package server.model.player;

import constants.Color;
import constants.Constants;
import exceptions.InvalidMoveException;
import org.junit.jupiter.api.Test;
import server.model.cards.AmmoTile;
import server.model.gameboard.GameBoard;
import server.model.items.AmmoCube;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PlayerBoardTest {
    @Test
    public void drawAmmoTileTest(){
        GameBoard testGameBoard = new GameBoard(4,8);
        ConcretePlayer testPlayer = new ConcretePlayer("Carlo");
        testPlayer.setPlayerCharacter(Figure.BANSHEE);

        PlayerBoard testPlayerBoard = testPlayer.getPlayerBoard();
        testPlayerBoard.setAmmo(new int[]{1, 1,1});

        List<AmmoCube> testList = new ArrayList<>();
        testList.add(new AmmoCube(Color.RED));
        testList.add(new AmmoCube(Color.RED));
        testList.add(new AmmoCube(Color.BLUE));
        testList.add(new AmmoCube(Color.RED));
        AmmoTile testAmmoTile = new AmmoTile(testList, false);


        testPlayerBoard.processAmmoTile(testAmmoTile);

        assertEquals(3,testPlayerBoard.getRedAmmo());
        assertEquals(2,testPlayerBoard.getBlueAmmo());
        assertEquals(1,testPlayerBoard.getYellowAmmo());

        testList.add(new AmmoCube(Color.YELLOW));


        try {
            testPlayerBoard.useAmmo(testList);
        } catch (InvalidMoveException e) {
            e.printStackTrace();
        }

        assertEquals(0,testPlayerBoard.getRedAmmo());
        assertEquals(1,testPlayerBoard.getBlueAmmo());
        assertEquals(0,testPlayerBoard.getYellowAmmo());

        assertThrows(InvalidMoveException.class, () ->
                testPlayerBoard.useAmmo(testList));
    }
}