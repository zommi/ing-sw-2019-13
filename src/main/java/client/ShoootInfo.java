package client;

import java.util.List;

public class ShoootInfo implements Info{

    private String weapon;
    List<MacroPack> activatedMacros;

    public ShoootInfo(String weapon){

    }

    public List<MacroPack> getActivatedMacros() {
        return activatedMacros;
    }

    public MacroPack getActivatedMacro(int macro){
        for(MacroPack macroPack : activatedMacros)
            if(macroPack.getMacroNumber() == macro)
                return activatedMacros.get(macro);
        return null;
    }

    public MicroPack getActivatedMicro(int macro, int micro){
        if(getActivatedMacro(macro) != null)
            return getActivatedMacro(macro).getActivatedMicro(micro);
        else return null;
    }
}
