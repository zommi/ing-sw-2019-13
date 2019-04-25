package Map;

import Cards.CollectableInterface;
import Cards.WeaponCard;
import Constants.Color;
import Constants.Directions;
import Player.Character;

import java.util.ArrayList;
import java.util.List;

public abstract class  SquareAbstract {

    private List<Character> charactersList;

    private Color color;
    private int row;
    private int col;
    // the following are just for visible squares
    //make optional??
    private SquareAbstract nSquare;
    private SquareAbstract wSquare;
    private SquareAbstract eSquare;
    private SquareAbstract sSquare;

    private Room room;

    /**
     * Returns the room to which this square belongs.
     * @return the room to which this square belongs
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Sets the room of the square.
     * @param room must be one of the existing rooms
     */
    public void setRoom(Room room){
        this.room = room;
    }

    /**
     * Returns the color of the square.
     * @return the color of the square
     */
    public Color getColor() {
        return color;
    }

    /**
     * Creates a square with the specified row, column and color.
     * @param row row of the square
     * @param col column of the square
     * @param color color of the square
     */
    protected SquareAbstract(int row, int col, Color color) {
        this.row = row;
        this.col = col;

        this.color = color;

        //new character list
        this.charactersList= new ArrayList<>();
    }

    /**
     * @return the square linked on the right if present, or null if there's no right link
     */
    public SquareAbstract geteSquare() {
        return eSquare;
    }

    /**
     * @param eSquare
     */
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

    /**
     * Returns the adjacent square in the given direction.
     * @param dir specifies the relative position of the near square
     * @return the adjacent square in the given direction
     */
    public SquareAbstract getNearFromDir(Directions dir){
        switch(dir){
            case NORTH: return getnSquare();
            case SOUTH: return getsSquare();
            case WEST:  return getwSquare();
            case EAST:  return geteSquare();

            default:    return null;
        }
    }

    /**
     * Returns the row of the square
     * @return the row of the square
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column of the square
     * @return the column of the square
     */
    public int getCol() {
        return col;
    }

    /**
     * Adds the given character to the list of the characters that physically stay on this square.
     * @param character the character that needs to be added to this square
     */
    public void addCharacter(Character character){
        charactersList.add(character);
        room.addCharacter(character);
    }

    /**
     * Removes a character from the list of the characters that physically stay on this square.
     * @param character the character that needs to be removed from this square
     */
    public void removeCharacter(Character character){
        charactersList.remove(character);
        room.removeCharacter(character);
    }

    /**
     * Returns a list of the characters that physically stay on this square.
     * @return a list of the characters that physically stay on this square
     */
    public List<Character> getCharacters() {
        ArrayList<Character> returnedList = (ArrayList<Character>) charactersList;
        return (ArrayList<Character>)  returnedList.clone();
    }

    /**
     * Returns a list with the characters that are visible from this square.
     * A character is visible if stays on the same square, or in a square of
     * the same room, or in the room of an adjacent square.
     * @return a list with the characters that are visible from this square
     */
    public List<Character> getVisibleCharacters(){
        List<Character> visibleCharacters = new ArrayList<>();
        visibleCharacters.addAll(room.getCharacters());
        for(Room tempRoom : this.getVisibleRooms()){
            visibleCharacters.addAll(tempRoom.getCharacters());
        }
        return visibleCharacters;

    }

    /**
     * Returns a list with the squares that are visible from this square.
     * @return a list with the squares that are visible from this square
     */
    public List<SquareAbstract> getVisibleSquares(){
        List<SquareAbstract> visibleSquares = new ArrayList<>();
        visibleSquares.addAll(room.getSquares());
        for(Room tempRoom : this.getVisibleRooms()){
            visibleSquares.addAll(tempRoom.getSquares());
        }
        return visibleSquares;
    }

