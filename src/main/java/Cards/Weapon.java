package Cards;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import Constants.Color;
import Items.*;


public class Weapon {

    private Bullet bullet;
    private Bullet bullet2;

    private Command command;
    private int x;  //I need it if I have to use the teleporter.
    private int y;  //I need it if I have to use the teleporter.
    private int damage;
    private int marks;
    private boolean teleporterflag; //If this is true I can use the move() method in character with the new coordinates.
    private ArrayList<AmmoCube> cost; //cost of the weapon

    private int x1;
    private int y1;
    private int damage1;
    private int marks1;
    private boolean teleporterflag1;
    private ArrayList<AmmoCube> cost1; //cost of the weapon first effect

    private int x2;
    private int y2;
    private int damage2;
    private int marks2;
    private boolean teleporterflag2;
    private ArrayList<AmmoCube> cost2; //cost of the weapon second effect


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
                path = "./weapons/TRACTORBEAM.txt";
                break;
            case 5:
                path = "./weapons/T.H.O.R.txt";
                break;
            case 6:
                path = "./weapons/VORTEXCANNON.txt";
                break;
            case 7:
                path = "./weapons/FURNACE.txt";
                break;
            case 8:
                path = "./weapons/PLASMAGUN.txt";
                break;
            case 9:
                path = "./weapons/HEATSEEKER.txt";
                break;
            case 10:
                path = "./weapons/WHISPER.txt";
                break;


            default:    //this should never happen
                path = "./.txt";

            generateattributesFromFile(path);  //by calling this method we initialize the attributes of the weapon
        }
    }

    public ArrayList<Bullet> shoot(int extra, int x, int y) { //fills the bullet

        if(extra == 1) { //extra effect number 1
            this.x1 = x;
            this.y1 = y;
            bullet = new Bullet(this.x1, this.y1, this.damage1, this.marks1, this.teleporterflag1);
        }
        else if(extra == 2){ //extra effect number 2
            this.x2 = x;
            this.y2 = y;
            bullet = new Bullet(this.x2, this.y2, this.damage2, this.marks2, this.teleporterflag2);
        }
        else if(extra == 0){
            this.x = x;
            this.y = y;
            bullet = new Bullet(this.x, this.y, this.damage, this.marks, this.teleporterflag);
        }
        //TODO
        ArrayList<Bullet> result = new ArrayList<Bullet>();
        result.add(bullet);
        result.add(bullet2);
        return result;
    }





    public Bullet PrepareBullet2(String path){
        Scanner scanner = null;
        int damage1;
        int marks1;
        boolean teleporterflag1;

        Bullet secondbullet;
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


        damage1 = Integer.parseInt(readInput.get(0));
        marks1 = Integer.parseInt(readInput.get(1));
        teleporterflag1 = Boolean.parseBoolean(readInput.get(2));

        secondbullet = new Bullet(0, 0, damage1, marks1, teleporterflag1);
        return(secondbullet);
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


        this.damage = Integer.parseInt(readInput.get(0));
        this.marks = Integer.parseInt(readInput.get(1));
        this.teleporterflag = Boolean.parseBoolean(readInput.get(2));

        if(Integer.parseInt(readInput.get(3)) != 0) { //it means that it costs some red ammocubes
            cost = new ArrayList<AmmoCube>();
            for (int i = 0; i < Integer.parseInt(readInput.get(3)); i++) {
                AmmoCube am = new AmmoCube(Color.RED);
                cost.add(am);
            }
        }
        if(Integer.parseInt(readInput.get(4)) != 0){ //It means that it costs some blue ammocubes
            if(cost == null)
                cost = new ArrayList<AmmoCube>();
            for(int i = 0; i < Integer.parseInt(readInput.get(4)); i++) {
                AmmoCube am = new AmmoCube(Color.BLUE);
                cost.add(am);
            }
        }
        if(Integer.parseInt(readInput.get(5))!= 0) { //it means that it costs some yellow ammocubes
            if(cost == null)
                cost = new ArrayList<AmmoCube>();
            for (int i = 0; i < Integer.parseInt(readInput.get(5)); i++) {
                AmmoCube am = new AmmoCube(Color.YELLOW);
                cost.add(am);
            }
        }

        if(Integer.parseInt(readInput.get(6))== 01)
            bullet2 = PrepareBullet2("./weapons/EXTRA1HELLION.txt");

        if(6 < readInput.size()) { //it means that the arraylist has more than 6 elements.
            this.damage1 = Integer.parseInt(readInput.get(6));
            this.marks1 = Integer.parseInt(readInput.get(7));
            this.teleporterflag1 = Boolean.parseBoolean(readInput.get(8));

            if (Integer.parseInt(readInput.get(9)) != 0) { //it means that it costs some red ammocubes
                cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(9)); i++) {
                    AmmoCube am = new AmmoCube(Color.RED);
                    cost.add(am);
                }
            }
            if (Integer.parseInt(readInput.get(10)) != 0) { //It means that it costs some blue ammocubes
                if (cost == null)
                    cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(10)); i++) {
                    AmmoCube am = new AmmoCube(Color.BLUE);
                    cost.add(am);
                }
            }
            if (Integer.parseInt(readInput.get(11)) != 0) { //it means that it costs some yellow ammocubes
                if (cost == null)
                    cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(11)); i++) {
                    AmmoCube am = new AmmoCube(Color.YELLOW);
                    cost.add(am);
                }
            }
        }


        if(Integer.parseInt(readInput.get(12))== 02)
            bullet2 = PrepareBullet2("./weapons/EXTRA2HELLION.txt");

        if(Integer.parseInt(readInput.get(12))== 03)
            bullet2 = PrepareBullet2("./weapons/EXTRA2FLAMETHROWER.txt");


        if(12 < readInput.size()) {  //it means that the arraylist has more than 12 elements.
            this.damage2 = Integer.parseInt(readInput.get(12));
            this.marks2 = Integer.parseInt(readInput.get(13));
            this.teleporterflag2 = Boolean.parseBoolean(readInput.get(14));

            if (Integer.parseInt(readInput.get(15)) != 0) { //it means that it costs some red ammocubes
                cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(15)); i++) {
                    AmmoCube am = new AmmoCube(Color.RED);
                    cost.add(am);
                }
            }
            if (Integer.parseInt(readInput.get(16)) != 0) { //It means that it costs some blue ammocubes
                if (cost == null)
                    cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(16)); i++) {
                    AmmoCube am = new AmmoCube(Color.BLUE);
                    cost.add(am);
                }
            }
            if (Integer.parseInt(readInput.get(17)) != 0) { //it means that it costs some yellow ammocubes
                if (cost == null)
                    cost = new ArrayList<AmmoCube>();
                for (int i = 0; i < Integer.parseInt(readInput.get(17)); i++) {
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