package Cards;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import Constants.Color;
import Items.*;


public class Weapon {

    private Bullet bullet;
    private Command command;
    private int x;  //I need it if I have to use the teleporter.
    private int y;  //I need it if I have to use the teleporter.
    private int damage;
    private int marks;
    private boolean teleporterflag; //If this is true I can use the move() method in character with the new coordinates.
    private ArrayList<AmmoCube> cost;

    private int x1;
    private int y1;
    private int damage1;
    private int marks1;
    private boolean teleporterflag1; //If this is true I can use the move() method in character with the new coordinates.
    private ArrayList<AmmoCube> cost1;

    private int x2;
    private int y2;
    private int damage2;
    private int marks2;
    private boolean teleporterflag2; //If this is true I can use the move() method in character with the new coordinates.
    private ArrayList<AmmoCube> cost2;


    public Weapon(int numweapon) {
        String path;
        switch (numweapon) {

            case 1:
                path = "./weapons/LOCKERIFLE.txt";
                break;
            case 2:
                path = "./weapons/ELECTROSCYTE.txt";
                break;
            case 3:
                path = "./weapons/MACHINEGUN.txt";
                break;

                //TODO complete the implementation of all the weapons
            case 4:
                path = "./.txt";
                break;


            default:    //this should never happen
                path = "./.txt";

            generateattributesFromFile(path);  //by calling this method we initialize the attributes of the weapon
        }
    }

    public Bullet shoot(int extra) { //fills the bullet
        if(extra == 1) { //extra effect number 1
            bullet = new Bullet(this.x1, this.y1, this.damage1, this.marks1, this.teleporterflag1,this.cost1);
        }
        else if(extra == 2){ //extra effect number 2
            bullet = new Bullet(this.x2, this.y2, this.damage2, this.marks2, this.teleporterflag2,this.cost2);
        }
        else {
            bullet = new Bullet(this.x, this.y, this.damage, this.marks, this.teleporterflag, this.cost);
        }
        return bullet;
    }

    public void generateattributesFromFile(String path){


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




        this.x = Integer.parseInt(readInput.get(0));
        this.y = Integer.parseInt(readInput.get(1));
        this.damage = Integer.parseInt(readInput.get(2));
        this.marks = Integer.parseInt(readInput.get(3));
        this.teleporterflag = Boolean.parseBoolean(readInput.get(4));

        if(Integer.parseInt(readInput.get(5)) != 0) { //it means that it costs some red ammocubes
            cost = new ArrayList<AmmoCube>();
            for (int i = 0; i < Integer.parseInt(readInput.get(5)); i++) {
                AmmoCube am = new AmmoCube(Color.RED);
                cost.add(am);
            }
        }
        if(Integer.parseInt(readInput.get(6)) != 0){ //It means that it costs some blue ammocubes
            if(cost == null)
                cost = new ArrayList<AmmoCube>();
            for(int i = 0; i < Integer.parseInt(readInput.get(6)); i++) {
                AmmoCube am = new AmmoCube(Color.BLUE);
                cost.add(am);
            }
        }
        if(Integer.parseInt(readInput.get(7))!= 0) { //it means that it costs some yellow ammocubes
            if(cost == null)
                cost = new ArrayList<AmmoCube>();
            for (int i = 0; i < Integer.parseInt(readInput.get(6)); i++) {
                AmmoCube am = new AmmoCube(Color.YELLOW);
                cost.add(am);
            }
        }




        if(8 < readInput.size()) { //it means that the arraylist has more than 8 elements.
            this.x1 = Integer.parseInt(readInput.get(8));
            this.y1 = Integer.parseInt(readInput.get(9));
            this.damage1 = Integer.parseInt(readInput.get(10));
            this.marks1 = Integer.parseInt(readInput.get(11));
            this.teleporterflag1 = Boolean.parseBoolean(readInput.get(12));

            if (Integer.parseInt(readInput.get(13)) != 0) { //it means that it costs some red ammocubes
                cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(13)); i++) {
                    AmmoCube am = new AmmoCube(Color.RED);
                    cost.add(am);
                }
            }
            if (Integer.parseInt(readInput.get(14)) != 0) { //It means that it costs some blue ammocubes
                if (cost == null)
                    cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(14)); i++) {
                    AmmoCube am = new AmmoCube(Color.BLUE);
                    cost.add(am);
                }
            }
            if (Integer.parseInt(readInput.get(15)) != 0) { //it means that it costs some yellow ammocubes
                if (cost == null)
                    cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(15)); i++) {
                    AmmoCube am = new AmmoCube(Color.YELLOW);
                    cost.add(am);
                }
            }
        }

        if(16 < readInput.size()) {  //it means that the arraylist has more than 16 elements.
            this.x2 = Integer.parseInt(readInput.get(16));
            this.y2 = Integer.parseInt(readInput.get(17));
            this.damage2 = Integer.parseInt(readInput.get(18));
            this.marks2 = Integer.parseInt(readInput.get(19));
            this.teleporterflag2 = Boolean.parseBoolean(readInput.get(20));

            if (Integer.parseInt(readInput.get(21)) != 0) { //it means that it costs some red ammocubes
                cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(21)); i++) {
                    AmmoCube am = new AmmoCube(Color.RED);
                    cost.add(am);
                }
            }
            if (Integer.parseInt(readInput.get(22)) != 0) { //It means that it costs some blue ammocubes
                if (cost == null)
                    cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(22)); i++) {
                    AmmoCube am = new AmmoCube(Color.BLUE);
                    cost.add(am);
                }
            }
            if (Integer.parseInt(readInput.get(23)) != 0) { //it means that it costs some yellow ammocubes
                if (cost == null)
                    cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(23)); i++) {
                    AmmoCube am = new AmmoCube(Color.YELLOW);
                    cost.add(am);
                }
            }

        }
    }

    public Command getCommand(){
        return this.command;
    }


    public Bullet charge() {
        // TODO
        return null;
    }

}