package Cards;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import Constants.Color;
import Constants.Constants;
import Items.*;





//PAY ATTENTION TO THE POWER GLOVE!!!! IT HAS a SECOND EXTRA EFFECT that IS PART OF THE FIRST ONE WHICH NEEDS TO BE CALLED FROM THE CONTROLLER!!!!!
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

    private String name;


    public Weapon(int numweapon) {
        String path;
        switch (numweapon) {

            case 0:
                path = "./weapons/LOCKERIFLE.txt";
                break;
            case 1:
                path = "./weapons/ELECTROSCYTE.txt";
                break;
            case 2:
                path = "./weapons/MACHINEGUN.txt";
                break;

                //TODO complete the implementation of all the weapons
            case 3:
                path = "./weapons/TRACTORBEAM.txt";
                break;
            case 4:
                path = "./weapons/T.H.O.R..txt";
                break;
            case 5:
                path = "./weapons/VORTEXCANNON.txt";
                break;
            case 6:
                path = "./weapons/FURNACE.txt";
                break;
            case 7:
                path = "./weapons/PLASMAGUN.txt";
                break;
            case 8:
                path = "./weapons/HEATSEEKER.txt";
                break;
            case 9:
                path = "./weapons/WHISPER.txt";
                break;
            case 10:
                path = "./weapons/HELLION.txt";
                break;
            case 11:
                path = "./weapons/FLAMETHROWER.txt";
                break;
            case 12:
                path = "./weapons/ZX-2.txt";
                break;
            case 13:
                path = "./weapons/GRENADELAUNCHER.txt";
                break;
            case 14:
                path = "./weapons/SHOTGUN.txt";
                break;
            case 15:
                path = "./weapons/ROCKETLAUNCHER.txt";
                break;
            case 16:
                path = "./weapons/POWERGLOVE.txt";
                break;
            case 17:
                path = "./weapons/RAILGUN.txt";
                break;
            case 18:
                path = "./weapons/SHOCKWAVE.txt";
                break;
            case 19:
                path = "./weapons/CYBERBLADE.txt";
                break;
            case 20:
                path = "./weapons/SLEDGEHAMMER.txt";
                break;


            default:    //this should never happen
                path = "./.txt";

        }

        this.name = getNameFromFile(path);
        generateAttributesFromFile(path);  //by calling this method we initialize the attributes of the weapon
    }

    private String getNameFromFile(String path) {
        File file = new File(path);
        return file.getName().substring(0,file.getName().lastIndexOf('.'));
    }

    /**
     * Returns the an ArrayList of two bullets, the first one is the one for the first target, the second one is the one for the second target.
     * @param extra is an int that indicates the extra effect of the weapon the player wants to use. X and Y are the coordinates i want to move the bullet receiver
     * @return the an ArrayList of two bullets, the first one is the one for the first target, the second one is the one for the second ta
     */
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
        ArrayList<Bullet> result = new ArrayList<Bullet>();
        result.add(bullet);
        result.add(bullet2);
        return result;
    }




    /**
     * Returns the bullet of the cascade effect
     * @param path of the file the method has to read from
     * @return the bullet of the cascade effect
     */
    public Bullet PrepareBullet2(String path){
        Scanner scanner = null;
        int damage1;
        int marks1;
        boolean teleporterflag1;

        Bullet secondbullet;
        List<String> readInput = null;
        readInput = new ArrayList<>();

        try {
            scanner = new Scanner(new File(path));
        } catch(FileNotFoundException e){
            //TODO handle exception
        } finally{
            while (scanner.hasNextLine()) {
                readInput.add(scanner.nextLine());
            }
            scanner.close();
        }


        damage1 = Integer.parseInt(readInput.get(0));
        marks1 = Integer.parseInt(readInput.get(1));
        teleporterflag1 = Boolean.parseBoolean(readInput.get(2));

        secondbullet = new Bullet(0, 0, damage1, marks1, teleporterflag1);
        return(secondbullet);
    }






    /**
     * Initalizes the attributes of the weapon reading from file
     * @param path of the file the method has to read from
     */
    public void generateAttributesFromFile(String path) {
        Scanner scanner = null;
        List<String> readInput = null;
        readInput = new ArrayList<>();
        try {
            scanner = new Scanner(new File(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while (scanner.hasNextLine()) {
            readInput.add(scanner.nextLine());
        }
        scanner.close();



        this.damage = Integer.parseInt(readInput.get(0));
        this.marks = Integer.parseInt(readInput.get(1));
        this.teleporterflag = Boolean.parseBoolean(readInput.get(2));

        this.cost = readCost(readInput,Constants.FIRST_EFFECT_WEAPON_COST_INDEX_FROM_FILE);

        if(Integer.parseInt(readInput.get(6))== 01)
            bullet2 = PrepareBullet2("./weapons/EXTRA1HELLION.txt");

        if(7 < readInput.size()) { //it means that the arraylist has more than 6 elements.
            this.damage1 = Integer.parseInt(readInput.get(7));
            this.marks1 = Integer.parseInt(readInput.get(8));
            this.teleporterflag1 = Boolean.parseBoolean(readInput.get(9));

            this.cost1 = readCost(readInput, Constants.SECOND_EFFECT_WEAPON_COST_INDEX_FROM_FILE);


            if (Integer.parseInt(readInput.get(13)) == 02)
                bullet2 = PrepareBullet2("./weapons/EXTRA2HELLION.txt");

            if (Integer.parseInt(readInput.get(13)) == 03)
                bullet2 = PrepareBullet2("./weapons/EXTRA2FLAMETHROWER.txt");
        }

        if(14< readInput.size()) {  //it means that the arraylist has more than 12 elements.
            this.damage2 = Integer.parseInt(readInput.get(14));
            this.marks2 = Integer.parseInt(readInput.get(15));
            this.teleporterflag2 = Boolean.parseBoolean(readInput.get(16));

            this.cost2 = readCost(readInput,Constants.THIRD_EFFECT_WEAPON_COST_INDEX_FROM_FILE);
        }
    }


    public ArrayList<AmmoCube> readCost(List<String> inputFromfile, int startIndex){
        ArrayList<AmmoCube> costToReturn = new ArrayList<AmmoCube>();
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < Integer.parseInt(inputFromfile.get(startIndex + i)); j++) {
                costToReturn.add(new AmmoCube(Color.fromIndex(i)));
            }
        }
        return costToReturn;
    }

    public Command getCommand(){
        return this.command;
    }


    public Bullet charge() {
        // TODO
        return null;
    }


    public ArrayList<AmmoCube> getCost() {
        return cost;
    }

    public ArrayList<AmmoCube> getCost1() {
        return cost1;
    }

    public ArrayList<AmmoCube> getCost2() {
        return cost2;
    }

    public String getName() {
        return this.name;
    }
}