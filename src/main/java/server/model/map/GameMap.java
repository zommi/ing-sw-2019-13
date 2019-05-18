package server.model.map;

import constants.Color;
import constants.Constants;
import exceptions.NoSuchSquareException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameMap {

    private List<SpawnPoint> spawnPoints;
    private ArrayList<ArrayList<SquareAbstract>> squares;        //TODO contains optional
    private List<Room> rooms;
    private List<Color> roomsToBuild;
    private boolean valid;

    /**
     * Generates the whole map, including the graph that links
     * all the squares and the rooms they are assigned to.
     * @param mapNum One of the different configurations of the map.
     *               It could go from 1 to 4.
     */

    public GameMap(int mapNum) {
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
        for(String s : readInput){
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
        ArrayList<SpawnPoint> returnedList = (ArrayList<SpawnPoint>) spawnPoints;
        return (ArrayList<SpawnPoint>) returnedList.clone();
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
    public SquareAbstract getSquare(int row, int col) throws NoSuchSquareException {

        try {
            return squares.get(row).get(col);
        }
        catch (IndexOutOfBoundsException e){
            throw new NoSuchSquareException();
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

    public ArrayList<ArrayList<SquareAbstract>> getSquares() {
        return squares;
    }

    public boolean isValid(){
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }
}