package server.model.map;

import constants.Color;
import constants.Constants;
import exceptions.NoSuchSquareException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;



public class GameMap implements Serializable, Iterable<SquareAbstract> {

    private static final int SQ_DIM = 4;
    private static final int SQ_X = 9;  //must be even
    private static final int SQ_Y = 5;  //must be even
    private static final int FRAME_OFFSET = 1;
    public static final String ANSI_RESET = "\u001B[0m";

    private List<SpawnPoint> spawnPoints = new ArrayList<>();
    private ArrayList<ArrayList<SquareAbstract>> squares = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private List<Color> roomsToBuild = new ArrayList<>();
    private boolean valid;
    //add to copy
    private int numRow;
    private int numCol;



    public GameMap(){

    }
    /**
     * Generates the whole map, including the graph that links
     * all the squares and the rooms they are assigned to.
     * @param mapNum One of the different configurations of the map.
     *               It could go from 1 to 4.
     */
    public GameMap(int mapNum) {
        numRow = 0;
        numCol = 0;
        valid = true;
        String path;
        switch(mapNum) {

            case 1:
                path = Constants.PATH_TO_MAP_11;
                break;
            case 2:
                path = Constants.PATH_TO_MAP_12;
                break;
            case 3:
                path = Constants.PATH_TO_MAP_21;
                break;
            case 4:
                path = Constants.PATH_TO_MAP_22;
                break;
            default:    //this should never happen
                path = Constants.PATH_TO_MAP_11;
        }


        //the following line populates the double ArrayList of SquareAbstract (squares)
        List<String> readList = generateSquareStructureFromFile(path);

        //now we're gonna link all the squares (to build a graph) inside the generated square structure
        linkSquares(readList);

        generateRooms();

        populateRooms();
    }

    /**
     * Returns a list of the rooms, one for each color.
     * @return a list of the rooms.
     */
    public List<Room> getRooms(){
        ArrayList<Room> returnedList = (ArrayList<Room>) rooms;
        return (ArrayList<Room>) returnedList.clone();
    }

    /**
     * Returns the room with the specified color
     * @param col color of the desired room
     * @return the room with the specified color
     */
    public Room getRoom(Color col){
        for(Room room: rooms){
            if(room.getColor() == col)
                return room;
        }
        return null;                                //TODO add exception
    }

    /**
     * Creates a list with a room for each color. It's just a
     * list of empty rooms.
     */
    public void generateRooms(){             //TODO it's public just for testing
        rooms = new ArrayList<>();
        for(Color color : roomsToBuild)
            rooms.add(new Room(color));
    }

    /**
     * Assign every square to his room, accordingly to the square's
     * color, as specified in the text file in which the map is described.
     */
    private void populateRooms(){

        for(int i = 0; i<squares.size(); i++){
            for(int j = 0; j<squares.get(i).size(); j++){
                if(squares.get(i).get(j) != null) {
                    getRoom(squares.get(i).get(j).getColor()).addSquare(squares.get(i).get(j));
                    squares.get(i).get(j).setRoom(getRoom(squares.get(i).get(j).getColor()));

                }
            }
        }
    }

