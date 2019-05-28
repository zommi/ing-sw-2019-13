package client.weapons;

import client.GameModel;
import client.Info;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ShootPack implements Info {

    private String weapon;
    List<MacroPack> activatedMacros;
    GameModel gameModel;



    public ShootPack(String weapon, GameModel gameModel){
        this.weapon = weapon;
        this.activatedMacros = new ArrayList<>();
        this.gameModel = gameModel;
    }

    public Weapon getWeaponFromName(){
        for(int i = 0; i < gameModel.getWeaponList().getList().size(); i++){
            if(weapon.equalsIgnoreCase(gameModel.getWeaponList().getList().get(i).getName())){
                return gameModel.getWeaponList().getList().get(i).getWeapon();
            }
        }
        return null;
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

    public MicroPack getActivatedMicro(int macro, int micro){
        if(getActivatedMacro(macro) != null)
            return getActivatedMacro(macro).getActivatedMicro(micro);
        else return null;
    }


}