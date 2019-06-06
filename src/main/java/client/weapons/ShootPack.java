package client.weapons;

import client.GameModel;
import client.Info;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShootPack implements Info {

    private String weapon;
    List<MacroPack> activatedMacros;


    public ShootPack(String weapon){
        this.weapon = weapon;
        this.activatedMacros = new ArrayList<>();
    }

    public String getWeapon(){
        return this.weapon;
    }

    public List<MacroPack> getActivatedMacros() {
        return activatedMacros;
    }

    public MacroPack getActivatedMacro(int macro){
        for(MacroPack macroPack : activatedMacros)
            if(macroPack.getMacroNumber() == macro)
                return macroPack;
        return null;
    }
}
