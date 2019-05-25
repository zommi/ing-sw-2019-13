package client.weapons;

import client.InputInterface;

import java.util.ArrayList;
import java.util.List;

public class ShootParser {

    private boolean isLimitedActivated;
    private Weapon weapon;
    private InputInterface input;
    private ShoootInfo shoootInfo;

    public ShoootInfo getWeaponInput(Weapon weapon, InputInterface input){
        this.isLimitedActivated = false;
        this.weapon = weapon;
        this.input = input;
        shoootInfo = new ShoootInfo(weapon.getName());
        List<List<MicroPack>> macroList = new ArrayList<>();
        switch(weapon.getType()){
            case EXTRA:
                for(MacroEffect macroEffect : weapon.getMacroEffects()){
                    if(macroEffect.isMandatory())
                        this.manageMacro(macroEffect);
                    else if((macroEffect.isConditional() && !this.isMacroActivated(macroEffect)) ||
                            (macroEffect.isLimited() && !this.isLimitedActivated) ||
                            (!macroEffect.isLimited() && !macroEffect.isConditional()))
                        this.chooseMacro(macroEffect);

                }
                break;
            case ALTERNATIVE:
                MacroEffect macroEffect = input.chooseOneMacro(weapon);
                manageMacro(macroEffect);
                break;
            case ONEMODE:
                manageMacro(weapon.getMacroEffect(0));
                break;
        }
        return shoootInfo;
    }

    private void manageMacro(MacroEffect macroEffect){
        MacroPack macroPack = new MacroPack(macroEffect.getNumber());
        shoootInfo.getActivatedMacros().add(macroPack);
        if(macroEffect.isLimited())
            isLimitedActivated = true;

        for(MicroEffect microEffect : macroEffect.getMicroEffects()){
            if(microEffect.isMandatory())
                this.manageMicro(microEffect);
            else if((microEffect.isConditional() && !this.isMicroActivated(microEffect)) ||
                    (microEffect.isLimited() && !this.isLimitedActivated) ||
                    (!microEffect.isLimited() && !microEffect.isConditional())){
                this.chooseMicro(microEffect);
            }

        }
    }

    private void manageMicro(MicroEffect microEffect){
        MicroPack microPack = new MicroPack(microEffect.getMacroNumber(), microEffect.getNumber());
        shoootInfo.getActivatedMacros().get(microEffect.getMacroNumber()).getActivatedMicros().add(microPack);
        if(microEffect.isLimited())
            isLimitedActivated = true;


    }

    private void chooseMicro(MicroEffect microEffect) {
        boolean answer = input.getChoice(microEffect);
        if(answer){
            this.manageMicro(microEffect);
        }
    }

    private boolean isMacroActivated(MacroEffect macroEffect){
        for(MacroPack macroPack : shoootInfo.getActivatedMacros()){
            if (macroPack.getMacroNumber() == macroEffect.getNumber())
                return true;
        }
        return false;
    }

    private boolean isMicroActivated(MicroEffect microEffect){
        for(MacroPack macroPack : shoootInfo.getActivatedMacros()){
            if(macroPack.getMacroNumber() == microEffect.getMacroNumber()){
                for(MicroPack microPack : macroPack.getActivatedMicros()){
                    if(microPack.getMicroNumber() == microEffect.getNumber()){
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    private void chooseMacro(MacroEffect macroEffect){
        boolean answer = input.getChoice(macroEffect);
        if(answer){
            this.manageMacro(macroEffect);
        }
    }


}
