package Player;

import Exceptions.CharacterTakenException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {

    @Test public void testCreator(){
        Character test1 = new Character(1);

        Character.getValidCharacters();

    }

}