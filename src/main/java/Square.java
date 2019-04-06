
import java.util.*;

public class Square implements SquareInterface{

    private ArrayList<Character> charactersList;
    private Room room;
    private Color color
    private int xValue;
    private int yValue;
    // the following are just for visible squares
    private SquareInterface nSquare;
    private SquareInterface wSquare;
    private SquareInterface eSquare;
    private SquareInterface sSquare;

    public Square(int x, int y, char color) {
        this.xValue = x;
        this.yValue = y;
        //this.color = color;

        //new character list
        this.charactersList= new ArrayList<Character>();


    }

    public Room getRoom(){
        return room;
    }


    public SquareInterface geteSquare() {
        return eSquare;
    }
    public void seteSquare(SquareInterface eSquare) {
        this.eSquare = eSquare;
    }
    public SquareInterface getnSquare() {
        return nSquare;
    }
    public void setnSquare(SquareInterface nSquare) {
        this.nSquare = nSquare;
    }
    public SquareInterface getwSquare() {
        return wSquare;
    }
    public void setwSquare(SquareInterface wSquare) {
        this.wSquare = wSquare;
    }
    public SquareInterface getsSquare() {
        return sSquare;
    }
    public void setsSquare(SquareInterface sSquare) {
        this.sSquare = sSquare;
    }
    public int getxValue() {
        return xValue;
    }
    public int getyValue() {
        return yValue;
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