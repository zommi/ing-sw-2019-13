package client;

import client.weapons.MacroEffect;
import client.weapons.MicroEffect;
import client.weapons.Weapon;

import java.util.Scanner;

public class CliInput implements InputInterface{
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
    public MacroEffect chooseOneMacro(Weapon weapon) {
        for(MacroEffect macroEffect : weapon.getMacroEffects()){
            System.out.println(macroEffect.getNumber() + ": " + macroEffect);
        }
        Scanner in = new Scanner(System.in);
        String s;
        int i = 0;
        boolean ask = true;
        while(ask) {
            System.out.println("choose a number\n");
            s = in.nextLine();
            try {
                i = Integer.parseInt(s);
                if(i < weapon.getMacroEffects().size())
                    ask = false;
                else
                    System.out.println("Choice not valid");
            } catch (NumberFormatException e) {
                //ask = true
                System.out.println("Number not valid");
            }
        }


        return weapon.getMacroEffect(i);
    }
}
