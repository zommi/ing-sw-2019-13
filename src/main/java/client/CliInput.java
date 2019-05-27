package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliInput extends InputAbstract{
    @Override
    public boolean getChoice(MacroEffect macroEffect) {
        Scanner in = new Scanner(System.in);

        System.out.println("Do you wanna activate this macro effect?\n"
                + macroEffect + "[y to activate]\n");

        String s = in.nextLine();

        return s.equals("y");

    }

    @Override
    public boolean getChoice(MicroEffect microEffect) {
        Scanner in = new Scanner(System.in);
        System.out.println("Do you wanna activate this micro effect?\n"
                + microEffect + "[y to activate]\n");
        String s = in.nextLine();
        return s.equals("y");
    }

    @Override
    public List<SquareInfo> askSquares(int maxSquares) {
        Scanner in = new Scanner(System.in);
        String rowString;
        String colString;
        boolean ask;
        boolean exit = false;
        int row;
        int col;
        List<SquareInfo> list = new ArrayList<>();
        int counter = maxSquares;      //always != 0
        do{
            ask = true;
            while(ask) {
                if(maxSquares != 1 && counter != maxSquares)
                    System.out.println("Give me the row: [or say 'stop']\n");
                else
                    System.out.println("Give me the row: \n");
                rowString = in.nextLine();

                if (rowString.equals("stop") && maxSquares != 1 && counter != maxSquares) {
                    exit = true;
                    break;
                }
                else{
                    try{
                        System.out.println("Give me the column:\n");
                        colString = in.nextLine();
                        row = Integer.parseInt(rowString);
                        col = Integer.parseInt(colString);
                        list.add(new SquareInfo(row, col));
                        ask = false;
                    }catch(NumberFormatException e){
                        System.out.println("Not a valid choice, try again.");
                    }
                }
            }
            if(exit)
                break;
            else
                counter = counter - 1;
        }while(counter > 0);
        return list;
    }

    @Override
    public List<String> askPlayers(int max){
        return askPlayersOrRooms(max, true);
    }

    @Override
    public List<String> askRooms(int max){
        return askPlayersOrRooms(max, false);
    }

    private List<String> askPlayersOrRooms(int maxTargetPlayerSize, boolean players) {
        List<String> names = players ? playersNames : roomsNames;
        Scanner in = new Scanner(System.in);
        String choice;
        boolean ask;
        boolean exit = false;
        int number = 0;
        List<String> list = new ArrayList<>();
        int counter = maxTargetPlayerSize;      //always != 0
        do{
            ask = true;
            while(ask) {
                if(maxTargetPlayerSize != 1 && counter != maxTargetPlayerSize)  //maybe maxTarget check is useless
                    System.out.println("Give me a number [or say 'stop']:\n");
                else
                    System.out.println("Give me a number:\n");
                choice = in.nextLine();
                if (choice.equals("stop") && maxTargetPlayerSize != 1 && counter != maxTargetPlayerSize) {
                    exit = true;
                    break;
                }
                else{
                    try{
                       number = Integer.parseInt(choice);
                       if(number < names.size()){
                           list.add(names.get(number));
                           ask = false;
                       }
                       else{
                           System.out.println("Number not valid.");
                       }
                    }catch(NumberFormatException e){
                        System.out.println("Not a valid choice, try again.");
                    }
                }
            }
            if(exit)
                break;
            else
                counter--;
        }while(counter > 0);
        return list;
    }

    @Override
    public MacroEffect chooseOneMacro(Weapon weapon) {
        for(MacroEffect macroEffect : weapon.getMacroEffects()){
            System.out.println(macroEffect.getNumber() + ": " + macroEffect);
        }
        Scanner in = new Scanner(System.in);
        String s;
        int i = 0;
        boolean ask = true;
        while(ask) {
            System.out.println("Choose a number\n");
            s = in.nextLine();
            try {
                i = Integer.parseInt(s);
                if(i < weapon.getMacroEffects().size())
                    ask = false;
                else
                    System.out.println("Choice not valid.");
            } catch (NumberFormatException e) {
                //ask = true
                System.out.println("Number not valid.");
            }
        }
        return weapon.getMacroEffect(i);
    }
}