    /**
     * Reads the file at the specified path and writes every line
     * of it as a string into a list. This list is built just for
     * making the parsing process easier.
     * @param path the path of the map file
     * @return a list with a string for every line of the text file
     */
    private List<String> generateSquareStructureFromFile(String path) {
        Scanner scanner = null;
        List<String> readInput = null;
        try {
            scanner = new Scanner(new File(path));
            readInput = new ArrayList<>();

            while (scanner.hasNextLine()) {
                readInput.add(scanner.nextLine());
            }
        } catch(FileNotFoundException e){
            //TODO handle exception
        } finally{
            scanner.close();
        }

        roomsToBuild = new ArrayList<>();
        squares = new ArrayList<>();
        spawnPoints = new ArrayList<>();
        for(int i = 0; i<readInput.size(); i++){
            if(i%2 == 0)
                squares.add(new ArrayList<>());
        }

        int row = 0;
        int col;
        char c;
        String s;
        while(row < readInput.size()){
            col = 0;
            while(col < readInput.get(row).length()){
                c = readInput.get(row).charAt(col);
                s = String.valueOf(c).toLowerCase();

                if(c=='R'||c=='B'||c=='Y'||c=='G'||c=='W'||c=='P') {
                    SpawnPoint tempSquare = new SpawnPoint(row/2, col/2, Color.fromString(s), this);
                    squares.get(row/2).add(tempSquare);
                    spawnPoints.add(tempSquare);
                    if(!roomsToBuild.contains(Color.fromString(s)))
                        roomsToBuild.add(Color.fromString(s));
                }
                else if(c=='r'||c=='b'||c=='y'||c=='g'||c=='w'||c=='p'){
                    squares.get(row/2).add(new Square(row/2, col/2, Color.fromString(s), this));
                    if(!roomsToBuild.contains(Color.fromString(s)))
                        roomsToBuild.add(Color.fromString(s));
                }
                else if(c==' ' && row%2==0 && col%2==0){
                    squares.get(row/2).add(null);
                }
                //update max
                if(row/2 + 1> numRow)
                    numRow = row/2 + 1;
                if(col/2 + 1> numCol)
                    numCol = col/2 + 1;
                col++;
            }
            row++;
        }

        return readInput;

    }

    /**
     * Looks for '-' and '|' characters in the read file. These symbols
     * mean that two square are linked, either they are in the same room
     * or not.
     * @param list the file that has already been converted into a list of strings
     */
    private void linkSquares(List<String> list){
        int row, col;
        char c;
        row = 0;
        while(row < list.size()){
            col = 0;
            while(col < list.get(row).length()){
                c = list.get(row).charAt(col);
                if(c=='-'){
                    squares.get(row/2).get(col/2).seteSquare(squares.get(row/2).get(col/2+1));
                    squares.get(row/2).get(col/2+1).setwSquare(squares.get(row/2).get(col/2));
                }
                else if(c=='|'){
                    squares.get(row/2).get(col/2).setsSquare(squares.get(row/2+1).get(col/2));
                    squares.get(row/2+1).get(col/2).setnSquare(squares.get(row/2).get(col/2));
                }
                col++;
            }
            row++;
        }
    }

    /**
     * Returns a list of the spawn points.
     * @return a list of the spawn points
     */
    public List<SpawnPoint> getSpawnPoints(){
        return spawnPoints;
    }

    /**
     * Returns the spawn point of the specified color. It assumes
     * there's at most one spawn point for every color.
     * @param color the color of the desired spawn point
     * @return the spawn point of the desired color
     */
    public SpawnPoint getSpawnPoint(Color color){
        for(SpawnPoint sp : spawnPoints){
            if(sp.getColor() == color)
                return sp;

        }
        return null;
    }

    /**
     * Returns a square, given its row and column.
     * @param row row of the square
     * @param col column of the square
     * @return  the square at the given row and column
     * @throws NoSuchSquareException if there's no square with the given coordinates
     */
    public SquareAbstract getSquare(int row, int col){

        try {
            return squares.get(row).get(col);
        }
        catch (IndexOutOfBoundsException e){
            return null;
        }

    }

    /**
     * Returns a list of the squares that have the same row of the given square.
     * Given square is not included in the list.
     * @param square squares in the returned list will have the same row of this square
     * @return a list of the squares that have the same row of the given square
     */
    public List<SquareAbstract> getSquaresWithSameRow(SquareAbstract square){
        List<SquareAbstract> squareList = new ArrayList<>();                                //TODO this method could be non static in SquareAbstract, invoking a static one here
        for(int i = 0; i<squares.size(); i++){
            for(int j = 0; j<squares.get(i).size(); j++){
                 if(squares.get(i).get(j) != null)
                    if(squares.get(i).get(j).getRow() == square.getRow() && squares.get(i).get(j) != square)
                            squareList.add(squares.get(i).get(j));

            }
        }
        return squareList;
    }

