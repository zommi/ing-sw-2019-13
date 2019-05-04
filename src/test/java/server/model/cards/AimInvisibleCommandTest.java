package server.model.cards;

import server.model.map.GameMap;
import server.model.player.GameCharacter;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AimInvisibleCommandTest {
    @Test
    void execute ()  throws exceptions.NoSuchSquareException{ //execute returns an arraylist of arraylist of non visible char

        GameCharacter c = new GameCharacter(server.model.player.Figure.DESTRUCTOR);
        GameCharacter c1 = new GameCharacter(server.model.player.Figure.BANSHEE);
        GameCharacter c2 = new GameCharacter(server.model.player.Figure.DOZER);
        GameCharacter c3 = new GameCharacter(server.model.player.Figure.VIOLET);
        GameCharacter c4 = new GameCharacter(server.model.player.Figure.SPROG);

        GameMap gameMap = new GameMap(1);

        GameMap.getSquare(0,0).addCharacter(c1);
        GameMap.getSquare(0,1).addCharacter(c2);
        GameMap.getSquare(1,2).addCharacter(c);
        GameMap.getSquare(2,2).addCharacter(c3);
        GameMap.getSquare(1,3).addCharacter(c4);


        ArrayList<GameCharacter> e = new ArrayList<GameCharacter>();
        e.add(new GameCharacter(server.model.player.Figure.DESTRUCTOR));
        e.add(new GameCharacter(server.model.player.Figure.VIOLET));
        e.add(new GameCharacter(server.model.player.Figure.SPROG));

        ArrayList<ArrayList<GameCharacter>> e1 = new ArrayList<ArrayList<GameCharacter>>();
        e1.add(e);

        AimInvisibleCommand aim = new AimInvisibleCommand();
        assertEquals(aim.execute(GameMap.getSquare(0,0)), e1);
    }
}
