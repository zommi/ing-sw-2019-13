package client.weapons;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MacroPack implements Serializable {
    private int macroNumber;
    private List<MicroPack> activatedMicros;

    public MacroPack(int macroNumber){
        this.macroNumber = macroNumber;
        this.activatedMicros = new ArrayList<>();
    }

    public int getMacroNumber() {
        return macroNumber;
    }

    public List<MicroPack> getActivatedMicros() {
        return activatedMicros;
    }

    public MicroPack getActivatedMicro(int micro){
        for(MicroPack microPack : activatedMicros){
            if(microPack.getMicroNumber() == micro)
                return microPack;
        }
        return null;
    }

    public void setMacroNumber(int macroNumber) {
        this.macroNumber = macroNumber;
    }
}
