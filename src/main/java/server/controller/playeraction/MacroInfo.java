package server.controller.playeraction;

import exceptions.NoSuchEffectException;

import java.util.List;

public class MacroInfo {
    private int macroNumber;
    private List<MicroInfo> activatedMicros;

    public int getMacroNumber() {
        return macroNumber;
    }

    public List<MicroInfo> getActivatedMicros() {
        return activatedMicros;
    }

    public MicroInfo getActivatedMicro(int micro) throws NoSuchEffectException{
        for(MicroInfo microInfo : activatedMicros){
            if(microInfo.getMicroNumber() == micro)
                return microInfo;
        }
        throw new NoSuchEffectException();
    }
}
