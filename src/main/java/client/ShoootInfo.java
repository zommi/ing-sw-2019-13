package client;

import exceptions.NoSuchEffectException;

import java.util.List;

public class ShoootInfo implements Info{

    private String weapon;
    List<MacroPack> activatedMacros;

    public ShoootInfo(String weapon){

    }

    public List<MacroPack> getActivatedMacros() {
        return activatedMacros;
    }

    public MacroPack getActivatedMacro(int macro) throws NoSuchEffectException {
        for(MacroPack macroPack : activatedMacros)
            if(macroPack.getMacroNumber() == macro)
                return activatedMacros.get(macro);
        throw new NoSuchEffectException();
    }

    public MicroPack getActivatedMicro(int macro, int micro) throws NoSuchEffectException{
        return getActivatedMacro(macro).getActivatedMicro(micro);
    }
}
