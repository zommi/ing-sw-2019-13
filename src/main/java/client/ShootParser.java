package client;

import exceptions.NoSuchEffectException;

import javax.crypto.Mac;
import java.util.ArrayList;
import java.util.List;

public class ShootParser {

    private boolean isLimitedActivated;
    private Weapon weapon;

    public void getWeaponInput(Weapon weapon, InputInterface input) throws NoSuchEffectException {
        this.isLimitedActivated = false;
        this.weapon = weapon;
        ShoootInfo shoootInfo = new ShoootInfo(weapon.getName());
        List<List<MicroPack>> macroList = new ArrayList<>();
        switch(weapon.getType()){
            case EXTRA:
                for(MacroEffect macroEffect : weapon.getMacroEffects()){
                    if(macroEffect.isMandatory())
                        this.manageMacro(macroEffect, input);
                    else if((macroEffect.isConditional() && !this.isMacroActivated(shoootInfo, macroEffect)) ||
                            (macroEffect.isLimited() && !this.isLimitedActivated) ||
                            (!macroEffect.isLimited() && !macroEffect.isConditional()))
                        this.chooseMacro(macroEffect, input);

                }
        }
    }

    private void manageMacro(MacroEffect macroEffect, InputInterface input){

    }

    private boolean isMacroActivated(ShoootInfo shoootInfo, MacroEffect macroEffect){
        for(MacroPack macroPack : shoootInfo.getActivatedMacros()){
            if (macroPack.getMacroNumber() == macroEffect.getNumber())
                return true;
        }
        return false;
    }

    private void chooseMacro(MacroEffect macroEffect, InputInterface input){
        input.getChoice(macroEffect);
    }
}
