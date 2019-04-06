
import java.util.*;

/**
 * 
 */
public abstract class PlayerAbstract {
    private String name;
    private Character character;

    /**
     * @return
     */
    public PlayerAbstract(String name) {
        this.name = name;
        this.character = chooseCharacter();
    }

    private Character chooseCharacter() {
    }

    /**
     * @return
     */
    public void spawn(SpawnPoint sp) {
        character.spawn(sp);
    }

    /**
     * @return
     */
    public void move(Square sq) {
        character.move(sq);
    }

    /**
     * @return
     */
    public void shoot() {
    }

    /**
     * @return
     */
    public void collect() {
    }

    /**
     * @return
     */
    public PlayerState getPlayerState() {
    }

}