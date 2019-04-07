
import java.util.*;
import java.io.*;       //maybe less
import java.util.Scanner;

public class Map {

    private ArrayList<SpawnPoint> spawnPoints;
    private ArrayList<ArrayList<SquareAbstract>> squares;


    public Map(int mapNum) {
        try{
            List<String> list = new ArrayList<String>();
            Scanner scanner;
            switch(mapNum){
                case 1:
                    scanner = new Scanner(new File("map1.txt"));
                    break;
                case 2:
                    scanner = new Scanner(new File("map2.txt"));
                    break;
                case 3:
                    scanner = new Scanner(new File("map3.txt"));
                    break;
                case 4:
                    scanner = new Scanner(new File("map4.txt"));
                    break;
            }
            while(scanner.hasNextLine()){
                list.add(scanner.nextLine());
            }
            scanner.close();

            squares = new ArrayList<ArrayList<SquareAbstract>>();
            for(int i=0; i<list.size(); i++){
                squares.add(new ArrayList<SquareAbstract>());
            }

            int row = 0, col = 0;       //will be incremented by 2
            int realRow, realCol;
            char c;
            while(row < list.size()){
                col = 0;
                while(col < list.get(row).length()){
                    c = list.get(row).charAt(col);
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

            //now we're gonna link all the squares
            row = 0;
            char c;
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
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public ArrayList<SpawnPoint> getSpawnPoints(){

    }



}