    /**
     * Returns a list with the characters that stand on a square that
     * has the same row OR the same column of this square. Thus, a character
     * could be in the same square too.
     * @return a list with the characters that stand on a square that
     *        has the same row OR the same column of this square
     */
    public List<Character> getCharactersThroughWalls(){
        List<Character> tempCharactersList = new ArrayList<>();
        List<SquareAbstract> rowSquares = Map.getSquaresWithSameRow(this);
        List<SquareAbstract> colSquares = Map.getSquaresWithSameCol(this);
        for(SquareAbstract tempSquare : rowSquares){
            tempCharactersList.addAll(tempSquare.getCharacters());

        }
        for(SquareAbstract tempSquare : colSquares){
            tempCharactersList.addAll(tempSquare.getCharacters());
        }
        tempCharactersList.addAll(this.getCharacters());
        return tempCharactersList;

    }

    /**
     * Returns a list with squares that are exactly two moves
     * away from the square on which the method is called.
     * @return a list with squares that are exactly two moves
     *         away from the square on which the method is called.
     */

    public List<SquareAbstract> getExactlyTwoMovementsSquares(){
        List<SquareAbstract> tempSquaresList = new ArrayList<>();
        for(SquareAbstract square : this.getAdjacentSquares()){
            for(SquareAbstract square2 : square.getAdjacentSquares())
                if(!tempSquaresList.contains(square2) && !square2.equals(this))
                    tempSquaresList.add(square2);
        }
        return tempSquaresList;
    }

    /**
     * Returns a list with characters that are at least two moves
     * away from the square on which the method is called.
     * @return a list with characters that are at least two moves
     *         away from the square on which the method is called
     */

    public List<Character> getExactlyTwoMovementsCharacters() {
        List<Character> tempCharactersList = new ArrayList<>();
        for(SquareAbstract square : getExactlyTwoMovementsSquares()){
            tempCharactersList.addAll(square.getCharacters());
        }
        return tempCharactersList;
    }

    /**
     * Returns a list with all the characters that stand on the linked adjacent squares.
     * @return a list with all the characters that stand on the linked adjacent squares
     */
    public List<Character> getExactlyOneMovementCharacters(){
        List<Character> tempCharactersList = new ArrayList<>();
        for(SquareAbstract square : this.getAdjacentSquares()){
            tempCharactersList.addAll(square.getCharacters());
        }
        return tempCharactersList;
    }

    /**
     * Returns a list with all the characters that stand on the linked adjacent squares,
     * plus those that stand on this square.
     * @return a list with all the characters that stand on the linked adjacent squares,
     * plus those that stand on this square
     */
    public List<Character> getUpToOneMovementCharacters(){
        List<Character> tempCharactersList = new ArrayList<>();
        for(SquareAbstract square : this.getAdjacentSquares()){
            tempCharactersList.addAll(square.getCharacters());
        }
        tempCharactersList.addAll(this.getCharacters());
        return tempCharactersList;
    }

    /**
     * Returns a list with characters that are at least one move away from
     * the square on which the method is called.
     * @return a list with characters that are at least one move away from
     *         the square on which the method is called
     */

    public List<Character> getAtLeastOneMovementCharacters(){
        List<Character> tempCharactersList = new ArrayList<>();
        tempCharactersList.addAll(Character.getTakenCharacters());
        tempCharactersList.removeAll(this.getCharacters());
        return tempCharactersList;
    }

    /**
     * Returns two squares that are linked in the given direction to this square.
     * The returned list may only contain the adjacent square, or may be empty,
     * accordingly to the map.
     * @param dir direction
     * @return a list of at most two squares that are linked in the given direction to this square
     */
    public List<SquareAbstract> getTwoSquaresInTheSameDirection(Directions dir){
        List<SquareAbstract> returnedList = new ArrayList<>();

        if(getNearFromDir(dir) != null){
            returnedList.add(getNearFromDir(dir));
            if(getNearFromDir(dir).getNearFromDir(dir) != null)
                returnedList.add(getNearFromDir(dir).getNearFromDir(dir));
        }

        return returnedList;
    }

    /**
     * Returns all the rooms of the adjacent squares. The room
     * of this square is not included in the returned list.
     * @return a list with all the rooms of the adjacent squares.
     */
    public List<Room> getVisibleRooms() {
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

    /**
     * Returns all the linked adjacent squares.
     * @return a list with all the linked adjacent squares
     */
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

    public abstract void removeItem(CollectableInterface itemToAdd);

    public abstract void addItem(CollectableInterface itemToLeave);
}
