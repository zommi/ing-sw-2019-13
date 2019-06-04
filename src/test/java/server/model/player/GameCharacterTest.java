package server.model.player;

import constants.Color;
import exceptions.NoSuchSquareException;
import org.junit.jupiter.api.Test;
import server.model.map.GameMap;
import server.model.map.SquareAbstract;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameCharacterTest {


    @Test
    public void testGetValidFigures(){
        GameCharacter.initialize();
        ArrayList<Figure>  test1= new ArrayList<Figure>();
        test1.add(Figure.DESTRUCTOR);
        test1.add(Figure.BANSHEE);
        test1.add(Figure.DOZER);
        test1.add(Figure.VIOLET);
        test1.add(Figure.SPROG);

        assertEquals(GameCharacter.getValidFigures(),test1);

        ArrayList<Figure>  test2= new ArrayList<Figure>();
        test2.add(Figure.BANSHEE);
        test2.add(Figure.DOZER);
        test2.add(Figure.VIOLET);
        test2.add(Figure.SPROG);

        GameCharacter testChar = new GameCharacter(Figure.DESTRUCTOR);
        ArrayList<GameCharacter> testList = new ArrayList<GameCharacter>();
        testList.add(testChar);
        assertEquals(GameCharacter.getValidFigures(),test2);
        assertEquals(GameCharacter.getTakenCharacters(),testList);

    }

    @Test
    public void testMove() throws NoSuchSquareException {
       GameMap gameMap = new GameMap(1);

       /*SquareAbstract square1 = new Square(3,3, 'w');
       SquareAbstract spawnPoint1 = new SpawnPoint(2,1,'R');*/
       ConcretePlayer player = new ConcretePlayer("pippo");

       SquareAbstract square1 = gameMap.getSquare(0,0);               //il punto 3,3 non esiste nella mappa, l'ho cambiato
       SquareAbstract spawnPoint1 = gameMap.getSpawnPoint(Color.RED);



       player.setPlayerCharacter(Figure.BANSHEE);
       GameCharacter gameCharacter = player.getGameCharacter();

       ArrayList<GameCharacter> tester = new ArrayList<>();
       tester.add(gameCharacter);
       gameCharacter.spawn(square1);
       assertEquals(gameCharacter.getPosition(),square1);
       assertEquals(square1.getCharacters(), tester);
       assertEquals(spawnPoint1.getCharacters(), Collections.EMPTY_LIST);
       gameCharacter.move(spawnPoint1);
       assertEquals(gameCharacter.getPosition(),spawnPoint1);
       assertEquals(spawnPoint1.getCharacters(), tester);
       assertEquals(square1.getCharacters(), Collections.EMPTY_LIST);
    }

}