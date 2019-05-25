package client;

import java.util.ArrayList;
import java.util.List;

public class ShootParser {

    private boolean isLimitedActivated;
    private Weapon weapon;

    public ShoootInfo getWeaponInput(Weapon weapon, InputInterface input){
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
            case ALTERNATIVE:

        }
        return null;
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
        boolean answer = input.getChoice(macroEffect);
        if(answer){
            this.manageMacro(macroEffect, input);
        }
    }
}