    /**
     * Returns a list of the squares that have the same column of the given square.
     * Given square is not included in the list.
     * @param square squares in the returned list will have the same column of this square
     * @return a list of the squares that have the same column of the given square
     */
    public List<SquareAbstract> getSquaresWithSameCol(SquareAbstract square){              //passed square won't be in the returned list
        List<SquareAbstract> squareList = new ArrayList<>();
        for(int i = 0; i<squares.size(); i++){
            for(int j = 0; j<squares.get(i).size(); j++){
                if(squares.get(i).get(j) != null)
                    if(squares.get(i).get(j).getCol() == square.getCol() && squares.get(i).get(j) != square)
                        squareList.add(squares.get(i).get(j));

            }
        }
        return squareList;
    }

    public GameMap createCopy(GameMap MapToCopy){
        GameMap result = new GameMap();
        result.squares = MapToCopy.getSquares();
        result.spawnPoints.addAll(MapToCopy.getSpawnPoints());
        result.roomsToBuild = null; //we don't need roomsToBuild
        result.valid = this.valid;
        for(int i = 0; i < MapToCopy.rooms.size(); i++){
            result.rooms.add(MapToCopy.rooms.get(i).roomCreateCopy(MapToCopy.getRooms().get(i)));
        }
        return result;
    }

    public ArrayList<ArrayList<SquareAbstract>> getSquares() {
        return squares;
    }

