package server.model.map;

import exceptions.NoSuchEffectException;
import exceptions.NotAlignedException;
import server.model.cards.CollectableInterface;
import constants.Color;
import constants.Direction;
import server.model.player.GameCharacter;

import java.util.*;

public abstract class  SquareAbstract {

    protected List<GameCharacter> charactersList;

    protected Color color;
    protected int row;
    protected int col;
    // the following are just for visible squares
    //make optional??
    protected SquareAbstract nSquare;
    protected SquareAbstract wSquare;
    protected SquareAbstract eSquare;
    protected SquareAbstract sSquare;

    protected Room room;

    protected GameMap gameMap;

    public GameMap getGameMap(){
        return gameMap;
    }

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
    protected SquareAbstract(int row, int col, Color color, GameMap gameMap) {
        this.row = row;
        this.col = col;

        this.color = color;

        this.gameMap = gameMap;

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
    public SquareAbstract getNearFromDir(Direction dir){
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
     * Adds the given gameCharacter to the list of the characters that physically stay on this square.
     * @param gameCharacter the gameCharacter that needs to be added to this square
     */
    public void addCharacter(GameCharacter gameCharacter){
        charactersList.add(gameCharacter);
        room.addCharacter(gameCharacter);
    }

    /**
     * Removes a gameCharacter from the list of the characters that physically stay on this square.
     * @param gameCharacter the gameCharacter that needs to be removed from this square
     */
    public void removeCharacter(GameCharacter gameCharacter){
        charactersList.remove(gameCharacter);
        room.removeCharacter(gameCharacter);
    }

    /**
     * Returns a list of the characters that physically stay on this square.
     * @return a list of the characters that physically stay on this square
     */
    public List<GameCharacter> getCharacters() {
        return charactersList;
    }

    /**
     * Returns a list with the characters that are visible from this square.
     * A character is visible if stays on the same square, or in a square of
     * the same room, or in the room of an adjacent square.
     * @return a list with the characters that are visible from this square
     */
    public List<GameCharacter> getVisibleCharacters(){
        List<GameCharacter> visibleGameCharacters = new ArrayList<>(room.getCharacters());
        for(Room tempRoom : this.getVisibleRooms()){
            visibleGameCharacters.addAll(tempRoom.getCharacters());
        }
        return visibleGameCharacters;

    }

    /**
     * Returns a list with the squares that are visible from this square.
     * @return a list with the squares that are visible from this square
     */
    public List<SquareAbstract> getVisibleSquares(){
        List<SquareAbstract> visibleSquares = new ArrayList<>(room.getSquares());
        for(Room tempRoom : this.getVisibleRooms()){
            visibleSquares.addAll(tempRoom.getSquares());
        }
        return visibleSquares;
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

    public abstract void removeItem(CollectableInterface itemToAdd);

    public abstract void addItem(CollectableInterface itemToLeave);

    public abstract boolean isEmpty();

    public List<SquareAbstract> getUpToTwoMovementsSquares(){
        return null;
    }
    public List<GameCharacter> getAtLeastTwoMovementCharacters() {
        return null;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof SquareAbstract)) {
            return false;
        }

        SquareAbstract squareAbstractObject = (SquareAbstract) obj;

        return this.row == squareAbstractObject.getRow()
                && this.col == squareAbstractObject.getCol()
                && this.color == squareAbstractObject.getColor();

    }

    public int distance(SquareAbstract destination){
        if(this.equals(destination))return 0;

        //square used to divide different groups of squares.
        //all squares in the same group are at the same distance
        final SquareAbstract impossibleSquare = new Square(-1,-1,Color.UNDEFINED,gameMap);

        //initialization of variables
        LinkedList<SquareAbstract> queue = new LinkedList<>();
        List<SquareAbstract> alreadyAdded = new ArrayList<>();
        SquareAbstract currentTarget;
        SquareAbstract currentSquare;
        queue.add(this);
        queue.add(impossibleSquare);
        alreadyAdded.add(this);
        int distance = 1;


        while(!queue.isEmpty()){
            currentSquare = queue.poll();

            //if a group ended, increase distance
            if(currentSquare.equals(impossibleSquare)) {
                distance++;
            } else {

                //add all the squares near to the queue
                for (Direction dir : Direction.values()) {
                    currentTarget = currentSquare.getNearFromDir(dir);
                    if(currentTarget != null && !alreadyAdded.contains(currentTarget)){
                        if(currentTarget.equals(destination)){
                            return distance;
                        }else{
                            alreadyAdded.add(currentTarget);
                            queue.add(currentTarget);
                        }
                    }
                }
                //if the computation of a group ended add a divider at the end of the queue
                if(queue.getFirst().equals(impossibleSquare))queue.add(impossibleSquare);
            }
        }
        return -1;
    }

    public List<SquareAbstract> getSquaresAtDistance(int distance){
        final SquareAbstract impossibleSquare = new Square(-1,-1,Color.UNDEFINED,gameMap);

        //initialization of variables
        LinkedList<SquareAbstract> queue = new LinkedList<>();
        List<SquareAbstract> alreadyAdded = new ArrayList<>();
        List<SquareAbstract> result = new ArrayList<>();
        queue.add(this);
        queue.add(impossibleSquare);
        alreadyAdded.add(this);
        int currentDistance = 1;
        SquareAbstract currentSquare;
        SquareAbstract nextSquare;

        if(distance == 0){
            result.add(this);
            return result;
        }


        while(!queue.isEmpty()){
            currentSquare = queue.poll();

            //if a group ended, increase distance
            if(currentSquare.equals(impossibleSquare)) {
                if(currentDistance == distance) return result;
                currentDistance++;
            } else {

                //add all the squares near to the queue
                for (Direction dir : Direction.values()) {
                    nextSquare = currentSquare.getNearFromDir(dir);
                    if (nextSquare != null && !alreadyAdded.contains(nextSquare)) {
                        if (currentDistance == distance) {
                            result.add(nextSquare);
                        } else {
                            alreadyAdded.add(nextSquare);
                            queue.add(nextSquare);
                        }
                    }
                }
                //if the computation of a group ended add a divider at the end of the queue
                if(queue.getFirst().equals(impossibleSquare))queue.add(impossibleSquare);
            }
        }
        return Collections.emptyList();
    }

    public boolean isSpawnPoint(){
        return this instanceof SpawnPoint;
    }

    public Direction getDirection(SquareAbstract squareAbstract) throws NotAlignedException {
        if(this == squareAbstract)
            return null;
        if(this.getRow() == squareAbstract.getRow() && this.getCol()<squareAbstract.getCol())
            return Direction.EAST;
        else if(this.getRow() == squareAbstract.getRow() && this.getCol()>squareAbstract.getCol())
            return Direction.WEST;
        else if(this.getRow() < squareAbstract.getRow() && this.getCol()==squareAbstract.getCol())
            return Direction.NORTH;
        else if(this.getRow() > squareAbstract.getRow() && this.getCol()==squareAbstract.getCol())
            return Direction.SOUTH;
        else
            throw new NotAlignedException();
    }

    public boolean areSameDirection(List<SquareAbstract> squareAbstractList) {
       try {
           if (squareAbstractList.isEmpty())
               return true;
           Direction direction = null;
           for (SquareAbstract squareAbstract : squareAbstractList) {
               if (this.getDirection(squareAbstract) != null) {
                   direction = this.getDirection(squareAbstract);
                   break;
               }
           }
           if (direction == null)
               return true;
           //at least one square is not the same as this
           for (SquareAbstract squareAbstract : squareAbstractList) {
               if (this.getDirection(squareAbstract) != null && this.getDirection(squareAbstract) != direction)
                   return false;
           }
           return true;
       }catch(NotAlignedException e){
           return false;
       }
    }
}
