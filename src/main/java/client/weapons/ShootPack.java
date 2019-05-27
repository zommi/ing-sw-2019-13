package client.weapons;

import client.Info;

import java.util.ArrayList;
import java.util.List;

public class ShootPack implements Info {

    private String weapon;
    List<MacroPack> activatedMacros;

    public ShootPack(String weapon){
        this.weapon = weapon;
        this.activatedMacros = new ArrayList<>();
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

    public MicroPack getActivatedMicro(int macro, int micro){
        if(getActivatedMacro(macro) != null)
            return getActivatedMacro(macro).getActivatedMicro(micro);
        else return null;
    }

    public String getWeapon() {
        return weapon;
    }
}
