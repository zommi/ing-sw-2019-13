
import java.util.*;
import java.lang.*;
/**
 * 
 */
public enum Character {
    DESTRUCTOR (0),
    BANSHEE (1),
    DOZER (2),
    VIOLET (3),
    SPROG (4);

    private static final int NUM_FIGURES = 5;
    private static boolean[] figureChosen = new boolean[NUM_FIGURES];
    private int id;

    private Square position;

    Character(int id){
        this.id = id;
        setTaken(id);
    }

    private int getId(){
        return this.id;
    }

    public static void setTaken(int id){
        figureChosen[id] = true;
    }

    public static boolean isTaken(int id){
        return figureChosen[id];
    }

    public static List<Character> getValidCharacters(){
        //Maybe implement in java functional
        List<Character> res = new ArrayList<>();
        for(Character c : Character.values()){
            if(!isTaken(c.getId())){
                res.add(c);
            }
        }
        return res;
    }

    public static Character getValue(int id){
        switch(id) {
            case 0 : return DESTRUCTOR;
            case 1 : return BANSHEE;
            case 2 : return DOZER;
            case 3 : return VIOLET;
            case 4 : return SPROG;
            default: return null;
        }
    }

    @Override
    public String toString() {
        switch(getId()) {
            case 0 : return "Destructor";
            case 1 : return "Banshee";
            case 2 : return "Dozer";
            case 3 : return "Violet";
            case 4 : return "Sprog";
            default: return "";
        }
    }

    private void setPosition(Square position){
        this.position = position;
    }
    /**
     *
     * @param sq
     */
    public void move(Square sq) {
        getPosition().removeCharacter(this);
        setPosition(sq);
        sq.addCharacter(this);
    }

    /**
     * @return
     */
    public Square getPosition() {
        return position;
    }

    public void spawn(SpawnPoint sp) {
        if(position == null){
            setPosition(sp);
        } else{
            System.out.println("Invalid move");
        }
    }
}