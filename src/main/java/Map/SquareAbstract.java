package Map;

import Player.Character;

import java.util.ArrayList;
import java.util.List;

public abstract class SquareAbstract {

    private ArrayList<Character> charactersList;

    private Color color;
    private int xValue;
    private int yValue;
    // the following are just for visible squares
    //make optional??
    private SquareAbstract nSquare;
    private SquareAbstract wSquare;
    private SquareAbstract eSquare;
    private SquareAbstract sSquare;

    private Room room;

    public Room getRoom() {
        return room;
    }

    public Color getColor() {
        return color;
    }

    public SquareAbstract(int x, int y, char color) {
        this.xValue = x;
        this.yValue = y;
        this.color = color;   //TODO

        //new character list
        this.charactersList= new ArrayList<Character>();


    }

    public SquareAbstract geteSquare() {
        return eSquare;
    }
    public void seteSquare(SquareAbstract eSquare) {
        this.eSquare = eSquare;
    }
    public SquareAbstract getnSquare() {
        return nSquare;
    }
    public void setnSquare(SquareAbstract nSquare) {
        this.nSquare = nSquare;
    }
    public SquareAbstract getwSquare() {
        return wSquare;
    }
    public void setwSquare(SquareAbstract wSquare) {
        this.wSquare = wSquare;
    }
    public SquareAbstract getsSquare() {
        return sSquare;
    }
    public void setsSquare(SquareAbstract sSquare) {
        this.sSquare = sSquare;
    }

    public int getxValue() {
        return xValue;
    }
    public int getyValue() {
        return yValue;
    }

    public void addCharacter(Character character){
        charactersList.add(character);
    }

    public void removeCharacter(Character character){
        charactersList.remove(character);
    }

    public List<Character> getCharacters() {
        return (ArrayList<Character>)  charactersList.clone();
    }

    public List<Character> getVisibleCharacters(){
        List<Character> visibleCharacters = new ArrayList<>();
        visibleCharacters.addAll(room.getCharacters());
        for(Room tempRoom : this.getVisibleRooms()){
            visibleCharacters.addAll(tempRoom.getCharacters());
        }
        return visibleCharacters;

    }

    public List<Character> getCharactersThroughWalls(){

    }

    public List<Character> getTwoMovementsCharacters(){
        List<Character> charactersList = new ArrayList<>();
        List<SquareAbstract> tempSquareList = new ArrayList<>();
        tempSquareList.add(this);
        for(SquareAbstract square : this.getAdjacentSquares()){
            charactersList.addAll(square.getOneMovementCharacters());
        }
        charactersList.removeAll(tempSquareList);
        return charactersList;

    }

    public List<Character> getOneMovementCharacters(){
        List<Character> charactersList = new ArrayList<>();
        for(SquareAbstract square : this.getAdjacentSquares()){
            charactersList.addAll(square.getCharacters());
        }
        return charactersList;
    }

    public List<Room> getVisibleRooms() {
        ArrayList<Room> roomsList = new ArrayList<Room>();
        if(nSquare != null && nSquare.getRoom() != room)
                roomsList.add(nSquare.getRoom());
        if(sSquare != null && sSquare.getRoom() != room)
                roomsList.add(sSquare.getRoom());
        if(wSquare != null && wSquare.getRoom() != room)
                roomsList.add(wSquare.getRoom());
        if(eSquare != null && eSquare.getRoom() != room)
                roomsList.add(eSquare.getRoom());

        return roomsList;

    }

    public List<SquareAbstract> getAdjacentSquares() {
        ArrayList<SquareAbstract> squaresList= new ArrayList<SquareAbstract>();
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
