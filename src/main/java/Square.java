
import java.util.*;

public class Square {

    private ArrayList<Character> charactersList;
    private Room room;
    private int xValue;
    private int yValue;
    // the following are just for visible squares
    private Square nSquare;
    private Square wSquare;
    private Square eSquare;
    private Square sSquare;

    public Square() {
        //new character list
        this.charactersList= new ArrayList<Character>();


    }

    public Room getRoom(){
        return room;
    }

    public Square geteSquare() {
        return eSquare;
    }

    public Square getnSquare() {
        return nSquare;
    }

    public Square getwSquare() {
        return wSquare;
    }

    public Square getsSquare() {
        return sSquare;
    }

    /**
     * @return
     */
    public List<Character> getCharacters() {
        return (ArrayList<Character>)  charactersList.clone();
    }

    public void addCharacter(Character character) {

        charactersList.add(character);
    }
    public void removeCharacter(Character character) {
        charactersList.remove(character);
    }

    public List<Room> getVisibleRooms() {
        ArrayList<Room> roomsList = new ArrayList<Room>();
        if(nSquare != null)
            roomsList.add(nSquare.getRoom());
        if(sSquare != null)
            roomsList.add(sSquare.getRoom());
        if(wSquare != null)
            roomsList.add(wSquare.getRoom());
        if(eSquare != null)
            roomsList.add(eSquare.getRoom());

        return roomsList;

    }

    /**
     * @return
     */
    public Color getColor() {
        return room.getColor();
    }

    /**
     * @return
     */
    public List<Square> getAdjacentSquares() {
        ArrayList<Square> squaresList= new ArrayList<Square>();
        if(nSquare != null)
            squaresList.add(nSquare);
        if(sSquare != null)
            squaresList.add(sSquare);
        if(wSquare != null)
            squaresList.add(wSquare);
        if(eSquare != null)
            squaresList.add(eSquare);
        return squaresList;
    }

}