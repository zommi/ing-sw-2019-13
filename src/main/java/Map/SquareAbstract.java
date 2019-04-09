package Map;

import Constants.Color;
import Player.Character;

import java.util.ArrayList;
import java.util.List;

public abstract class SquareAbstract {

    private List<Character> charactersList;

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


        switch(color){                                  //TODO maybe it could be done with abbreviations inside the enum

            case 'R':
            case 'r': this.color = Color.RED;
                break;
            case 'B':
            case 'b': this.color = Color.BLUE;
                break;
            case 'Y':
            case 'y': this.color = Color.YELLOW;
                break;
            case 'G':
            case 'g': this.color = Color.GREEN;
                break;
            case 'W':
            case 'w': this.color = Color.WHITE;
                break;
            case 'P':
            case 'p': this.color = Color.PURPLE;
                break;
            default: this.color = Color.UNDEFINED;
        }

        room = Map.getRooms().get(this.color.ordinal());

        //new character list
        this.charactersList= new ArrayList<>();


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
        room.addCharacter(character);
    }

    public void removeCharacter(Character character){
        charactersList.remove(character);
        room.removeCharacter(character);
    }

    public List<Character> getCharacters() {
        ArrayList<Character> returnedList = (ArrayList<Character>) charactersList;
        return (ArrayList<Character>)  returnedList.clone();
    }

    public List<Character> getVisibleCharacters(){                  //same square, same room and visible rooms
        List<Character> visibleCharacters = new ArrayList<>();
        visibleCharacters.addAll(room.getCharacters());
        for(Room tempRoom : this.getVisibleRooms()){
            visibleCharacters.addAll(tempRoom.getCharacters());
        }
        return visibleCharacters;

    }

    public List<Character> getCharactersThroughWalls(){             //in square with same x OR same y, including same square
        List<Character> tempCharactersList = new ArrayList<>();
        List<SquareAbstract> xSquares = Map.getSquaresWithSameX(this);
        List<SquareAbstract> ySquares = Map.getSquaresWithSameY(this);
        for(SquareAbstract tempSquare : xSquares){
            tempCharactersList.addAll(tempSquare.getCharacters());

        }
        for(SquareAbstract tempSquare : ySquares){
            tempCharactersList.addAll(tempSquare.getCharacters());
        }
        tempCharactersList.addAll(this.getCharacters());
        return tempCharactersList;

    }

    /*public List<Character> getUpToTwoMovementsCharacters(){
        List<Character> tempCharactersList = new ArrayList<>();
        List<SquareAbstract> tempSquareList = new ArrayList<>();
        tempSquareList.add(this);
        for(SquareAbstract square : this.getAdjacentSquares()){
            tempCharactersList.addAll(square.getOneMovementCharacters());
        }
        tempCharactersList.removeAll(tempSquareList);
        return tempCharactersList;

    }*/


    public List<Character> getExactlyOneMovementCharacters(){
        List<Character> tempCharactersList = new ArrayList<>();
        for(SquareAbstract square : this.getAdjacentSquares()){
            tempCharactersList.addAll(square.getCharacters());
        }
        return tempCharactersList;
    }

    public List<Character> getUpToOneMovementCharacters(){
        List<Character> tempCharactersList = new ArrayList<>();
        for(SquareAbstract square : this.getAdjacentSquares()){
            tempCharactersList.addAll(square.getCharacters());
        }
        tempCharactersList.addAll(this.getCharacters());
        return tempCharactersList;
    }

    public List<SquareAbstract> getTwoSquareInTheSameDirection(char c){
        List<SquareAbstract> returnedList = new ArrayList<>();
        switch(c){
            case 'n': if(nSquare!=null && nSquare.nSquare!=null){
                returnedList.add(nSquare);
                returnedList.add(nSquare.nSquare);
            }
            case 's': if(sSquare!=null && sSquare.sSquare!=null){
                returnedList.add(sSquare);
                returnedList.add(sSquare.sSquare);
            }
            case 'w': if(wSquare!=null && wSquare.wSquare!=null){
                returnedList.add(wSquare);
                returnedList.add(wSquare.wSquare);
            }
            case 'e': if(eSquare!=null && eSquare.eSquare!=null){
                returnedList.add(eSquare);
                returnedList.add(eSquare.eSquare);
            }
        }
        return returnedList;
    }

    public List<Room> getVisibleRooms() {                       //own room isn't included in the returned list
        List<Room> roomsList = new ArrayList<>();
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
        List<SquareAbstract> squaresList= new ArrayList<>();
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
