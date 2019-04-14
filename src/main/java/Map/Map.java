package Map;

import Constants.Color;
import Exceptions.NoSuchSquareException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map {

    private static List<SpawnPoint> spawnPoints;
    private static ArrayList<ArrayList<SquareAbstract>> squares;        //TODO contains optional
    private static List<Room> rooms;

    /**
     * Generates the whole map, including the graph that links
     * all the squares and the rooms they are assigned to.
     * @param mapNum One of the different configurations of the map.
     *               It could go from 1 to 4.
     */

    public Map(int mapNum) {
        String path;
        switch(mapNum) {

            case 1:
                path = "./maps/map11.txt";
                break;
            case 2:
                path = "./maps/map12.txt";
                break;
            case 3:
                path = "./maps/map21.txt";
                break;
            case 4:
                path = "./maps/map22.txt";
                break;
            default:    //this should never happen
                path = "./maps/map11.txt";
        }

        generateRooms();

        //the following line populates the double ArrayList of SquareAbstract (squares)
        List<String> readList = generateSquareStructureFromFile(path);

        //now we're gonna link all the squares (to build a graph) inside the generated square structure
        linkSquares(readList);

        populateRooms();
    }

    /**
     * Returns a list of the rooms, one for each color.
     * @return a list of the rooms.
     */
    public static List<Room> getRooms(){
        ArrayList<Room> returnedList = (ArrayList<Room>) rooms;
        return (ArrayList<Room>) returnedList.clone();
    }

    /**
     * Returns the room with the specified color
     * @param col color of the desired room
     * @return the room with the specified color
     */
    public static Room getRoom(Color col){
        return rooms.get(col.ordinal());
    }

    /**
     * Creates a list with a room for each color. It's just a
     * list of empty rooms.
     */
    public static void generateRooms(){             //TODO it's public just for testing
        rooms = new ArrayList<>();
        for(Color color : Color.values())
            if (color != Color.UNDEFINED)
                rooms.add(new Room(color));
    }

    /**
     * Assign every square to his room, accordingly to the square's
     * color, as specified in the text file in which the map is described.
     */
    private static void populateRooms(){

        for(int i = 0; i<squares.size(); i++){
            for(int j = 0; j<squares.get(i).size(); j++){
                if(squares.get(i).get(j) != null) {
                    rooms.get(squares.get(i).get(j).getColor().ordinal()).addSquare(squares.get(i).get(j));
                    squares.get(i).get(j).setRoom(rooms.get(squares.get(i).get(j).getColor().ordinal()));

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
    private static List<String> generateSquareStructureFromFile(String path) {
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
                    SpawnPoint tempSquare = new SpawnPoint(row/2, col/2, Color.fromString(s));
                    squares.get(row/2).add(tempSquare);
                    spawnPoints.add(tempSquare);
                }
                else if(c=='r'||c=='b'||c=='y'||c=='g'||c=='w'||c=='p'){
                    squares.get(row/2).add(new Square(row/2, col/2, Color.fromString(s)));
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
    private static void linkSquares(List<String> list){
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
     * Returns the spawn point of the specified color.
     * @param color the color of the desired spawn point
     * @return the spawn point of the desired color
     */
    public static SpawnPoint getSpawnPoint(Color color){
        for(SpawnPoint sp : spawnPoints){
            if(sp.getColor() == color)
                return sp;

        }
        return null;
    }

    /**
     * Returns a square, given its coordinates.
     * @param x x coordinate of the square
     * @param y y coordinate of the square
     * @return  the square at the specified coordinates
     * @throws NoSuchSquareException if there's no square with the given coordinates
     */
    public static SquareAbstract getSquareFromXY(int x, int y) throws NoSuchSquareException {

        try {
            return squares.get(x).get(y);
        }
        catch (IndexOutOfBoundsException e){
            throw new NoSuchSquareException();
        }

    }

    /**
     * Returns a list of the squares that have the same x coordinate of the given square.
     * Given square is not included in the list.
     * @param square squares in the returned list will have the same x of this square
     * @return a list of the squares that have the same x coordinate of the given square
     */
    public static List<SquareAbstract> getSquaresWithSameX(SquareAbstract square){          //passed square won't be in the returned list
        List<SquareAbstract> squareList = new ArrayList<>();                                //TODO this method could be non static in SquareAbstract, invoking a static one here
        for(int i = 0; i<squares.size(); i++){
            for(int j = 0; j<squares.get(i).size(); j++){
                 if(squares.get(i).get(j) != null)
                    if(squares.get(i).get(j).getxValue() == square.getxValue() && squares.get(i).get(j) != square)
                            squareList.add(squares.get(i).get(j));

            }
        }
        return squareList;
    }

    /**
     * Returns a list of the squares that have the same y coordinate of the given square.
     * Given square is not included in the list.
     * @param square squares in the returned list will have the same y of this square
     * @return a list of the squares that have the same y coordinate of the given square
     */
    public static List<SquareAbstract> getSquaresWithSameY(SquareAbstract square){              //passed square won't be in the returned list
        List<SquareAbstract> squareList = new ArrayList<>();
        for(int i = 0; i<squares.size(); i++){
            for(int j = 0; j<squares.get(i).size(); j++){
                if(squares.get(i).get(j) != null)
                    if(squares.get(i).get(j).getyValue() == square.getyValue() && squares.get(i).get(j) != square)
                        squareList.add(squares.get(i).get(j));

            }
        }
        return squareList;
    }



}