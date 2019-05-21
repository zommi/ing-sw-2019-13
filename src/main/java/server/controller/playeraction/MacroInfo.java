package server.controller.playeraction;

import server.controller.playeraction.MicroInfo;
import exceptions.NoSuchEffectException;
import server.controller.Info;

import java.io.Serializable;
import java.util.List;

public class MacroInfo implements Serializable, Info {
    private int macroNumber;
    private List<MicroInfo> activatedMicros;

    public MacroInfo(int macroNumber, List<MicroInfo> activatedMicros){
        this.macroNumber = macroNumber;
        this.activatedMicros = activatedMicros;
    }

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
