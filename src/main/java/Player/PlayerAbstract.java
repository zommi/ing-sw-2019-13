package Player;

import java.util.*;
import Map.*;

/**
 * 
 */
public abstract class PlayerAbstract {
    private String name;
    private Character character;

    /**
     * @return
     */
    PlayerAbstract(){
    }


    public PlayerAbstract(String name) {
        this.name = name;
        this.character = chooseCharacter();
    }

    public Character chooseCharacter() {
        int idSelection;
        Scanner sc = new Scanner(System.in);
        //save a list with all the available characters
        List<Character> list = Character.getValidCharacters();
        //Read input
        do {
            System.out.println("Choose your character:" + list.toString());
            idSelection = sc.nextInt();
        } while(!list.contains(Character.getValue(idSelection)));
        System.out.println("You chose: " + Character.getValue(idSelection) +". Great Choice!");
        //set True corresponding cell
        Character.setTaken(idSelection);
        return Character.getValue(idSelection);
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
    public void move() {
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
        return null;
    }

}