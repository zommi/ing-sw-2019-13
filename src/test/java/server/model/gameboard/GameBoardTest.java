package server.model.gameboard;

import constants.Constants;
import server.model.map.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    @Test
    public void setupTest(){
        GameBoard testGB = GameBoard.instance(4,8);

        List<SquareAbstract> testList = new ArrayList<>();

        for(Room room : GameMap.getRooms()){
            testList.addAll(room.getSquares());
        }

        testGB.setupGameBoard();

        for(SpawnPoint sp : GameMap.getSpawnPoints()){
            assertEquals(Constants.NUMBER_OF_WEAPON_PER_SPAWN_POINT, sp.getWeaponCards().size());
        }

        for(SquareAbstract square : testList){
            System.out.println(square + "\n");
            if(square instanceof Square)assertTrue(((Square) square).getAmmoTile() != null);
        }

        int expectedNumberOfWeaponCardsAfterSetup = Constants.NUMBER_OF_WEAPONS - GameMap.getSpawnPoints().size()*3;
        int expectedNumberOfAmmoTilesAfterSetup = Constants.NUMBER_OF_AMMOTILE - testList.size() + GameMap.getSpawnPoints().size();

        assertEquals(expectedNumberOfAmmoTilesAfterSetup, testGB.getAmmoTileDeck().getDeck().size());
        assertEquals(expectedNumberOfWeaponCardsAfterSetup, testGB
                .getWeaponDeck().getSize());
    }

}