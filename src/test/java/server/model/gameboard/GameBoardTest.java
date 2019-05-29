package server.model.gameboard;

import constants.Constants;
import org.junit.jupiter.api.Test;
import server.model.map.Room;
import server.model.map.SpawnPoint;
import server.model.map.Square;
import server.model.map.SquareAbstract;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameBoardTest {

    @Test
    public void setupTest(){
        GameBoard testGB = new GameBoard(4,8);

        List<SquareAbstract> testList = new ArrayList<>();

        for(Room room : testGB.getMap().getRooms()){
            testList.addAll(room.getSquares());
        }


        for(SpawnPoint sp : testGB.getMap().getSpawnPoints()){
            assertEquals(Constants.NUMBER_OF_WEAPON_PER_SPAWN_POINT, sp.getWeaponCards().size());
        }

        for(SquareAbstract square : testList){
            System.out.println(square + "\n");
            if(square instanceof Square)assertTrue(((Square) square).getAmmoTile() != null);
        }

        int expectedNumberOfWeaponCardsAfterSetup = Constants.NUMBER_OF_WEAPONS - testGB.getMap().getSpawnPoints().size()*3;
        int expectedNumberOfAmmoTilesAfterSetup = Constants.NUMBER_OF_AMMOTILE - testList.size() + testGB.getMap().getSpawnPoints().size();

        assertEquals(expectedNumberOfAmmoTilesAfterSetup, testGB.getAmmoTileDeck().getDeck().size());
        assertEquals(expectedNumberOfWeaponCardsAfterSetup, testGB
                .getWeaponDeck().getSize());
    }

}