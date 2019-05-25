package client;

import java.util.Scanner;

public class CliInput implements InputInterface{
    @Override
    public boolean getChoice(MacroEffect macroEffect) {
        Scanner in = new Scanner(System.in);

        System.out.println("Do you wanna activate this macro effect? [y to activate]\n"
                + macroEffect.getDescription());

        String s = in.nextLine();

        return s.equals("y");

    }

    @Override
    public MacroEffect chooseOneMacro(Weapon weapon) {
        System.out.println("choose a number\n");
        for(MacroEffect macroEffect : weapon.getMacroEffects()){
            System.out.println(macroEffect.getNumber()+": "+macroEffect.getDescription());
        }
        Scanner in = new Scanner(System.in);
        String s;
        boolean ask = true;
        while(ask) {
            s = in.nextLine();
            try {
                int i = Integer.parseInt(s);
                ask = false;
            } catch (NumberFormatException e) {
                //ask = true
            }
        }

        return null;
    }
}
