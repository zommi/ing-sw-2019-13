package client.weapons;

import client.GameModel;
import client.InputAbstract;

public class ShootParser {

    private boolean isLimitedActivated;
    private Weapon weapon;
    private InputAbstract input;
    private GameModel gameModel;

    public ShootParser(GameModel gameModel){
        this.gameModel = gameModel;
    }
    private ShootPack shootPack;

    public ShootPack getWeaponInput(Weapon weapon, InputAbstract input){
        this.isLimitedActivated = false;
        this.weapon = weapon;
        this.input = input; //magari togliere questo assegnamento e metterlo in un setter apposta
        shootPack = new ShootPack(weapon.getName());
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
            default:
                //this should never happen (it depends on json file)
        }
        return shootPack;
    }

    private void manageMacro(MacroEffect macroEffect){
        MacroPack macroPack = new MacroPack(macroEffect.getNumber());
        shootPack.getActivatedMacros().add(macroPack);
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
        shootPack.getActivatedMacro(microEffect.getMacroNumber()).getActivatedMicros().add(microPack);
        if(microEffect.isLimited())
            isLimitedActivated = true;

        //start asking stuff
        if(microEffect.isMoveFlag() && !microEffect.isGenerateSquareFlag()) {
            microPack.setSquare(input.askSquares(1).get(0));
        }

        if(microEffect.getMaxTargetPlayerSize() != 0)
            microPack.setPlayersList(input.askPlayers(microEffect.getMaxTargetPlayerSize()));

        if(microEffect.getMaxNmSquareSize() != 0 && !microEffect.isGenerateNMSquareFlag())
            microPack.setNoMoveSquaresList(input.askSquares(microEffect.getMaxNmSquareSize()));

        if(microEffect.getMaxTargetRoomSize() != 0)
            microPack.setRoomsList(input.askRooms(microEffect.getMaxTargetRoomSize()));


    }

    private void chooseMicro(MicroEffect microEffect) {
        boolean answer = input.getChoice(microEffect);
        if(answer){
            this.manageMicro(microEffect);
        }
    }

    private boolean isMacroActivated(MacroEffect macroEffect){
        for(MacroPack macroPack : shootPack.getActivatedMacros()){
            if (macroPack.getMacroNumber() == macroEffect.getNumber())
                return true;
        }
        return false;
    }

    private boolean isMicroActivated(MicroEffect microEffect){
        for(MacroPack macroPack : shootPack.getActivatedMacros()){
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