    public boolean isValid(){
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public Iterator<SquareAbstract> iterator() {
        return new MapIterator();
    }

    private class MapIterator implements Iterator<SquareAbstract>{
        SquareAbstract currentSquare;
        boolean notFirstTime;

        @Override
        public boolean hasNext() {
            if(!notFirstTime)
                return true;        //assuming a map has at least one square
            if(squares.get(currentSquare.getRow()).size() > currentSquare.getCol() + 1 )
                return true;
            return (squares.size() - 1 != currentSquare.getRow());
        }

        @Override
        public SquareAbstract next() throws NoSuchElementException {
            if(!hasNext())
                throw new NoSuchElementException();
            else{
                if(notFirstTime) {
                    if (currentSquare.getCol() != squares.get(currentSquare.getRow()).size() - 1) {
                        for (int i = currentSquare.getCol() + 1; ; i++) {
                            if (getSquare(currentSquare.getRow(), i) != null) {
                                currentSquare = getSquare(currentSquare.getRow(), i);
                                return currentSquare;
                            }
                        }
                    } else {
                        for (int i = 0; ; i++) {
                            if (getSquare(currentSquare.getRow() + 1, i) != null) {
                                currentSquare = getSquare(currentSquare.getRow() + 1, i);
                                return currentSquare;
                            }
                        }
                    }
                }
                else{
                    notFirstTime = true;
                    for(SquareAbstract squareAbstract : squares.get(0)){
                        if(squareAbstract != null) {
                            currentSquare = squareAbstract;
                            return currentSquare;
                        }
                    }
                    //this should never happen, assuming the first line has at least one square
                    return null;
                }

            }
        }
    }

    public void printOnCLI(){
        String[][] grid = new String[numRow *SQ_Y + 2*FRAME_OFFSET][numCol *SQ_X + 2*FRAME_OFFSET];
        fillEmpty(grid);
        printSquares(grid);
        plot(grid);
        System.out.println();
    }

    private void fillEmpty(String[][] grid) {

        grid[0][0] = "╔";
        for (int c = 1; c < numCol *SQ_X + 2*FRAME_OFFSET - 1; c++) {
            grid[0][c] = "═";
        }

        grid[0][numCol *SQ_X + 2*FRAME_OFFSET - 1] = "╗";

        for (int r = 1; r < numRow *SQ_Y + 2*FRAME_OFFSET - 1; r++) {
            grid[r][0] = "║";
            for (int c = 1; c < numCol *SQ_X + 2*FRAME_OFFSET - 1; c++) {
                grid[r][c] = " ";
            }
            grid[r][numCol *SQ_X + 2*FRAME_OFFSET -1] = "║";
        }

        grid[numRow *SQ_Y + 2*FRAME_OFFSET - 1][0] = "╚";
        for (int c = 1; c < numCol *SQ_X + 2*FRAME_OFFSET - 1; c++) {
            grid[numRow *SQ_Y + 2*FRAME_OFFSET - 1][c] = "═";
        }

        grid[numRow *SQ_Y +2*FRAME_OFFSET- 1][numCol *SQ_X +2*FRAME_OFFSET - 1] = "╝";

    }

    private void printSquares(String[][] grid){
        Iterator<SquareAbstract> iterator = this.iterator();
        while(iterator.hasNext()){
            printSquare(grid, iterator.next());
        }
    }

    private void printSquare(String[][] grid, SquareAbstract sq){
        int baseRow = FRAME_OFFSET + sq.getRow()*SQ_Y;
        int baseCol = FRAME_OFFSET+sq.getCol()*SQ_X;
        String color = sq.getColor().getAnsi();

        if(!sq.getCharacters().isEmpty())
            grid[baseRow + (SQ_Y-2)/2 + 1][baseCol + (SQ_X-2)/2 + 1] = sq.getCharacters().get(0).getColor().getAnsi()+"∎"+color;
        if(sq.getCharacters().size()>1)
            grid[baseRow + (SQ_Y-2)/2 + 1][baseCol + (SQ_X-2)/2 + 3] = sq.getCharacters().get(1).getColor().getAnsi()+"∎"+color;
        if(sq.getCharacters().size()>2)
            grid[baseRow + (SQ_Y-2)/2 + 1][baseCol + (SQ_X-2)/2 - 1] = sq.getCharacters().get(2).getColor().getAnsi()+"∎"+color;
        if(sq.getCharacters().size()>3)
            grid[baseRow + (SQ_Y-2)/2 - 1][baseCol + (SQ_X-2)/2 + 1] = sq.getCharacters().get(3).getColor().getAnsi()+"∎"+color;
        if(sq.getCharacters().size()>4)
            grid[baseRow + (SQ_Y-2)/2 + 3][baseCol + (SQ_X-2)/2 + 1] = sq.getCharacters().get(4).getColor().getAnsi()+"∎"+color;

        grid[baseRow][baseCol] = color + "╔";
        grid[baseRow][baseCol + SQ_X - 1] = "╗" + ANSI_RESET;
        grid[baseRow + SQ_Y - 1][baseCol] = color + "╚";
        grid[baseRow + SQ_Y - 1][baseCol + SQ_X - 1] = "╝" + ANSI_RESET;

        for(int c = baseCol + 1; c < baseCol + 1 + SQ_X - 2; c++){
            grid[baseRow][c] = "═";
            grid[baseRow + SQ_Y - 1][c] = "═";
        }

        for(int r = baseRow + 1; r < baseRow + 1 + SQ_Y - 2; r++){
            grid[r][baseCol] = color + "║";
            grid[r][baseCol + SQ_X - 1] = "║" + ANSI_RESET;
        }

        if(sq.getnSquare() != null)
            grid[baseRow][baseCol + (SQ_X-2)/2 + 1] = "╬";
        if(sq.getsSquare() != null)
            grid[baseRow + SQ_Y - 1][baseCol + (SQ_X-2)/2 + 1] = "╬";
        if(sq.getwSquare() != null)
            grid[baseRow + (SQ_Y-2)/2 + 1][baseCol] = color + "╬";
        if(sq.geteSquare() != null)
            grid[baseRow + (SQ_Y-2)/2 + 1][baseCol + SQ_X - 1] = "╬" + color;




    }

    private void plot(String[][] grid) {
        for (int r = 0; r < numRow *SQ_Y + 2*FRAME_OFFSET; r++) {
            System.out.println();
            for (int c = 0; c < numCol *SQ_X + 2*FRAME_OFFSET; c++) {
                System.out.print(grid[r][c]);
            }
        }
    }

}