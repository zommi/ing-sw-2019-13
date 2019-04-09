package Map;

import Constants.Color;
import Exceptions.NoSuchSquareException;

import java.util.*;
import java.io.*;       //maybe less
import java.util.Scanner;

public class Map {

    private static List<SpawnPoint> spawnPoints;
    private static ArrayList<ArrayList<SquareAbstract>> squares;
    private static List<Room> rooms;

    public Map(int mapNum) throws FileNotFoundException{    //TODO assign room to every square and viceversa
        String path;
        switch(mapNum) {

            case 1:
                path = "map11.txt";
                break;
            case 2:
                path = "map12.txt";
                break;
            case 3:
                path = "map21.txt";
                break;
            case 4:
                path = "map22.txt";
                break;
            default:    //this should never happen
                path = "map11.txt";
        }

        rooms = new ArrayList<>();
        for(Color color : Color.values())
            rooms.add(new Room(color));

        //the following line populates the double ArrayList of SquareAbstract (squares)
        List<String> readList = generateSquareStructureFromFile(path);
        //now we're gonna link all the squares (to build a graph) inside the generated square structure
        linkSquares(squares, readList);

        populateRooms();
    }


    public static List<Room> getRooms(){
        ArrayList<Room> returnedList = (ArrayList<Room>) rooms;
        return (ArrayList<Room>) returnedList.clone();
    }

    private static void populateRooms(){

        for(int i = 0; i<squares.size(); i++){
            for(int j = 0; j<squares.get(i).size(); j++){
                rooms.get(squares.get(i).get(j).getColor().ordinal()).addSquare(squares.get(i).get(j));
                //a ref to the room is created in SquareAbstract's constructor
            }
        }
    }

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
        for(int i = 0; i< readInput.size(); i++){
            squares.add(new ArrayList<>());
        }

        int row = 0;
        int col;
        char c;
        while(row < readInput.size()){
            col = 0;
            while(col < readInput.get(row).length()){
                c = readInput.get(row).charAt(col);
                if(c=='R'||c=='B'||c=='Y'||c=='G'||c=='W'||c=='P') {
                    SpawnPoint tempSquare = new SpawnPoint(row/2+1, col/2+1, c);
                    squares.get(row).add(tempSquare);
                    spawnPoints.add(tempSquare);
                }
                else if(c=='r'||c=='b'||c=='y'||c=='g'||c=='w'||c=='p'){
                    squares.get(row).add(new Square(row/2+1, col/2+1, c));
                }
                else if(c==' ' && row%2==0 && col%2==0){
                    squares.get(row).add(null);
                }
                col++;
            }
            row++;
        }
        return readInput;


    }

    private static void linkSquares(ArrayList<ArrayList<SquareAbstract>> squares, List<String> list){
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

    public List<SpawnPoint> getSpawnPoints(){
        ArrayList<SpawnPoint> returnedList = (ArrayList<SpawnPoint>) spawnPoints;
        return (ArrayList<SpawnPoint>) returnedList.clone();
    }

    public static SquareAbstract getSquareFromXY(int x, int y) throws NoSuchSquareException {

        try {
            return squares.get(x).get(y);
        }
        catch (IndexOutOfBoundsException e){
            throw new NoSuchSquareException();
        }

    }

    public static List<SquareAbstract> getSquaresWithSameX(SquareAbstract square){          //passed square won't be in the returned list
        List<SquareAbstract> squareList = new ArrayList<>();
        for(int i = 0; i<squares.size(); i++){
            for(int j = 0; j<squares.get(i).size(); j++){
                if(squares.get(i).get(j).getxValue() == square.getxValue() && squares.get(i).get(j) != square)
                    squareList.add(squares.get(i).get(j));

            }
        }
        return squareList;
    }

    public static List<SquareAbstract> getSquaresWithSameY(SquareAbstract square){              //passed square won't be in the returned list
        List<SquareAbstract> squareList = new ArrayList<>();
        for(int i = 0; i<squares.size(); i++){
            for(int j = 0; j<squares.get(i).size(); j++){
                if(squares.get(i).get(j).getyValue() == square.getyValue() && squares.get(i).get(j) != square)
                    squareList.add(squares.get(i).get(j));

            }
        }
        return squareList;
    }



}