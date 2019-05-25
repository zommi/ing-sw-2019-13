package client;

import client.weapons.MacroPack;

import java.util.ArrayList;
import java.util.List;

public class ProvaLista {
    public static void main(String[] args) {
        List<MacroPack> list = new ArrayList<>();
        MacroPack macroPack = new MacroPack(0);
        list.add(macroPack);
        if(macroPack.equals(list.get(0)))
            System.out.println("okkkkkkkkkkkkk");

        macroPack.setMacroNumber(1);
        System.out.println(list.get(0).getMacroNumber());

        list.get(0).setMacroNumber(0);
        System.out.println(macroPack.getMacroNumber());

    }
}
